package com.htec.filesystem.service;

import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.validator.FileSystemValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final FileSystemValidator fileSystemValidator;

    public ShelfService(ShelfRepository shelfRepository,
                        FileSystemValidator fileSystemValidator) {
        this.shelfRepository = shelfRepository;
        this.fileSystemValidator = fileSystemValidator;
    }

    public void createShelf(CreateShelfRequestModel createShelfRequestModel) {

        Long userId = createShelfRequestModel.getUserId();
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
}
