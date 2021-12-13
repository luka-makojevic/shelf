package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class FileService {

    private final UserAPICallService userAPICallService;

    public FileService(UserAPICallService userAPICallService) {

        this.userAPICallService = userAPICallService;
    }

    public void saveUserProfilePicture(Long id, Map<String, Pair<String, String>> files) {

        if (files == null || files.get("image") == null)
            throw ExceptionSupplier.couldNotSaveImage.get();

        byte[] bytes = Base64.getDecoder().decode(files.get("image").getSecond());

        String fileName = files.get("image").getFirst();

        String homePath = System.getProperty("user.home");
        String localPath = "/shelf-files/user-data/" + id + "/profile-picture/";

        userAPICallService.updateUserPhotoById(id, localPath + fileName);

        String uploadDir = homePath + localPath;
        FileUtil.saveFile(uploadDir, fileName, bytes);
    }

    public FileResponseModel getFile(String path) {

        String homePath = System.getProperty("user.home");

        byte[] imageBytes;
        try (FileInputStream fileInputStream = new FileInputStream(homePath + "/" + path)) {

            imageBytes = StreamUtils.copyToByteArray(fileInputStream);
        } catch (IOException ex) {
            throw ExceptionSupplier.fileNotFound.get();
        }

        return new FileResponseModel(imageBytes, path);
    }
}
