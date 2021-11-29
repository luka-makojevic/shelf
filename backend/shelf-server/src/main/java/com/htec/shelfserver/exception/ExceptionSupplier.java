package com.htec.shelfserver.exception;

import com.htec.shelfserver.util.ErrorMessages;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class ExceptionSupplier {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Supplier<ShelfException> recordAlreadyExists = () -> new ShelfException(
            ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> recordNotFoundWithEmail = () -> new ShelfException(
            ErrorMessages.NO_RECORD_FOUND_WITH_EMAIL.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> recordNotFoundWithId = () -> new ShelfException(
            ErrorMessages.NO_RECORD_FOUND_WITH_ID.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> passwordNotValid = () -> new ShelfException(
            ErrorMessages.PASSWORD_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> authenticationCredentialsNotValid = () -> new ShelfException(
            ErrorMessages.AUTHENTICATION_CREDENTIALS_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> emailNotValid = () -> new ShelfException(
            ErrorMessages.EMAIL_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> firstNameNotValid = () -> new ShelfException(
            ErrorMessages.FIRST_NAME_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> lastNameNotValid = () -> new ShelfException(
            ErrorMessages.LAST_NAME_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> emailFailedToSend = () -> new ShelfException(
            ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage()
    );

    public static final Supplier<ShelfException> emailNotVerified = () -> new ShelfException(
            ErrorMessages.EMAIL_ADDRESS_NOT_VERIFIED.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );

    public static final Supplier<ShelfException> userDoesNotHavePermission = () -> new ShelfException(
            ErrorMessages.USER_DOES_NOT_HAVE_PERMISSION.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );

    public static final Supplier<ShelfException> tokenNotFound = () -> new ShelfException(
            ErrorMessages.TOKEN_NOT_FOUND.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> tokenExpired = () -> new ShelfException(
            ErrorMessages.TOKEN_EXPIRED.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> userNotFound = () -> new ShelfException(
            ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> tokenNotValid = () -> new ShelfException(
            ErrorMessages.TOKEN_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> accessTokenNotActive = () -> new ShelfException(
            ErrorMessages.TOKEN_NOT_ACTIVE.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );
    public static final Supplier<ShelfException> pageWrong = () -> new ShelfException(
            ErrorMessages.PAGE_NUMBER_WRONG.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> sizeWrong = () -> new ShelfException(
            ErrorMessages.SIZE_NUMBER_WRONG.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> wrongRoleUpdate = () -> new ShelfException(
            ErrorMessages.ROLE_UPDATE_FAILED.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().format(formatter),
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );
}
