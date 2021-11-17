package com.htec.shelfserver.exceptionSupplier;

import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.util.ErrorMessages;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class ExceptionSupplier {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String formatDateTime = LocalDateTime.now().format(formatter);

    public static final Supplier<ShelfException> recordAlreadyExists = () -> new ShelfException(
            ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            formatDateTime,
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> recordNotFoundWithEmail = () -> new ShelfException(
            ErrorMessages.NO_RECORD_FOUND_WITH_EMAIL.getErrorMessage(),
            HttpStatus.NOT_FOUND.value(),
            formatDateTime,
            ErrorMessages.NOT_FOUND.getErrorMessage()
    );

    public static final Supplier<ShelfException> passwordNotValid = () -> new ShelfException(
            ErrorMessages.PASSWORD_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            formatDateTime,
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> emailNotValid = () -> new ShelfException(
            ErrorMessages.EMAIL_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            formatDateTime,
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> firstNameNotValid = () -> new ShelfException(
            ErrorMessages.FIRST_NAME_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            formatDateTime,
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> lastNameNotValid = () -> new ShelfException(
            ErrorMessages.LAST_NAME_NOT_VALID.getErrorMessage(),
            HttpStatus.BAD_REQUEST.value(),
            formatDateTime,
            ErrorMessages.BAD_REQUEST.getErrorMessage()
    );

    public static final Supplier<ShelfException> emailFailedToSend = () -> new ShelfException(
            ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            formatDateTime,
            ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage()
    );

    public static final Supplier<ShelfException> emailNotVerified = () -> new ShelfException(
            ErrorMessages.EMAIL_ADDRESS_NOT_VERIFIED.getErrorMessage(),
            HttpStatus.FORBIDDEN.value(),
            formatDateTime,
            ErrorMessages.FORBIDDEN.getErrorMessage()
    );
}
