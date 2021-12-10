package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class FileService {

    private final UserAPICallService userAPICallService;

    public FileService(UserAPICallService userAPICallService) {

        this.userAPICallService = userAPICallService;
    }

    public void saveUserProfilePicture(Long id, Map<String, Pair<String, String>> files) {

        byte[] bytes = Base64.getDecoder().decode(files.get("image").getSecond());

        if (files.get("image") == null)
            throw ExceptionSupplier.couldNotSaveImage.get();

        String fileName = files.get("image").getFirst();

        String homePath = System.getProperty("user.home");
        String localPath = "/shelf-files/user-photos/" + id + "/";

        userAPICallService.updateUserPhotoById(id, localPath + fileName);

        String uploadDir = homePath + localPath;
        FileUtil.saveFile(uploadDir, fileName, bytes);
    }
}
