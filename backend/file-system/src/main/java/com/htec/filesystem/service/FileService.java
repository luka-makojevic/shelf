package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.ShelfItemMapper;
import com.htec.filesystem.model.request.LogRequestModel;
import com.htec.filesystem.model.request.RenameFileRequestModel;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.FolderTreeRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.FileUtil;
import com.htec.filesystem.util.FunctionEvents;
import com.htec.filesystem.validator.FileSystemValidator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
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
    private final String trash = "trash";
    private final String shelves = "shelves";
    private final int MAX_FILE_SIZE = 5242880;

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final ShelfRepository shelfRepository;
    private final FileSystemValidator fileSystemValidator;
    private final FolderTreeRepository folderTreeRepository;

    public FileService(UserAPICallService userAPICallService,
                       FileRepository fileRepository,
                       FolderRepository folderRepository,
                       ShelfRepository shelfRepository,
                       FileSystemValidator fileSystemValidator,
                       FolderTreeRepository folderTreeRepository) {

        this.userAPICallService = userAPICallService;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.shelfRepository = shelfRepository;
        this.fileSystemValidator = fileSystemValidator;
        this.folderTreeRepository = folderTreeRepository;
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

    public List<Long> saveFile(Long shelfId, Long folderId, Map<String, Pair<String, String>> files, Long userId) {

        List<Long> newFileIds = new ArrayList<>();

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

            int fileSize = bytes.length;

            if (fileSize > MAX_FILE_SIZE) {
                throw ExceptionSupplier.fileSizeIsTooLarge.get();
            }

            String fileName = filesPair.getValue().getFirst();
            String localPath;
            String dbPath;

            ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                    .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

            if (folderId != 0) {

                FolderEntity folderEntity = folderRepository.findById(folderId)
                        .orElseThrow(ExceptionSupplier.noFolderWithGivenId);

                int fileCounter = 0;
                if (fileRepository.findByNameAndParentFolderId(fileName, folderId).isPresent()) {
                    String newFileName = fileName;

                    while (fileRepository.findByNameAndParentFolderId(newFileName, folderId).isPresent()) {
                        fileCounter++;
                        int extensionIndex = fileName.lastIndexOf('.');
                        if (extensionIndex != -1) {

                            String nameWithoutExtension = fileName.substring(0, extensionIndex);
                            String extension = fileName.substring(extensionIndex);
                            newFileName = nameWithoutExtension + " (" + fileCounter + ")" + extension;
                        } else {
                            newFileName = fileName + " (" + fileCounter + ")";
                        }
                    }
                    fileName = newFileName;
                }

                dbPath = folderEntity.getPath() + pathSeparator + fileName;
                localPath = userPath + folderEntity.getPath() + pathSeparator;

            } else {

                int fileCounter = 0;
                if (fileRepository.findByNameAndShelfIdAndParentFolderIdIsNull(fileName, shelfId).isPresent()) {
                    String newFileName = fileName;

                    while (fileRepository.findByNameAndShelfIdAndParentFolderIdIsNull(newFileName, shelfId).isPresent()) {

                        fileCounter++;
                        int extensionIndex = fileName.lastIndexOf('.');
                        if (extensionIndex != -1) {

                            String nameWithoutExtension = fileName.substring(0, extensionIndex);
                            String extension = fileName.substring(extensionIndex);
                            newFileName = nameWithoutExtension + " (" + fileCounter + ")" + extension;
                        } else {
                            newFileName = fileName + " (" + fileCounter + ")";
                        }
                    }
                    fileName = newFileName;
                }
                localPath = userPath + shelfEntity.getUserId() + pathSeparator + shelves + pathSeparator + shelfId + pathSeparator;
                dbPath = shelfEntity.getUserId() + pathSeparator + shelves + pathSeparator + shelfId + pathSeparator + fileName;
            }

            String uploadDir = homePath + localPath;
            FileUtil.saveFile(uploadDir, fileName, bytes);

            newFileIds.add(saveFileIntoDB(dbPath, fileName, shelfId, folderId));
        }

        return newFileIds;
    }

    public Long saveFileIntoDB(String filePath, String fileName, long shelfId, long folderId) {

        FileEntity fileEntity = new FileEntity();

        fileEntity.setName(fileName);
        fileEntity.setPath(filePath);
        fileEntity.setShelfId(shelfId);
        fileEntity.setDeleted(false);
        if (folderId != 0) fileEntity.setParentFolderId(folderId);

        fileEntity.setCreatedAt(LocalDateTime.now());
        fileRepository.saveAndFlush(fileEntity);

        return fileEntity.getId();
    }

    public void copyFile(Long fileId, Long shelfId, Long userId) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserId(userId);

        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(ExceptionSupplier.fileNotFound);

        shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.shelfNotFound);

        if (!shelfEntities.stream().map(ShelfEntity::getId).collect((Collectors.toList())).contains(fileEntity.getShelfId())) {
            throw ExceptionSupplier.userNotAllowedToAccessShelf.get();
        }

        String src = homePath + userPath + fileEntity.getPath();
        String dst = homePath + userPath + userId + pathSeparator + shelves + pathSeparator + shelfId + pathSeparator + fileEntity.getName();

        if (fileRepository.findByNameAndShelfId(fileEntity.getName(), shelfId).isPresent()) {
            throw ExceptionSupplier.fileAlreadyExists.get();
        }

        FileEntity backupFile = new FileEntity();
        backupFile.setName(fileEntity.getName());
        backupFile.setCreatedAt(LocalDateTime.now());
        backupFile.setShelfId(shelfId);
        backupFile.setDeleted(false);
        backupFile.setPath(userId + pathSeparator + shelves + pathSeparator + shelfId + pathSeparator + fileEntity.getName());
        fileRepository.save(backupFile);

        try {
            Files.copy(Paths.get(src), Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw ExceptionSupplier.couldNotCopyFile.get();
        }
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

        for (FileEntity fileEntity : fileEntities) {

            if (fileEntity.getParentFolderId() != null) {

                Optional<FolderEntity> folderEntity = folderRepository.findById(fileEntity.getParentFolderId());
                if (!folderEntity.isPresent())
                    fileEntity.setParentFolderId(null);
                else if (Boolean.TRUE.equals(folderEntity.get().getDeleted()))
                    fileEntity.setParentFolderId(null);
            }
        }

        List<Long> folderIds = fileEntities.stream().map(FileEntity::getParentFolderId).collect(Collectors.toList());

        List<FileEntity> fileEntitiesNotDeleted = fileRepository.findAllByUserIdAndNotDeletedAndParentFolderIdsIn(user.getId(), folderIds);

        Map<Optional<Long>, List<FileEntity>> filesByFolder = fileEntitiesNotDeleted.stream().
                collect(Collectors.groupingBy(e -> Optional.ofNullable(e.getParentFolderId())));

        Map<String, Integer> filesCount = new HashMap<>();

        for (FileEntity fileEntity : fileEntities) {
            List<FileEntity> existingFiles = filesByFolder.getOrDefault(Optional.ofNullable(fileEntity.getParentFolderId()), new ArrayList<>());

            if (existingFiles.stream().anyMatch(e -> e.getName().equals(fileEntity.getRealName()))) {
                filesCount.merge(fileEntity.getName(), 1, Integer::sum);
            }
        }

        for (FileEntity fileEntity : fileEntities) {

            String newPath = "";
            int extensionIndex = fileEntity.getName().lastIndexOf('.');
            String nameWithoutExtension = fileEntity.getName();
            String extension = "";
            if (extensionIndex != -1) {

                nameWithoutExtension = fileEntity.getName().substring(0, extensionIndex);

                extension = fileEntity.getName().substring(extensionIndex);

                int uuidIndex = extension.lastIndexOf('_');
                if (uuidIndex != -1) {
                    extension = extension.substring(0, uuidIndex);
                }
            } else {
                int uuidIndex = fileEntity.getName().lastIndexOf('_');
                if (uuidIndex != -1) {
                    nameWithoutExtension = fileEntity.getName().substring(0, uuidIndex);
                }
            }

            Integer fileNameCounter = filesCount.getOrDefault(fileEntity.getName(), 0);

            String oldPath = fileEntity.getPath();
            String fileNameWithUUID = fileEntity.getName();

            if (fileNameCounter > 0) {

                List<FileEntity> existingFiles = filesByFolder.getOrDefault(Optional.ofNullable(fileEntity.getParentFolderId()), new ArrayList<>());

                do {
                    fileEntity.setName(nameWithoutExtension + "(" + filesCount.get(fileEntity.getName()) + ")" + extension);
                    filesCount.put(fileEntity.getName(), fileNameCounter + 1);
                } while (existingFiles.stream().anyMatch(e -> e.getName().equals(fileEntity.getName())));

            } else {

                fileEntity.setName(nameWithoutExtension + extension);
            }

            if (fileEntity.getParentFolderId() != null) {

                FolderEntity folderEntity = folderRepository.findById(fileEntity.getParentFolderId())
                        .orElseThrow(ExceptionSupplier.noFolderWithGivenId);

                if (Boolean.FALSE.equals(folderEntity.getDeleted())) {
                    newPath = folderEntity.getPath() + pathSeparator + fileEntity.getName();
                } else {
                    newPath = fileEntity.getPath().replace(trash, shelves + pathSeparator + fileEntity.getShelfId());
                    newPath = newPath.replace(fileNameWithUUID, fileEntity.getName());

                    int shelvesIndex = newPath.lastIndexOf(shelves);
                    newPath = newPath.substring(0, shelvesIndex + shelves.length() + fileEntity.getShelfId().toString().length() + 1)
                            + pathSeparator + fileEntity.getName();
                }
            } else {
                newPath = fileEntity.getPath().replace(trash, shelves + pathSeparator + fileEntity.getShelfId());
                newPath = newPath.replace(fileNameWithUUID, fileEntity.getName());

                int shelvesIndex = newPath.lastIndexOf(shelves);
                newPath = newPath.substring(0, shelvesIndex + shelves.length() + fileEntity.getShelfId().toString().length() + 1)
                        + pathSeparator + fileEntity.getName();
            }

            fileEntity.setPath(newPath);
            fileEntity.setDeletedAt(null);
            fileEntity.setTrashVisible(null);

            recoverFilesInFileSystem(newPath, oldPath);
            fileRepository.save(fileEntity);
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
            fileEntity.setDeletedAt(LocalDateTime.now());
        }
    }

    private String moveFilesInFileSystem(String newFileName, Long shelfId, String oldPath) {

        try {
            int index = oldPath.indexOf(pathSeparator);
            String userId = oldPath.substring(0, index);

            String newPath = homePath + userPath + userId + pathSeparator + trash + pathSeparator + newFileName;
            newPath = newPath.replace(shelves + pathSeparator + shelfId, trash);

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

    private void recoverFilesInFileSystem(String newPath, String oldPath) {

        try {
            String newFileSystemPath = homePath + userPath + newPath;
            String oldFileSystemPath = homePath + userPath + oldPath;

            Files.move(Paths.get(oldFileSystemPath), Paths.get(newFileSystemPath));

        } catch (IOException e) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }

    public void fileRename(Long userId, RenameFileRequestModel renameFileRequestModel) {

        String fileName = renameFileRequestModel.getFileName();

        Long fileId = renameFileRequestModel.getFileId();

        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(ExceptionSupplier.noFileWithGivenId);

        if (fileEntity.getParentFolderId() != null) {

            if (fileRepository.findByNameAndParentFolderId(fileName, fileEntity.getParentFolderId()).isPresent()) {
                throw ExceptionSupplier.fileNameAlreadyExists.get();
            }

        } else {

            if (fileRepository.findByNameAndShelfId(fileName, fileEntity.getShelfId()).isPresent()) {
                throw ExceptionSupplier.fileNameAlreadyExists.get();
            }

        }

        ShelfEntity shelfEntity = shelfRepository.findById(fileEntity.getShelfId())
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        fileSystemValidator.isFileNameValid(renameFileRequestModel.getFileName());

        if (!Objects.equals(shelfEntity.getUserId(), userId))
            throw ExceptionSupplier.userNotAllowedToAccessFile.get();

        if (fileRepository.findByNameAndParentFolderIdAndIdNot(fileName,
                fileEntity.getParentFolderId(),
                fileEntity.getId()).isPresent())
            throw ExceptionSupplier.fileAlreadyExists.get();

        int dotIndex = fileEntity.getName().lastIndexOf(".");

        if (dotIndex != -1) {

            String fileExtension = fileEntity.getName().substring(dotIndex);
            fileName += fileExtension;
        }

        if (fileEntity.getParentFolderId() != null) {

            if (fileRepository.findByNameAndParentFolderId(fileName, fileEntity.getParentFolderId()).isPresent()) {
                throw ExceptionSupplier.fileNameAlreadyExists.get();
            }
        } else {

            if (fileRepository.findByNameAndShelfId(fileName, fileEntity.getShelfId()).isPresent()) {
                throw ExceptionSupplier.fileNameAlreadyExists.get();
            }
        }

        String oldFilePath = homePath + userPath + fileEntity.getPath();
        File oldFile = new File(oldFilePath);

        String newFilePath = oldFilePath.replace(fileEntity.getName(), fileName);
        File newFile = new File(newFilePath);

        String dbPath = newFilePath.replace(homePath + userPath, "");
        fileEntity.setPath(dbPath);
        fileEntity.setName(fileName);
        fileEntity.setUpdatedAt(LocalDateTime.now());
        fileRepository.save(fileEntity);

        oldFile.renameTo(newFile);
    }

    public List<ShelfItemDTO> getAllFilesFromTrash(Long userId) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserId(userId);
        List<Long> shelfIds = shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList());

        List<FileEntity> fileEntities = fileRepository.findAllByShelfIdInAndDeletedTrue(shelfIds);

        return new ArrayList<>(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(fileEntities));
    }

    public Long deleteFile(Long userId, List<Long> fileIds) {

        List<FileEntity> fileEntities = fileRepository.findAllByUserIdAndDeletedAndIdIn(userId, true, fileIds);

        if (fileEntities.size() != fileIds.size()) {
            throw ExceptionSupplier.filesNotFound.get();
        }

        if (!fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()).containsAll(fileIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFile.get();
        }

        Long shelfId = fileEntities.isEmpty() ? null : fileEntities.get(0).getShelfId();

        for (FileEntity fileEntity : fileEntities) {
            String fullPath = homePath + userPath + fileEntity.getPath();
            if (!(new File(fullPath)).delete()) {
                throw ExceptionSupplier.couldNotDeleteFile.get();
            }
        }

        fileRepository.deleteAll(fileEntities);
        return shelfId;
    }

    public ZipOutputStream downloadFilesToZip(Long userId, List<Long> fileIds, List<Long> folderIds, OutputStream outputStream) {

        List<FileEntity> fileEntities = getFileEntities(userId, fileIds, folderIds);

        try (ZipOutputStream zippedOut = new ZipOutputStream(outputStream)) {

            for (FileEntity fileEntity : fileEntities) {

                String fullPath = homePath + userPath + fileEntity.getPath();
                FileSystemResource resource = new FileSystemResource(fullPath);
                ZipEntry entry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
                entry.setSize(resource.contentLength());
                entry.setTime(System.currentTimeMillis());
                zippedOut.putNextEntry(entry);
                StreamUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
            }

            zippedOut.finish();
            return zippedOut;
        } catch (IOException e) {
            throw ExceptionSupplier.couldNotDownloadFiles.get();
        }
    }

    public List<FileEntity> getFileEntities(Long userId, List<Long> fileIds, List<Long> folderIds) {

        List<FileEntity> fileEntities = fileRepository.findAllByUserIdAndDeletedAndIdIn(userId, false, fileIds);

        List<FolderEntity> folderEntities = folderRepository.findByUserIdAndFolderIdsAndDeleted(userId, folderIds, false);

        validation(fileIds, fileEntities, folderIds, folderEntities);

        List<Long> downStreamFoldersIds = folderTreeRepository.getFolderDownStreamTrees(folderIds, false)
                .stream().map(FolderEntity::getId).collect(Collectors.toList());

        List<FileEntity> filesToDownload = fileRepository.findAllByParentFolderIdIn(downStreamFoldersIds)
                .stream().filter(fileEntity -> fileEntity.getDeleted() == null || !fileEntity.getDeleted()).collect(Collectors.toList());

        fileEntities.addAll(filesToDownload);

        if (fileEntities.isEmpty()) {
            throw ExceptionSupplier.noFilesToBeDownloaded.get();
        }

        return fileEntities;
    }

    private void validation(List<Long> fileIds, List<FileEntity> fileEntities, List<Long> folderIds, List<FolderEntity> folderEntities) {
        if (fileIds != null && fileEntities != null) {

            if (fileEntities.size() != fileIds.size()) {
                throw ExceptionSupplier.filesNotFound.get();
            }

            if (!fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()).containsAll(fileIds)) {
                throw ExceptionSupplier.userNotAllowedToDownloadFile.get();
            }
        }

        if (folderIds != null && folderEntities != null) {

            if (folderEntities.size() != folderIds.size()) {
                throw ExceptionSupplier.folderNotFound.get();
            }

            if (!folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()).containsAll(folderIds)) {
                throw ExceptionSupplier.userNotAllowedToDownloadFile.get();
            }
        }
    }

    public void logFile(Long backupFileId, LogRequestModel logRequestModel) {

        Integer eventId = logRequestModel.getEventId();
        String fileName = logRequestModel.getFileName();

        FileEntity logFile = fileRepository.findById(backupFileId)
                .orElseThrow(ExceptionSupplier.fileNotFound);

        FunctionEvents event = FunctionEvents.values()[eventId - 1];

        String logMessage = event.getEvent() + "\t" + fileName + "\t" + LocalDateTime.now() + "\n";

        String sourceFilePath = homePath + userPath + logFile.getPath();

        try {
            Files.writeString(Path.of(sourceFilePath), logMessage, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long createLogFile(Long shelfId, Long folderId, String logFileName, Long userId) {

        Map<String, Pair<String, String>> files = new HashMap<>();

        Pair<String, String> file = Pair.of(logFileName, "");
        files.put(logFileName, file);

        List<Long> logFileIds = saveFile(shelfId, folderId, files, userId);

        return logFileIds.get(0);
    }

    public Long getLogFileId(Long shelfId, String logFileName, Long userId) {

        Optional<FileEntity> fileEntityOptional = fileRepository.findByNameAndShelfIdAndParentFolderIdIsNull(logFileName, shelfId);
        if (fileEntityOptional.isPresent()) {

            return fileEntityOptional.get().getId();
        }

        return createLogFile(shelfId, 0L, logFileName, userId);
    }

    public Map<Long, String> getFileNamesFromIds(List<Long> fileIds) {

        Map<Long, String> filesToDelete = new HashMap<>();

        List<FileEntity> fileEntities = fileRepository.findAllByIdIn(fileIds);

        for (FileEntity fileEntity : fileEntities) {
            filesToDelete.put(fileEntity.getId(), fileEntity.getRealName());
        }

        return filesToDelete;
    }
}
