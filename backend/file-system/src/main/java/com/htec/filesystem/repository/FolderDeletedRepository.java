package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderDeletedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderDeletedRepository extends JpaRepository<FolderDeletedEntity, Long> {

    List<FolderDeletedEntity> findAllByShelfIdInAndParentFolderIdIsNull(List<Long> shelfIds);
}
