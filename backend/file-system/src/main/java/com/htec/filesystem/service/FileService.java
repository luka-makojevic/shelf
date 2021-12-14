package com.htec.filesystem.service;

import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Service
public class FileService {

    private final UserAPICallService userAPICallService;

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final FileRepository fileRepository;
    private final ShelfRepository shelfRepository;

    public FileService(UserAPICallService userAPICallService,
                       FileRepository fileRepository,
                       ShelfRepository shelfRepository) {

        this.userAPICallService = userAPICallService;
        this.fileRepository = fileRepository;
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

    public boolean initializeFolders(Long id) {

        String userDataPath = homePath + userPath + id;

        if (!new File(userDataPath).mkdirs()) {
            return false;
        }

        String userProfilePicturePath = userDataPath + pathSeparator + "profile-picture";

        if (!new File(userProfilePicturePath).mkdirs()) {
            return false;
        }

        String userShelvesPath = userDataPath + pathSeparator + "shelves";

        return new File(userShelvesPath).mkdirs();
    }

    public void saveFile(Long shelfId, Long folderId, Map<String, Pair<String, String>> files) {

        if (files == null || files.get("file") == null)
            throw ExceptionSupplier.couldNotUploadFile.get();

        byte[] bytes = Base64.getDecoder().decode(files.get("file").getSecond());

        String fileName = files.get("file").getFirst();
        String localPath;
        String dbPath;

        ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        if (folderId != 0) {

            FileEntity fileEntity = fileRepository.findById(folderId)
                    .orElseThrow(ExceptionSupplier.noFileWithGivenId);

            if (!fileEntity.isFolder())
                throw ExceptionSupplier.noFileWithGivenId.get();

            dbPath = fileEntity.getPath() + fileEntity.getName() + pathSeparator;
            localPath = userPath + dbPath;

        } else {

            localPath = userPath + shelfEntity.getUserId() + pathSeparator + "shelves" + pathSeparator + shelfId + pathSeparator;
            dbPath = shelfEntity.getUserId() + pathSeparator + "shelves" + pathSeparator + shelfId + pathSeparator;
        }

        if (fileRepository.findByPathAndName(fileName, dbPath).isPresent())
            throw ExceptionSupplier.fileAlreadyExists.get();

        String uploadDir = homePath + localPath;
        FileUtil.saveFile(uploadDir, fileName, bytes);
        saveFileIntoDB(dbPath, fileName, shelfId);
    }

    public void saveFileIntoDB(String filePath, String fileName, long shelfId) {

        FileEntity fileEntity = new FileEntity();

        fileEntity.setName(fileName);
        fileEntity.setPath(filePath);
        fileEntity.setShelfId(shelfId);
        fileEntity.setFolder(false);
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileRepository.save(fileEntity);
    }
}
