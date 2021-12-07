package com.htec.filesystem.service;

import com.htec.filesystem.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final UserAPICallService userAPICallService;

    public FileService(UserAPICallService userAPICallService) {
        this.userAPICallService = userAPICallService;
    }

    public void saveUserProfilePicture(MultipartFile multipartFile, Long id) {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        String homePath = System.getProperty("user.home");
        String localPath = "/shelf/user-photos/" + id + "/" + fileName;

        String uploadDir = homePath + localPath;
        FileUtil.saveFile(uploadDir, fileName, multipartFile);

        userAPICallService.updateUserPhotoById(id, localPath);
    }
}
