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

    public static final Supplier<ShelfException> shelfNameNotValid = () -> new ShelfException(
            ErrorMessages.SHELF_NAME_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> couldNotUploadFile = () -> new ShelfException(
            ErrorMessages.COULD_NOT_UPLOAD_FILE.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> fileAlreadyExists = () -> new ShelfException(
            ErrorMessages.FILE_WITH_THE_SAME_NAME_ALREADY_EXISTS.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );

    public static final Supplier<ShelfException> shelfAlreadyExists = () -> new ShelfException(
            ErrorMessages.SHELF_WITH_THE_SAME_NAME_ALREADY_EXISTS.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );

    public static final Supplier<ShelfException> noFileWithGivenId = () -> new ShelfException(
            ErrorMessages.NO_FILE_WITH_GIVEN_ID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> noFolderWithGivenId = () -> new ShelfException(
            ErrorMessages.NO_FOLDER_WITH_GIVEN_ID.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> noShelfWithGivenId = () -> new ShelfException(
            ErrorMessages.NO_SHELF_WITH_GIVEN_ID.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
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

    public static final Supplier<ShelfException> shelfNotFound = () -> new ShelfException(
            ErrorMessages.SHELF_NOT_FOUND.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

}
