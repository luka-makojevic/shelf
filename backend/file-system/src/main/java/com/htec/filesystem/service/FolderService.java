package com.htec.filesystem.service;

import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.FileMapper;
import com.htec.filesystem.model.request.CreateFolderRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FileTreeRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.FolderTreeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final FolderTreeRepository folderTreeRepository;
    private final FileTreeRepository fileTreeRepository;

    public FolderService(FolderRepository folderRepository,
                         FileRepository fileRepository,
                         FolderTreeRepository folderTreeRepository,
                         FileTreeRepository fileTreeRepository) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.folderTreeRepository = folderTreeRepository;
        this.fileTreeRepository = fileTreeRepository;
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

    public ResponseEntity<List<ShelfItemDTO>> getFiles(Long userId, Long folderId) {

        List<ShelfItemDTO> fileDTOS = new ArrayList<>();

        List<FolderEntity> allFolders = folderRepository
                .findAllByUserIdAndParentFolderIdAndIsDeleted(userId, folderId, false);

        List<FileEntity> allFiles = fileRepository
                .findAllByUserIdAndParentFolderIdAndIsDeleted(userId, folderId, false);

        fileDTOS.addAll(FileMapper.INSTANCE.fileEntitiesToShelfItemDTOs(allFiles));
        fileDTOS.addAll(FileMapper.INSTANCE.folderEntitiesToShelfItemDTOs(allFolders));

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

        if (parentFolderId == 0) {
            if (folderRepository.findByNameAndParentFolderId(name, null).isPresent())
                throw ExceptionSupplier.folderAlreadyExists.get();
        } else {
            if (folderRepository.findByNameAndParentFolderId(name, parentFolderId).isPresent())
                throw ExceptionSupplier.folderAlreadyExists.get();
        }

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

    @Transactional
    public void updateDeleted(Long userId, List<Long> folderIds, Boolean deleted) {

        List<FolderEntity> folderEntities = folderRepository.findByUserIdAndFolderIds(userId, folderIds);

        if (!folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()).containsAll(folderIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFolder.get();
        }

        List<FolderEntity> downStreamFolders = folderTreeRepository.getFolderDownStreamTrees(folderIds, !deleted);

        List<Long> downStreamFoldersIds = downStreamFolders.stream().map(FolderEntity::getId).collect(Collectors.toList());

        List<FileEntity> downStreamFiles = fileTreeRepository.getFileDownStreamTrees(folderIds, !deleted);

        List<Long> downStreamFilesIds = downStreamFiles.stream().map(FileEntity::getId).collect(Collectors.toList());

        folderRepository.updateDeletedByFolderIds(deleted, downStreamFoldersIds);

        fileRepository.updateDeletedByFileIds(deleted, downStreamFilesIds);
    }
}
