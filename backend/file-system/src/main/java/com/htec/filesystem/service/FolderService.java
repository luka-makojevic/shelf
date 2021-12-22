package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.BreadCrumbDTO;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.BreadCrumbsMapper;
import com.htec.filesystem.mapper.ShelfItemMapper;
import com.htec.filesystem.model.request.CreateFolderRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FileTreeRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.FolderTreeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    private final FileService fileService;

    public FolderService(FolderRepository folderRepository,
                         FileRepository fileRepository,
                         FolderTreeRepository folderTreeRepository,
                         FileTreeRepository fileTreeRepository,
                         FileService fileService) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.folderTreeRepository = folderTreeRepository;
        this.fileTreeRepository = fileTreeRepository;
        this.fileService = fileService;
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

    public ResponseEntity<ShelfContentResponseModel> getFiles(Long userId, Long folderId) {

        List<ShelfItemDTO> itemDTOs = new ArrayList<>();

        List<FolderEntity> allFolders = folderRepository
                .findAllByUserIdAndParentFolderIdAndIsDeleted(userId, folderId, false);

        List<FileEntity> allFiles = fileRepository
                .findAllByUserIdAndParentFolderIdAndIsDeleted(userId, folderId, false);

        itemDTOs.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(allFiles));
        itemDTOs.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(allFolders));

        List<BreadCrumbDTO> breadCrumbDTOS = generateBreadCrumbs(folderId);

        return ResponseEntity.status(HttpStatus.OK).body(new ShelfContentResponseModel(breadCrumbDTOS, itemDTOs));
    }

    private List<BreadCrumbDTO> generateBreadCrumbs(Long folderId) {

        List<FolderEntity> folderUpStreamTree = folderTreeRepository.getFolderUpStreamTree(folderId, false);

        List<BreadCrumbDTO> breadCrumbs = new ArrayList<>(
                BreadCrumbsMapper.INSTANCE.folderEntitiesToBreadCrumbDTOs(folderUpStreamTree));

        Collections.reverse(breadCrumbs);

        ShelfEntity shelfEntity = folderRepository.getShelfByFolderId(folderId)
                .orElseThrow(ExceptionSupplier.shelfNotFound);

        breadCrumbs.add(0, new BreadCrumbDTO(shelfEntity.getName(), shelfEntity.getId()));

        return breadCrumbs;
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
    public void deleteFolder(AuthUser authUser, List<Long> folderIds) {

        List<FolderEntity> folderEntities = folderRepository.findByUserIdAndFolderIds(authUser.getId(), folderIds);

        if (!folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()).containsAll(folderIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFolder.get();
        }

        List<FileEntity> filesNotToBeDeleted = fileTreeRepository.getFileDownStreamTrees(folderIds, true);
        List<Long> filesNotToBeDeletedIds = filesNotToBeDeleted.stream().map(FileEntity::getId).collect(Collectors.toList());

        List<FolderEntity> downStreamFolders = folderTreeRepository.getFolderDownStreamTrees(folderIds, false);
        List<Long> downStreamFoldersIds = downStreamFolders.stream().map(FolderEntity::getId).collect(Collectors.toList());

        moveTrashFoldersToBaseShelfDirectory(filesNotToBeDeleted);

        folderRepository.deletedByFolderIdsIn(downStreamFoldersIds);

        fileRepository.deletedByParentFolderIdsInAndIdNotIn(downStreamFoldersIds, filesNotToBeDeletedIds);

        fileRepository.saveAll(filesNotToBeDeleted);
    }

    private void moveTrashFoldersToBaseShelfDirectory(List<FileEntity> filesNotToBeDeleted) {
        try {
            for (FileEntity fileEntity : filesNotToBeDeleted) {

                String path = fileEntity.getPath();
                int index = StringUtils.ordinalIndexOf(path, "/", 3);

                String newPath = homePath + userPath + path.substring(0, index);
                String oldPath = homePath + userPath + path;

                Files.move(Paths.get(oldPath), Paths.get(newPath));

                fileEntity.setPath(newPath);
            }
        } catch (IOException e) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }
}
