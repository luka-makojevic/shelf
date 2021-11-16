package com.htec.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends Exception {

    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
