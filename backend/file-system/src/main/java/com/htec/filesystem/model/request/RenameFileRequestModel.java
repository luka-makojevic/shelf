package com.htec.filesystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RenameFileRequestModel {
    private Long fileId;
    private String fileName;
}
