package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPath(String path);

    List<FileEntity> findAllByShelfId(Long shelfId);

    List<FileEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FileEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.parentFolderId = :folderId AND f.isDeleted = :deleted")
    List<FileEntity> findAllByUserIdAndParentFolderId(Long userId, Long folderId, Boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = :deleted WHERE f.parentFolderId IN (:folderIdsToBeDeleted)")
    void updateDeletedByParentFolderIds(Boolean deleted, List<Long> folderIdsToBeDeleted);

    @Transactional
    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = :deleted WHERE f.id IN (:fileIdsToBeDeleted)")
    void updateDeletedByFileIds(Boolean deleted, List<Long> fileIdsToBeDeleted);

}
