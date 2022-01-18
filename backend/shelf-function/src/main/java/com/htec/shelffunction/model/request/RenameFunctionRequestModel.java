package com.htec.shelffunction.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RenameFunctionRequestModel {

    private Long functionId;
    private String newName;
}
