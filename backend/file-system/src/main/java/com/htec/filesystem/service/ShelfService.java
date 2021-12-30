package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.BreadCrumbDTO;
import com.htec.filesystem.dto.ShelfDTO;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.ShelfItemMapper;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.model.request.ShelfEditRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.validator.FileSystemValidator;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final FileSystemValidator fileSystemValidator;
    private final FolderService folderService;
    private final FileService fileService;

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    public ShelfService(ShelfRepository shelfRepository,
                        FolderRepository folderRepository,
                        FileRepository fileRepository,
                        FileSystemValidator fileSystemValidator,
                        FolderService folderService,
                        FileService fileService) {
        this.shelfRepository = shelfRepository;
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.fileSystemValidator = fileSystemValidator;
        this.folderService = folderService;
        this.fileService = fileService;
    }

    @Transactional
    public boolean createShelf(CreateShelfRequestModel createShelfRequestModel, Long userId) {

        String shelfName = createShelfRequestModel.getShelfName();

        fileSystemValidator.isShelfNameValid(shelfName);

        if (shelfRepository.findByNameAndUserId(shelfName, userId).isPresent())
            throw ExceptionSupplier.shelfAlreadyExists.get();

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setName(shelfName);
        shelfEntity.setUserId(userId);
        shelfEntity.setDeleted(false);
        shelfEntity.setCreatedAt(LocalDateTime.now());

        ShelfEntity savedEntity = shelfRepository.save(shelfEntity);

        shelfRepository.flush();
        Long shelfId = savedEntity.getId();

        String shelfPath = homePath + userPath + userId + pathSeparator + "shelves" + pathSeparator + shelfId;

        return new File(shelfPath).mkdirs();
    }

    @Transactional
    public void updateIsDeletedShelf(AuthUser user, List<Long> shelfIds, boolean delete) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds);

        if (shelfEntities.size() != shelfIds.size()) {
            throw ExceptionSupplier.shelfWithProvidedIdNotFound.get();
        }

        if (!shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList()).containsAll(shelfIds)) {
            throw ExceptionSupplier.userNotAllowedToDeleteShelf.get();
        }

        shelfRepository.updateDeletedByIds(delete, shelfIds);
        folderRepository.updateDeletedByShelfIds(delete, shelfIds);
        fileRepository.updateDeletedByShelfIds(delete, shelfIds);
    }

    public List<ShelfDTO> getAllShelvesById(Long userId) {

        List<ShelfEntity> entityShelves = shelfRepository.findAllByIdAndNotDeleted(userId);

        return ShelfItemMapper.INSTANCE.shelfEntitiesToShelfDTOs(entityShelves);
    }

    @Transactional
    public void hardDeleteShelf(Long shelfId, Long userId) {

        ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.shelfNotFound);

        if (!Objects.equals(shelfEntity.getUserId(), userId))
            throw ExceptionSupplier.userNotAllowedToDeleteShelf.get();

        List<FileEntity> fileEntities = fileRepository.findAllByShelfIdInAndTrashVisible(Collections.singletonList(shelfId), true);
        List<FolderEntity> folderEntities = folderRepository.findAllByShelfIdInAndTrashVisible(Collections.singletonList(shelfId), true);

        List<Long> fileIds = fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList());
        List<Long> folderIds = folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList());

        String shelfPath = homePath + userPath + userId + pathSeparator + "shelves" + pathSeparator + shelfId;

        try {
            fileService.deleteFile(userId, fileIds);

            folderService.hardDeleteFolder(userId, folderIds);

            FileUtils.deleteDirectory(new File(shelfPath));
            shelfRepository.deleteById(shelfId);
        } catch (IOException e) {
            throw ExceptionSupplier.couldNotDeleteShelf.get();
        }
    }

    public ShelfContentResponseModel getShelfContent(Long shelfId, Long userId) {

        ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        if (!Objects.equals(shelfEntity.getUserId(), userId))
            throw ExceptionSupplier.userNotAllowedToAccessShelf.get();

        List<ShelfItemDTO> dtoItems = new ArrayList<>();

        List<FileEntity> fileEntities = fileRepository.findAllByShelfIdAndParentFolderIdIsNullAndDeletedFalse(shelfId);
        List<FolderEntity> folderEntities = folderRepository.findAllByShelfIdAndParentFolderIdIsNullAndDeletedFalse(shelfId);


        dtoItems.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(fileEntities));
        dtoItems.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(folderEntities));

        List<BreadCrumbDTO> breadCrumbDTOS = new ArrayList<>();
        breadCrumbDTOS.add(new BreadCrumbDTO(shelfEntity.getName(), shelfEntity.getId()));

        return new ShelfContentResponseModel(breadCrumbDTOS, dtoItems);
    }

    public void updateShelfName(ShelfEditRequestModel shelfEditRequestModel, Long userId) {

        String shelfName = shelfEditRequestModel.getShelfName();
        Long shelfId = shelfEditRequestModel.getShelfId();

        fileSystemValidator.isShelfNameValid(shelfName);

        ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                .orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        List<ShelfEntity> shelfList = shelfRepository.findAllByUserIdAndDeletedFalse(userId);

        if (shelfList.stream().map(ShelfEntity::getName).collect(Collectors.toList()).contains(shelfName))
            throw ExceptionSupplier.shelfAlreadyExists.get();

        if (!Objects.equals(shelfEntity.getUserId(), userId))
            throw ExceptionSupplier.userNotAllowedToAccessShelf.get();

        if (!shelfEntity.getName().equals(shelfName))
            shelfEntity.setName(shelfName);

        shelfEntity.setUpdatedAt(LocalDateTime.now());
        shelfRepository.save(shelfEntity);
    }

    public ShelfContentResponseModel getFirstLevelTrash(Long userId) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserId(userId);
        List<Long> shelfIds = shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList());

        List<FileEntity> fileEntities = fileRepository.findAllByShelfIdInAndTrashVisible(shelfIds, true);
        List<FolderEntity> folderEntities = folderRepository.findAllByShelfIdInAndTrashVisible(shelfIds, true);

        for (FileEntity fileEntity : fileEntities) {
            fileEntity.setName(fileEntity.getRealName());
        }

        List<ShelfItemDTO> trashItems = new ArrayList<>();
        trashItems.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(fileEntities));
        trashItems.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(folderEntities));

        List<BreadCrumbDTO> breadCrumbDTOS = new ArrayList<>();
        breadCrumbDTOS.add(new BreadCrumbDTO("trash", userId));

        return new ShelfContentResponseModel(breadCrumbDTOS, trashItems);
    }
}