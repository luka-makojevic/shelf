package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPath(String path);

    List<FileEntity> findAllByShelfId(Long shelfId);

    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = ?1 WHERE f.shelfId IN (?2)")
    void updateIsDeletedByShelfIds(boolean delete, List<Long> shelfIds);

    List<FileEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FileEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = ?1 AND f.parentFolderId = ?2 AND f.isDeleted = ?3")
    List<FileEntity> findAllByUserIdAndParentFolderId(Long userId, Long folderId, Boolean isDeleted);
}
