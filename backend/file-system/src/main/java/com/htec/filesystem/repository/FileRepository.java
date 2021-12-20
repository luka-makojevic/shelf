package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPath(String path);

    List<FileEntity> findAllByShelfId(Long shelfId);

    List<FileEntity> findAllByShelfIdIn(List<Long> shelfId);

    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = :deleted WHERE f.shelfId IN (:shelfIds)")
    void updateIsDeletedByShelfIds(@Param("deleted") Boolean delete,
                                   @Param("shelfIds") List<Long> shelfIds);

    List<FileEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FileEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.parentFolderId = :folderId AND f.isDeleted = :deleted")
    List<FileEntity> findAllByUserIdAndParentFolderIdAndIsDeleted(@Param("userId") Long userId,
                                                                  @Param("folderId") Long folderId,
                                                                  @Param("deleted") Boolean deleted);

    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = :deleted WHERE f.parentFolderId IN (:folderIdsToBeDeleted)")
    void updateDeletedByParentFolderIds(@Param("deleted") Boolean deleted,
                                        @Param("folderIdsToBeDeleted") List<Long> folderIdsToBeDeleted);

    @Modifying
    @Query("UPDATE FileEntity f SET f.isDeleted = :deleted WHERE f.id IN (:fileIdsToBeDeleted)")
    void updateDeletedByFileIds(@Param("deleted") Boolean deleted,
                                @Param("fileIdsToBeDeleted") List<Long> fileIdsToBeDeleted);

    List<FileEntity> findAllByShelfIdAndParentFolderIdIsNull(Long shelfId);

}
