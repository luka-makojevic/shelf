package com.htec.filesystem.controller;

import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FileService;
import com.htec.filesystem.service.UserAPICallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    FileController(FileService fileService,
                   UserAPICallService userAPICallService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/profile/{id}")
    public ResponseEntity saveUser(RequestEntity<byte[]> entity,
                                   @RequestParam("image") MultipartFile multipartFile,
                                   @PathVariable Long id) {

        fileService.saveUserProfilePicture(multipartFile, id, entity);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Image Uploaded", HttpStatus.OK.value()));
    }
}
