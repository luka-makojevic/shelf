package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ShelfException;

import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.*;
import com.htec.filesystem.util.ErrorMessages;
import com.htec.filesystem.validator.FileSystemValidator;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.htec.filesystem.model.request.RenameFolderRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.*;
import com.htec.filesystem.util.ErrorMessages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    FileSystemValidator fileSystemValidator;
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

    AuthUser user;
    FileEntity fileEntity;
    List<Long> folderIds;
    List<FileEntity> fileEntities;
    List<FolderEntity> folderEntities;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        fileEntity = new FileEntity();
        folderIds = new ArrayList<>();
        fileEntities = new ArrayList<>();
        folderEntities = new ArrayList<>();
    }

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

    void hardDeleteFolder() {

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setId(1L);
        user.setId(1L);
        folderEntities.add(folderEntity);
        folderIds.add(folderEntity.getId());
        fileEntity.setId(1L);
        fileEntities.add(fileEntity);

        when(folderRepository.findByUserIdAndFolderIdsAndDeleted(user.getId(), folderIds, true)).thenReturn(folderEntities);
        when(folderTreeRepository.getFolderDownStreamTrees(folderIds, true)).thenReturn(folderEntities);
        when(fileRepository.findAllByParentFolderIdIn(folderIds)).thenReturn(fileEntities);

        try (MockedStatic<FileUtils> mocked = mockStatic(FileUtils.class)) {

            mocked.when(() -> FileUtils.forceDelete(any())).then(invocationOnMock -> null);

            folderService.hardDeleteFolder(user.getId(), folderIds);

        }

        verify(folderRepository, times(1)).findByUserIdAndFolderIdsAndDeleted(user.getId(), folderIds, true);
        verify(folderTreeRepository, times(1)).getFolderDownStreamTrees(folderIds, true);
        verify(fileRepository, times(1)).findAllByParentFolderIdIn(folderIds);
        verify(fileRepository, times(1)).delete(fileEntity);
        verify(folderRepository, times(1)).deleteAllInBatch(folderEntities);
    }

    @Test
    void hardDeleteFolder_UserNotAllowed() {

        FolderEntity folderEntity1 = new FolderEntity();
        FolderEntity folderEntity2 = new FolderEntity();
        folderEntity1.setId(1L);
        folderEntity2.setId(2L);
        user.setId(1L);
        folderEntities.add(folderEntity1);
        folderIds.add(folderEntity1.getId());
        folderIds.add(folderEntity2.getId());
        fileEntity.setId(1L);
        fileEntities.add(fileEntity);

        when(folderRepository.findByUserIdAndFolderIdsAndDeleted(user.getId(), folderIds, true)).thenReturn(folderEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> folderService.hardDeleteFolder(user.getId(), folderIds));

        verify(folderRepository, times(1)).findByUserIdAndFolderIdsAndDeleted(user.getId(), folderIds, true);
        verify(folderTreeRepository, times(0)).getFolderDownStreamTrees(folderIds, true);
        verify(fileRepository, times(0)).findAllByParentFolderIdIn(folderIds);
        verify(fileRepository, times(0)).delete(fileEntity);
        verify(folderRepository, times(0)).deleteAllInBatch(folderEntities);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_FOLDER.getErrorMessage(), exception.getMessage());
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