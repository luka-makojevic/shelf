package com.htec.filesystem.util;

public enum ErrorMessages {

    COULD_NOT_SAVE_IMAGE_FILE("Could not save image file."),
    COULD_NOT_UPLOAD_FILE("Could not upload file."),
    FILE_WITH_THE_SAME_NAME_ALREADY_EXISTS("File with the same name already exists."),
    NO_FILE_WITH_GIVEN_ID("There is no file with the provided id."),
    NO_FOLDER_WITH_GIVEN_ID("There is no folder with the provided id."),
    NO_SHELF_WITH_GIVEN_ID("There is no shelf with the provided id."),
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
