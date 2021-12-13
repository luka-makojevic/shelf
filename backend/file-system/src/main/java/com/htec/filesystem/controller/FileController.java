package com.htec.filesystem.controller;

import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FileService;
import com.htec.filesystem.service.UserAPICallService;
import com.htec.filesystem.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    FileController(FileService fileService,
                   UserAPICallService userAPICallService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload/profile/{id}")
    public ResponseEntity uploadFile(@RequestBody Map<String, Pair<String, String>> files,
                                     @PathVariable Long id) {


        fileService.saveUserProfilePicture(id, files);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Image Uploaded", HttpStatus.OK.value()));
    }

    @GetMapping("/download/**")
    public ResponseEntity<byte[]> getFile(RequestEntity<byte[]> request) throws IOException {

        FileResponseModel fileResponseModel = fileService.getFile(FileUtil.getFilePath(request.getUrl().getPath()));
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(fileResponseModel.getImageContent());
    }

    @GetMapping("/preview/**")
    public ResponseEntity<byte[]> getImage(RequestEntity<byte[]> request) throws IOException {

        FileResponseModel fileResponseModel = fileService.getFile(FileUtil.getFilePath(request.getUrl().getPath()));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileResponseModel.getImageContent());
    }
}
