package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.ShelfDTO;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.mapper.FileMapper;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.validator.FileSystemValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final FileSystemValidator fileSystemValidator;

    public ShelfService(ShelfRepository shelfRepository,
                        FolderRepository folderRepository,
                        FileRepository fileRepository,
                        FileSystemValidator fileSystemValidator) {
        this.shelfRepository = shelfRepository;
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.fileSystemValidator = fileSystemValidator;
    }

    public void createShelf(CreateShelfRequestModel createShelfRequestModel, Long userId) {

        String shelfName = createShelfRequestModel.getShelfName();

        fileSystemValidator.isShelfNameValid(shelfName);

        if (shelfRepository.findByNameAndUserId(shelfName, userId).isPresent())
            throw ExceptionSupplier.shelfAlreadyExists.get();

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setName(shelfName);
        shelfEntity.setUserId(userId);
        shelfEntity.setDeleted(false);
        shelfEntity.setCreatedAt(LocalDateTime.now());

        shelfRepository.save(shelfEntity);
    }

    @Transactional
    public void softDeleteShelf(AuthUser user, List<Long> shelfIds) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByIdAndUserIdIn(user.getId(), shelfIds);

        if (!shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList()).containsAll(shelfIds)) {
            throw ExceptionSupplier.userNotAllowed.get();
        }

        shelfRepository.updateIsDeletedByIds(shelfIds);
        folderRepository.updateIsDeletedByShelfIds(shelfIds);
        fileRepository.updateIsDeletedByShelfIds(shelfIds);
    }

    public List<ShelfDTO> getAllShelvesById(Long userId) {

        List<ShelfDTO> dtoShelves = new ArrayList<>();

        List<ShelfEntity> entityShelves = shelfRepository.findAllById(userId);

        for (ShelfEntity shelfEntity : entityShelves) {

            ShelfDTO shelfDto = FileMapper.INSTANCE.shelfEntityToShelfDto(shelfEntity);
            dtoShelves.add(shelfDto);
        }
        return dtoShelves;
    }

    @Transactional
    public void hardDeleteShelf(List<Long> shelfIdList, Long userId) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserIdAndIdIn(userId, shelfIdList);

        if (!shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList()).containsAll(shelfIdList)) {
            throw ExceptionSupplier.userNotAllowed.get();
        }

        shelfRepository.deleteByIdIn(shelfIdList);
    }
}
