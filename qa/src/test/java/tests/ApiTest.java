package tests;

import com.google.gson.Gson;
import helpers.BaseHelperPropertieManager;
import helpers.ExcelReader;
import helpers.RestHelpers;
import helpers.RestFunctionHelpers;
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
    public void apiPostNTCUserRegisteredCheck() throws IOException {
        ExcelReader excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");

        // Loop through methods
        for (int i = 2; i <= excelReader.getLastRowNumberFromSheet("apiTest"); i++) {
            User user = new User();
            user.setValuesForInvalidUser(i, user.email, user.password, user.firstName, user.lastName);
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
            String[] expectedMess = {"Record already exists.", "Email is not valid.", "Password is not valid."};
            String[] expectedStatus = {"400"};
            assertTrue(Arrays.toString(expectedMess).contains(response.jsonPath().get("message").toString()));
            assertTrue(Arrays.toString(expectedStatus).contains(response.jsonPath().get("status").toString()));
            System.out.println("---------------------------------");
        }
    }

    @Test
    public void applyTokenConfirmation() throws IOException
    {
        User user = new User();
        user.setValuesForValidUserToLogin(user.email, user.password);
        Gson gson = new Gson();
        String parsedJson = gson.toJson(user);

        Response response = RestHelpers.generateToken(parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");

        gson = new Gson();
        parsedJson = gson.toJson(tokenGenerated);

        RestFunctionHelpers restFunctionHelpers = new RestFunctionHelpers();
        Response getResponse = (Response) restFunctionHelpers.sendingPostReqWithGeneratedToken(parsedJson,tokenGenerated);

        assertEquals("Email confirmed", getResponse.jsonPath().get("message").toString());
        assertEquals(200, getResponse.jsonPath().get("status").toString());
    }
}
