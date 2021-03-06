package com.htec.filesystem.util;

public enum ErrorMessages {

    COULD_NOT_SAVE_IMAGE_FILE("Could not save image file."),
    COULD_NOT_COPY_FILE("Could not copy file."),
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    SHELF_NAME_NOT_VALID("Shelf name is not in a valid format."),
    FILE_NAME_NOT_VALID("File name is not in a valid format."),
    NO_FILES_TO_DOWNLOAD("There are no files to download."),
    FILE_SIZE_IS_TOO_LARGE("File size is over 5MB."),
    FILE_NAME_ALREADY_EXISTS("File with the same name already exists"),
    FOLDER_NAME_ALREADY_EXISTS("Folder with the same name already exists"),
    FOLDER_NAME_NOT_VALID("Folder name is not in a valid format."),
    COULD_NOT_UPLOAD_FILE("Could not upload file."),
    COULD_NOT_DELETE_SHELF("Could not delete shelf."),
    COULD_NOT_DELETE_FILE("Could not delete file."),
    COULD_NOT_DELETE_FOLDER("Could not delete folder."),
    FILE_WITH_THE_SAME_NAME_ALREADY_EXISTS("File with the same name already exists."),
    FILE_COULD_NOT_BE_RENAMED("File couldn't be renamed."),
    FOLDER_WITH_THE_SAME_NAME_ALREADY_EXISTS("Folder with the same name already exists."),
    SHELF_WITH_THE_SAME_NAME_ALREADY_EXISTS("Shelf with the same name already exists."),
    NO_FILE_WITH_GIVEN_ID("There is no file with the provided id."),
    NO_FOLDER_WITH_GIVEN_ID("There is no folder with the provided id."),
    NO_SHELF_WITH_GIVEN_ID("There is no shelf with the provided id."),
    COULD_NOT_FIND_USER_BY_ID("Record with provided id is not found."),
    TOKEN_HEADER_NOT_FOUND("Token header not found"),
    TOKEN_NOT_FOUND("Token not found"),
    COULD_NOT_UPDATE_USER("Could not update user."),
    FILE_NOT_FOUND("File not found."),
    COULD_NOT_DOWNLOAD_FILES("Could not download files."),
    FILES_NOT_FOUND("Files not found."),
    SHELF_WITH_PROVIDED_ID_NOT_FOUND("There is no shelf with provided id."),
    SHELF_NOT_FOUND("Shelf not found."),
    FOLDER_NOT_FOUND("Folder not found."),
    USER_NOT_ALLOWED_TO_DELETE_SHELF("You are not allowed to delete this shelf."),
    USER_NOT_ALLOWED_TO_DELETE_FILE("You are not allowed to delete this file."),
    USER_NOT_ALLOWED_TO_DOWNLOAD_FILE("You are not allowed to download this file."),
    USER_NOT_ALLOWED_TO_DELETE_FOLDER("You are not allowed to delete this folder."),
    USER_NOT_ALLOWED_TO_ACCESS_THIS_SHELF("You are not allowed to access this shelf."),
    USER_NOT_ALLOWED_TO_ACCESS_THIS_FILE("You are not allowed to access this file."),
    USER_NOT_ALLOWED_TO_ACCESS_THIS_FOLDER("You are not allowed to access this folder."),
    AUTHENTICATION_CREDENTIALS_NOT_VALID("Authentication credentials not valid."),
    FOLDER_IS_NOT_IN_THE_GIVEN_SHELF("Folder is not in the given shelf."),
    BAD_REQUEST("Bad request."),
    UNAUTHORIZED("Unauthorized."),
    NOT_FOUND("Not found."),
    FORBIDDEN("Forbidden."),
    SERIALIZING_ERROR("Error when serializing KafkaRequestModel to byte[]");

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
