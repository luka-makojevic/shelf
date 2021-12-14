package com.htec.filesystem.util;

import com.htec.filesystem.exception.ExceptionSupplier;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    public static void saveFile(String uploadDir, String fileName, byte[] content) {

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try {
                Path filePath = uploadPath.resolve(fileName);

                File file = new File(filePath.toString());

                OutputStream os = new FileOutputStream(file);

                os.write(content);

                os.close();

            } catch (IOException ioe) {
                throw ExceptionSupplier.couldNotSaveImage.get();
            }

        } catch (IOException e) {
            throw ExceptionSupplier.couldNotSaveImage.get();
        }

    }
}
