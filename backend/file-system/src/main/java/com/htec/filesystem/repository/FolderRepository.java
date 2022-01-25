package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByShelfId(Long shelfId);

    List<FolderEntity> findAllByShelfIdIn(List<Long> shelfId);

    @Modifying
    @Query("UPDATE FolderEntity f SET f.deleted = :deleted WHERE f.shelfId IN (:shelfIds)")
    void updateDeletedByShelfIds(@Param("deleted") Boolean delete,
                                 @Param("shelfIds") List<Long> shelfIds);

    List<FolderEntity> findAllByParentFolderId(Long folderId);

    List<FolderEntity> findAllByParentFolderIdAndDeleted(Long folderId, Boolean deleted);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.parentFolderId = :folderId AND f.deleted = :deleted")
    List<FolderEntity> findAllByUserIdAndParentFolderIdAndDeleted(@Param("userId") Long userId,
                                                                  @Param("folderId") Long folderId,
                                                                  @Param("deleted") Boolean deleted);

    Optional<FolderEntity> findByNameAndParentFolderId(String name, Long parentFolderId);

    Optional<FolderEntity> findByNameAndParentFolderIdAndDeleted(String name, Long parentFolderId, Boolean deleted);

    Optional<FolderEntity> findByNameAndParentFolderIdAndIdNot(String name, Long parentFolderId, Long folderId);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.id IN (:folderIds)")
    List<FolderEntity> findByUserIdAndFolderIds(@Param("userId") Long userId,
                                                @Param("folderIds") List<Long> folderIds);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.id IN (:folderIds) AND f.deleted = :deleted")
    List<FolderEntity> findByUserIdAndFolderIdsAndDeleted(@Param("userId") Long userId,
                                                          @Param("folderIds") List<Long> folderIds,
                                                          @Param("deleted") Boolean deleted);

    @Modifying
    @Query("UPDATE FolderEntity f SET f.deleted = :deleted WHERE f.parentFolderId IN (:folderIdsToBeDeleted)")
    void updateDeletedByParentFolderIds(@Param("deleted") Boolean deleted,
                                        @Param("folderIdsToBeDeleted") List<Long> folderIdsToBeDeleted);

    @Modifying
    @Query("UPDATE FolderEntity f SET f.deleted = :deleted WHERE f.id IN (:folderIdsToBeDeleted)")
    void updateDeletedByFolderIdIn(@Param("deleted") Boolean deleted,
                                   @Param("folderIdsToBeDeleted") List<Long> folderIdsToBeDeleted);

    @Modifying
    @Query("UPDATE FolderEntity f SET f.trashVisible = :trashVisible WHERE f.id IN (:folderIds)")
    void updateTrashVisibleByFolderIdIn(@Param("trashVisible") Boolean trashVisible,
                                        @Param("folderIds") List<Long> folderIds);

    List<FolderEntity> findAllByShelfIdAndParentFolderIdIsNullAndDeletedFalse(Long shelfId);


    @Query("SELECT s " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE f.id = :folderId")
    Optional<ShelfEntity> getShelfByFolderId(@Param("folderId") Long folderId);

    Optional<FolderEntity> findByNameAndParentFolderIdAndShelfId(String name, Long parentFolderId, Long shelfId);

    Optional<FolderEntity> findByNameAndParentFolderIdAndShelfIdAndDeleted(String name, Long parentFolderId, Long shelfId, Boolean deleted);

    List<FolderEntity> findAllByShelfIdInAndTrashVisible(List<Long> shelfId, Boolean trashVisible);

    Optional<FolderEntity> findByNameAndShelfIdAndIdNot(String name, Long shelfId, Long folderId);

    Optional<FolderEntity> findByNameAndShelfId(String folderName, Long shelfId);
}
