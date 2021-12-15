package com.htec.filesystem.service;

import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.mapper.FileMapper;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    public FolderService(FolderRepository folderRepository, FileRepository fileRepository) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
    }

    public boolean initializeFolders(Long userId) {

        String userDataPath = homePath + userPath + userId;

        if (!new File(userDataPath).mkdirs()) {
            return false;
        }

        String userProfilePicturePath = userDataPath + pathSeparator + "profile-picture";

        if (!new File(userProfilePicturePath).mkdirs()) {
            return false;
        }

        String userShelvesPath = userDataPath + pathSeparator + "shelves";

        return new File(userShelvesPath).mkdirs();
    }

    public ResponseEntity<List<FileDTO>> getFiles(Long folderId) {

        List<FileDTO> fileDTOS = new ArrayList<>();

        List<FolderEntity> allFolders = folderRepository.findAllByParentFolderId(folderId);
        List<FileEntity> allFiles = fileRepository.findAllByParentFolderId(folderId);

        fileDTOS.addAll(FileMapper.INSTANCE.fileEntityToFileDTO(allFiles));
        fileDTOS.addAll(FileMapper.INSTANCE.folderEntityToFileDTO(allFolders));

        fileDTOS.removeIf(FileDTO::isDeleted);

        return ResponseEntity.status(HttpStatus.OK).body(fileDTOS);
    }

}
