package com.htec.filesystem.service;

import com.htec.filesystem.model.response.ImageResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ImageService {

    public ImageResponseModel getImage(String path) throws IOException {

        String USER_FOLDER = System.getProperty("user.home");

        byte[] imageBytes;
        FileInputStream fileInputStream = new FileInputStream(USER_FOLDER + "/" + path);

        imageBytes = StreamUtils.copyToByteArray(fileInputStream);

        return new ImageResponseModel(imageBytes, path);
//        return null;
    }
}
