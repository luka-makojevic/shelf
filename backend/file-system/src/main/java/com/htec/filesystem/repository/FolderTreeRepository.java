package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FolderEntity;
import com.htec.filesystem.util.SqlConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderTreeRepository extends JpaRepository<FolderEntity, Long> {

    @Query(value = SqlConstants.FOLDER_DOWN_STREAM_TREE_QUERY_SQL, nativeQuery = true)
    List<FolderEntity> getFolderDownStreamTrees(@Param("folderIds") List<Long> folderIds, Boolean deleted);

    @Query(value = SqlConstants.FOLDER_UP_STREAM_TREE_QUERY_SQL, nativeQuery = true)
    List<FolderEntity> getFolderUpStreamTree(Long folderId, Boolean deleted);
}
