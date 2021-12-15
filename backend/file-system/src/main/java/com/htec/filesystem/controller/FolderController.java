package com.htec.filesystem.controller;

import com.htec.filesystem.dto.FileDTO;
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

    @PostMapping("/initialize/{id}")
    public ResponseEntity initializeFolders(@PathVariable Long id) {

        HttpStatus retStatus = HttpStatus.OK;

        if (!folderService.initializeFolders(id)) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage("Folders created", retStatus.value()));
    }

    @GetMapping("/get/{folderId}")
    public ResponseEntity<List<FileDTO>> getFiles(@PathVariable Long folderId) {

        return folderService.getFiles(folderId);

    }


}
