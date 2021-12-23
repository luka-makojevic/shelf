package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SendAuhtorizedRequests
{
    public Response sendingPostReq(String basePath, String parsedJson)
    {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
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
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
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
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        builder.setBasePath(String.format("/users/%d", id));
        builder.addHeader("Authorization","Bearer "+tokenGenerated);
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification reqSpec = builder.build();
        Response response = RestHelpers.sendPutRequest(reqSpec);
        return response;
    }
}
