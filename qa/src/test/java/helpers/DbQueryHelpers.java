package helpers;

public class DbQueryHelpers
{
    public static String selectUserFromDb(String email)
    {
        String query= "SELECT * FROM shelf.user WHERE shelf.user.email='"+email+"'";
        return query;
    }

    public static String deleteUserFromDb(String email)
    {
        String query= "DELETE FROM shelf.user WHERE shelf.user.email='"+email+"'";
        return query;
    }
}
