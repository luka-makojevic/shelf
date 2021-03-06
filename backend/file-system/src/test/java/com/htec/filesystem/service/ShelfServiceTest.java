package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.mapper.ShelfItemMapper;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.model.request.ShelfEditRequestModel;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    FileService fileService;

    @Mock
    FolderService folderService;

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
        verify(folderRepository, times(1)).updateDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(1)).updateDeletedByShelfIds(delete, shelfIds);
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
        verify(folderRepository, times(1)).updateDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(1)).updateDeletedByShelfIds(delete, shelfIds);
    }

    @Test
    void softDeleteShelf_idsNotFound() {

        user.setId(1L);
        shelf.setId(1L);
        shelf.setUserId(2L);
        shelfIds.add(1L);
        boolean delete = true;

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.updateIsDeletedShelf(user, shelfIds, delete));

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(0)).updateDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(0)).updateDeletedByShelfIds(delete, shelfIds);
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

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.updateIsDeletedShelf(user, shelfIds, delete));

        verify(shelfRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), shelfIds);
        verify(folderRepository, times(0)).updateDeletedByShelfIds(delete, shelfIds);
        verify(fileRepository, times(0)).updateDeletedByShelfIds(delete, shelfIds);
        verify(shelfRepository, times(0)).save(shelf);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_SHELF.getErrorMessage(), exception.getMessage());
    }

    @Test
    void hardDeleteShelf() {

        Long shelfId = 1L;
        Long userId = 2L;
        file.setId(1L);
        folder.setId(1L);
        fileEntities.add(file);
        folderEntities.add(folder);
        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(userId);
        shelfIds.add(shelfId);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

        when(fileRepository.findAllByShelfIdInAndTrashVisible(shelfIds, true)).thenReturn(fileEntities);
        when(folderRepository.findAllByShelfIdInAndTrashVisible(shelfIds, true)).thenReturn(folderEntities);

        try (MockedStatic<FileUtils> mocked = mockStatic(FileUtils.class)) {

            shelfService.hardDeleteShelf(shelfId, userId);

            mocked.verify(() -> FileUtils.deleteDirectory(any(File.class)));

        }

        verify(shelfRepository, times(1)).findById(shelfId);
        verify(fileRepository, times(1)).findAllByShelfIdInAndTrashVisible(shelfIds, true);
        verify(folderRepository, times(1)).findAllByShelfIdInAndTrashVisible(shelfIds, true);
        verify(fileService, times(1)).deleteFile(userId, fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()));
        verify(folderService, times(1)).hardDeleteFolder(userId, folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()));
        verify(shelfRepository, times(1)).deleteById(shelfId);
    }

    @Test
    void hardDeleteShelf_ShelfNotFound() {

        Long shelfId = 1L;
        Long userId = 2L;
        file.setId(1L);
        folder.setId(1L);
        fileEntities.add(file);
        folderEntities.add(folder);
        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(userId);
        shelfIds.add(shelfId);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.hardDeleteShelf(shelfId, userId));

        verify(shelfRepository, times(1)).findById(shelfId);
        verify(fileRepository, times(0)).findAllByShelfIdInAndTrashVisible(shelfIds, true);
        verify(folderRepository, times(0)).findAllByShelfIdInAndTrashVisible(shelfIds, true);
        verify(fileService, times(0)).deleteFile(userId, fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()));
        verify(folderService, times(0)).hardDeleteFolder(userId, folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()));
        verify(shelfRepository, times(0)).deleteById(shelfId);

        assertEquals(ErrorMessages.SHELF_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void hardDeleteShelf_UserNotValid() {

        Long shelfId = 1L;
        Long userId = 2L;
        file.setId(1L);
        folder.setId(1L);
        fileEntities.add(file);
        folderEntities.add(folder);
        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(3L);
        shelfIds.add(shelfId);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.hardDeleteShelf(shelfId, userId));

        verify(shelfRepository, times(1)).findById(shelfId);
        verify(fileRepository, times(0)).findAllByShelfIdInAndTrashVisible(shelfIds, true);
        verify(folderRepository, times(0)).findAllByShelfIdInAndTrashVisible(shelfIds, true);
        verify(fileService, times(0)).deleteFile(userId, fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList()));
        verify(folderService, times(0)).hardDeleteFolder(userId, folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList()));
        verify(shelfRepository, times(0)).deleteById(shelfId);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_SHELF.getErrorMessage(), exception.getMessage());
    }

    @Test
    void getShelfContent() {

        Long shelfId = 15L;
        Long userId = 1L;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(userId);

        FileEntity fileEntity = new FileEntity();
        List<FileEntity> fileList = new ArrayList<>();
        fileList.add(fileEntity);

        FolderEntity folderEntity = new FolderEntity();
        List<FolderEntity> folderList = new ArrayList<>();
        folderList.add(folderEntity);

        List<ShelfItemDTO> dtoList = new ArrayList<>();
        dtoList.addAll(ShelfItemMapper.INSTANCE.fileEntitiesToShelfItemDTOs(fileList));
        dtoList.addAll(ShelfItemMapper.INSTANCE.folderEntitiesToShelfItemDTOs(folderList));


        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(fileRepository.findAllByShelfIdAndParentFolderIdIsNullAndDeletedFalse(anyLong())).thenReturn(fileList);
        when(folderRepository.findAllByShelfIdAndParentFolderIdIsNullAndDeletedFalse(anyLong())).thenReturn(folderList);

        List<ShelfItemDTO> returnFileDtos = shelfService.getShelfContent(shelfId, userId).getShelfItems();

        assertEquals(dtoList.size(), returnFileDtos.size());
    }

    @Test
    void getShelfContent_UserNotAllowedToAccessShelf() {

        Long shelfId = 15L;
        Long userId = 1L;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(3L);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.getShelfContent(shelfId, userId));

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_ACCESS_THIS_SHELF.getErrorMessage(), exception.getMessage());
    }

    @Test
    void updateShelfName() {

        ShelfEditRequestModel shelfEditRequestModel = new ShelfEditRequestModel(7L, "shelf1");
        Long userId = 4L;
        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setName("newShelf");
        shelfEntity.setUserId(userId);
        List<ShelfEntity> shelfEntities = new ArrayList<>();
        shelfEntities.add(shelfEntity);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(shelfRepository.findAllByUserIdAndDeletedFalse(anyLong())).thenReturn(shelfEntities);

        shelfService.updateShelfName(shelfEditRequestModel, userId);
        verify(shelfRepository, times(1)).save(any(ShelfEntity.class));
    }

    @Test
    void updateShelfName_ShelfAlreadyExists() {

        ShelfEditRequestModel shelfEditRequestModel = new ShelfEditRequestModel(7L, "shelf1");
        Long userId = 4L;
        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setName("shelf1");
        shelfEntity.setUserId(userId);
        List<ShelfEntity> shelfEntities = new ArrayList<>();
        shelfEntities.add(shelfEntity);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(shelfRepository.findAllByUserIdAndDeletedFalse(anyLong())).thenReturn(shelfEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.updateShelfName(shelfEditRequestModel, userId));

        assertEquals(ErrorMessages.SHELF_WITH_THE_SAME_NAME_ALREADY_EXISTS.getErrorMessage(), exception.getMessage());
    }

    @Test
    void updateShelfName_UserNotAllowedToAccessShelf() {

        ShelfEditRequestModel shelfEditRequestModel = new ShelfEditRequestModel(7L, "shelf1");
        Long userId = 4L;
        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setName("newShelf");
        List<ShelfEntity> shelfEntities = new ArrayList<>();
        shelfEntities.add(shelfEntity);

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(shelfRepository.findAllByUserIdAndDeletedFalse(anyLong())).thenReturn(shelfEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> shelfService.updateShelfName(shelfEditRequestModel, userId));

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_ACCESS_THIS_SHELF.getErrorMessage(), exception.getMessage());
    }
}