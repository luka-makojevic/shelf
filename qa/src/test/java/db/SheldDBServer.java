package db;

import helpers.propertieHelpers.PropertieManager;
import java.sql.*;

public class SheldDBServer
{
        public String DB_URL = PropertieManager.getInstance().getdbURI("db-url");
        public String DB_TABLE = PropertieManager.getInstance().getdbNameTable("shelf-table");
        public String DB_USER = PropertieManager.getInstance().getdbUser("db-user");
        public String DB_PASSWORD = PropertieManager.getInstance().getdbPass("db-pass");

        public ResultSet testDB(String query) throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(DB_URL+DB_TABLE, DB_USER, DB_PASSWORD);
                Statement smt = con.createStatement();
                ResultSet rs = smt.executeQuery(query);
                return rs;
        }

        public void executeQuery(String query) throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(DB_URL+DB_TABLE, DB_USER, DB_PASSWORD);
                Statement smt = con.createStatement();
                smt.executeUpdate(query);
                con.close();
        }
}
