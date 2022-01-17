package com.htec.shelffunction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FunctionDTO {
    private Long id;
    private String name;
    private Long shelfId;
    private Boolean custom;
    private Long eventId;
    private String language;
}