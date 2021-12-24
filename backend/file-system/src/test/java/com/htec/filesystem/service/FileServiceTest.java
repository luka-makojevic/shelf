package com.htec.filesystem.service;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.model.request.RenameFileRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.repository.FolderRepository;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.util.ErrorMessages;
import com.htec.filesystem.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
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
        Long userId = 2L;

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

            fileService.saveFile(shelfId, folderId, files, userId);

            mocked.verify(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class)));

        }
    }

    @Test
    void saveFile_CouldNotUploadFile() {

        long shelfId = 1;
        long folderId = 1;
        Long userId = 4L;

        Map<String, Pair<String, String>> files = null;

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.saveFile(shelfId, folderId, files, userId));

        assertEquals(ErrorMessages.COULD_NOT_UPLOAD_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void saveFile_NoShelfWithGivenId() {

        long shelfId = 3;
        long folderId = 1;
        Long userId = 4L;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";


        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);
        shelfEntity.setId(1L);

        shelfEntity.setUserId(userId);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(3L);
        folderEntity.setId(1L);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setShelfId(1L);
        fileEntity.setId(1L);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            when(folderRepository.findById(folderEntity.getId())).thenReturn(Optional.of(folderEntity));
            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            ShelfException exception = Assertions.assertThrows(ShelfException.class,
                    () -> fileService.saveFile(shelfId, folderId, files, userId));

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
        Long userId = 4L;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);
        shelfEntity.setId(1L);

        shelfEntity.setUserId(userId);

        FolderEntity folderEntity = new FolderEntity();

        folderEntity.setShelfId(1L);
        folderEntity.setId(1L);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            when(folderRepository.findById(folderEntity.getId())).thenReturn(Optional.empty());

            ShelfException exception = Assertions.assertThrows(ShelfException.class,
                    () -> fileService.saveFile(shelfId, folderId, files, userId));

            assertEquals(ErrorMessages.NO_FOLDER_WITH_GIVEN_ID.getErrorMessage(), exception.getMessage());
        }
    }

    @Test
    void saveFile_FolderIsNotInGivenShelf() {

        long shelfId = 3;
        long folderId = 1;
        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("file", Pair.of("test.jpeg", "content"));
        String fileName = "test.jpeg";
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/2/shelves" + shelfId + "/";
        String uploadDir = homePath + localPath;
        Long userId = 4L;

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(2L);
        shelfEntity.setId(1L);
        shelfEntity.setUserId(userId);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setShelfId(1L);
        folderEntity.setId(1L);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setShelfId(1L);
        fileEntity.setId(1L);

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            when(folderRepository.findById(folderEntity.getId())).thenReturn(Optional.of(folderEntity));
            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(byte[].class))).then(invocationOnMock -> null);

            ShelfException exception = Assertions.assertThrows(ShelfException.class,
                    () -> fileService.saveFile(shelfId, folderId, files, userId));

            assertEquals(ErrorMessages.FOLDER_IS_NOT_IN_THE_GIVEN_SHELF.getErrorMessage(), exception.getMessage());
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

        file.setId(1L);
        user.setId(1L);

        when(fileRepository.findByIdAndUserIdAndDeleted(user.getId(), file.getId(), false)).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.getFile(user, file.getId(), true));

        assertEquals(ErrorMessages.FILE_NOT_FOUND.getErrorMessage(), exception.getMessage());

        verify(fileRepository, times(1)).findByIdAndUserIdAndDeleted(user.getId(), file.getId(), false);
    }

    @Test
    void updateDeletedFilesTrue() {

        user.setId(1L);
        file.setId(1L);
        file.setPath("test/fileName");
        fileEntities.add(file);
        fileIds.add(1L);

        when(fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds)).thenReturn(fileEntities);

        try (MockedStatic<Files> mocked = mockStatic(Files.class)) {

            mocked.when(() -> Files.move(any(), any())).then(invocationOnMock -> null);

            fileService.updateDeletedFiles(user, fileIds, true, true);

        }

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(1)).saveAll(fileEntities);
        verify(fileRepository, times(1)).updateDeletedByIds(true, fileIds);
    }

    @Test
    void updateDeletedFilesFalse() {

        user.setId(1L);
        file.setId(1L);
        file.setPath("test/fileName");
        fileEntities.add(file);
        fileIds.add(1L);

        when(fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds)).thenReturn(fileEntities);


        try (MockedStatic<Files> mocked = mockStatic(Files.class)) {

            mocked.when(() -> Files.move(any(), any())).then(invocationOnMock -> null);

            fileService.updateDeletedFiles(user, fileIds, true, true);

        }

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(1)).saveAll(fileEntities);
        verify(fileRepository, times(1)).updateDeletedByIds(true, fileIds);
    }

    @Test
    void updateDeletedFiles_idsNotFound() {

        user.setId(1L);
        file.setId(1L);
        fileIds.add(1L);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.updateDeletedFiles(user, fileIds, true, true));

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(0)).saveAll(fileEntities);
        verify(fileRepository, times(0)).updateDeletedByIds(true, fileIds);

        assertEquals(ErrorMessages.FILES_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void softDeleteFiles_idsNotEquals() {

        user.setId(1L);
        file.setId(1L);
        fileIds.add(3L);
        fileEntities.add(file);

        when(fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds)).thenReturn(fileEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.updateDeletedFiles(user, fileIds, true, true));

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(0)).saveAll(fileEntities);
        verify(fileRepository, times(0)).updateDeletedByIds(true, fileIds);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void fileRename() {

        Long userId = 4L;
        RenameFileRequestModel renameFileRequestModel = new RenameFileRequestModel(4L, "fileName");

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(userId);
        shelfEntity.setId(1L);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setShelfId(1L);
        fileEntity.setName("test.jpg");
        fileEntity.setPath("testPath");

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(fileEntity));

        fileService.fileRename(userId, renameFileRequestModel);

        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    void fileRename_UserNotAllowedToAccessFile() {

        Long userId = 4L;
        RenameFileRequestModel renameFileRequestModel = new RenameFileRequestModel(4L, "fileName");

        ShelfEntity shelfEntity = new ShelfEntity();
        shelfEntity.setUserId(1L);
        shelfEntity.setId(1L);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setShelfId(1L);
        fileEntity.setName("test.jpg");
        fileEntity.setPath("testPath");

        when(shelfRepository.findById(anyLong())).thenReturn(Optional.of(shelfEntity));
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(fileEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.fileRename(userId, renameFileRequestModel));

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_ACCESS_THIS_FILE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void deleteFile() throws IOException {

        user.setId(1L);
        file.setId(1L);
        file.setPath("test/test1");
        fileEntities.add(file);
        fileIds.add(1L);

        when(fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds)).thenReturn(fileEntities);

        try (MockedStatic<FileUtils> mocked = mockStatic(FileUtils.class)) {

            mocked.when(() -> FileUtils.forceDelete(any())).then(invocationOnMock -> null);

            fileService.deleteFile(user, fileIds);

        }

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(1)).deleteAll(fileEntities);
    }

    @Test
    void deleteFile_idsNotFound() {

        user.setId(1L);
        file.setId(1L);
        fileIds.add(1L);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.deleteFile(user, fileIds));

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(0)).deleteAll(fileEntities);

        assertEquals(ErrorMessages.FILES_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void deleteFile_idsNotEquals() {

        user.setId(1L);
        file.setId(1L);
        fileIds.add(3L);
        fileEntities.add(file);

        when(fileRepository.findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds)).thenReturn(fileEntities);

        ShelfException exception = Assertions.assertThrows(ShelfException.class,
                () -> fileService.deleteFile(user, fileIds));

        verify(fileRepository, times(1)).findAllByUserIdAndDeletedAndIdIn(user.getId(), false, fileIds);
        verify(fileRepository, times(0)).deleteAll(fileEntities);

        assertEquals(ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_FILE.getErrorMessage(), exception.getMessage());
    }
}