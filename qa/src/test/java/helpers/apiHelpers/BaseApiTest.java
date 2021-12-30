package helpers.apiHelpers;

import com.google.gson.Gson;
import db.SheldDBServer;
import helpers.dbHelpers.Cleanup;
import helpers.excelHelpers.ExcelReader;
import org.junit.BeforeClass;
import models.User;
import response.ResponseToJson;

import java.io.IOException;

public class BaseApiTest {

    public static ExcelReader excelReader;
    public static SheldDBServer sheldDBServer;
    public static Cleanup cleanup;
    public static User user;
    public static Gson gson;
    public static SendAuhtorizedRequests sendAuhtorizedRequests;
    public static ResponseToJson responseToJson;

    @BeforeClass
    public static void initialize() throws IOException
    {
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        sheldDBServer = new SheldDBServer();
        user = new User();
        gson = new Gson();
        sendAuhtorizedRequests = new SendAuhtorizedRequests();
        cleanup = new Cleanup();
        responseToJson = new ResponseToJson();
    }

}
