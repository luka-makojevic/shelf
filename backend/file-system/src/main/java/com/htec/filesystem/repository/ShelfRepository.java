package com.htec.filesystem.repository;

import com.htec.filesystem.entity.ShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<ShelfEntity, Long> {

    Optional<ShelfEntity> findByName(String name);

    @Modifying
    @Query("UPDATE ShelfEntity sh SET sh.isDeleted = ?1 WHERE sh.id IN (?2)")
    void updateIsDeletedByIds(boolean isDeleted, List<Long> shelfIds);

    @Query("SELECT s FROM ShelfEntity s WHERE s.userId = ?1 AND s.isDeleted = 0")
    List<ShelfEntity> findAllById(Long userId);

    List<ShelfEntity> findAllByUserIdAndIdIn(Long userId, List<Long> shelfIds);
}
