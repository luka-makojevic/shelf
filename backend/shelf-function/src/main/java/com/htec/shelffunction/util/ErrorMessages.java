package com.htec.shelffunction.util;

public enum ErrorMessages {

    TOKEN_HEADER_NOT_FOUND("Token header not found"),
    USER_NOT_ALLOWED_TO_ACCESS_THIS_SHELF("You are not allowed to access this shelf."),
    AUTHENTICATION_CREDENTIALS_NOT_VALID("Authentication credentials not valid."),
    USER_NOT_ALLOWED_TO_DELETE_THIS_FUNCTION("You are not allowed to delete this function."),
    USER_NOT_ALLOWED_TO_GET_THIS_FUNCTION("You are not allowed to get this function."),
    FUNCTION_NOT_FOUND("Function not found."),
    FUNCTION_ALREADY_EXISTS("Function already exists."),
    COULD_NOT_DELETE_FILE("Could not delete file."),
    NOT_FOUND("Not found."),
    INTERNAL_SERVER_ERROR("Internal server error."),
    FORBIDDEN("Forbidden."),
    BAD_REQUEST("Bad request."),
    DESERIALIZING_ERROR("Error when deserializing byte[] to KafkaRequestModel."),
    FUNCTION_IS_NOT_SYNCHRONIZED("Function not synchronized.");

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
