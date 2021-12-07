package com.htec.filesystem.controller;

import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FileService;
import com.htec.filesystem.service.UserAPICallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final UserAPICallService userAPICallService;

    FileController(FileService fileService,
                   UserAPICallService userAPICallService) {
        this.fileService = fileService;
        this.userAPICallService = userAPICallService;
    }

    @PostMapping("/upload/profile/{id}")
    public ResponseEntity saveUser(@RequestParam("image") MultipartFile multipartFile,
                                   @PathVariable Long id) {

        fileService.saveUserProfilePicture(multipartFile, id);
        userAPICallService.updateUserPhotoById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Image Uploaded", HttpStatus.OK.value()));
    }
}
