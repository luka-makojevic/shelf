package com.htec.filesystem.validator;

import com.htec.filesystem.exception.ShelfException;
import com.htec.filesystem.util.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileSystemValidatorTest {

    @InjectMocks
    FileSystemValidator fileSystemValidator;

    @BeforeEach
    void setUp() {
    }

    @Test
    void isShelfNameValid() {

        String shelfName = "";

        ShelfException shelfException = assertThrows(ShelfException.class,
                () -> fileSystemValidator.isShelfNameValid(shelfName));

        Assertions.assertEquals(ErrorMessages.SHELF_NAME_NOT_VALID.getErrorMessage(), shelfException.getMessage());
    }
}