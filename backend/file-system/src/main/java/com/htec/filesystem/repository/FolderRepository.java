package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {

    List<FolderEntity> findAllByShelfId(Long shelfId);

    @Modifying
    @Query("UPDATE FolderEntity f SET f.isDeleted = true WHERE f.shelfId IN (?1)")
    void updateIsDeletedByShelfId(List<Long> shelfIds);
}
