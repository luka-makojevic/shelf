package com.htec.filesystem.exception;

import com.htec.filesystem.model.response.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ShelfExceptionHandler {

    @ExceptionHandler(value = {ShelfException.class})
    public ResponseEntity<Object> handleShelfException(ShelfException ex, WebRequest webRequest) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorMessage(
                        ex.getMessage(), ex.getStatus(), ex.getTimestamp(), ex.getErrorMessage()
                ));
    }
}