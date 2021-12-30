package helpers.apiHelpers;

import com.google.gson.Gson;
import db.SheldDBServer;
import helpers.dbHelpers.Cleanup;
import helpers.excelHelpers.ExcelReader;
import models.FileSys;
import org.junit.BeforeClass;
import models.User;
import response.ResponseToJson;

import java.io.File;
import java.io.IOException;

public class BaseApiTest {

    public static ExcelReader excelReader;
    public static File file;
    public static SheldDBServer sheldDBServer;
    public static Cleanup cleanup;
    public static User user;
    public static FileSys fileSys;
    public static Gson gson;
    public static SendAuhtorizedRequests sendAuhtorizedRequests;
    public static ResponseToJson responseToJson;

    @BeforeClass
    public static void initialize() throws IOException
    {
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        file = new File("src/main/upload-dir/trolol.txt");
        sheldDBServer = new SheldDBServer();
        user = new User();
        fileSys = new FileSys();
        gson = new Gson();
        sendAuhtorizedRequests = new SendAuhtorizedRequests();
        cleanup = new Cleanup();
        responseToJson = new ResponseToJson();
    }
}
