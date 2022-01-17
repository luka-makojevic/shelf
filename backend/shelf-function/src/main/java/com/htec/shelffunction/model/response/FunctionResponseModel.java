package com.htec.shelffunction.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FunctionResponseModel {

    private Long functionId;
    private String code;
    private String language;
    private Long eventId;
    private String eventName;
    private String shelfName;
    private Long shelfId;
    private String functionName;
}
