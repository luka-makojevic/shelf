package com.htec.shelffunction.exception;

import com.htec.shelffunction.util.ErrorMessages;
import com.zaxxer.hikari.util.FastList;
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

    public static final Supplier<ShelfException> userNotAllowedToGetFunction = () -> new ShelfException(
            ErrorMessages.USER_NOT_ALLOWED_TO_GET_THIS_FUNCTION.getErrorMessage(),
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

    public static final Supplier<ShelfException> deserializationException = () -> new ShelfException(
            ErrorMessages.DESERIALIZING_ERROR.getErrorMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.DESERIALIZING_ERROR.getErrorMessage()
    );

    public static final Supplier<ShelfException> functionAlreadyExists = () -> new ShelfException(
            ErrorMessages.FUNCTION_ALREADY_EXISTS.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> functionIsNotSynchronized = () -> new ShelfException(
            ErrorMessages.FUNCTION_IS_NOT_SYNCHRONIZED.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> errorInFunctionCode = () -> new ShelfException(
            ErrorMessages.FUNCTION_ERROR.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> predefinedFunctionDoesNotExist = () -> new ShelfException(
            ErrorMessages.PREDEFINED_FUNCTION_DOES_NOT_EXIST.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> eventNotAllowed = () -> new ShelfException(
            ErrorMessages.EVENT_NOT_ALLOWED.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );
}
