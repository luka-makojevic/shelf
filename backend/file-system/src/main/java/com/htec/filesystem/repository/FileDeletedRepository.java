package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileDeletedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileDeletedRepository extends JpaRepository<FileDeletedEntity, Long> {

    List<FileDeletedEntity> findAllByShelfIdInAndParentFolderIdIsNull(List<Long> shelfIds);
}
