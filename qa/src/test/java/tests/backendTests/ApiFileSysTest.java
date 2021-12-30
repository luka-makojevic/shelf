package tests.backendTests;

import helpers.apiHelpers.BaseApiTest;
import helpers.dbHelpers.DbQueryHelpers;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ApiFileSysTest extends BaseApiTest
{
    @Test
    public void apiCreateShelfPTC() throws SQLException, ClassNotFoundException {

        user.setValuesForValidUser(excelReader);
        String parsedJson = gson.toJson(user);
        sendAuhtorizedRequests.sendingPostReq("/register", parsedJson);

        String sql = null;
        ResultSet rs = sheldDBServer.testDB(DbQueryHelpers.fetchEmailTokenVerify("srdjan.rados@htecgroup.com"));
        while (rs.next()) {
            sql = rs.getString("token");
        }
        String token = responseToJson.setToken(sql);
        parsedJson = gson.toJson(token);
        sendAuhtorizedRequests.sendingPostReqForEmailVerifyToken("/tokens/confirmation", parsedJson);

        user.setValuesForValidUserToLogin(excelReader);
        parsedJson = gson.toJson(user);

        Response response = sendAuhtorizedRequests.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");

        String shelfName = responseToJson.setShelfName("shelfName123");
        parsedJson = gson.toJson(shelfName);
        response = sendAuhtorizedRequests.sendingPostReqForCreateShelf(tokenGenerated,parsedJson);

        assertEquals("Shelf created", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());

    }

    @Test
    public void apiCreateFolderfPTC() throws SQLException, ClassNotFoundException {

        user.setValuesForValidUser(excelReader);
        String parsedJson = gson.toJson(user);
        sendAuhtorizedRequests.sendingPostReq("/register", parsedJson);

        String sql = null;
        ResultSet rs = sheldDBServer.testDB(DbQueryHelpers.fetchEmailTokenVerify("srdjan.rados@htecgroup.com"));
        while (rs.next()) {
            sql = rs.getString("token");
        }
        String token = responseToJson.setToken(sql);
        parsedJson = gson.toJson(token);
        sendAuhtorizedRequests.sendingPostReqForEmailVerifyToken("/tokens/confirmation", parsedJson);

        user.setValuesForValidUserToLogin(excelReader);
        parsedJson = gson.toJson(user);

        Response response = sendAuhtorizedRequests.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");

        String shelfName = responseToJson.setShelfName("shelfName123");
        parsedJson = gson.toJson(shelfName);
        sendAuhtorizedRequests.sendingPostReqForCreateShelf(tokenGenerated,parsedJson);

        response = sendAuhtorizedRequests.sendingGetShelfReq(tokenGenerated);
        ArrayList<Integer> shelfId = response.jsonPath().get("id");
        fileSys.setValues("FolderName",0, shelfId.get(0));
        parsedJson = gson.toJson(fileSys);
        System.out.println("ovo je:"+parsedJson);

        response = sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        assertEquals("Folder created", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiUploadFilesfPTC() throws SQLException, ClassNotFoundException {

        user.setValuesForValidUser(excelReader);
        String parsedJson = gson.toJson(user);
        sendAuhtorizedRequests.sendingPostReq("/register", parsedJson);

        String sql = null;
        ResultSet rs = sheldDBServer.testDB(DbQueryHelpers.fetchEmailTokenVerify("srdjan.rados@htecgroup.com"));
        while (rs.next()) {
            sql = rs.getString("token");
        }
        String token = responseToJson.setToken(sql);
        parsedJson = gson.toJson(token);
        sendAuhtorizedRequests.sendingPostReqForEmailVerifyToken("/tokens/confirmation", parsedJson);

        user.setValuesForValidUserToLogin(excelReader);
        parsedJson = gson.toJson(user);

        Response response = sendAuhtorizedRequests.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");

        String shelfName = responseToJson.setShelfName("shelfName123");
        parsedJson = gson.toJson(shelfName);
        sendAuhtorizedRequests.sendingPostReqForCreateShelf(tokenGenerated,parsedJson);

        response = sendAuhtorizedRequests.sendingGetShelfReq(tokenGenerated);
        ArrayList<Integer> shelfIdArray = response.jsonPath().get("id");
        Integer shelfId = shelfIdArray.get(0);
        fileSys.setValues("FolderName",0, shelfId);
        parsedJson = gson.toJson(fileSys);
        System.out.println("ovo je:"+parsedJson);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);
        response = sendAuhtorizedRequests.sendingPostReqForUploadFile(tokenGenerated,file,shelfId,0);

        assertEquals("File Uploaded", response.jsonPath().get("message").toString());
    }
    @AfterClass
    public static void setUp() throws SQLException, ClassNotFoundException
    {
        cleanup.cleanUp("srdjan.rados@htecgroup.com");
    }
}
