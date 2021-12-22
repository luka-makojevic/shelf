package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.RenameFileRequestModel;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.FileUtil;
import com.htec.filesystem.validator.FileSystemValidator;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final UserAPICallService userAPICallService;

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final ShelfRepository shelfRepository;

    public FileService(UserAPICallService userAPICallService,
                       FileRepository fileRepository,
                       FolderRepository folderRepository,
                       ShelfRepository shelfRepository) {

        this.userAPICallService = userAPICallService;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.shelfRepository = shelfRepository;
    }

    public void saveUserProfilePicture(Long id, Map<String, Pair<String, String>> files) {

        if (files == null || files.get("image") == null)
            throw ExceptionSupplier.couldNotSaveImage.get();

        byte[] bytes = Base64.getDecoder().decode(files.get("image").getSecond());

        String fileName = files.get("image").getFirst();

        String localPath = userPath + id + pathSeparator + "profile-picture" + pathSeparator;

        String dbPath = id + pathSeparator + "profile-picture" + pathSeparator + fileName;

        userAPICallService.updateUserPhotoById(id, dbPath);

        String uploadDir = homePath + localPath;
        FileUtil.saveFile(uploadDir, fileName, bytes);
    }

    public FileResponseModel getFile(String path) {

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
        if (folderId != 0)
            fileEntity.setParentFolderId(folderId);

        fileEntity.setDeleted(false);
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileRepository.save(fileEntity);
    }

    @Transactional
    public void updateDeletedFiles(AuthUser user, List<Long> fileIds, boolean delete) {

        List<FileEntity> fileEntities = fileRepository.findAllByUserIdAndIdIn(user.getId(), fileIds, !delete);

        if (fileEntities.size() != fileIds.size()) {
            throw ExceptionSupplier.filesNotFound.get();
        }

        if (!fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()).containsAll(fileIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFile.get();
        }

        for (FileEntity fileEntity : fileEntities) {
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();
            fileEntity.setName(fileEntity.getName() + "_" + uuidAsString);
        }

        fileRepository.saveAll(fileEntities);
        fileRepository.updateIsDeletedByIds(delete, fileIds);
    }

    public boolean fileRename(Long userId, RenameFileRequestModel renameFileRequestModel) {

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

        String oldFilePath = homePath + userPath + fileEntity.getPath();
        File oldFile =  new File(oldFilePath);

        String newFilePath = oldFilePath.replace(fileEntity.getName(), fileName);
        File newFile = new File(newFilePath);

        String dbPath = newFilePath.replace(homePath + userPath, "");
        fileEntity.setPath(dbPath);
        fileEntity.setName(fileName);
        fileRepository.save(fileEntity);

        return oldFile.renameTo(newFile);
    }
}
