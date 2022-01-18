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

    public static String insertValidUser(String email,String firstName,String lastName)
    {
        String query= "INSERT into shelf.user(\n" +
                "shelf.user.created_at,\n" +
                "shelf.user.email,\n" +
                "shelf.user.email_verified,\n" +
                "shelf.user.first_name,\n" +
                "shelf.user.last_name,\n" +
                "shelf.user.password,\n" +
                "shelf.user.salt,\n" +
                "shelf.user.free_space,\n" +
                "shelf.user.role_id,\n" +
                "shelf.user.picture_name)\n" +
                "values('2022-01-10 22:21:32.393819','"+email+"',1,'"+firstName+"','"+lastName+"','$2a$10$L.C6EVoBKM6uAIaDuC669OrfHNgtCfJwIt3A02kWEb0Q1q1yfqPu2','OvhYWItg',1048576,3,'default-avatar.jpg');";
        return query;
    }
}
