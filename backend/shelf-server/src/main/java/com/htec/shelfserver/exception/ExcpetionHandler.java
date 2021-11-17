package com.htec.shelfserver.exception;

import com.htec.shelfserver.responseModel.ResponseMessage;
import com.htec.shelfserver.util.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExcpetionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest webRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = LocalDateTime.now().format(formatter);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), formatDateTime, ErrorMessages.BAD_REQUEST.getErrorMessage()));
    }

}