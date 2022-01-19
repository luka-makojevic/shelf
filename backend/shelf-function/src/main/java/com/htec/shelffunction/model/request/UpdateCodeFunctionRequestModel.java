package com.htec.shelffunction.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCodeFunctionRequestModel {

    private Long functionId;
    private String code;
}
