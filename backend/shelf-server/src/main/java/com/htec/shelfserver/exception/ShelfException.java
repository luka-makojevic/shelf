
package com.htec.shelfserver.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShelfException extends RuntimeException {

    private String message;
    private Integer status;
    private String timestamp;
    private String errorMessage;
}
