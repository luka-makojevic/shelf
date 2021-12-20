package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByShelfId(Long shelfId);

    List<FolderEntity> findAllByShelfIdIn(List<Long> shelfId);

    @Modifying
    @Query("UPDATE FolderEntity f SET f.isDeleted = ?1 WHERE f.shelfId IN (?2)")
    void updateIsDeletedByShelfIds(boolean delete, List<Long> shelfIds);

    List<FolderEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = ?1 AND f.parentFolderId = ?2 AND f.isDeleted = ?3")
    List<FolderEntity> findAllByUserIdAndParentFolderId(Long userId, Long folderId, Boolean isDeleted);

    Optional<FolderEntity> findByNameAndParentFolderId(String name, Long parentFolderId);
}
