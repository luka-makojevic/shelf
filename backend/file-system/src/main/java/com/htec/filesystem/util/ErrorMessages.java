package com.htec.filesystem.util;

public enum ErrorMessages {

    COULD_NOT_SAVE_IMAGE_FILE("Could not save image file."),
    COULD_NOT_FIND_USER_BY_ID("Record with provided id is not found."),
    TOKEN_HEADER_NOT_FOUND("Token header not found"),
    TOKEN_NOT_FOUND("Token not found"),
    COULD_NOT_UPDATE_USER("Could not update user."),
    FILE_NOT_FOUND("File not found."),
    AUTHENTICATION_CREDENTIALS_NOT_VALID("Authentication credentials not valid."),
    BAD_REQUEST("Bad request."),
    UNAUTHORIZED("Unauthorized."),
    NOT_FOUND("Not found."),
    FORBIDDEN("Forbidden.");

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
