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

        Response response = sendAuthorizedRequests.sendingPostReq("/login",parsedJson);
        tokenGenerated = response.jsonPath().get("jwtToken");
    }

    @Test
    public void apiCreateShelfPTC()
    {
        Response response = restFileSysRequests.setShelf("Shelf123", tokenGenerated);

        assertEquals("Shelf created", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiRenameShelf()
    {
        restFileSysRequests.setShelf("Shelfara123",tokenGenerated);
        Integer shelfId = restFileSysRequests.getShelfId(tokenGenerated);

        fileSys.setValuesForRenameShelf("NoviShelfName", shelfId);
        String parsedJson = gson.toJson(fileSys);

        Response response = sendAuthorizedRequests.sendingPutReqForRenameShelf(tokenGenerated, parsedJson);

        assertEquals("Shelf name updated.", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiDeleteShelf()
    {
        restFileSysRequests.setShelf("Shelfara123",tokenGenerated);
        Integer shelfId = restFileSysRequests.getShelfId(tokenGenerated);

        Response response = sendAuthorizedRequests.sendingDeleteReqForDeleteShelf(tokenGenerated, shelfId);

        assertEquals("Successfully deleted shelves.", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiCreateFolderfPTC()
    {
        restFileSysRequests.setShelf("Shelf123",tokenGenerated);
        restFileSysRequests.setFolder(tokenGenerated,"FolderName123");

        String parsedJson = gson.toJson(fileSys);

        Response response = sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        assertEquals("Folder created", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiCantCreateFolderWithTheSameName()
    {
        restFileSysRequests.setShelf("ShelfName123",tokenGenerated);
        restFileSysRequests.setFolder(tokenGenerated,"FolderName123");

        String parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        restFileSysRequests.setFolder(tokenGenerated,"FolderName123");

        parsedJson = gson.toJson(fileSys);

        Response response = sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated, parsedJson);

        assertEquals("Folder with the same name already exists.", response.jsonPath().get("message").toString());
        assertEquals("403", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiMoveFolderToTrash()
    {
        restFileSysRequests.setShelf("ShelfName123",tokenGenerated);
        restFileSysRequests.setFolder(tokenGenerated,"FolderName");
        String parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        restFileSysRequests.setFolder(tokenGenerated,"FolderName123");
        parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuthorizedRequests.sendingPutReqWithGeneratedTokenFLCShelf(tokenGenerated, fileSys.shelfId);

        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
        Integer folderId = (Integer) shelfItems.get(0).get("id");

        List<Long> list = new ArrayList<>();
        list.add(Long.valueOf(folderId));

        response = sendAuthorizedRequests.sendingPutReqToMoveFolderToTrash(tokenGenerated, list);

        assertEquals("Folders moved to trash", response.jsonPath().get("message").toString());
    }

    @Test
    public void apiDeleteFolderFromTrash()
    {
        List<Long> list = restFileSysRequests.createFolderAndMoveItToTrash(tokenGenerated);
        Response response = sendAuthorizedRequests.sendingDeleteReqToDeleteFolderFromTrash(tokenGenerated, list);

        assertEquals("Folders deleted", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiRecoverFolderFromTrash()
    {
        List<Long> list = restFileSysRequests.createFolderAndMoveItToTrash(tokenGenerated);
        Response response = sendAuthorizedRequests.sendingPutReqToRecoverFolderFromTrash(tokenGenerated, list);

        assertEquals("Folders recovered from trash", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiRenameFolder()
    {
        restFileSysRequests.setShelf("Shelfara123", tokenGenerated);
        restFileSysRequests.setFolder(tokenGenerated, "Fascikla");
        String parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated, parsedJson);
        Response response = sendAuthorizedRequests.sendingPutReqWithGeneratedTokenFLCShelf(tokenGenerated, fileSys.shelfId);


        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
        Integer folderId = (Integer) shelfItems.get(0).get("id");

        fileSys.setValuesForRenameFolder("NewFolderName",folderId);
        parsedJson = gson.toJson(fileSys);

        response = sendAuthorizedRequests.sendingPutReqToRenameFolder(tokenGenerated, parsedJson);

        assertEquals("File renamed", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiUploadFilefPTC()
    {
        restFileSysRequests.setShelf("ShelfName123",tokenGenerated);
        restFileSysRequests.setFolder(tokenGenerated,"FolderName");
        String parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuthorizedRequests.sendingPostReqForUploadFile(tokenGenerated,file, fileSys.shelfId, 0);
        assertEquals("File Uploaded", response.jsonPath().get("message").toString());
    }

    @Test
    public void apiUploadFilesfPTC()
    {
        restFileSysRequests.setShelf("ShelfName123",tokenGenerated);
        restFileSysRequests.setFolder(tokenGenerated,"FolderName");
        String parsedJson = gson.toJson(fileSys);

        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);

        Response response = sendAuthorizedRequests.sendingPostReqForUploadFiles(tokenGenerated, files, fileSys.shelfId, 0);
        assertEquals("File Uploaded", response.jsonPath().get("message").toString());
    }

    @AfterClass
    public static void setUp() throws SQLException, ClassNotFoundException
    {
        cleanup.cleanUp("srdjan.rados@htecgroup.com");
    }
}
