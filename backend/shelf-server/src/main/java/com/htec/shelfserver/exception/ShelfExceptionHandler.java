package com.htec.shelfserver.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ShelfExceptionHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @ExceptionHandler(value = {ShelfException.class})
    public ResponseEntity<Object> handleBadRequestException(ShelfException ex, WebRequest webRequest) {
        String formatDateTime = LocalDateTime.now().format(formatter);

        ex.setTimestamp(formatDateTime);
        return ResponseEntity.status(ex.getStatus())
                .body(ex);
    }

}