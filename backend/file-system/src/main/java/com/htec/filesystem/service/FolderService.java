package com.htec.filesystem.service;

import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.FileMapper;
import com.htec.filesystem.model.request.CreateFolderRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
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

    public boolean createFolder(CreateFolderRequestModel createFolderRequestModel, Long userId) {

        Long parentFolderId = createFolderRequestModel.getParentFolderId();
        Long shelfId = createFolderRequestModel.getShelfId();
        String folderName = createFolderRequestModel.getFolderName();
        String fileSystemPath = homePath + userPath;
        String dbPath;


        if (parentFolderId != 0) {

            FolderEntity folderEntity = folderRepository.findById(parentFolderId).orElseThrow(ExceptionSupplier.noFolderWithGivenId);
            fileSystemPath += folderEntity.getPath() + pathSeparator;
            dbPath = folderEntity.getPath() + pathSeparator;
        } else {

            dbPath = userId + pathSeparator + "shelves" + pathSeparator + shelfId + pathSeparator;
            fileSystemPath += userId + pathSeparator + "shelves" + pathSeparator + shelfId + pathSeparator;
        }

        dbPath += folderName;
        fileSystemPath += folderName;

        createFolderInDb(folderName, dbPath, shelfId, parentFolderId);

        return new File(fileSystemPath).mkdirs();
    }

    public void createFolderInDb(String name, String path, Long shelfId, Long parentFolderId) {

        if (folderRepository.findByPath(path).isPresent())
            throw ExceptionSupplier.folderAlreadyExists.get();

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setName(name);
        folderEntity.setPath(path);
        if (parentFolderId != 0)
            folderEntity.setParentFolderId(parentFolderId);
        folderEntity.setShelfId(shelfId);
        folderEntity.setDeleted(false);
        folderEntity.setCreatedAt(LocalDateTime.now());

        folderRepository.save(folderEntity);
    }
}
