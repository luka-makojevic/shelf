package com.htec.account.util;

public enum ErrorMessages {

    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
    RECORD_ALREADY_EXISTS("Record already exists."),
    INTERNAL_SERVER_ERROR("Internal server error."),
    NO_RECORD_FOUND_WITH_ID("Record with provided id is not found."),
    NO_RECORD_FOUND_WITH_EMAIL("Record with provided email is not found."),
    AUTHENTICATION_CREDENTIALS_NOT_VALID("Authentication credentials not valid."),
    USER_IS_NOT_LOGGED_IN("User is not logged in."),
    COULD_NOT_UPDATE_RECORD("Could not update record."),
    COULD_NOT_DELETE_RECORD("Could not delete record."),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address is not verified."),
    USER_DOES_NOT_HAVE_PERMISSION("User does not have permission."),
    PASSWORD_NOT_VALID("Password is not valid."),
    EMAIL_NOT_VALID("Email is not valid."),
    FIRST_NAME_NOT_VALID("First name is not valid."),
    LAST_NAME_NOT_VALID("Last name is not valid."),
    BAD_REQUEST("Bad request."),
    UNAUTHORIZED("Unauthorized."),
    NOT_FOUND("Not found."),
    FORBIDDEN("Forbidden."),
    TOKEN_NOT_FOUND("Token not found."),
    TOKEN_EXPIRED("Token expired."),
    TOKEN_NOT_EXPIRED("Token not expired."),
    USER_NOT_FOUND("User not found."),
    TOKEN_NOT_VALID("Token not valid."),
    TOKEN_NOT_ACTIVE("Access token has expired or is not yet valid."),
    PAGE_NUMBER_WRONG("Page index must not be less than one."),
    SIZE_NUMBER_WRONG("Page size must not be less than one."),
    ROLE_UPDATE_FAILED("User already has that role."),
    EMAIL_RESET_REQUEST_WAS_NOT_SENT("Email reset request was not sent"),
    COULD_NOT_INITIALIZE_FOLDER("Could not initialize user folder.");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
