package com.htec.filesystem.util;

public class SqlConstants {
    public static final String FOLDER_DOWN_STREAM_TREE_QUERY_SQL =
            "WITH RECURSIVE down_stream_tree AS (" +
                    "  SELECT     init_fld.*" +
                    "  FROM       folder init_fld" +
                    "  WHERE      init_fld.id IN (:folderIds) " +
                    "  UNION " +
                    "  SELECT     fld.*" +
                    "  FROM       down_stream_tree INNER JOIN folder fld" +
                    "  ON         fld.parent_folder_id = down_stream_tree.id" +
                    ") " +
                    "SELECT * FROM down_stream_tree";

    public static final String FOLDER_UP_STREAM_TREE_QUERY_SQL =
            "WITH RECURSIVE up_stream_tree AS (" +
                    "  SELECT     init_fld.*" +
                    "  FROM       folder init_fld" +
                    "  WHERE      init_fld.id = :folderId " +
                    "  UNION " +
                    "  SELECT     fld.*" +
                    "  FROM       up_stream_tree INNER JOIN folder fld " +
                    "  ON         fld.id = up_stream_tree.parent_folder_id" +
                    ")" +
                    "SELECT * FROM up_stream_tree";

    public static final String FILE_DOWN_STREAM_TREE_QUERY_SQL =
            "WITH RECURSIVE down_stream_tree AS (" +
                    "   SELECT     init_fld.*" +
                    "   FROM       folder init_fld" +
                    "   WHERE      init_fld.id IN (:folderIds)" +
                    "   UNION" +
                    "   SELECT     fl.*" +
                    "   FROM       down_stream_tree INNER JOIN file fl " +
                    "   ON         fl.parent_folder_id = down_stream_tree.id" +
                    ")" +
                    "SELECT * FROM down_stream_tree " +
                    "LIMIT 18446744073709551615 OFFSET 1";
}
