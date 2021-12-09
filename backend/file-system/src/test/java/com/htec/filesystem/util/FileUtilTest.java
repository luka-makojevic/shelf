package com.htec.filesystem.util;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.htec.filesystem.exception.ShelfException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUtilTest {

    @Mock
    private Paths paths;

    @Mock
    private MultipartFile mockMultipartFile;

    @Mock
    private Files files;

    @InjectMocks
    private FileUtil fileUtil;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveFile() throws IOException {

        String uploadDir = "/home/stefan/shelf-files/user-photos/1/";
        String fileName = "test1.jpg";
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test1.jpg".getBytes());
        FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
        Path uploadPath = fileSystem.getPath("/home/stefan/shelf-files/user-photos/1/");

        try (MockedStatic<Paths> mocked = mockStatic(Paths.class)) {

            when(Paths.get(anyString())).thenReturn(uploadPath);

            assertEquals(uploadPath, Paths.get(String.valueOf(uploadPath)));

            mocked.verify(() -> Paths.get(String.valueOf(uploadPath)));
        }

        InputStream inputStream = multipartFile.getInputStream();
        Path filePath = uploadPath.resolve(fileName);

        try (MockedStatic<Files> mocked = mockStatic(Files.class)) {

            when(Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenReturn(1L);

            FileUtil.saveFile(uploadDir, fileName, multipartFile);

            mocked.verify(() -> Files.copy(any(InputStream.class), any(Path.class), eq(StandardCopyOption.REPLACE_EXISTING)));
        }
    }
}