package helpers;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestHelpers {

    /**
     * Method for sending Post request when request specification object is inserted as parameter
     *
     * @param reqSpec request specification object
     * @return response which can be used for assertion
     * @author lidija.veljkovic
     *
     */

    public static Response sendPostRequest(RequestSpecification reqSpec){

        return given().spec(reqSpec).log().all().relaxedHTTPSValidation().when()
                .post().then().log().all().extract().response();
    }

    /**
     * Method for sending Get request when request specification object is inserted as parameter
     *
     * @param reqSpec request specification object
     * @return response which can be used for assertion
     * @author lidija.veljkovic
     *
     */

    public static Response sendGetRequest(RequestSpecification reqSpec){

        return given().spec(reqSpec).log().all().relaxedHTTPSValidation().when()
                .get().then().log().all().extract().response();
    }

    /**
     * Method for sending Patch request when request specification object is inserted as parameter
     *
     * @param reqSpec request specification object
     * @return response which can be used for assertion
     * @author lidija.veljkovic
     *
     */

    public static Response sendPatchRequest(RequestSpecification reqSpec){

        return given().spec(reqSpec).log().all().relaxedHTTPSValidation().when()
                .patch().then().log().all().extract().response();
    }

    /**
     * Method for sending Delete request when request specification object is inserted as parameter
     *
     * @param reqSpec request specification object
     * @return response which can be used for assertion
     * @author lidija.veljkovic
     *
     */

    public static Response sendDeleteRequest(RequestSpecification reqSpec){

        return given().spec(reqSpec).log().all().relaxedHTTPSValidation().when()
                .delete().then().log().all().extract().response();
    }

    /**
     * Method for sending post request too confirm token
     *
     * @param parsedJson request specification String
     * @return response which can be used for assertion
     * @author srdjanshelf
     *
     */
    public static Response sendingPostReqWithGeneratedToken(String parsedJson, String tokenGenerated)
    {
        RequestSpecBuilder getBuilder = new RequestSpecBuilder();
        getBuilder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        getBuilder.setBasePath("/tokens/confirmation");
        getBuilder.addHeader("Authorization","Bearer "+tokenGenerated);
        getBuilder.setContentType("application/json");
        getBuilder.setBody(parsedJson);
        RequestSpecification reqSpec = getBuilder.build();
        Response getResponse = RestHelpers.sendPostRequest(reqSpec);
        return getResponse;
    }


}
