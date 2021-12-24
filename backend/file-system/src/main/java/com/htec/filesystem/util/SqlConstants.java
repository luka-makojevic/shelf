package com.htec.filesystem.util;

public class SqlConstants {
    public static final String FOLDER_DOWN_STREAM_TREE_QUERY_SQL =
            "WITH RECURSIVE down_stream_tree AS (" +
                    "  SELECT     init_fld.*" +
                    "  FROM       folder init_fld" +
                    "  WHERE      init_fld.id IN (:folderIds) AND init_fld.deleted = :deleted" +
                    "  UNION " +
                    "  SELECT     fld.*" +
                    "  FROM       down_stream_tree INNER JOIN folder fld" +
                    "  ON         fld.parent_folder_id = down_stream_tree.id" +
                    "  WHERE      fld.deleted = :deleted" +
                    ") " +
                    "SELECT * FROM down_stream_tree";

    public static final String FOLDER_UP_STREAM_TREE_QUERY_SQL =
            "WITH RECURSIVE up_stream_tree AS (" +
                    "  SELECT     init_fld.*" +
                    "  FROM       folder init_fld" +
                    "  WHERE      init_fld.id = :folderId AND init_fld.deleted = :deleted" +
                    "  UNION " +
                    "  SELECT     fld.*" +
                    "  FROM       up_stream_tree INNER JOIN folder fld " +
                    "  ON         fld.id = up_stream_tree.parent_folder_id" +
                    "  WHERE      fld.deleted = :deleted" +
                    ")" +
                    "SELECT * FROM up_stream_tree";

    public static final String FILE_DOWN_STREAM_TREE_QUERY_SQL =
            "WITH RECURSIVE down_stream_tree AS (" +
                    "   SELECT     init_fld.*" +
                    "   FROM       folder init_fld" +
                    "   WHERE      init_fld.id IN (:folderIds)  AND init_fld.deleted = :deleted" +
                    "   UNION" +
                    "   SELECT     fl.*" +
                    "   FROM       down_stream_tree INNER JOIN file fl " +
                    "   ON         fl.parent_folder_id = down_stream_tree.id" +
                    "   WHERE      fl.deleted = :deleted" +
                    ")" +
                    "SELECT * FROM down_stream_tree " +
                    "LIMIT 18446744073709551615 OFFSET 1";
}
