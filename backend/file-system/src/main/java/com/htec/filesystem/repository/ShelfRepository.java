package com.htec.filesystem.repository;

import com.htec.filesystem.entity.ShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfRepository extends JpaRepository<ShelfEntity, Long> {
}
