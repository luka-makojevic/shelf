package com.htec.shelffunction.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PredefinedFunctionRequestModel {

    private String function;
    private String name;
    private Long eventId;
    private Long shelfId;
    private Long functionParam;
    private String language;
    private String logFileName;
}
