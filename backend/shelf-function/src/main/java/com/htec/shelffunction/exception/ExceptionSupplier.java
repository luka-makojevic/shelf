package com.htec.shelffunction.exception;

import com.htec.shelffunction.util.ErrorMessages;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class ExceptionSupplier {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public static final Supplier<ShelfException> userNotAllowedToAccessShelf = () -> new ShelfException(
            ErrorMessages.USER_NOT_ALLOWED_TO_ACCESS_THIS_SHELF.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );

    public static final Supplier<ShelfException> userNotAllowedToDeleteFunction = () -> new ShelfException(
            ErrorMessages.USER_NOT_ALLOWED_TO_DELETE_THIS_FUNCTION.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );

    public static final Supplier<ShelfException> functionNotFound = () -> new ShelfException(
            ErrorMessages.FUNCTION_NOT_FOUND.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> couldNotDeleteFile = () -> new ShelfException(
            ErrorMessages.COULD_NOT_DELETE_FILE.getErrorMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage()
    );
}
