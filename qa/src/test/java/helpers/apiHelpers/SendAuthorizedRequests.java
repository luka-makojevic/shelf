package helpers.apiHelpers;

import helpers.dbHelpers.DbQueryHelpers;
import helpers.propertieHelpers.PropertieManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.FileSys;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static helpers.apiHelpers.BaseApiTest.*;

public class SendAuthorizedRequests
{
    public Response sendingPostReq(String basePath, String parsedJson)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getURI(""));
        builder.setBasePath(basePath);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);
        return response;
    }

    public Response sendingGetReqWithGeneratedToken(String tokenGenerated,Integer id)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getURI(""));
        builder.setBasePath(String.format("/users/%d", id));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody("");
        RequestSpecification reqSpec = builder.build();
        Response response = RestHelpers.sendGetRequest(reqSpec);
        return response;
    }

    public Response sendingPutReqWithGeneratedToken(String parsedJson, String tokenGenerated, Integer id)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getURI(""));
        builder.setBasePath(String.format("/users/%d", id));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification reqSpec = builder.build();
        Response response = RestHelpers.sendPutRequest(reqSpec);
        return response;
    }

    public Response sendingPostReqForCreateShelf(String tokenGenerated,String parsedJson)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("/shelf");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);
        return response;
    }

    public Response sendingGetShelfReq(String tokenGenerated)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("/shelf");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody("");
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendGetRequest(rSpec);
        return response;
    }

    public Response sendingPostReqForCreateFolder(String tokenGenerated,String parsedJson)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("/folder");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);
        return response;
    }


    public Response sendingPostReqForUploadFile(String tokenGenerated, File file, Integer shelfId, Integer folderId)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath(String.format("/file/upload/%d/%d",shelfId,folderId));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.addHeader("Shelf-Header","File-request");
        builder.setContentType("multipart/form-data");
        builder.addMultiPart("image", file);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);
        return response;
    }

    public Response sendingDeleteReqForDeleteShelf(String tokenGenerated, Integer shelfId)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath(String.format("/shelf/%d", shelfId));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendDeleteRequest(rSpec);
        return response;
    }

    public Response sendingPutReqForRenameShelf(String tokenGenerated, String parsedJson)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("/shelf/rename");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPutRequest(rSpec);
        return response;
    }


    public Response sendingPostReqForUploadFiles(String tokenGenerated,List<File> files, Integer shelfId, Integer folderId)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath(String.format("/file/upload/%d/%d",shelfId,folderId));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.addHeader("Shelf-Header","File-request");
        builder.setContentType("multipart/form-data");

        for (File file : files)
        {
            builder.addMultiPart(file.getName(), file);
        }

        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);
        return response;
    }

    public String setValidUserForFileSys(String email) throws SQLException, ClassNotFoundException {
        user.setValuesForValidUser(excelReader);
        String parsedJson = gson.toJson(user);
        sendAuthorizedRequests.sendingPostReq("/register", parsedJson);

        String sql = null;
        ResultSet rs = sheldDBServer.testDB(DbQueryHelpers.fetchEmailTokenVerify(email));
        while (rs.next()) {
            sql = rs.getString("token");
        }
        String token = responseToJson.setToken(sql);
        parsedJson = gson.toJson(token);
        sendAuthorizedRequests.sendingPostReq("/tokens/confirmation", parsedJson);

        user.setValuesForValidUserToLogin(excelReader);
        parsedJson = gson.toJson(user);

        Response response = sendAuthorizedRequests.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");
        return tokenGenerated;
    }

//    public Response setShelf(String shelfTitle, String tokenGenerated)
//    {
//        String shelfName = responseToJson.setShelfName(shelfTitle);
//        String parsedJson = gson.toJson(shelfName);
//        Response response = sendingPostReqForCreateShelf(tokenGenerated,parsedJson);
//        return response;
//    }

//    public Integer getShelfId(String tokenGenerated)
//    {
//        Response response = sendAuthorizedRequests.sendingGetShelfReq(tokenGenerated);
//        ArrayList<Integer> shelfIdArray = response.jsonPath().get("id");
//        Integer shelfId = shelfIdArray.get(0);
//        return shelfId;
//    }

    public Response sendingPutReqWithGeneratedTokenFLCShelf(String tokenGenerated, Integer shelfId)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath(String.format("/shelf/%d", shelfId));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        RequestSpecification reqSpec = builder.build();
        Response response = RestHelpers.sendGetRequest(reqSpec);
        return response;
    }

    public Response sendingDeleteReqToDeleteFolderFromTrash(String tokenGenerated, List<Long> list)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("/folder");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(list);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendDeleteRequest(rSpec);
        return response;
    }

    public Response sendingPutReqToRenameFolder(String tokenGenerated, String parsedJson)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("folder/rename");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPutRequest(rSpec);
        return response;
    }

    public Response sendingPutReqToRecoverFolderFromTrash(String tokenGenerated, List<Long> list)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath("/folder/recover");
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(list);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPutRequest(rSpec);
        return response;
    }

    public Response sendingPutReqToMoveFolderToTrash(String tokenGenerated, List<Long> list)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath(String.format("/folder/move-to-trash"));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(list);
        RequestSpecification reqSpec = builder.build();
        Response response = RestHelpers.sendPutRequest(reqSpec);
        return response;
    }

//    public FileSys setFolder(String tokenGenerated, String folderTitle)
//    {
//        Response response = sendAuthorizedRequests.sendingGetShelfReq(tokenGenerated);
//        ArrayList<Integer> shelfIdArray = response.jsonPath().get("id");
//        Integer shelfId = shelfIdArray.get(0);
//        fileSys.setValues(folderTitle,0, shelfId);
//        return fileSys;
//    }

//    public List<Long> createFolderAndMoveItToTrash(String tokenGenerated)
//    {
//        sendAuthorizedRequests.setShelf("Shelfara123",tokenGenerated);
//        sendAuthorizedRequests.setFolder(tokenGenerated,"Fascikla");
//        String parsedJson = gson.toJson(fileSys);
//
//        sendAuthorizedRequests.sendingPostReqForCreateFolder(tokenGenerated,parsedJson);
//        Response response = sendAuthorizedRequests.sendingPutReqWithGeneratedTokenFLCShelf(tokenGenerated,fileSys.shelfId);
//
//        List<Map<String,Object>> shelfItems = responseToJson.setShelfItems(response.jsonPath().get("shelfItems"));
//        Integer folderId = (Integer) shelfItems.get(0).get("id");
//
//        List<Long> list = new ArrayList<>();
//        list.add(Long.valueOf(folderId));
//
//        sendAuthorizedRequests.sendingPutReqToMoveFolderToTrash(tokenGenerated, list);
//        return list;
//    }

}
