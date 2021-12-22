package db;

import helpers.BaseHelperPropertieManager;
import java.sql.*;

public class SheldDBServer
{
        public String DB_URL = BaseHelperPropertieManager.getInstance().getdbURI("db-url");
        public String DB_TABLE = BaseHelperPropertieManager.getInstance().getdbNameTable("shelf-table");
        public String DB_USER = BaseHelperPropertieManager.getInstance().getdbUser("db-user");
        public String DB_PASSWORD = BaseHelperPropertieManager.getInstance().getdbPass("db-pass");

        public ResultSet testDB(String query) throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(DB_URL+DB_TABLE, DB_USER, DB_PASSWORD);
                Statement smt = con.createStatement();
                ResultSet rs = smt.executeQuery(query);
                return rs;
        }

        public void deleteQuery(String query) throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(DB_URL+DB_TABLE, DB_USER, DB_PASSWORD);
                Statement smt = con.createStatement();
                smt.executeUpdate(query);
                con.close();
        }
}
