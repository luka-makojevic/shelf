package com.htec.filesystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadRequestModel {

    private List<Long> fileIds;
    private List<Long> folderIds;
}
