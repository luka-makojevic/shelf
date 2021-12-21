package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FileService;
import com.htec.filesystem.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload/profile/{id}")
    public ResponseEntity uploadProfilePicture(@RequestBody Map<String, Pair<String, String>> files,
                                               @PathVariable Long id) {

        fileService.saveUserProfilePicture(id, files);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Image Uploaded", HttpStatus.OK.value()));
    }

    @GetMapping("/download/**")
    public ResponseEntity<byte[]> getFile(RequestEntity<byte[]> request) {

        FileResponseModel fileResponseModel = fileService.getFile(FileUtil.getFilePath(request.getUrl().getPath()));
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(fileResponseModel.getImageContent());
    }

    @GetMapping("/preview/**")
    public ResponseEntity<byte[]> getImage(RequestEntity<byte[]> request) {

        FileResponseModel fileResponseModel = fileService.getFile(FileUtil.getFilePath(request.getUrl().getPath()));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileResponseModel.getImageContent());
    }

    @PostMapping("/upload/{shelfId}/{folderId}")
    public ResponseEntity uploadFile(@RequestBody Map<String, Pair<String, String>> files,
                                     @PathVariable Long shelfId,
                                     @PathVariable Long folderId) {


        fileService.saveFile(shelfId, folderId, files);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("File Uploaded", HttpStatus.OK.value()));
    }

    @PutMapping("/move-to-trash")
    public ResponseEntity<TextResponseMessage> softDeleteFile(@AuthenticationUser AuthUser user, @RequestBody List<Long> fileIds) {

        fileService.updateDeletedFiles(user, fileIds, true);
        return ResponseEntity.ok().body(new TextResponseMessage("File/s moved to trash.", HttpStatus.OK.value()));
    }
}
