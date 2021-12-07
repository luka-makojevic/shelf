package com.htec.filesystem.service;

import com.htec.filesystem.model.request.UpdateUserPhotoByIdRequestModel;
import com.htec.filesystem.util.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    public void saveUserProfilePicture(MultipartFile multipartFile, Long id) {

        String fileName = id.toString();

        String homePath = System.getProperty("user.home");

        String uploadDir = homePath + "/shelf/user-photos/" + id;
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }
}
