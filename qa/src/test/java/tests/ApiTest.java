package tests;

import com.google.gson.Gson;
import helpers.BaseHelperPropertieManager;
import helpers.RestHelpers;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import pages.User;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiTest {

    @Test
    public void apiPostUserRegisteredCheck() throws IOException
    {
        User user = new User();
        user.setValuesForValidUser(user.email, user.password, user.firstName, user.lastName);
        Gson gson = new Gson();
        String parsedJson = gson.toJson(user);

        // Sending Post request
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        builder.setBasePath("/register");
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);

        //Assertions
        assertEquals("User registered",response.jsonPath().get("message").toString());
        assertEquals("201",response.jsonPath().get("status").toString());
    }

    @Test
    public void apiPostNTCUserRegisteredCheck() throws IOException
    {
        User user = new User();
        user.setValuesForInvalidUser(user.email, user.password, user.firstName, user.lastName);
        Gson gson = new Gson();
        String parsedJson = gson.toJson(user);

        // Sending Post request
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI(""));
        builder.setBasePath("/register");
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);

        //Assertions
        String[] expectedMess = {"Email is not valid.","Password is not valid."};
        String[] expectedStatus = {"400"};
        assertTrue(Arrays.toString(expectedMess).contains(response.jsonPath().get("message").toString()));
        assertTrue(Arrays.toString(expectedStatus).contains(response.jsonPath().get("status").toString()));
    }
}
