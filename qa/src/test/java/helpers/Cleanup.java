package helpers;

import db.SheldDBServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cleanup
{
    // Data
    String email = "srdjan.rados@htecgroup.com";

    public void cleanup() throws SQLException, ClassNotFoundException
    {
        SheldDBServer sheldDBServer = new SheldDBServer();
        ResultSet rs = sheldDBServer.testDB(DbQueryHelpers.selectUserFromDb(email));
        String table = null;

        boolean status = false;
        while (rs.next()) {
            table = rs.getString("email");
            if (email.equals(table)) {
                sheldDBServer = new SheldDBServer();
                String sql = DbQueryHelpers.deleteUserFromDb(email);
                sheldDBServer.deleteQuery(sql);
                status = true;
                break;
            }
            if(status == false)
            {
                System.out.println("Record not found");
            }
        }
    }
}
