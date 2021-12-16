package com.htec.filesystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFolderRequestModel {

    private String folderName;
    private Long parentFolderId;
    private Long shelfId;
}
