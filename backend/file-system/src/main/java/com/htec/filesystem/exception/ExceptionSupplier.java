package com.htec.filesystem.exception;

import com.htec.filesystem.util.ErrorMessages;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class ExceptionSupplier {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Supplier<ShelfException> couldNotUpdateUser = () -> new ShelfException(
            ErrorMessages.COULD_NOT_UPDATE_USER.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> couldNotSaveImage = () -> new ShelfException(
            ErrorMessages.COULD_NOT_SAVE_IMAGE_FILE.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> couldNotFindUserById = () -> new ShelfException(
            ErrorMessages.COULD_NOT_FIND_USER_BY_ID.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> tokenHeaderNotFound = () -> new ShelfException(
            ErrorMessages.TOKEN_HEADER_NOT_FOUND.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> tokenNotFound = () -> new ShelfException(
            ErrorMessages.TOKEN_HEADER_NOT_FOUND.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> authenticationCredentialsNotValid = () -> new ShelfException(
            ErrorMessages.AUTHENTICATION_CREDENTIALS_NOT_VALID.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> fileNotFound = () -> new ShelfException(
            ErrorMessages.FILE_NOT_FOUND.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

}
