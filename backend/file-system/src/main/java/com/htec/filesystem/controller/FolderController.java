package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.model.request.CreateFolderRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FolderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;

    private final String FOLDERS_CREATED = "Folders created";
    private final String FOLDERS_MOVED_TO_TRASH = "Folder moved to trash";
    private final String FOLDERS_RECOVERED_FROM_TRASH = "Folders recovered from trash";

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/initialize/{userId}")
    public ResponseEntity<TextResponseMessage> initializeFolders(@PathVariable Long userId) {

        HttpStatus retStatus = HttpStatus.OK;

        if (!folderService.initializeFolders(userId)) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage(FOLDERS_CREATED, retStatus.value()));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<ShelfContentResponseModel> getFiles(@AuthenticationUser AuthUser user, @PathVariable Long folderId) {
        return folderService.getFiles(user.getId(), folderId);
    }

    @PostMapping
    public ResponseEntity<TextResponseMessage> createFolder(@RequestBody CreateFolderRequestModel createFolderRequestModel, @AuthenticationUser AuthUser authUser) {

        HttpStatus retStatus = HttpStatus.OK;

        if (!folderService.createFolder(createFolderRequestModel, authUser.getId())) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage("Folder created", retStatus.value()));
    }

    @PutMapping("/move-to-trash")
    public ResponseEntity<TextResponseMessage> moveToTrash(@AuthenticationUser AuthUser user, @RequestBody List<Long> folderIds) {

        folderService.updateDeleted(user.getId(), folderIds, true);

        return ResponseEntity.ok().body(new TextResponseMessage(FOLDERS_MOVED_TO_TRASH, HttpStatus.OK.value()));
    }

    @PutMapping("/recover")
    public ResponseEntity<TextResponseMessage> recover(@AuthenticationUser AuthUser user, @RequestBody List<Long> folderIds) {

        folderService.updateDeleted(user.getId(), folderIds, false);

        return ResponseEntity.ok().body(new TextResponseMessage(FOLDERS_RECOVERED_FROM_TRASH, HttpStatus.OK.value()));
    }
}
