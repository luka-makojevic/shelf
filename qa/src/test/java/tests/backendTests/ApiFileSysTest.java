package tests.backendTests;

import helpers.apiHelpers.BaseApiTest;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ApiFileSysTest extends BaseApiTest
{
    public static String tokenGenerated;

    @BeforeClass
    public static void createValidUserAndGenerateToken() throws SQLException, ClassNotFoundException
    {
        createValidUser.createValidUser("srdjan.rados@htecgroup.com","Srdjan","Rados");

        user.setValuesForValidUserToLogin(excelReader);
        String parsedJson = gson.toJson(user);

        Response response = sendAuhtorizedRequests.sendingPostReq("/login",parsedJson);
        tokenGenerated = response.jsonPath().get("jwtToken");
    }

    @Test
    public void apiCreateShelfPTC()
    {
        Response response = sendAuhtorizedRequests.setShelf("Shelf123", tokenGenerated);

        assertEquals("Shelf created", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiRenameShelf()
    {
        sendAuhtorizedRequests.setShelf("Shelfara123",tokenGenerated);
        sendAuhtorizedRequests.renameShelf(tokenGenerated, "Shelfara");
        String parsedJson = gson.toJson(fileSys);

        Response response = sendAuhtorizedRequests.sendingPutReqForRenameShelf(tokenGenerated, parsedJson);

        assertEquals("Shelf name updated.", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiDeleteShelf()
    {
        sendAuhtorizedRequests.setShelf("Shelfara123",tokenGenerated);

        Response response = sendAuhtorizedRequests.sendingGetShelfReq(tokenGenerated);
        ArrayList<Integer> shelfIdArray = response.jsonPath().get("id");
        Integer shelfId = shelfIdArray.get(0);

        response = sendAuhtorizedRequests.sendingDeleteReqForDeleteShelf(tokenGenerated, shelfId);

        assertEquals("Successfully deleted shelves.", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiCreateFolderfPTC()
    {
        sendAuhtorizedRequests.setShelf("Shelf123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName123");

        String parsedJson = gson.toJson(fileSys);

        Response response = sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        assertEquals("Folder created", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiCantCreateFolderWithTheSameName()
    {
        sendAuhtorizedRequests.setShelf("ShelfName123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName123");

        String parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName123");

        parsedJson = gson.toJson(fileSys);

        Response response = sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated, parsedJson);

        assertEquals("Folder with the same name already exists.", response.jsonPath().get("message").toString());
        assertEquals("403", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiMovedFolderToTrash()
    {
        sendAuhtorizedRequests.setShelf("ShelfName123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName");
        String parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName123");
        parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuhtorizedRequests.setShelfFolderId(tokenGenerated);

        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
        Integer folderId = (Integer) shelfItems.get(0).get("id");

        List<Long> list = new ArrayList<>();
        list.add(Long.valueOf(folderId));

        response = sendAuhtorizedRequests.sendingPutReqToMoveFolderToTrash(tokenGenerated, list);

        assertEquals("Folders moved to trash", response.jsonPath().get("message").toString());
    }

    @Test
    public void apiDeleteFolderFromTrash()
    {
        sendAuhtorizedRequests.setShelf("Shelfara123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"Fascikla");
        String parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuhtorizedRequests.setShelfFolderId(tokenGenerated);

        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
        Integer folderId = (Integer) shelfItems.get(0).get("id");

        List<Long> list = new ArrayList<>();
        list.add(Long.valueOf(folderId));

        sendAuhtorizedRequests.sendingPutReqToMoveFolderToTrash(tokenGenerated, list);
        response = sendAuhtorizedRequests.sendingDeleteReqToDeleteFolderFromTrash(tokenGenerated, list);

        assertEquals("Folders deleted", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiRecoverFolderFromTrash()
    {
        sendAuhtorizedRequests.setShelf("Shelfara123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"Fascikla");
        String parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);
        Response response = sendAuhtorizedRequests.setShelfFolderId(tokenGenerated);

        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
        Integer folderId = (Integer) shelfItems.get(0).get("id");

        List<Long> list = new ArrayList<>();
        list.add(Long.valueOf(folderId));

        sendAuhtorizedRequests.sendingPutReqToMoveFolderToTrash(tokenGenerated, list);
        response = sendAuhtorizedRequests.sendingPutReqToRecoverFolderFromTrash(tokenGenerated, list);

        assertEquals("Folders recovered from trash", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiUploadFilefPTC()
    {
        sendAuhtorizedRequests.setShelf("ShelfName123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName");
        String parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuhtorizedRequests.sendingPostReqForUploadFile(tokenGenerated,file, fileSys.shelfId, 0);
        assertEquals("File Uploaded", response.jsonPath().get("message").toString());
    }

    @Test
    public void apiUploadFilesfPTC()
    {
        sendAuhtorizedRequests.setShelf("ShelfName123",tokenGenerated);
        sendAuhtorizedRequests.setFolder(tokenGenerated,"FolderName");
        String parsedJson = gson.toJson(fileSys);

        sendAuhtorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuhtorizedRequests.sendingPostReqForUploadFiles(tokenGenerated, files, fileSys.shelfId, 0);
        assertEquals("File Uploaded", response.jsonPath().get("message").toString());
    }


    @AfterClass
    public static void setUp() throws SQLException, ClassNotFoundException
    {
        cleanup.cleanUp("srdjan.rados@htecgroup.com");
    }
}
