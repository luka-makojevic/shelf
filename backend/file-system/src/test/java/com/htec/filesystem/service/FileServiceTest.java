package com.htec.filesystem.service;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystem;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private UserAPICallService userAPICallService;

    @Mock
    private MockMultipartFile mockMultipartFile;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void saveUserProfilePicture() {

        MockMultipartFile multipartFile = new MockMultipartFile("file",
                "test1.jpg",
                "multipart/form-data",
                "test1.jpg".getBytes());
        FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

        long id = 1L;
        String fileName = "test1.jpg";
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-photos/" + id + "/";
        String uploadDir = homePath + localPath;

        try (MockedStatic<StringUtils> mocked = mockStatic(StringUtils.class)) {

            when(StringUtils.cleanPath(anyString())).thenReturn(fileName);

            assertEquals(fileName, StringUtils.cleanPath(multipartFile.getOriginalFilename()));

            mocked.verify(() -> StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        }

        try (MockedStatic<FileUtil> mocked = mockStatic(FileUtil.class)) {

            mocked.when(() -> FileUtil.saveFile(anyString(), anyString(), any(MultipartFile.class))).then(invocationOnMock -> null);

            doNothing().when(userAPICallService).updateUserPhotoById(id, localPath + fileName);
            fileService.saveUserProfilePicture(multipartFile, id);

            mocked.verify(() -> FileUtil.saveFile(anyString(), anyString(), any(MultipartFile.class)));

        }
    }

    @Test
    void saveUserProfilePicture_CouldNotSaveImage() {

        long id = 1L;

        when(mockMultipartFile.getOriginalFilename()).thenReturn(null);

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            fileService.saveUserProfilePicture(mockMultipartFile, id);
        });

        Assertions.assertEquals("Could not save image file.", exception.getMessage());
    }
}