package helpers.dbHelpers;

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

    public static String fetchEmailTokenVerify(String email)
    {
        String query= "SELECT * FROM shelf.email_verify_token INNER JOIN shelf.user ON email_verify_token.user_id = user.id WHERE shelf.user.email ='"+email+"'";
        return query;
    }
}
