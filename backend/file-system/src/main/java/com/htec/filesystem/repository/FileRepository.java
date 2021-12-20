package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPath(String path);

    List<FileEntity> findAllByShelfId(Long shelfId);

    List<FileEntity> findAllByShelfIdIn(List<Long> shelfId);

    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = ?1 WHERE f.shelfId IN (?2)")
    void updateIsDeletedByShelfIds(boolean delete, List<Long> shelfIds);

    List<FileEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FileEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
<<<<<<< HEAD
            "WHERE s.userId = ?1 AND f.parentFolderId = ?2 AND f.isDeleted = ?3")
    List<FileEntity> findAllByUserIdAndParentFolderId(Long userId, Long folderId, Boolean isDeleted);
=======
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

>>>>>>> CU-1xpd34y - Soft Delete folder
}
