package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.validator.FileSystemValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

        if (shelfRepository.findByName(shelfName).isPresent())
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

        for (Long shelfId : shelfIds) {
            ShelfEntity shelfEntity = shelfRepository.findById(shelfId)
                    .orElseThrow(ExceptionSupplier.shelfNotFound);

            if (!Objects.equals(shelfEntity.getUserId(), user.getId())) {
                throw ExceptionSupplier.userNotAllowed.get();
            }

            folderRepository.updateIsDeletedByShelfId(shelfEntity.getId());
            fileRepository.updateIsDeletedByShelfId(shelfEntity.getId());

            shelfEntity.setDeleted(true);
            shelfRepository.save(shelfEntity);
        }
    }
}
