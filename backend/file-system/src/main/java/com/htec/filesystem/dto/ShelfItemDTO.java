package com.htec.filesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShelfItemDTO {
    private Long id;
    private String name;
    private String path;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;

    private Boolean deleted;
    private Boolean folder;
    private Boolean trashVisible;

    private Long shelfId;
    private Long parentFolderId;
}
