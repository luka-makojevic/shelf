package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByShelfId(Long shelfId);

    Optional<FolderEntity> findById(Long folderId);

    List<FolderEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = ?1 AND f.parentFolderId = ?2 AND f.isDeleted = ?3")
    List<FolderEntity> findAllByUserIdAndParentFolderId(Long userId, Long folderId, Boolean isDeleted);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = ?1 AND f.id IN (?2)")
    List<FolderEntity> findByUserIdAndFolderId(Long userId, List<Long> folderIds);

    @Query("UPDATE FolderEntity f SET f.isDeleted = ?1 WHERE f.path LIKE ?2")
    void updateIsDeletedByPath(Boolean isDeleted, String path);
}
