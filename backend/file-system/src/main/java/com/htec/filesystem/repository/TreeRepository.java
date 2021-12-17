package com.htec.filesystem.repository;

import com.htec.filesystem.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreeRepository extends JpaRepository<FileEntity, Long> {

    String DOWN_STREAM_TREE_QUERY_SQL = "WITH RECURSIVE down_stream_tree AS (" +
                                        "  SELECT     init_fld.*" +
                                        "  FROM       folder init_fld" +
                                        "  WHERE      init_fld.id IN (:folderIds)" +
                                        "  UNION " +
                                        "  SELECT     fld.*" +
                                        "  FROM       down_stream_tree INNER JOIN folder fld" +
                                         " ON         fld.parent_folder_id = down_stream_tree.id" +
                                        ") " +
                                        "SELECT * FROM down_stream_tree";

    @Query(value = DOWN_STREAM_TREE_QUERY_SQL , nativeQuery = true)
    void getDownStreamTrees(List<Long> folderIds);

    String UP_STREAM_TREE_QUERY_SQL = "WITH RECURSIVE up_stream_tree AS (" +
                                        "  SELECT     init_fld.*" +
                                        "  FROM       folder init_fld" +
                                        "  WHERE      init_fld.id = :folderId" +
                                        "  UNION " +
                                        "  SELECT     fld.*" +
                                        "  FROM       up_stream_tree INNER JOIN folder fld " +
                                        "  ON         fld.id = up_stream_tree.parent_folder_id" +
                                        ")" +
                                    "SELECT * FROM up_stream_tree";

    @Query(value = UP_STREAM_TREE_QUERY_SQL , nativeQuery = true)
    void getUpStreamTree(Long folderId);
}
