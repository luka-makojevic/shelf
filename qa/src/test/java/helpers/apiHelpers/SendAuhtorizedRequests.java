package helpers.apiHelpers;

import helpers.propertieHelpers.PropertieManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import response.ResponseToJson;

import java.io.File;

public class SendAuhtorizedRequests
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

    public Response sendingPostReqForEmailVerifyToken(String basePath, String parsedJson)
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


    public Response sendingPostReqForUploadFile(String tokenGenerated,File file, Integer shelfId, Integer folderId)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(PropertieManager.getInstance().getShelfURI(""));
        builder.setBasePath(String.format("/file/upload/%d/%d",shelfId,folderId));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.addHeader("Shelf-Header","File-request");
        builder.setContentType("multipart/form-data");
        builder.addMultiPart("file",file);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);
        return response;
    }


}
