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
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
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

        when(shelfRepository.save(any(ShelfEntity.class))).thenReturn(new ShelfEntity());

        shelfService.createShelf(createShelfRequestModel, userId);

        verify(shelfRepository, times(1)).save(any(ShelfEntity.class));
    }

    @Test
    void updateIsDeletedShelfTrue() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        shelfEntities.add(shelf);
        shelfIds.add(1L);
        boolean delete = true;

        when(shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds)).thenReturn(shelfEntities);

        shelfService.updateIsDeletedShelf(user, shelfIds, delete);

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(1)).updateIsDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(1)).updateIsDeletedByShelfIds(delete, shelfIds);
    }

    @Test
    void updateIsDeletedShelfFalse() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(user.getId());
        shelfEntities.add(shelf);
        shelfIds.add(1L);
        boolean delete = false;

        when(shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds)).thenReturn(shelfEntities);

        shelfService.updateIsDeletedShelf(user, shelfIds, delete);

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(1)).updateIsDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(1)).updateIsDeletedByShelfIds(delete, shelfIds);
    }

    @Test
    void softDeleteShelf_idsNotFound() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);
        shelfIds.add(1L);
        boolean delete = true;

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.updateIsDeletedShelf(user, shelfIds, delete);
        });

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(0)).updateIsDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(0)).updateIsDeletedByShelfIds(delete, shelfIds);
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.SHELF_WITH_PROVIDED_ID_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void softDeleteShelf_idsNotEquals() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);
        shelfIds.add(3L);
        shelfEntities.add(shelf);
        boolean delete = true;

        when(shelfRepository.findAllByUserIdAndIdIn(user.getId(), shelfIds)).thenReturn(shelfEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            shelfService.updateIsDeletedShelf(user, shelfIds, delete);
        });

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(0)).updateIsDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(0)).updateIsDeletedByShelfIds(delete, shelfIds);
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED.getErrorMessage(), exception.getMessage());
    }

    @Test
    void hardDeleteShelf() {

        Long shelfId = 1L;
        Long userId = 2L;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(userId);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

        try (MockedStatic<FileUtils> mocked = mockStatic(FileUtils.class)) {


            shelfService.hardDeleteShelf(shelfId, userId);

            mocked.verify(() -> FileUtils.deleteDirectory(any(File.class)));

        }

    }
}