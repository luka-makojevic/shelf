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
import com.htec.filesystem.model.request.RenameFolderRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.*;
import com.htec.filesystem.validator.FileSystemValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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
    private final ShelfRepository shelfRepository;
    private final FileSystemValidator fileSystemValidator;

    private final FileService fileService;

    public FolderService(FolderRepository folderRepository,
                         FileRepository fileRepository,
                         FolderTreeRepository folderTreeRepository,
                         FileTreeRepository fileTreeRepository,
                         FileService fileService,
                         ShelfRepository shelfRepository,
                         FileSystemValidator fileSystemValidator1) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.folderTreeRepository = folderTreeRepository;
        this.fileTreeRepository = fileTreeRepository;
        this.fileService = fileService;
        this.shelfRepository = shelfRepository;
        this.fileSystemValidator = fileSystemValidator1;
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

        if (!new File(userShelvesPath).mkdirs()) {
            return false;
        }

        String userFunctionsPath = userDataPath + pathSeparator + "functions";

        return new File(userFunctionsPath).mkdirs();
    }

    public ResponseEntity<ShelfContentResponseModel> getItems(Long userId, Long folderId, Boolean deleted) {

        List<ShelfItemDTO> itemDTOs = new ArrayList<>();

        List<FolderEntity> allFolders = folderRepository
                .findAllByUserIdAndParentFolderIdAndDeleted(userId, folderId, deleted);

        List<FileEntity> allFiles = fileRepository
                .findAllByUserIdAndParentFolderIdAndDeleted(userId, folderId, deleted);

        for (FileEntity fileEntity : allFiles) {
            fileEntity.setName(fileEntity.getRealName());
        }

        itemDTOs.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(allFiles));
        itemDTOs.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(allFolders));

        List<BreadCrumbDTO> breadCrumbDTOS = generateBreadCrumbs(folderId, deleted);

        return ResponseEntity.status(HttpStatus.OK).body(new ShelfContentResponseModel(breadCrumbDTOS, itemDTOs));
    }

    private List<BreadCrumbDTO> generateBreadCrumbs(Long folderId, Boolean deleted) {

        List<FolderEntity> folderUpStreamTree = folderTreeRepository.getFolderUpStreamTree(folderId, deleted);

        List<BreadCrumbDTO> breadCrumbs = new ArrayList<>(
                BreadCrumbsMapper.INSTANCE.folderEntitiesToBreadCrumbDTOs(folderUpStreamTree));

        Collections.reverse(breadCrumbs);

        ShelfEntity shelfEntity = folderRepository.getShelfByFolderId(folderId)
                .orElseThrow(ExceptionSupplier.shelfNotFound);

        if (!deleted) {
            breadCrumbs.add(0, new BreadCrumbDTO(shelfEntity.getName(), shelfEntity.getId()));
        } else {
            breadCrumbs.add(0, new BreadCrumbDTO("trash", shelfEntity.getId()));
        }

        return breadCrumbs;
    }

    public boolean createFolder(CreateFolderRequestModel createFolderRequestModel, Long userId) {

        fileSystemValidator.isFolderNameValid(createFolderRequestModel.getFolderName());

        Long parentFolderId = createFolderRequestModel.getParentFolderId();
        Long shelfId = createFolderRequestModel.getShelfId();
        String folderName = createFolderRequestModel.getFolderName();
        String fileSystemPath = homePath + userPath;
        String dbPath;

        ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);
        if (!Objects.equals(shelfEntity.getUserId(), userId)) {
            throw ExceptionSupplier.userNotAllowedToAccessShelf.get();
        }

        if (parentFolderId != 0) {

            FolderEntity folderEntity = folderRepository.findById(parentFolderId)
                    .orElseThrow(ExceptionSupplier.noFolderWithGivenId);
            if (!Objects.equals(folderEntity.getShelfId(), shelfId)) {
                throw ExceptionSupplier.folderIsNotInGivenShelf.get();
            }
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
            if (folderRepository.findByNameAndParentFolderIdAndShelfIdAndDeleted(name, null, shelfId, false).isPresent())
                throw ExceptionSupplier.folderAlreadyExists.get();
        } else {
            if (folderRepository.findByNameAndParentFolderIdAndDeleted(name, parentFolderId, false).isPresent())
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

        if (deleted) {

            moveFoldersToTrash(folderEntities);

            replaceFoldersShelfPath(downStreamFolders);
            replaceFilesShelfPath(downStreamFiles);
        } else {

            recoverFoldersFromTrash(folderEntities);

            deleteDummyFolders(folderIds);

            List<Long> parentIds = downStreamFolders.stream().map(FolderEntity::getParentFolderId)
                    .collect(Collectors.toList());
            parentIds.addAll(downStreamFiles.stream().map(FileEntity::getParentFolderId)
                    .collect(Collectors.toList()));

            List<FolderEntity> parentFolders = folderRepository.findAllById(parentIds);

            Map<Long, FolderEntity> parentFoldersMap = parentFolders.stream()
                    .collect(Collectors.toMap(FolderEntity::getId, Function.identity()));

            downStreamFiles = downStreamFiles.stream()
                    .filter(file -> file.getTrashVisible() == null || !file.getTrashVisible()).collect(Collectors.toList());

            replaceFoldersTrashPath(downStreamFolders, parentFoldersMap);
            replaceFilesTrashPath(downStreamFiles, parentFoldersMap);
        }

        folderRepository.saveAll(folderEntities);
        fileRepository.saveAll(downStreamFiles);

        folderRepository.updateTrashVisibleByFolderIdIn(deleted, folderIds);
    }

    private void deleteDummyFolders(List<Long> folderIds) {
        try {
            Set<FolderEntity> upStreamFolders = folderTreeRepository.getFolderUpStreamTrees(folderIds, false)
                    .stream()
                    .filter(folderEntity -> folderEntity.getParentFolderId() == null)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            for (FolderEntity upStreamFolder : upStreamFolders) {
                FileUtils.deleteDirectory(
                        new File(homePath +
                                userPath +
                                upStreamFolder.getPath()
                                        .replace("shelves" + pathSeparator + upStreamFolder.getShelfId(), "trash")));
            }
        } catch (IOException e) {
            throw ExceptionSupplier.internalServerError.get();
        }

    }

    private void replaceFilesTrashPath(List<FileEntity> files, Map<Long, FolderEntity> parentFoldersMap) {
        for (FileEntity file : files) {

            FolderEntity parentFolderEntity = parentFoldersMap.get(file.getParentFolderId());
            String newPath;

            if (parentFolderEntity != null && !parentFolderEntity.getDeleted()) {

                newPath = (parentFolderEntity.getPath()
                        + pathSeparator
                        + file.getName())
                        .replace("trash", "shelves" + pathSeparator + file.getShelfId() + pathSeparator + file.getId());
            } else {

                String oldPath = file.getPath();
                newPath = (oldPath.substring(0, StringUtils.ordinalIndexOf(oldPath, pathSeparator, 2))
                        + pathSeparator
                        + file.getName())
                        .replace("trash", "shelves" + pathSeparator + file.getShelfId());

                file.setParentFolderId(null);
            }

            if (file.getTrashVisible() != null) {
                file.setName(file.getRealName());
            }
            file.setDeletedAt(null);
            file.setTrashVisible(false);
            file.setDeleted(false);
            file.setPath(newPath);
        }
    }

    private void replaceFoldersTrashPath(List<FolderEntity> folders, Map<Long, FolderEntity> parentFoldersMap) {
        for (FolderEntity folder : folders) {

            FolderEntity parentFolderEntity = parentFoldersMap.get(folder.getParentFolderId());
            String newPath;

            if (parentFolderEntity != null && !parentFolderEntity.getDeleted()) {

                newPath = (parentFolderEntity.getPath() + pathSeparator + folder.getId())
                        .replace("trash", "shelves" + pathSeparator + folder.getShelfId());
            } else {

                String oldPath = folder.getPath();
                newPath = (oldPath.substring(0, StringUtils.ordinalIndexOf(oldPath, pathSeparator, 2))
                        + pathSeparator + folder.getId())
                        .replace("trash", "shelves" + pathSeparator + folder.getShelfId());

                folder.setParentFolderId(null);
            }

            folder.setDeletedAt(null);
            folder.setTrashVisible(false);
            folder.setDeleted(false);
            folder.setPath(newPath);
        }
    }

    private void recoverFoldersFromTrash(List<FolderEntity> folderEntities) {
        try {
            for (FolderEntity folderEntity : folderEntities) {

                String path = folderEntity.getPath();

                Long shelfId = folderEntity.getShelfId();

                String oldPath = homePath + userPath + path;

                Long parentFolderId = folderEntity.getParentFolderId();

                if (parentFolderId != null) {

                    Optional<FolderEntity> parentFolderOptional = folderRepository.findById(parentFolderId);

                    if (!parentFolderOptional.isPresent() || parentFolderOptional.get().getDeleted()) {
                        path = path.substring(0, StringUtils.ordinalIndexOf(path, pathSeparator, 2));
                        folderEntity.setParentFolderId(null);
                    }
                }

                String newPath = (homePath + userPath + path).replace("trash", "shelves" + pathSeparator + shelfId);

                File from = new File(oldPath);
                File to = new File(newPath);

                if (!to.exists()) {

                    FileUtils.moveDirectory(from, to);
                } else {

                    moveFoldersToExistingShelfFolder(folderEntity, shelfId);

                    FileUtils.deleteDirectory(from);
                }
            }
        } catch (IOException ex) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }

    private void replaceFoldersShelfPath(List<FolderEntity> folders) {
        for (FolderEntity folder : folders) {
            String path = folder.getPath();
            folder.setPath(path.replace("shelves" + pathSeparator + folder.getShelfId(), "trash"));
            folder.setDeleted(true);
            folder.setDeletedAt(LocalDateTime.now());
        }
    }

    private void replaceFilesShelfPath(List<FileEntity> files) {
        for (FileEntity file : files) {
            String path = file.getPath();
            file.setPath(path.replace("shelves" + pathSeparator + file.getShelfId(), "trash"));
            file.setDeleted(true);
            file.setDeletedAt(LocalDateTime.now());
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

                if (!to.exists()) {

                    FileUtils.moveDirectory(from, to);
                } else {

                    moveFoldersToExistingTrashFolder(folderEntity, shelfId);

                    FileUtils.deleteDirectory(from);
                }
            }
        } catch (IOException ex) {
            throw ExceptionSupplier.internalServerError.get();
        }
    }

    public void hardDeleteFolder(Long userId, List<Long> folderIds) {

        List<FolderEntity> folderEntities = folderRepository.findByUserIdAndFolderIdsAndDeleted(userId, folderIds, true);

        if (!folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()).containsAll(folderIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteFolder.get();
        }

        List<FolderEntity> downStreamFolders = folderTreeRepository.getFolderDownStreamTrees(folderIds, true);

        List<Long> downStreamFoldersIds = downStreamFolders.stream().map(FolderEntity::getId).collect(Collectors.toList());

        List<FileEntity> downStreamFiles = fileRepository.findAllByParentFolderIdIn(downStreamFoldersIds);

        try {
            for (FolderEntity folderEntity : folderEntities) {

                FileUtils.deleteDirectory(new File(homePath + userPath + folderEntity.getPath()));
            }

            deleteDummyFolders(folderIds);

            downStreamFiles = downStreamFiles.stream()
                    .filter(file -> file.getTrashVisible() == null || !file.getTrashVisible()).collect(Collectors.toList());

            downStreamFiles.forEach(fileEntity -> {
                try {
                    fileRepository.delete(fileEntity);
                } catch (Exception ignored) {

                }
            });
            folderRepository.deleteAllInBatch(downStreamFolders);
        } catch (IOException e) {
            throw ExceptionSupplier.couldNotDeleteFolder.get();
        }
    }

    private void moveFoldersToExistingShelfFolder(FolderEntity folderEntity, Long shelfId) throws IOException {
        List<FolderEntity> foldersInsideExistingFolder = folderRepository
                .findAllByParentFolderIdAndDeleted(folderEntity.getId(), true);

        for (FolderEntity folder : foldersInsideExistingFolder) {

            String oldPathFolder = homePath + userPath + folder.getPath();
            String newPathFolder = oldPathFolder.replace("trash", "shelves" + pathSeparator + shelfId);

            File fromFolder = new File(oldPathFolder);
            File toFolder = new File(newPathFolder);

            if (!toFolder.exists()) {

                FileUtils.moveDirectory(fromFolder, toFolder);
            } else {

                moveFoldersToExistingShelfFolder(folder, shelfId);
            }
        }

        List<FileEntity> filesInsideExistingFolder = fileRepository
                .findAllByParentFolderIdInAndDeleted(foldersInsideExistingFolder
                        .stream()
                        .map(FolderEntity::getId)
                        .collect(Collectors.toList()), true);

        for (FileEntity file : filesInsideExistingFolder) {

            String oldPathFile = homePath + userPath + file.getPath();
            String newPathFile = oldPathFile.replace("trash", "shelves" + pathSeparator + shelfId);

            File fromFile = new File(oldPathFile);
            File toFolder = (new File(newPathFile)).getParentFile();
            FileUtils.moveFileToDirectory(fromFile, toFolder, false);
        }
    }

    private void moveFoldersToExistingTrashFolder(FolderEntity folderEntity, Long shelfId) throws IOException {
        List<FolderEntity> foldersInsideExistingFolder = folderRepository
                .findAllByParentFolderIdAndDeleted(folderEntity.getId(), false);

        for (FolderEntity folder : foldersInsideExistingFolder) {

            String oldPathFolder = homePath + userPath + folder.getPath();
            String newPathFolder = oldPathFolder.replace("shelves" + pathSeparator + shelfId, "trash");

            File fromFolder = new File(oldPathFolder);
            File toFolder = new File(newPathFolder);

            if (!toFolder.exists()) {

                FileUtils.moveDirectory(fromFolder, toFolder);
            } else {

                moveFoldersToExistingTrashFolder(folder, shelfId);
            }
        }

        List<FileEntity> filesInsideExistingFolder = fileRepository
                .findAllByParentFolderIdInAndDeleted(foldersInsideExistingFolder
                        .stream()
                        .map(FolderEntity::getId)
                        .collect(Collectors.toList()), false);

        for (FileEntity file : filesInsideExistingFolder) {

            String oldPathFile = homePath + userPath + file.getPath();
            String newPathFile = oldPathFile.replace("shelves" + pathSeparator + shelfId, "trash");

            File fromFile = new File(oldPathFile);
            File toFolder = (new File(newPathFile)).getParentFile();
            FileUtils.moveFileToDirectory(fromFile, toFolder, false);
        }
    }

    public void folderRename(Long userId, RenameFolderRequestModel renameFolderRequestModel) {

        fileSystemValidator.isFolderNameValid(renameFolderRequestModel.getFolderName());

        Long folderId = renameFolderRequestModel.getFolderId();
        String folderName = renameFolderRequestModel.getFolderName();

        FolderEntity folderEntity = folderRepository.findById(folderId)
                .orElseThrow(ExceptionSupplier.noFolderWithGivenId);

        if (folderEntity.getParentFolderId() != null) {

            if (folderRepository.findByNameAndParentFolderId(folderName, folderEntity.getParentFolderId()).isPresent()) {
                throw ExceptionSupplier.folderNameAlreadyExists.get();
            }

        } else {

            if (folderRepository.findByNameAndShelfId(folderName, folderEntity.getShelfId()).isPresent()) {
                throw ExceptionSupplier.folderNameAlreadyExists.get();
            }

        }

        ShelfEntity shelfEntity = shelfRepository.findById(folderEntity.getShelfId())
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        if (!Objects.equals(shelfEntity.getUserId(), userId))
            throw ExceptionSupplier.userNotAllowedToAccessFolder.get();

        if (folderEntity.getParentFolderId() != null) {

            if (folderRepository.findByNameAndParentFolderIdAndIdNot(folderName, folderEntity.getParentFolderId(), folderId).isPresent()) {
                throw ExceptionSupplier.folderAlreadyExists.get();
            }
        } else {
            if (folderRepository.findByNameAndShelfIdAndIdNot(folderName, folderEntity.getShelfId(), folderId)
                    .isPresent()) {
                throw ExceptionSupplier.folderAlreadyExists.get();
            }
        }

        if (folderEntity.getParentFolderId() != null) {

            if (folderRepository.findByNameAndParentFolderId(folderName, folderEntity.getParentFolderId()).isPresent()) {
                throw ExceptionSupplier.folderNameAlreadyExists.get();
            }
        } else {

            if (folderRepository.findByNameAndShelfId(folderName, folderEntity.getShelfId()).isPresent()) {
                throw ExceptionSupplier.folderNameAlreadyExists.get();
            }
        }

        folderEntity.setName(folderName);
        folderEntity.setUpdatedAt(LocalDateTime.now());
        folderRepository.save(folderEntity);
    }
}
