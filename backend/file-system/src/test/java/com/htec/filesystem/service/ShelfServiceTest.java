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
import java.util.Optional;

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

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        shelf = new ShelfEntity();
        folder = new FolderEntity();
        file = new FileEntity();
        shelfIds = new ArrayList<>();
        shelfEntities = new ArrayList<>();
    }

    @Test
    void createShelf() {

        CreateShelfRequestModel createShelfRequestModel = new CreateShelfRequestModel("test");
        Long userId = 2L;

        shelfService.createShelf(createShelfRequestModel, userId);
        verify(shelfRepository, times(1)).save(any());
    }

    @Test
    void softDeleteShelf() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        shelfEntities.add(shelf);
        shelfIds.add(1L);

        when(shelfRepository.findById(shelfIds.get(0))).thenReturn(Optional.of(shelf));

        shelfService.softDeleteShelf(user, shelfIds);

        verify(shelfRepository, times(1)).save(shelf);
        verify(folderRepository, times(1)).updateIsDeletedByShelfId(shelf.getId());
        verify(fileRepository, times(1)).updateIsDeletedByShelfId(shelf.getId());
    }

    @Test
    void softDeleteShelf_shelfNotFound() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        shelfIds.add(1L);

        when(shelfRepository.findById(shelfIds.get(0))).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.softDeleteShelf(user, shelfIds);
        });

        verify(shelfRepository, times(1)).findById(shelf.getId());
        verify(folderRepository, times(0)).updateIsDeletedByShelfId(shelf.getId());
        verify(fileRepository, times(0)).updateIsDeletedByShelfId(shelf.getId());
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.SHELF_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void softDeleteShelf_idsNotEquals() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);
        shelfIds.add(1L);

        when(shelfRepository.findById(shelfIds.get(0))).thenReturn(Optional.of(shelf));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.softDeleteShelf(user, shelfIds);
        });

        verify(shelfRepository, times(1)).findById(shelf.getId());
        verify(folderRepository, times(0)).updateIsDeletedByShelfId(shelf.getId());
        verify(fileRepository, times(0)).updateIsDeletedByShelfId(shelf.getId());
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED.getErrorMessage(), exception.getMessage());
    }
}