package tests;

import com.google.gson.Gson;
import helpers.BaseHelperPropertieManager;
import helpers.RestHelpers;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;
import pages.User;

import java.io.IOException;

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
        builder.setBaseUri(BaseHelperPropertieManager.getInstance().getURI("/users"));
        builder.setBasePath("/register");
        builder.setContentType("application/json");
        builder.setBody(parsedJson);
        RequestSpecification rSpec = builder.build();
        Response response = RestHelpers.sendPostRequest(rSpec);

        //Assertions
        Assert.assertEquals("User registered",response.jsonPath().get("message").toString());
        Assert.assertEquals("201",response.jsonPath().get("status").toString());
    }
}
