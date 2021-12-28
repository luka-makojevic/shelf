package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.mapper.ShelfItemMapper;
import com.htec.filesystem.model.request.RenameFileRequestModel;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    private final UserAPICallService userAPICallService;

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;
    private final String zipName = "shelfFiles.zip";

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final ShelfRepository shelfRepository;

    public FileService(UserAPICallService userAPICallService, FileRepository fileRepository, FolderRepository folderRepository, ShelfRepository shelfRepository) {

        this.userAPICallService = userAPICallService;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.shelfRepository = shelfRepository;
    }

    public void saveUserProfilePicture(Long id, Map<String, Pair<String, String>> files) {

        if (files == null || files.get("image") == null) throw ExceptionSupplier.couldNotSaveImage.get();

        byte[] bytes = Base64.getDecoder().decode(files.get("image").getSecond());

        String fileName = files.get("image").getFirst();

        String localPath = userPath + id + pathSeparator + "profile-picture" + pathSeparator;

        String dbPath = id + pathSeparator + "profile-picture" + pathSeparator + fileName;

        userAPICallService.updateUserPhotoById(id, dbPath);

        String uploadDir = homePath + localPath;
        FileUtil.saveFile(uploadDir, fileName, bytes);
    }

    public FileResponseModel getFile(Long userId, Long id, boolean file) {

        String path = "";

        if (file && userId != null) {

            FileEntity fileEntity = fileRepository.findByIdAndUserIdAndDeleted(id, userId, false)
                    .orElseThrow(ExceptionSupplier.fileNotFound);

            path = fileEntity.getPath();
        } else {

            path = userAPICallService.getUserPhotoPath(id);
        }

        String folder = homePath + userPath;

        byte[] imageBytes;
        try (FileInputStream fileInputStream = new FileInputStream(folder + path)) {

            imageBytes = StreamUtils.copyToByteArray(fileInputStream);
        } catch (IOException ex) {
            throw ExceptionSupplier.fileNotFound.get();
        }

        return new FileResponseModel(imageBytes, path);
    }

    public void saveFile(Long shelfId, Long folderId, Map<String, Pair<String, String>> files, Long userId) {

        if (files == null) {
            throw ExceptionSupplier.couldNotUploadFile.get();
        }

        if (folderId != 0) {

            FolderEntity folder = folderRepository.findById(folderId)
                    .orElseThrow(ExceptionSupplier.noFolderWithGivenId);

            if (!Objects.equals(folder.getShelfId(), shelfId)) {
                throw ExceptionSupplier.folderIsNotInGivenShelf.get();
            }
        }

        ShelfEntity shelf = shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        if (!Objects.equals(shelf.getUserId(), userId)) {
            throw ExceptionSupplier.userNotAllowedToAccessShelf.get();
        }


        for (Map.Entry<String, Pair<String, String>> filesPair : files.entrySet()) {

            byte[] bytes = Base64.getDecoder().decode(filesPair.getValue().getSecond());

            String fileName = filesPair.getValue().getFirst();
            String localPath;
            String dbPath;

            ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                    .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

            if (folderId != 0) {

                FolderEntity folderEntity = folderRepository.findById(folderId)
                        .orElseThrow(ExceptionSupplier.noFolderWithGivenId);

                dbPath = folderEntity.getPath() + pathSeparator + fileName;
                localPath = userPath + folderEntity.getPath() + pathSeparator;

                if (fileRepository.findByNameAndParentFolderId(fileName, folderId).isPresent()) {
                    throw ExceptionSupplier.fileAlreadyExists.get();
                }

            } else {

                localPath = userPath + shelfEntity.getUserId() + pathSeparator + "shelves" + pathSeparator + shelfId + pathSeparator;
                dbPath = shelfEntity.getUserId() + pathSeparator + "shelves" + pathSeparator + shelfId + pathSeparator + fileName;

                if (fileRepository.findByNameAndShelfIdAndParentFolderIdIsNull(fileName, shelfId).isPresent()) {
                    throw ExceptionSupplier.fileAlreadyExists.get();
                }
            }

            if (fileRepository.findByPath(dbPath).isPresent()) {
                throw ExceptionSupplier.fileAlreadyExists.get();
            }

            String uploadDir = homePath + localPath;
            FileUtil.saveFile(uploadDir, fileName, bytes);
            saveFileIntoDB(dbPath, fileName, shelfId, folderId);
        }
    }

    public void saveFileIntoDB(String filePath, String fileName, long shelfId, long folderId) {

        FileEntity fileEntity = new FileEntity();

        fileEntity.setName(fileName);
        fileEntity.setPath(filePath);
        fileEntity.setShelfId(shelfId);
        fileEntity.setDeleted(false);
        if (folderId != 0) fileEntity.setParentFolderId(folderId);

        fileEntity.setCreatedAt(LocalDateTime.now());
        fileRepository.save(fileEntity);
    }

    @Transactional
    public void updateDeletedFiles(AuthUser user, List<Long> fileIds, Boolean deleted, Boolean trashVisible) {

        List<FileEntity> fileEntities = fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), !deleted, fileIds);

        if (fileEntities.size() != fileIds.size()) {
            throw ExceptionSupplier.filesNotFound.get();
        }

        if (!fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()).containsAll(fileIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFile.get();
        }

        if (deleted) {
            moveFilesToTrash(fileEntities, trashVisible);
        } else {
            recover(user, fileEntities);
        }

        fileRepository.saveAll(fileEntities);

        fileRepository.updateDeletedByIds(deleted, fileIds);
    }

    void recover(AuthUser user, List<FileEntity> fileEntities) {

        List<Long> folderIds = fileEntities.stream().map(FileEntity::getParentFolderId).collect(Collectors.toList());

        List<FileEntity> fileEntitiesNotDeleted = fileRepository.findAllByUserIdAndNotDeletedAndParentFolderIdsIn(user.getId(), folderIds);

        Map<Optional<Long>, List<FileEntity>> filesByFolder = fileEntitiesNotDeleted.stream().collect(Collectors.groupingBy(e -> Optional.ofNullable(e.getParentFolderId())));

        Map<String, Integer> filesCount = new HashMap<>();

        for (FileEntity fileEntity : fileEntities) {
            List<FileEntity> existingFiles = filesByFolder.getOrDefault(Optional.ofNullable(fileEntity.getParentFolderId()), new ArrayList<>());

            if (existingFiles.stream().anyMatch(e -> e.getName().equals(fileEntity.getName()))) {
                filesCount.merge(fileEntity.getName(), 1, Integer::sum);
            }
        }

        for (FileEntity fileEntity : fileEntities) {

            Integer fileNameCounter = filesCount.getOrDefault(fileEntity.getName(), 0);

            String oldPath = fileEntity.getPath();
            String fileNameWithUUID = fileEntity.getName();

            if (fileNameCounter > 0) {
                filesCount.put(fileEntity.getName(), fileNameCounter - 1);

                int extensionIndex = fileEntity.getName().lastIndexOf('.');
                String nameWithoutExtension = fileEntity.getName().substring(0, extensionIndex);
                String extension = fileEntity.getName().substring(extensionIndex);

                int index = fileEntity.getPath().lastIndexOf(pathSeparator);
                String newPath = fileEntity.getPath().substring(0, index);

                fileEntity.setName(nameWithoutExtension + "(" + fileNameCounter + ")" + extension);
                fileEntity.setPath(newPath + pathSeparator + fileEntity.getName());
                fileEntity.setDeletedAt(null);
            }

            recoverFilesOnFileSystem(fileEntity.getPath(), oldPath, fileNameWithUUID);
        }
    }


    private void moveFilesToTrash(List<FileEntity> fileEntities, Boolean trashVisible) {

        for (FileEntity fileEntity : fileEntities) {
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();

            String newFileName = fileEntity.getName() + "_" + uuidAsString;

            String newDbPath = moveFilesInFileSystem(newFileName, fileEntity.getShelfId(), fileEntity.getPath());

            fileEntity.setPath(newDbPath);
            fileEntity.setName(newFileName);
            fileEntity.setTrashVisible(trashVisible);
        }
    }

    private String moveFilesInFileSystem(String newFileName, Long shelfId, String oldPath) {

        try {
            int index = oldPath.indexOf(pathSeparator);
            String userId = oldPath.substring(0, index);

            String newPath = homePath + userPath + userId + pathSeparator + "trash" + pathSeparator + newFileName;
            newPath = newPath.replace("shelves" + pathSeparator + shelfId, "trash");

            Path uploadPath = Paths.get(newPath.replace(newFileName, ""));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.move(Paths.get(homePath + userPath + oldPath), Paths.get(newPath));
            return newPath.replace(homePath + userPath, "");
        } catch (IOException e) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }

    private void recoverFilesOnFileSystem(String newPath, String oldPath, String fileNameWithUUID) {

        try {

            int index = oldPath.lastIndexOf(pathSeparator);
            String oldPathWithoutFileName = oldPath.substring(0, index);

            String newPathFull = homePath + userPath + newPath;
            String oldPathFull = homePath + userPath + oldPathWithoutFileName + pathSeparator + fileNameWithUUID;

            Files.move(Paths.get(oldPathFull), Paths.get(newPathFull));

        } catch (IOException e) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }

    public void fileRename(Long userId, RenameFileRequestModel renameFileRequestModel) {

        String fileName = renameFileRequestModel.getFileName();

        Long fileId = renameFileRequestModel.getFileId();

        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(ExceptionSupplier.noFileWithGivenId);

        ShelfEntity shelfEntity = shelfRepository.findById(fileEntity.getShelfId())
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        if (!Objects.equals(shelfEntity.getUserId(), userId))
            throw ExceptionSupplier.userNotAllowedToAccessFile.get();

        if (fileRepository.findByNameAndParentFolderIdAndIdNot(fileName,
                fileEntity.getParentFolderId(),
                fileEntity.getId()).isPresent())
            throw ExceptionSupplier.fileAlreadyExists.get();

        int dotIndex = fileEntity.getName().lastIndexOf(".");
        String fileExtension = fileEntity.getName().substring(dotIndex);

        fileName += fileExtension;

        String oldFilePath = homePath + userPath + fileEntity.getPath();
        File oldFile = new File(oldFilePath);

        String newFilePath = oldFilePath.replace(fileEntity.getName(), fileName);
        File newFile = new File(newFilePath);

        String dbPath = newFilePath.replace(homePath + userPath, "");
        fileEntity.setPath(dbPath);
        fileEntity.setName(fileName);
        fileRepository.save(fileEntity);

        oldFile.renameTo(newFile);
    }

    public List<ShelfItemDTO> getAllFilesFromTrash(Long userId) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserId(userId);
        List<Long> shelfIds = shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList());

        List<FileEntity> fileEntities = fileRepository.findAllByShelfIdInAndDeletedTrue(shelfIds);

        return new ArrayList<>(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(fileEntities));
    }

    public void deleteFile(AuthUser user, List<Long> fileIds) throws IOException {

        List<FileEntity> fileEntities = fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), true, fileIds);

        if (fileEntities.size() != fileIds.size()) {
            throw ExceptionSupplier.filesNotFound.get();
        }

        if (!fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()).containsAll(fileIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFile.get();
        }

        for (FileEntity fileEntity : fileEntities) {
            String fullPath = homePath + userPath + fileEntity.getPath();
            FileUtils.forceDelete(new File(fullPath));
        }

        fileRepository.deleteAll(fileEntities);
    }

    public void downloadFilesToZip(AuthUser user, List<Long> fileIds) {

        List<FileEntity> fileEntities = fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);

        if (fileEntities.size() != fileIds.size()) {
            throw ExceptionSupplier.filesNotFound.get();
        }

        if (!fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()).containsAll(fileIds)) {
            throw ExceptionSupplier.userNotAllowedToDownloadFile.get();
        }

        try (FileOutputStream fos = new FileOutputStream("multiCompressed.zip"); ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (FileEntity fileEntity : fileEntities) {

                String fullPath = homePath + userPath + fileEntity.getPath();
                File fileToZip = new File(fullPath);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
            }
            zipOut.finish();
        } catch (IOException e) {
            throw ExceptionSupplier.couldNotDownloadFiles.get();
        }
    }
}
