package com.htec.filesystem.service;

import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.model.request.RenameFolderRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.*;
import com.htec.filesystem.util.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FolderTreeRepository folderTreeRepository;
    @Mock
    private FileTreeRepository fileTreeRepository;
    @Mock
    private ShelfRepository shelfRepository;

    @Mock
    private File file;

    @InjectMocks
    private FolderService folderService;

    @Test
    void getFiles_EmptyList() {

        Long testUserId = 1L;
        Long testFolderId = 1L;


        when(folderRepository.findAllByUserIdAndParentFolderIdAndDeleted(testUserId, testFolderId, false))
                .thenReturn(new ArrayList<>());
        when(fileRepository.findAllByUserIdAndParentFolderIdAndDeleted(testUserId, testFolderId, false))
                .thenReturn(new ArrayList<>());
        when(folderRepository.getShelfByFolderId(testFolderId)).thenReturn(Optional.of(new ShelfEntity()));

        ResponseEntity<ShelfContentResponseModel> files = folderService.getItems(testUserId, testFolderId, false);

        Assertions.assertEquals(0, Objects.requireNonNull(files.getBody()).getShelfItems().size());
    }

    @Test
    void getFiles_NotEmptyList() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderIdAndDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        add(new FolderEntity());
                        add(new FolderEntity());
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByUserIdAndParentFolderIdAndDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FileEntity>() {
                    {
                        add(new FileEntity());
                        add(new FileEntity());
                    }
                }
        );
        when(folderRepository.getShelfByFolderId(testFolderId)).thenReturn(Optional.of(new ShelfEntity()));

        ResponseEntity<ShelfContentResponseModel> files = folderService.getItems(testUserId, testFolderId, false);

        Assertions.assertEquals(5, Objects.requireNonNull(files.getBody()).getShelfItems().size());
    }

    @Test
    void getFiles_FileFolderType() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderIdAndDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByUserIdAndParentFolderIdAndDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FileEntity>() {
                    {
                        add(new FileEntity());
                    }
                }
        );
        when(folderRepository.getShelfByFolderId(testFolderId)).thenReturn(Optional.of(new ShelfEntity()));

        ResponseEntity<ShelfContentResponseModel> files = folderService.getItems(testUserId, testFolderId, false);

        Assertions.assertEquals(2, Objects.requireNonNull(files.getBody()).getShelfItems().size());
        Assertions.assertFalse(Objects.requireNonNull(files.getBody()).getShelfItems().get(0).getFolder());
        Assertions.assertTrue(Objects.requireNonNull(files.getBody()).getShelfItems().get(1).getFolder());
    }

    @Test
    void uploadDeleted() {

    }

    @Test
    void uploadDeleted_Multiple() {

    }

    @Test
    void uploadDeleted_UserNotAllowed() {

    }

    @Test
    void folderRename() {

        Long userId = 2L;
        RenameFolderRequestModel renameFolderRequestModel = new RenameFolderRequestModel();
        renameFolderRequestModel.setFolderId(3L);
        renameFolderRequestModel.setFolderName("testName");

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setName("test1");
        folderEntity.setId(3L);
        folderEntity.setParentFolderId(null);

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);

        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folderEntity));
        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

        folderService.folderRename(userId, renameFolderRequestModel);

        verify(folderRepository, times(1)).save(any(FolderEntity.class));
    }

    @Test
    void folderRename_NoFolderWithGivenId() {

        Long userId = 2L;
        RenameFolderRequestModel renameFolderRequestModel = new RenameFolderRequestModel();
        renameFolderRequestModel.setFolderId(3L);
        renameFolderRequestModel.setFolderName("testName");

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setName("test1");
        folderEntity.setId(3L);
        folderEntity.setParentFolderId(null);

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);

        when(folderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> folderService.folderRename(userId, renameFolderRequestModel));

        assertEquals(ErrorMessages.NO_FOLDER_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
    }

    @Test
    void folderRename_NoShelfWithGivenId() {

        Long userId = 2L;
        RenameFolderRequestModel renameFolderRequestModel = new RenameFolderRequestModel();
        renameFolderRequestModel.setFolderId(3L);
        renameFolderRequestModel.setFolderName("testName");

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setName("test1");
        folderEntity.setId(3L);
        folderEntity.setParentFolderId(null);

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);

        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folderEntity));
        when(shelfRepository.findById(anyLong())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> folderService.folderRename(userId, renameFolderRequestModel));

        assertEquals(ErrorMessages.NO_SHELF_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
    }

   @Test
    void folderRename_UserNotAllowedToAccessFolder() {

        Long userId = 2L;
        RenameFolderRequestModel renameFolderRequestModel = new RenameFolderRequestModel();
        renameFolderRequestModel.setFolderId(3L);
        renameFolderRequestModel.setFolderName("testName");

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setName("test1");
        folderEntity.setId(3L);
        folderEntity.setParentFolderId(null);

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(3L);

       when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folderEntity));
       when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> folderService.folderRename(userId, renameFolderRequestModel));

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_ACCESS_THIS_FOLDER.getErrorMessage(), exception.getMessage());
    }

    @Test
    void folderRename_FolderAlreadyExists() {

        Long userId = 2L;
        RenameFolderRequestModel renameFolderRequestModel = new RenameFolderRequestModel();
        renameFolderRequestModel.setFolderId(3L);
        renameFolderRequestModel.setFolderName("testName");

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setName("testName");
        folderEntity.setId(3L);
        folderEntity.setParentFolderId(null);

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);

        when(folderRepository.findById(anyLong())).thenReturn(Optional.of(folderEntity));
        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(folderRepository.findByNameAndShelfIdAndIdNot(anyString(), anyLong(), anyLong())).thenReturn(Optional.of(folderEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> folderService.folderRename(userId, renameFolderRequestModel));

        assertEquals(ErrorMessages.FOLDER_WITH_THE_SAME_NAME_ALREADY_EXISTS.getErrorMessage(), exception.getMessage());
    }

}