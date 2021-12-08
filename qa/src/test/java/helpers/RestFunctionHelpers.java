package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestFunctionHelpers
{
    public Response sendingGetReqWithGeneratedToken(String tokenGenerated,Integer id)
    {
        RequestSpecBuilder getBuilder = new RequestSpecBuilder();
        getBuilder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        getBuilder.setBasePath(String.format("/users/%d", id));
        getBuilder.addHeader("Authorization","Bearer "+tokenGenerated);
        getBuilder.setContentType("application/json");
        getBuilder.setBody("");
        RequestSpecification reqSpec = getBuilder.build();
        Response getResponse = RestHelpers.sendGetRequest(reqSpec);
        return getResponse;
    }

    public Response generateToken(String parsedJson)
    {
        // Sending Post request to fetch token and id
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        builder.setBasePath("/login");
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response generateToken = RestHelpers.sendPostRequest(rSpec);
        return generateToken;
    }

    public Response sendingPutReqWithGeneratedToken(String parsedJson, String tokenGenerated, Integer id)
    {
        RequestSpecBuilder getBuilder = new RequestSpecBuilder();
        getBuilder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        getBuilder.setBasePath(String.format("/users/%d", id));
        getBuilder.addHeader("Authorization","Bearer "+tokenGenerated);
        getBuilder.setContentType("application/json");
        getBuilder.setBody(parsedJson);
        RequestSpecification reqSpec = getBuilder.build();
        Response getResponse = RestHelpers.sendPutRequest(reqSpec);
        return getResponse;
    }
}
