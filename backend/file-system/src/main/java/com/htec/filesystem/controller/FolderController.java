package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.model.request.CreateFolderRequestModel;
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

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/initialize/{userId}")
    public ResponseEntity initializeFolders(@PathVariable Long userId) {

        HttpStatus retStatus = HttpStatus.OK;

        if (!folderService.initializeFolders(userId)) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage("Folders created", retStatus.value()));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<List<FileDTO>> getFiles(@AuthenticationUser AuthUser user, @PathVariable Long folderId) {

        return folderService.getFiles(user.getId(), folderId);
    }

    @PostMapping("/create")
    public ResponseEntity createFolder(@RequestBody CreateFolderRequestModel createFolderRequestModel, @AuthenticationUser AuthUser authUser) {

        HttpStatus retStatus = HttpStatus.OK;

        if (!folderService.createFolder(createFolderRequestModel, authUser.getId())) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage("Folder created", retStatus.value()));
    }
}
