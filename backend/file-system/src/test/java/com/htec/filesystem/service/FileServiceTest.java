package com.htec.filesystem.service;

import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.ErrorMessages;
import com.htec.filesystem.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private UserAPICallService userAPICallService;

    @Mock
    FileRepository fileRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    ShelfRepository shelfRepository;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void saveUserProfilePicture() {

        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("image", Pair.of("test.jpeg", "content"));

        long id = 1L;
        String fileName = "test.jpeg";
        String homePath = "/home/stefan/";
        String localPath = "1/profile-picture/";
        String uploadDir = homePath + localPath;

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            doNothing().when(userAPICallService).updateUserPhotoById(id, localPath + fileName);
            fileService.saveUserProfilePicture(id, files);

            mocked.verify(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class)));

        }
    }

    @Test
    void saveUserProfilePicture_CouldNotSaveImage() {

        long id = 1L;

        Map<String, Pair<String, String>> files = new HashMap<>();

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            fileService.saveUserProfilePicture(id, files);
        });

        Assertions.assertEquals(ErrorMessages.COULD_NOT_SAVE_IMAGE_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void saveFile() {

        long shelfId = 1;
        long folderId = 0;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String fileName = "test.jpeg";
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";
        String uploadDir = homePath + localPath;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2l);
        shelfEntity.setId(1l);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setDeleted(false);
        fileEntity.setShelfId(1l);
        fileEntity.setId(1l);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
            //when(fileRepository.findById(anyLong())).thenReturn(Optional.of(fileEntity));

            fileService.saveFile(shelfId, folderId, files);

            mocked.verify(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class)));

        }
    }

    @Test
    void saveFile_CouldNotUploadFile() {

        long shelfId = 1;
        long folderId = 1;

        Map<String, Pair<String, String>> files = new HashMap<>();

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            fileService.saveFile(shelfId, folderId, files);
        });

        Assertions.assertEquals(ErrorMessages.COULD_NOT_UPLOAD_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void saveFile_NoShelfWithGivenId() {

        long shelfId = 3;
        long folderId = 1;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String fileName = "test.jpeg";
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";
        String uploadDir = homePath + localPath;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2l);
        shelfEntity.setId(1l);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setDeleted(false);
        fileEntity.setShelfId(1l);
        fileEntity.setId(1l);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
                fileService.saveFile(shelfId, folderId, files);
            });

            Assertions.assertEquals(ErrorMessages.NO_SHELF_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
        }
    }

    @Test
    void saveFile_NoFolderWithGivenId() {

        long shelfId = 1;
        long folderId = 1;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String fileName = "test.jpeg";
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";
        String uploadDir = homePath + localPath;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2l);
        shelfEntity.setId(1l);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setDeleted(false);
        folderEntity.setShelfId(1l);
        folderEntity.setId(1l);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
            when(folderRepository.findById(folderEntity.getId())).thenReturn(Optional.empty());


            ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
                fileService.saveFile(shelfId, folderId, files);
            });

            Assertions.assertEquals(ErrorMessages.NO_FOLDER_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
        }
    }

    @Test
    void saveFileIntoDB() {

        String filePath = "shelf-files/user-data/2/shelves/1/";
        String fileName = "test.jpg";
        long shelfId = 1;
        long folderId = 1;

        fileService.saveFileIntoDB(filePath, fileName, shelfId, folderId);

        verify(fileRepository, times(1)).save(any());
    }

    @Test
    void getFile_FileNotFound() {
        String path = "test.jpg";

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            fileService.getFile(path);
        });

        Assertions.assertEquals(ErrorMessages.FILE_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }
}