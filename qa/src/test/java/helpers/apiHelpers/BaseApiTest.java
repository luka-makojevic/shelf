package helpers.apiHelpers;

import com.google.gson.Gson;
import db.SheldDBServer;
import helpers.dbHelpers.Cleanup;
import helpers.dbHelpers.CreateValidUser;
import helpers.excelHelpers.ExcelReader;
import models.FileSys;
import org.junit.BeforeClass;
import models.User;
import response.ResponseToJson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseApiTest {

    public static ExcelReader excelReader;
    public static File file;

    public static List<File> files;

    public static SheldDBServer sheldDBServer;
    public static CreateValidUser createValidUser;
    public static Cleanup cleanup;
    public static User user;
    public static FileSys fileSys;
    public static Gson gson;
    public static SendAuthorizedRequests sendAuthorizedRequests;
    public static RestFileSysRequests restFileSysRequests;
    public static ResponseToJson responseToJson;
    public static UploadFilesFromPTCDirectory uploadFilesFromPTCDirectory;


    @BeforeClass
    public static void initialize() throws IOException
    {
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        file = new File("src/main/upload-dir/PTC-files-ready-to-upload/dummy.png");

        files = new ArrayList<>();
        uploadFilesFromPTCDirectory = new UploadFilesFromPTCDirectory();
        uploadFilesFromPTCDirectory.uploadFilesFromPTCdir();

        sheldDBServer = new SheldDBServer();
        user = new User();
        fileSys = new FileSys();
        gson = new Gson();
        sendAuthorizedRequests = new SendAuthorizedRequests();
        restFileSysRequests = new RestFileSysRequests();
        createValidUser = new CreateValidUser();
        cleanup = new Cleanup();
        responseToJson = new ResponseToJson();
    }
}
