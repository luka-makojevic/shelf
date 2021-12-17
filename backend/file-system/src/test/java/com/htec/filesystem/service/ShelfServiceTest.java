package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.ErrorMessages;
import com.htec.filesystem.validator.FileSystemValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ShelfServiceTest {

    @Mock
    ShelfRepository shelfRepository;

    @Mock
    FileSystemValidator fileSystemValidator;

    @Mock
    FileRepository fileRepository;

    @Mock
    FolderRepository folderRepository;

    @InjectMocks
    ShelfService shelfService;

    AuthUser user;
    ShelfEntity shelf;
    FolderEntity folder;
    FileEntity file;
    List<ShelfEntity> shelfEntities;
    List<Long> shelfIds;
    List<FolderEntity> folderEntities;
    List<FileEntity> fileEntities;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        shelf = new ShelfEntity();
        folder = new FolderEntity();
        file = new FileEntity();
        shelfIds = new ArrayList<>();
        shelfEntities = new ArrayList<>();
        folderEntities = new ArrayList<>();
        fileEntities = new ArrayList<>();
    }

    @Test
    void createShelf() {

        CreateShelfRequestModel createShelfRequestModel = new CreateShelfRequestModel("test");
        Long userId = 2L;

        shelfService.createShelf(createShelfRequestModel, userId);
        verify(shelfRepository, times(1)).save(any());
    }

    @Test
    void updateIsDeletedShelfTrue() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        shelfEntities.add(shelf);
        shelfIds.add(1L);
        boolean isDeleted = true;

        when(shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds)).thenReturn(shelfEntities);

        shelfService.updateIsDeletedShelf(user, shelfIds, isDeleted);

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(1)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
        verify(fileRepository, times(1)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
    }

    @Test
    void updateIsDeletedShelfFalse() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        shelfEntities.add(shelf);
        shelfIds.add(1L);
        boolean isDeleted = false;

        when(shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds)).thenReturn(shelfEntities);

        shelfService.updateIsDeletedShelf(user, shelfIds, isDeleted);

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(1)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
        verify(fileRepository, times(1)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
    }

    @Test
    void softDeleteShelf_idsNotFound() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);
        shelfIds.add(1L);
        boolean isDeleted = true;

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.updateIsDeletedShelf(user, shelfIds, isDeleted);
        });

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(0)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
        verify(fileRepository, times(0)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.SHELF_WITH_PROVIDED_ID_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void softDeleteShelf_idsNotEquals() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);
        shelfIds.add(1L);
        boolean isDeleted = true;

        when(shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds)).thenReturn(shelfEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.updateIsDeletedShelf(user, shelfIds, isDeleted);
        });

        verify(folderRepository, times(0)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
        verify(fileRepository, times(0)).updateIsDeletedByShelfIds(isDeleted, shelfIds);
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED.getErrorMessage(), exception.getMessage());
    }
}