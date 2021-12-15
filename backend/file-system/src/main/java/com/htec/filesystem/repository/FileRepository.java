package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByPath(String path);
    List<FileEntity> findAllByShelfId(Long id);
}
