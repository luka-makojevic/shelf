package com.htec.filesystem.service;

import com.htec.filesystem.dto.FileDTO;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FolderService folderService;

    @Test
    void getFiles_EmptyList() {

        Long testFolderId = 1L;

        when(folderRepository.findAllByParentFolderId(1L)).thenReturn(new ArrayList<>());
        when(fileRepository.findAllByParentFolderId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testFolderId);

        Assertions.assertEquals(0 , Objects.requireNonNull(files.getBody()).size());
    }

    @Test
    void getFiles_NotEmptyList() {

        Long testFolderId = 1L;

        when(folderRepository.findAllByParentFolderId(1L)).thenReturn(
                new ArrayList<FolderEntity>(){
                    {
                        add(new FolderEntity());
                        add(new FolderEntity());
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByParentFolderId(1L)).thenReturn(
                new ArrayList<FileEntity>(){
                    {
                        add(new FileEntity());
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testFolderId);

        Assertions.assertEquals(5 , Objects.requireNonNull(files.getBody()).size());
    }


    @Test
    void getFiles_NotEmptyListWithDeletedFile() {

        Long testFolderId = 1L;

        when(folderRepository.findAllByParentFolderId(1L)).thenReturn(
                new ArrayList<FolderEntity>(){
                    {
                        add(new FolderEntity());
                        add(new FolderEntity());
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByParentFolderId(1L)).thenReturn(
                new ArrayList<FileEntity>(){
                    {
                        FileEntity deletedFile = new FileEntity();
                        deletedFile.setDeleted(true);
                        add(deletedFile);
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testFolderId);

        Assertions.assertEquals(4 , Objects.requireNonNull(files.getBody()).size());
    }

    @Test
    void getFiles_FileFolderType() {

        Long testFolderId = 1L;

        when(folderRepository.findAllByParentFolderId(1L)).thenReturn(
                new ArrayList<FolderEntity>(){
                    {
                        add(new FolderEntity());
                    }
                }
        );
        when(fileRepository.findAllByParentFolderId(1L)).thenReturn(
                new ArrayList<FileEntity>(){
                    {
                        add(new FileEntity());
                    }
                }
        );

        ResponseEntity<List<FileDTO>> files = folderService.getFiles(testFolderId);

        Assertions.assertEquals(2, Objects.requireNonNull(files.getBody()).size());
        Assertions.assertEquals(false , Objects.requireNonNull(files.getBody()).get(0).isFolder());
        Assertions.assertEquals(true , Objects.requireNonNull(files.getBody()).get(1).isFolder());
    }
}