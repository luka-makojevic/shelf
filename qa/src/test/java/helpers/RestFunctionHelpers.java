package helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestFunctionHelpers
{
    public RestFunctionHelpers sendingPostReqWithGeneratedToken(String parsedJson,String tokenGenerated)
    {
        RequestSpecBuilder getBuilder = new RequestSpecBuilder();
        getBuilder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        getBuilder.setBasePath("/tokens/confirmation");
        getBuilder.addHeader("Authorization","Bearer "+tokenGenerated);
        getBuilder.setContentType("application/json");
        getBuilder.setBody(parsedJson);
        RequestSpecification reqSpec = getBuilder.build();
        Response getResponse = RestHelpers.sendPostRequest(reqSpec);
        return (RestFunctionHelpers) getResponse;
    }
}
