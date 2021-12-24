package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPath(String path);

    List<FileEntity> findAllByShelfId(Long shelfId);

    List<FileEntity> findAllByShelfIdIn(List<Long> shelfId);

    @Modifying
    @Query("UPDATE FileEntity f SET f.deleted = :deleted WHERE f.shelfId IN (:shelfIds)")
    void updateDeletedByShelfIds(@Param("deleted") Boolean delete,
                                 @Param("shelfIds") List<Long> shelfIds);

    List<FileEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FileEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.parentFolderId = :folderId AND f.deleted = :deleted")
    List<FileEntity> findAllByUserIdAndParentFolderIdAndDeleted(@Param("userId") Long userId,
                                                                @Param("folderId") Long folderId,
                                                                @Param("deleted") Boolean deleted);

    @Modifying
    @Query("UPDATE FileEntity f SET f.deleted = :deleted WHERE f.parentFolderId IN (:folderIdsToBeDeleted)")
    void updateDeletedByParentFolderIds(@Param("deleted") Boolean deleted,
                                        @Param("folderIdsToBeDeleted") List<Long> folderIdsToBeDeleted);

    @Modifying
    @Query("UPDATE FileEntity f SET f.deleted = :deleted WHERE f.id IN (:fileIdsToBeDeleted)")
    void updateDeletedByFileIds(@Param("deleted") Boolean deleted,
                                @Param("fileIdsToBeDeleted") List<Long> fileIdsToBeDeleted);

    List<FileEntity> findAllByShelfIdAndParentFolderIdIsNullAndDeletedFalse(Long shelfId);

    @Query("SELECT f FROM FileEntity f INNER JOIN ShelfEntity sh " +
            "ON (f.shelfId = sh.id) WHERE sh.userId = :userId AND f.id IN (:fileIds) AND f.deleted = :delete")
    List<FileEntity> findAllByUserIdAndIdIn(@Param("userId") Long userId,
                                            @Param("fileIds") List<Long> fileIds);

    @Modifying
    @Query("UPDATE FileEntity f SET f.deleted = :delete WHERE f.id IN (:fileIds)")
    void updateDeletedByIds(@Param("delete") boolean delete,
                            @Param("fileIds") List<Long> fileIds);

    Optional<FileEntity> findByNameAndParentFolderId(String name, Long parentFolderId);

    Optional<FileEntity> findByNameAndShelfId(String name, Long shelfId);

    @Query("SELECT f FROM FileEntity f INNER JOIN ShelfEntity sh " +
            "ON (f.shelfId = sh.id) " +
            "WHERE sh.userId = :userId AND f.deleted = false AND (f.parentFolderId IN (:parentFolderIds) OR f.parentFolderId IS NULL)")
    List<FileEntity> findAllByUserIdAndParentFolderIdsIn(@Param("userId") Long userId,
                                                         @Param("parentFolderIds") List<Long> parentFolderIds);

    Optional<FileEntity> findByNameAndShelfIdAndParentFolderIdIsNull(String name, Long shelfId);

    Optional<FileEntity> findByNameAndParentFolderIdAndIdNot(String name, Long parentFolderId, Long id);

    List<FileEntity> findAllByShelfIdInAndDeletedTrue(List<Long> shelfId);
}
