package com.htec.filesystem.service;

import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
import com.htec.filesystem.repository.*;
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
}