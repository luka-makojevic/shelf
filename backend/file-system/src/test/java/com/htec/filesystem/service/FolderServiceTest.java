package com.htec.filesystem.service;

import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.model.request.CreateFolderRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.util.ErrorMessages;
import com.htec.filesystem.util.FileUtil;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private File file;

    @InjectMocks
    private FolderService folderService;

    @Test
    void getFiles_EmptyList() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderId(testUserId, testFolderId, false))
                .thenReturn(new ArrayList<>());
        when(fileRepository.findAllByUserIdAndParentFolderId(testUserId, testFolderId, false))
                .thenReturn(new ArrayList<>());

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testUserId, testFolderId);

        Assertions.assertEquals(0, Objects.requireNonNull(files.getBody()).size());
    }

    @Test
    void getFiles_NotEmptyList() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderId(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        add(new FolderEntity());
                        add(new FolderEntity());
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByUserIdAndParentFolderId(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FileEntity>() {
                    {
                        add(new FileEntity());
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testUserId, testFolderId);

        Assertions.assertEquals(5, Objects.requireNonNull(files.getBody()).size());
    }

    @Test
    void getFiles_FileFolderType() {

        Long testUserId = 1L;
        Long testFolderId = 1L;

        when(folderRepository.findAllByUserIdAndParentFolderId(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FolderEntity>() {
                    {
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByUserIdAndParentFolderId(testUserId, testFolderId, false)).thenReturn(
                new ArrayList<FileEntity>() {
                    {
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testUserId, testFolderId);

        Assertions.assertEquals(2, Objects.requireNonNull(files.getBody()).size());
        Assertions.assertEquals(false, Objects.requireNonNull(files.getBody()).get(0).isFolder());
        Assertions.assertEquals(true, Objects.requireNonNull(files.getBody()).get(1).isFolder());
    }

    @Test
    void createFolderInDb() {

        String name = "folder1";
        String path = "shelf-files/user-data/2/shelves/1/folder1";
        Long shelfId =1L;
        Long parentFolderId=0L;

        folderService.createFolderInDb(name, path, shelfId, parentFolderId);

        verify(folderRepository, times(1)).save(any(FolderEntity.class));
    }
}