package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.model.request.RenameFileRequestModel;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FileService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    private final String FILE_UPLOADED = "File Uploaded";
    private final String FILE_DOWNLOADED = "File Downloaded";
    private final String FILE_RENAMED = "File renamed";
    private final String IMAGE_UPLOADED = "Image Uploaded";
    private final String FILES_MOVED_TO_TRASH = "File/s moved to trash.";
    private final String FILES_RECOVERED_FROM_TRASH = "File/s recovered from trash.";
    private final String FILES_DELETED = "File/s deleted.";

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload/profile/{id}")
    public ResponseEntity<TextResponseMessage> uploadProfilePicture(@RequestBody Map<String, Pair<String, String>> files,
                                                                    @PathVariable Long id) {

        fileService.saveUserProfilePicture(id, files);
        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(IMAGE_UPLOADED, HttpStatus.OK.value()));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getFile(@AuthenticationUser AuthUser user,
                                          @PathVariable Long id,
                                          @RequestParam(value = "file") boolean file) {

        FileResponseModel fileResponseModel = fileService.getFile(user.getId(), id, file);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(fileResponseModel.getImageContent());
    }

    @GetMapping(value = "/zip-download")
    public ResponseEntity<TextResponseMessage> zipDownload(@AuthenticationUser AuthUser user,
                                                           @RequestParam List<Long> fileIds) {

        fileService.downloadFilesToZip(user, fileIds);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + "asde" + "\"")
                .body(new TextResponseMessage(IMAGE_UPLOADED, HttpStatus.OK.value()));
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id,
                                           @RequestParam(value = "file") boolean file) {

        FileResponseModel fileResponseModel = fileService.getFile(null, id, file);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileResponseModel.getImageContent());
    }

    @PostMapping("/upload/{shelfId}/{folderId}")
    public ResponseEntity<TextResponseMessage> uploadFile(@RequestBody Map<String, Pair<String, String>> files,
                                                          @PathVariable Long shelfId,
                                                          @PathVariable Long folderId,
                                                          @AuthenticationUser AuthUser authUser) {

        fileService.saveFile(shelfId, folderId, files, authUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FILE_UPLOADED, HttpStatus.OK.value()));
    }

    @PutMapping("/move-to-trash")
    public ResponseEntity<TextResponseMessage> softDeleteFile(@AuthenticationUser AuthUser user, @RequestBody List<Long> fileIds) {

        fileService.updateDeletedFiles(user, fileIds, true, true);
        return ResponseEntity.ok().body(new TextResponseMessage(FILES_MOVED_TO_TRASH, HttpStatus.OK.value()));
    }

//    @PutMapping("/recover")
//    public ResponseEntity<TextResponseMessage> recoverSoftDeletedFile(@AuthenticationUser AuthUser user,
//                                                                      @RequestBody List<Long> fileIds) {
//
//        fileService.updateDeletedFiles(user, fileIds, false);
//        return ResponseEntity.ok().body(new TextResponseMessage(FILES_RECOVERED_FROM_TRASH, HttpStatus.OK.value()));
//    }

    @PutMapping("/rename")
    public ResponseEntity<TextResponseMessage> renameFile(@AuthenticationUser AuthUser user,
                                                          @RequestBody RenameFileRequestModel renameFileRequestModel) {

        fileService.fileRename(user.getId(), renameFileRequestModel);
        return ResponseEntity.ok().body(new TextResponseMessage(FILE_RENAMED, HttpStatus.OK.value()));
    }

    @DeleteMapping
    public ResponseEntity<TextResponseMessage> deleteFile(@AuthenticationUser AuthUser user, @RequestBody List<Long> fileIds) throws IOException {

        fileService.deleteFile(user, fileIds);
        return ResponseEntity.ok().body(new TextResponseMessage(FILES_DELETED, HttpStatus.OK.value()));
    }
}
