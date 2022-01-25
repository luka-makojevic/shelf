package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.model.request.LogRequestModel;
import com.htec.filesystem.model.request.RenameFileRequestModel;
import com.htec.filesystem.model.response.FileResponseModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.EventService;
import com.htec.filesystem.service.FileService;
import com.htec.filesystem.util.FunctionEvents;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final EventService eventService;

    private final String FILE_UPLOADED = "File/s Uploaded";
    private final String FILE_COPIED = "File Copied";
    private final String FILE_LOGGED = "File logged";
    private final String FILE_RENAMED = "File renamed";
    private final String IMAGE_UPLOADED = "Image Uploaded";
    private final String FILES_MOVED_TO_TRASH = "File/s moved to trash.";
    private final String FILES_RECOVERED_FROM_TRASH = "File/s recovered from trash.";
    private final String FILES_DELETED = "File/s deleted.";

    public FileController(FileService fileService,
                          EventService eventService) {
        this.fileService = fileService;
        this.eventService = eventService;
    }


    @PostMapping("/upload/profile/{id}")
    public ResponseEntity<TextResponseMessage> uploadProfilePicture(@RequestBody Map<String, Pair<String, String>> files, @PathVariable Long id) {

        fileService.saveUserProfilePicture(id, files);
        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(IMAGE_UPLOADED, HttpStatus.OK.value()));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getFile(@AuthenticationUser AuthUser user,
                                          @PathVariable Long id,
                                          @RequestParam(value = "file") boolean file) {

        FileResponseModel fileResponseModel = fileService.getFile(user.getId(), id, file);

        eventService.reportEvent(FunctionEvents.DOWNLOAD, Collections.singletonList(id), user.getId(), null, null);

        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(fileResponseModel.getImageContent());
    }

    @GetMapping(value = "/zip-download")
    public ResponseEntity<StreamingResponseBody> zipDownload(@AuthenticationUser AuthUser user,
                                                             @RequestParam(required = false) List<Long> fileIds,
                                                             @RequestParam(required = false) List<Long> folderIds) {

        List<Long> fileIdsToReport = fileService.getFileEntities(user.getId(), fileIds, folderIds)
                .stream().map(FileEntity::getId).collect(Collectors.toList());

        eventService.reportEvent(FunctionEvents.DOWNLOAD, fileIdsToReport, user.getId(), null, null);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.zip")
                .body(outputStream -> fileService.downloadFilesToZip(user.getId(), fileIds, folderIds, outputStream));
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

        List<Long> newFileIds = fileService.saveFile(shelfId, folderId, files, authUser.getId());

        eventService.reportEvent(FunctionEvents.UPLOAD, newFileIds, authUser.getId(), shelfId, null);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FILE_UPLOADED, HttpStatus.OK.value()));
    }

    @PostMapping("/log/{shelfId}")
    public ResponseEntity<Long> getLogFileId(@RequestBody String logFileName,
                                             @PathVariable Long shelfId,
                                             @AuthenticationUser AuthUser authUser) {

        Long logFileId = fileService.getLogFileId(shelfId, logFileName, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(logFileId);
    }

    @PostMapping("/copy/{fileId}/shelf/{shelfId}/user/{userId}")
    public ResponseEntity<TextResponseMessage> copyFile(@PathVariable Long fileId,
                                                        @PathVariable Long shelfId,
                                                        @PathVariable Long userId) {

        fileService.copyFile(fileId, shelfId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FILE_COPIED, HttpStatus.OK.value()));
    }

    @PutMapping("/log/event/backupFileId/{backupFileId}")
    public ResponseEntity<TextResponseMessage> logFile(@PathVariable Long backupFileId,
                                                       @RequestBody LogRequestModel logRequestModel) {

        fileService.logFile(backupFileId, logRequestModel);
        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FILE_LOGGED, HttpStatus.OK.value()));
    }

    @PutMapping("/move-to-trash")
    public ResponseEntity<TextResponseMessage> softDeleteFile(@AuthenticationUser AuthUser user, @RequestBody List<Long> fileIds) {

        fileService.updateDeletedFiles(user, fileIds, true, true);
        return ResponseEntity.ok().body(new TextResponseMessage(FILES_MOVED_TO_TRASH, HttpStatus.OK.value()));
    }

    @PutMapping("/rename")
    public ResponseEntity<TextResponseMessage> renameFile(@AuthenticationUser AuthUser user,
                                                          @RequestBody RenameFileRequestModel renameFileRequestModel) {

        fileService.fileRename(user.getId(), renameFileRequestModel);
        return ResponseEntity.ok().body(new TextResponseMessage(FILE_RENAMED, HttpStatus.OK.value()));
    }

    @DeleteMapping
    public ResponseEntity<TextResponseMessage> deleteFile(@AuthenticationUser AuthUser user, @RequestBody List<Long> fileIds) {

        Map<Long, String> filesToDelete = fileService.getFileNamesFromIds(fileIds);
        Long deletedFilesShelfId = fileService.deleteFile(user.getId(), fileIds);

        eventService.reportEvent(FunctionEvents.DELETE, fileIds, user.getId(), deletedFilesShelfId, filesToDelete);

        return ResponseEntity.ok().body(new TextResponseMessage(FILES_DELETED, HttpStatus.OK.value()));
    }

    @PutMapping("/recover")
    public ResponseEntity<TextResponseMessage> recoverFile(@AuthenticationUser AuthUser user, @RequestBody List<Long> fileIds) {

        fileService.updateDeletedFiles(user, fileIds, false, true);
        return ResponseEntity.ok().body(new TextResponseMessage(FILES_RECOVERED_FROM_TRASH, HttpStatus.OK.value()));
    }
}
