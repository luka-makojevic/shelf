package com.htec.filesystem.service;

import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.repository.ShelfRepository;
import com.htec.filesystem.validator.FileSystemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShelfServiceTest {

    @Mock
    ShelfRepository shelfRepository;

    @Mock
    FileSystemValidator fileSystemValidator;

    @InjectMocks
    ShelfService shelfService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createShelf() {

        CreateShelfRequestModel createShelfRequestModel = new CreateShelfRequestModel("test");
        Long userId = 2L;

        shelfService.createShelf(createShelfRequestModel, userId);
        verify(shelfRepository, times(1)).save(any());
    }
}