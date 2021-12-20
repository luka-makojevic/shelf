package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByShelfId(Long shelfId);

    List<FolderEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.parentFolderId = :folderId AND f.isDeleted = :deleted")
    List<FolderEntity> findAllByUserIdAndParentFolderId(Long userId, Long folderId, Boolean deleted);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.id IN (:folderIds)")
    List<FolderEntity> findByUserIdAndFolderId(Long userId, List<Long> folderIds);

    @Transactional
    @Modifying
    @Query("UPDATE FolderEntity f SET f.isDeleted = :deleted WHERE f.parentFolderId IN (:folderIdsToBeDeleted)")
    void updateDeletedByParentFolderIds(Boolean deleted, List<Long> folderIdsToBeDeleted);

    @Transactional
    @Modifying
    @Query("UPDATE FolderEntity f SET f.isDeleted = :deleted WHERE f.id IN (:folderIdsToBeDeleted)")
    void updateDeletedByFolderIds(Boolean deleted, List<Long> folderIdsToBeDeleted);
}
