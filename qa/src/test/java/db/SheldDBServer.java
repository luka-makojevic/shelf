package db;

import java.sql.*;

public class SheldDBServer
{
        public String DB_URL = "jdbc:mysql://10.10.0.136/shelf";
        public String DB_USER = "root";
        public String DB_PASSWORD = "root";

        public ResultSet testDB(String query) throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                //System.out.println("Driver loaded");

                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                //System.out.println("connected to db");
                Statement smt = con.createStatement();
                ResultSet rs = smt.executeQuery(query);
                return rs;
        }

        public void deleteQuery(String query) throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                //System.out.println("Driver loaded");

                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                //System.out.println("connected to db");
                Statement smt = con.createStatement();
                smt.executeUpdate(query);
                con.close();
        }
}
