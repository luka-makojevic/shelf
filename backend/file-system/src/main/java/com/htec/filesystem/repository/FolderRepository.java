package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.entity.ShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByShelfId(Long shelfId);

    List<FolderEntity> findAllByShelfIdIn(List<Long> shelfId);

    List<FolderEntity> findAllByParentFolderId(Long folderId);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.parentFolderId = :folderId")
    List<FolderEntity> findAllByUserIdAndParentFolderId(@Param("userId") Long userId,
                                                        @Param("folderId") Long folderId);

    Optional<FolderEntity> findByNameAndParentFolderId(String name, Long parentFolderId);

    @Query("SELECT f " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId AND f.id IN (:folderIds)")
    List<FolderEntity> findByUserIdAndFolderIds(@Param("userId") Long userId,
                                                @Param("folderIds") List<Long> folderIds);

    List<FolderEntity> findAllByShelfIdAndParentFolderIdIsNull(Long shelfId);


    @Query("SELECT s " +
            "FROM FolderEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE f.id = :folderId")
    Optional<ShelfEntity> getShelfByFolderId(@Param("folderId") Long folderId);
}
