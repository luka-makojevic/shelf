package helpers.dbHelpers;

import db.SheldDBServer;

import java.sql.SQLException;

public class CreateValidUser
{
    public String email;
    public String firstName;
    public String lastName;

    public void createValidUser(String email,String firstName,String lastName) throws SQLException, ClassNotFoundException
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;

        SheldDBServer sheldDBServer = new SheldDBServer();
        sheldDBServer.executeQuery(DbQueryHelpers.insertValidUser(email,firstName,lastName));
    }
}
