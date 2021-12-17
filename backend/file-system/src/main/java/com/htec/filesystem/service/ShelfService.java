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
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
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

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

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
    public void updateIsDeletedShelf(AuthUser user, List<Long> shelfIds, boolean delete) {

        List<ShelfEntity> shelfEntities = shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds);

        if (shelfEntities.size() != shelfIds.size()) {
            throw ExceptionSupplier.shelfWithProvidedIdNotFound.get();
        }

        if (!shelfEntities.stream().map(ShelfEntity::getId).collect(Collectors.toList()).containsAll(shelfIds)) {
            throw ExceptionSupplier.userNotAllowed.get();
        }

        shelfRepository.updateIsDeletedByIds(delete, shelfIds);
        folderRepository.updateIsDeletedByShelfIds(delete, shelfIds);
        fileRepository.updateIsDeletedByShelfIds(delete, shelfIds);
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
    public void hardDeleteShelf(Long shelfId, Long userId) {

        ShelfEntity shelfEntity = shelfRepository.findById(shelfId).orElseThrow(ExceptionSupplier.noShelfWithGivenId);

        if (shelfEntity.getUserId() != userId)
            throw ExceptionSupplier.userNotAllowed.get();

        String shelfPath = homePath + userPath + userId + pathSeparator+ "shelves" + pathSeparator + shelfId;

        try {
            FileUtils.deleteDirectory(new File(shelfPath));
            shelfRepository.deleteById(shelfId);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
