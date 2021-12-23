package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import com.htec.filesystem.util.SqlConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileTreeRepository extends JpaRepository<FileEntity, Long> {

    @Query(value = SqlConstants.FILE_DOWN_STREAM_TREE_QUERY_SQL, nativeQuery = true)
    List<FileEntity> getFileDownStreamTrees(@Param("folderIds") List<Long> folderIds,
                                            @Param("deleted") Boolean deleted);
}
