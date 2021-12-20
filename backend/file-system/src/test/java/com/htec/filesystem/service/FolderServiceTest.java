package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.dto.ShelfItemDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FileTreeRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.FolderTreeRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private File file;

    @InjectMocks
    private FolderService folderService;

    @Test
    void getFiles_EmptyList() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderIdAndIsDeleted(testUserId, testFolderId, false))
                .thenReturn(new ArrayList<>());
        when(fileRepository.findAllByUserIdAndParentFolderIdAndIsDeleted(testUserId, testFolderId, false))
                .thenReturn(new ArrayList<>());

        ResponseEntity<List<ShelfItemDTO>> files = folderService.getFiles(testUserId, testFolderId);

        Assertions.assertEquals(0, Objects.requireNonNull(files.getBody()).size());
    }

    @Test
    void getFiles_NotEmptyList() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderIdAndIsDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        add(new FolderEntity());
                        add(new FolderEntity());
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByUserIdAndParentFolderIdAndIsDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FileEntity>() {
                    {
                        add(new FileEntity());
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<ShelfItemDTO>> files = folderService.getFiles(testUserId, testFolderId);

        Assertions.assertEquals(5, Objects.requireNonNull(files.getBody()).size());
    }

    @Test
    void getFiles_FileFolderType() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderIdAndIsDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByUserIdAndParentFolderIdAndIsDeleted(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FileEntity>() {
                    {
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<ShelfItemDTO>> files = folderService.getFiles(testUserId, testFolderId);

        Assertions.assertEquals(2, Objects.requireNonNull(files.getBody()).size());
        Assertions.assertFalse(Objects.requireNonNull(files.getBody()).get(0).isFolder());
        Assertions.assertTrue(Objects.requireNonNull(files.getBody()).get(1).isFolder());
    }

    @Test
    void uploadDeleted() {

        Long testUserId = 1L;

        List<Long> testFolderIds = new ArrayList<>();
        testFolderIds.add(1L);

        AuthUser testAuthUser = new AuthUser();
        testAuthUser.setId(testUserId);

        ArrayList<FolderEntity> folderEntities = new ArrayList<FolderEntity>() {
            {
                FolderEntity folderEntity = new FolderEntity();
                folderEntity.setId(1L);
                add(folderEntity);
            }
        };

        ArrayList<FileEntity> fileEntities = new ArrayList<FileEntity>() {
            {
                FileEntity folderEntity = new FileEntity();
                folderEntity.setId(1L);
                add(folderEntity);
            }
        };

        List<Long> downStreamFoldersIds = folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList());

        List<Long> downStreamFilesIds = fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList());

        when(folderRepository.findByUserIdAndFolderIds(testAuthUser.getId(), testFolderIds)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        FolderEntity folderEntity = new FolderEntity();
                        folderEntity.setId(1L);
                        add(folderEntity);
                    }
                }
        );
        when(folderTreeRepository.getFolderDownStreamTrees(testFolderIds, false)).thenReturn(folderEntities);
        when(fileTreeRepository.getFileDownStreamTrees(testFolderIds, false)).thenReturn(fileEntities);

        folderService.updateDeleted(testAuthUser.getId(), testFolderIds, true);

        verify(folderRepository, times(1)).findByUserIdAndFolderIds(testAuthUser.getId(), testFolderIds);
        verify(folderTreeRepository, times(1)).getFolderDownStreamTrees(testFolderIds, false);
        verify(fileTreeRepository, times(1)).getFileDownStreamTrees(testFolderIds, false);
        verify(folderRepository, times(1)).updateDeletedByFolderIds(true, downStreamFoldersIds);
        verify(fileRepository, times(1)).updateDeletedByFileIds(true, downStreamFilesIds);
    }

    @Test
    void uploadDeleted_Multiple() {

        Long testUserId = 1L;

        List<Long> testFolderIds = new ArrayList<>();
        testFolderIds.add(1L);
        testFolderIds.add(2L);
        testFolderIds.add(3L);

        AuthUser testAuthUser = new AuthUser();
        testAuthUser.setId(testUserId);

        ArrayList<FolderEntity> folderEntities = new ArrayList<FolderEntity>() {
            {
                FolderEntity folderEntity = new FolderEntity();
                folderEntity.setId(1L);
                add(folderEntity);
            }
        };

        ArrayList<FileEntity> fileEntities = new ArrayList<FileEntity>() {
            {
                FileEntity folderEntity = new FileEntity();
                folderEntity.setId(1L);
                add(folderEntity);
            }
        };

        List<Long> downStreamFoldersIds = folderEntities.stream().map(FolderEntity::getId).collect(Collectors.toList());

        List<Long> downStreamFilesIds = fileEntities.stream().map(FileEntity::getId).collect(Collectors.toList());

        when(folderRepository.findByUserIdAndFolderIds(testAuthUser.getId(), testFolderIds)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        FolderEntity folderEntity = new FolderEntity();
                        folderEntity.setId(1L);
                        add(folderEntity);

                        folderEntity = new FolderEntity();
                        folderEntity.setId(2L);
                        add(folderEntity);

                        folderEntity = new FolderEntity();
                        folderEntity.setId(3L);
                        add(folderEntity);
                    }
                }
        );
        when(folderTreeRepository.getFolderDownStreamTrees(testFolderIds, false)).thenReturn(folderEntities);
        when(fileTreeRepository.getFileDownStreamTrees(testFolderIds, false)).thenReturn(fileEntities);

        folderService.updateDeleted(testAuthUser.getId(), testFolderIds, true);

        verify(folderRepository, times(1)).findByUserIdAndFolderIds(testAuthUser.getId(), testFolderIds);
        verify(folderTreeRepository, times(1)).getFolderDownStreamTrees(testFolderIds, false);
        verify(fileTreeRepository, times(1)).getFileDownStreamTrees(testFolderIds, false);
        verify(folderRepository, times(1)).updateDeletedByFolderIds(true, downStreamFoldersIds);
        verify(fileRepository, times(1)).updateDeletedByFileIds(true, downStreamFilesIds);
    }

    @Test
    void uploadDeleted_UserNotAllowed() {

        Long testUserId = 1L;

        List<Long> testFolderIds = new ArrayList<>();
        testFolderIds.add(1L);

        AuthUser testAuthUser = new AuthUser();
        testAuthUser.setId(testUserId);

        when(folderRepository.findByUserIdAndFolderIds(testAuthUser.getId(), testFolderIds)).thenReturn(new ArrayList<>());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            folderService.updateDeleted(testAuthUser.getId(), testFolderIds, true);
        });

        verify(folderRepository, times(1)).findByUserIdAndFolderIds(testAuthUser.getId(), testFolderIds);
        verify(folderTreeRepository, times(0)).getFolderDownStreamTrees(testFolderIds, false);
        verify(fileTreeRepository, times(0)).getFileDownStreamTrees(testFolderIds, false);
        verify(folderRepository, times(0)).updateDeletedByFolderIds(true, testFolderIds);
        verify(fileRepository, times(0)).updateDeletedByFileIds(true, testFolderIds);

        Assertions.assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_FOLDER.getErrorMessage(), exception.getMessage());
    }

    @Test
    void createFolderInDb() {

        String name = "folder1";
        String path = "shelf-files/user-data/2/shelves/1/folder1";
        Long shelfId = 1L;
        Long parentFolderId = 0L;

        folderService.createFolderInDb(name, path, shelfId, parentFolderId);

        verify(folderRepository, times(1)).save(any(FolderEntity.class));
    }
}