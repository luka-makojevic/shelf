package com.htec.shelfserver.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private String message;
    private Integer status;
    private String timestamp;
    private String errorMessage;
}
