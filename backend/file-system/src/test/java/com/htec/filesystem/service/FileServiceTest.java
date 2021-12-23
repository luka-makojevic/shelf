package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    AuthUser user;
    FileEntity file;
    List<Long> fileIds;
    List<FileEntity> fileEntities;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        file = new FileEntity();
        fileIds = new ArrayList<>();
        fileEntities = new ArrayList<>();
    }

    @Test
    void saveUserProfilePicture() {

        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("image", Pair.of("test.jpeg", "content"));

        long id = 1L;
        String fileName = "test.jpeg";
        String homePath = "/home/stefan/";
        String localPath = "1/profile-picture/";

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

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.saveUserProfilePicture(id, files));

        assertEquals(ErrorMessages.COULD_NOT_SAVE_IMAGE_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void saveFile() {

        long shelfId = 1;
        long folderId = 0;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);
        shelfEntity.setId(1L);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setShelfId(1L);
        fileEntity.setId(1L);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));

            fileService.saveFile(shelfId, folderId, files);

            mocked.verify(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class)));

        }
    }

    @Test
    void saveFile_CouldNotUploadFile() {

        long shelfId = 1;
        long folderId = 1;

        Map<String, Pair<String, String>> files = null;

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.saveFile(shelfId, folderId, files));

        assertEquals(ErrorMessages.COULD_NOT_UPLOAD_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void saveFile_NoShelfWithGivenId() {

        long shelfId = 3;
        long folderId = 1;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);
        shelfEntity.setId(1L);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setShelfId(1L);
        fileEntity.setId(1L);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            ShelfException exception = Assertions.assertThrows(ShelfException.class,
                    () -> fileService.saveFile(shelfId, folderId, files));

            assertEquals(ErrorMessages.NO_SHELF_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
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
        shelfEntity.setUserId(2L);
        shelfEntity.setId(1L);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setId(1L);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
            when(folderRepository.findById(folderEntity.getId())).thenReturn(Optional.empty());

            ShelfException exception = Assertions.assertThrows(ShelfException.class,
                    () -> fileService.saveFile(shelfId, folderId, files));

            assertEquals(ErrorMessages.NO_FOLDER_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
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

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.getFile(path));

        assertEquals(ErrorMessages.FILE_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void updateDeletedFilesTrue() {
        user.setId(1L);
        file.setId(1L);
        fileEntities.add(file);
        fileIds.add(1L);

        when(fileRepository.findAllByUserIdAndIdIn(user.getId(), fileIds)).thenReturn(fileEntities);

        fileService.updateDeletedFiles(user, fileIds);

        verify(fileRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), fileIds);
        verify(fileRepository, times(1)).saveAll(fileEntities);
    }

    @Test
    void updateDeletedFilesFalse() {
        user.setId(1L);
        file.setId(1L);
        fileEntities.add(file);
        fileIds.add(1L);

        when(fileRepository.findAllByUserIdAndIdIn(user.getId(), fileIds)).thenReturn(fileEntities);

        fileService.updateDeletedFiles(user, fileIds);

        verify(fileRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), fileIds);
        verify(fileRepository, times(1)).saveAll(fileEntities);
    }

    @Test
    void updateDeletedFiles_idsNotFound() {

        user.setId(1L);
        file.setId(1L);
        fileIds.add(1L);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.updateDeletedFiles(user, fileIds));

        verify(fileRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), fileIds);
        verify(fileRepository, times(0)).saveAll(fileEntities);

        assertEquals(ErrorMessages.FILES_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void softDeleteFiles_idsNotEquals() {

        user.setId(1L);
        file.setId(1L);
        fileIds.add(3L);
        fileEntities.add(file);

        when(fileRepository.findAllByUserIdAndIdIn(user.getId(), fileIds)).thenReturn(fileEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.updateDeletedFiles(user, fileIds));

        verify(fileRepository, times(1)).findAllByUserIdAndIdIn(user.getId(), fileIds);
        verify(fileRepository, times(0)).saveAll(fileEntities);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_FILE.getErrorMessage(), exception.getMessage());
    }
}