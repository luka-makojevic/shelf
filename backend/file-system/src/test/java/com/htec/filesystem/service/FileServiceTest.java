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
import org.springframework.data.util.Pair;
import org.springframework.http.RequestEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.htec.filesystem.security.SecurityConstants;
import org.springframework.data.util.Pair;

import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private UserAPICallService userAPICallService;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void saveUserProfilePicture() {

        Map<String, Pair<String, String>> files = new HashMap<>();
        files.put("image", Pair.of("test1.jpg", "content"));

        long id = 1L;
        String fileName = "test1.jpg";
        String homePath = "/home/stefan/";
        String localPath = "/shelf-files/user-data/" + id + "/profile-picture/";
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

        Assertions.assertEquals("Could not save image file.", exception.getMessage());
    }
}