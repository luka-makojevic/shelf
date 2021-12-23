package helpers;

import com.google.gson.Gson;
import org.junit.BeforeClass;
import pages.User;

import java.io.IOException;

public class BaseApiTest {

    public static ExcelReader excelReader;
    public static User user;
    public static Gson gson;
    public static SendAuhtorizedRequests sendAuhtorizedRequests;

    @BeforeClass
    public static void initialize() throws IOException
    {
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        user = new User();
        gson = new Gson();
        sendAuhtorizedRequests = new SendAuhtorizedRequests();
    }

}
