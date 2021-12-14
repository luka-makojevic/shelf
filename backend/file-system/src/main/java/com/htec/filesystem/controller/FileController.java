package com.htec.filesystem.controller;

import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FileService;
import com.htec.filesystem.service.UserAPICallService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService,
                          UserAPICallService userAPICallService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload/profile/{id}")
    public ResponseEntity uploadFile(@RequestBody Map<String, Pair<String, String>> files,
                                     @PathVariable Long id) {

        fileService.saveUserProfilePicture(id, files);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Image Uploaded", HttpStatus.OK.value()));
    }

    @PostMapping("/initialize/{id}")
    public ResponseEntity initializeFolders(@PathVariable Long id) {

        HttpStatus retStatus = HttpStatus.OK;

        if (!fileService.initializeFolders(id)) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage("Folders created", retStatus.value()));
    }

}
