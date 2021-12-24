package com.htec.filesystem.service;

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
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
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

        String userTrashPath = userDataPath + pathSeparator + "trash";

        if (!new File(userTrashPath).mkdirs()) {
            return false;
        }

        String userShelvesPath = userDataPath + pathSeparator + "shelves";

        return new File(userShelvesPath).mkdirs();
    }

    public ResponseEntity<ShelfContentResponseModel> getItems(Long userId, Long folderId, Boolean deleted) {

        List<ShelfItemDTO> itemDTOs = new ArrayList<>();

        List<FolderEntity> allFolders = folderRepository
                .findAllByUserIdAndParentFolderIdAndDeleted(userId, folderId, deleted);

        List<FileEntity> allFiles = fileRepository
                .findAllByUserIdAndParentFolderIdAndDeleted(userId, folderId, deleted);


        itemDTOs.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(allFiles));
        itemDTOs.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(allFolders));

        List<BreadCrumbDTO> breadCrumbDTOS = generateBreadCrumbs(folderId, deleted);

        return ResponseEntity.status(HttpStatus.OK).body(new ShelfContentResponseModel(breadCrumbDTOS, itemDTOs));
    }

    private List<BreadCrumbDTO> generateBreadCrumbs(Long folderId, Boolean deleted) {

        List<FolderEntity> folderUpStreamTree = folderTreeRepository.getFolderUpStreamTree(folderId , deleted);

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

        Long newFolderId = createFolderInDb(folderName, dbPath, shelfId, parentFolderId);

        dbPath += newFolderId;
        fileSystemPath += newFolderId;

        return new File(fileSystemPath).mkdirs();
    }

    public Long createFolderInDb(String name, String path, Long shelfId, Long parentFolderId) {

        if (parentFolderId == 0) {
            if (folderRepository.findByNameAndParentFolderIdAndShelfId(name, null, shelfId).isPresent())
                throw ExceptionSupplier.folderAlreadyExists.get();
        } else {
            if (folderRepository.findByNameAndParentFolderId(name, parentFolderId).isPresent())
                throw ExceptionSupplier.folderAlreadyExists.get();
        }

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setName(name);
        folderEntity.setPath(path);
        folderEntity.setDeleted(false);

        if (parentFolderId != 0)
            folderEntity.setParentFolderId(parentFolderId);

        folderEntity.setShelfId(shelfId);
        folderEntity.setCreatedAt(LocalDateTime.now());

        FolderEntity createdFolder = folderRepository.save(folderEntity);

        folderEntity.setPath(folderEntity.getPath() + createdFolder.getId());

        folderRepository.save(folderEntity);

        return folderEntity.getId();
    }

    @Transactional
    public void updateDeletedFolders(Long userId, List<Long> folderIds, Boolean deleted) {

        List<FolderEntity> folderEntities = folderRepository.findByUserIdAndFolderIds(userId, folderIds);

        if (!folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()).containsAll(folderIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFolder.get();
        }

        List<FolderEntity> downStreamFolders = folderTreeRepository.getFolderDownStreamTrees(folderIds, !deleted);

        List<Long> downStreamFoldersIds = downStreamFolders.stream().map(FolderEntity::getId).collect(Collectors.toList());

        List<FileEntity> downStreamFiles = fileRepository.findAllByParentFolderIdIn(downStreamFoldersIds);

        List<Long> downStreamFilesIds = downStreamFiles.stream().map(FileEntity::getId).collect(Collectors.toList());

        if (deleted) {

            moveFoldersToTrash(folderEntities);

            replaceFoldersShelfPath(downStreamFolders);
            replaceFilesShelfPath(downStreamFiles);

        } else {
            // todo: recover(folderEntities);
        }

        downStreamFolders.forEach(folderEntity -> folderEntity.setDeleted(deleted));

        downStreamFiles.forEach(file -> file.setDeleted(deleted));

        folderRepository.saveAll(folderEntities);
        fileRepository.saveAll(downStreamFiles);

        folderRepository.updateTrashVisibleByFolderIdIn(true, folderIds);
    }

    private void replaceFoldersShelfPath(List<FolderEntity> folders) {
        for (FolderEntity folder : folders) {
            String path = folder.getPath();
            folder.setPath(path.replace("shelves" + pathSeparator + folder.getShelfId(), "trash"));
        }
    }

    private void replaceFilesShelfPath(List<FileEntity> files) {
        for (FileEntity file : files) {
            String path = file.getPath();
            file.setPath(path.replace("shelves" + pathSeparator + file.getShelfId(), "trash"));
        }
    }

    private void moveFoldersToTrash(List<FolderEntity> folderEntities) {

        try {
            for (FolderEntity folderEntity : folderEntities) {

                String path = folderEntity.getPath();
                Long shelfId = folderEntity.getShelfId();

                String oldPath = homePath + userPath + path;
                String newPath = oldPath.replace("shelves" + pathSeparator + shelfId, "trash");

                File from = new File(oldPath);
                File to = new File(newPath);

                FileUtils.moveDirectory(from, to);
            }
        } catch (IOException ex) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }

}
