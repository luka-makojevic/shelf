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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                .findAllByUserIdAndParentFolderIdAndDeleted(userId, folderId, false);

        List<FileEntity> allFiles = fileRepository
                .findAllByUserIdAndParentFolderIdAndDeleted(userId, folderId, false);


        itemDTOs.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(allFiles));
        itemDTOs.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(allFolders));

        List<BreadCrumbDTO> breadCrumbDTOS = generateBreadCrumbs(folderId);

        return ResponseEntity.status(HttpStatus.OK).body(new ShelfContentResponseModel(breadCrumbDTOS, itemDTOs));
    }

    private List<BreadCrumbDTO> generateBreadCrumbs(Long folderId) {

        List<FolderEntity> folderUpStreamTree = folderTreeRepository.getFolderUpStreamTree(folderId);

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
            if (folderRepository.findByNameAndParentFolderIdAndShelfId(name, null, shelfId).isPresent())
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
        folderEntity.setCreatedAt(LocalDateTime.now());

        folderRepository.save(folderEntity);
    }

    @Transactional
    public void moveToTrash(Long userId, List<Long> folderIds) {


    }

}
