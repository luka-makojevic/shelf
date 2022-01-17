package com.htec.filesystem.repository;

import com.htec.filesystem.entity.ShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<ShelfEntity, Long> {

    Optional<ShelfEntity> findByName(String name);

    Optional<ShelfEntity> findByNameAndUserId(String name, Long userId);

    @Modifying
    @Query("UPDATE ShelfEntity sh SET sh.deleted = :deleted WHERE sh.id IN (:shelfIds)")
    void updateDeletedByIds(@Param("deleted") Boolean delete,
                            @Param("shelfIds") List<Long> shelfIds);

    @Query("SELECT s FROM ShelfEntity s WHERE s.userId = :userId AND s.deleted = 0")
    List<ShelfEntity> findAllByIdAndNotDeleted(@Param("userId") Long userId);

    List<ShelfEntity> findAllByIdAndUserIdIn(Long userId, List<Long> shelfIds);

    List<ShelfEntity> findAllByUserIdAndIdIn(Long userId, List<Long> shelfIds);

    List<ShelfEntity> findAllByUserIdAndDeletedFalse(Long userId);

    List<ShelfEntity> findAllByUserId(Long userId);

    Optional<ShelfEntity> findByIdAndUserId(Long shelfId, Long userId);
}
