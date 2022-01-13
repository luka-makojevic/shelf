package com.htec.shelffunction.util;

public enum ErrorMessages {

    TOKEN_HEADER_NOT_FOUND("Token header not found"),
    USER_NOT_ALLOWED_TO_ACCESS_THIS_SHELF("You are not allowed to access this shelf."),
    AUTHENTICATION_CREDENTIALS_NOT_VALID("Authentication credentials not valid."),
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
