package com.htec.shelfserver.util;

public enum ErrorMessages {

    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields."),
    RECORD_ALREADY_EXISTS("Record already exists."),
    INTERNAL_SERVER_ERROR("Internal server error."),
    NO_RECORD_FOUND("Record with provided id is not found."),
    NO_RECORD_FOUND_WITH_EMAIL("Record with provided email is not found."),
    AUTHENTICATION_CREDENTIALS_NOT_VALID("Authentication credentials not valid."),
    COULD_NOT_UPDATE_RECORD("Could not update record."),
    COULD_NOT_DELETE_RECORD("Could not delete record."),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address is not verified."),
    USER_NOT_VALID("User is not valid."),
    PASSWORD_NOT_VALID("Password is not valid."),
    EMAIL_NOT_VALID("Email is not valid."),
    FIRST_NAME_NOT_VALID("First name is not valid."),
    LAST_NAME_NOT_VALID("Last name is not valid."),
    BAD_REQUEST("Bad request."),
    NOT_FOUND("Not found."),
    FORBIDDEN("Forbidden");


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
