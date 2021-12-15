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
    List<FolderEntity> folderEntities;
    List<FileEntity> fileEntities;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        shelf = new ShelfEntity();
        folder = new FolderEntity();
        file = new FileEntity();
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
    void softDeleteShelf() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        folderEntities.add(folder);
        fileEntities.add(file);

        when(shelfRepository.findById(shelf.getId())).thenReturn(Optional.of(shelf));

        when(folderRepository.findAllByShelfId(shelf.getId())).thenReturn(folderEntities);

        when(fileRepository.findAllByShelfId(shelf.getId())).thenReturn(fileEntities);

        shelfService.softDeleteShelf(user, shelf.getId());

        verify(shelfRepository, times(1)).save(shelf);
        verify(folderRepository, times(1)).saveAll(folderEntities);
        verify(fileRepository, times(1)).saveAll(fileEntities);
    }

    @Test
    void softDeleteShelf_shelfNotFound() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());

        when(shelfRepository.findById(shelf.getId())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.softDeleteShelf(user, shelf.getId());
        });

        verify(shelfRepository, times(1)).findById(shelf.getId());
        verify(folderRepository, times(0)).findAllByShelfId(shelf.getId());
        verify(fileRepository, times(0)).findAllByShelfId(shelf.getId());
        verify(shelfRepository, times(0)).save(shelf);
        verify(folderRepository, times(0)).save(folder);
        verify(fileRepository, times(0)).save(file);

        assertEquals(ErrorMessages.SHELF_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void softDeleteShelf_idsNotEquals() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);

        when(shelfRepository.findById(shelf.getId())).thenReturn(Optional.of(shelf));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.softDeleteShelf(user, shelf.getId());
        });

        verify(shelfRepository, times(1)).findById(shelf.getId());
        verify(folderRepository, times(0)).findAllByShelfId(shelf.getId());
        verify(fileRepository, times(0)).findAllByShelfId(shelf.getId());
        verify(shelfRepository, times(0)).save(shelf);
        verify(folderRepository, times(0)).save(folder);
        verify(fileRepository, times(0)).save(file);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED.getErrorMessage(), exception.getMessage());
    }
}