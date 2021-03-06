package com.htec.filesystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "trash_visible")
    private Boolean trashVisible;

    @Column(name = "shelf_id")
    private Long shelfId;

    @Column(name = "parent_folder_id")
    private Long parentFolderId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getRealName() {
        if (Boolean.TRUE.equals(deleted) && Boolean.TRUE.equals(trashVisible) && name != null) {
            int index = name.lastIndexOf("_");

            return name.substring(0, index);
        }
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
