package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.FileMapper;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.TreeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FolderService {

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final TreeRepository treeRepository;

    public FolderService(FolderRepository folderRepository,
                         FileRepository fileRepository,
                         TreeRepository treeRepository) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.treeRepository = treeRepository;
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

    public ResponseEntity<List<FileDTO>> getFiles(Long userId, Long folderId) {

        List<FileDTO> fileDTOS = new ArrayList<>();

        List<FolderEntity> allFolders = folderRepository
                .findAllByUserIdAndParentFolderId(userId, folderId, false);

        List<FileEntity> allFiles = fileRepository
                .findAllByUserIdAndParentFolderId(userId, folderId, false);

        fileDTOS.addAll(FileMapper.INSTANCE.fileEntityToFileDTO(allFiles));
        fileDTOS.addAll(FileMapper.INSTANCE.folderEntityToFileDTO(allFolders));

        return ResponseEntity.status(HttpStatus.OK).body(fileDTOS);
    }

    public void updateDeleted(AuthUser user, @RequestBody List<Long> folderIds, Boolean isDeleted) {

        List<FolderEntity> folderEntities = folderRepository.findByUserIdAndFolderId(user.getId(), folderIds);

        if (!folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()).containsAll(folderIds)) {
            throw ExceptionSupplier.userNotAllowed.get();
        }

        List<Long> folderIdsToBeDeleted = folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList());

        folderRepository.updateDeletedByParentFolderIds(isDeleted, folderIdsToBeDeleted);

        fileRepository.updateDeletedByParentFolderIds(isDeleted, folderIdsToBeDeleted);
    }
}
