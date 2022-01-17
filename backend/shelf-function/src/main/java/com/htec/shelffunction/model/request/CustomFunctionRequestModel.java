package com.htec.shelffunction.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomFunctionRequestModel {

    private String name;
    private Long eventId;
    private Long shelfId;
    private String language;
}
