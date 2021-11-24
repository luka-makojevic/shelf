package com.htec.shelfserver.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private String message;
    private Integer status;
    private String timestamp;
    private String errorMessage;
}
