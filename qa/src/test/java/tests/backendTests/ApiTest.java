package tests.backendTests;

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
    public void apiPostNTCUserRegisteredCheck() throws IOException
    {
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
        public void apiGetUserById() throws IOException
        {
            User user = new User();
            user.setValuesForValidUserToLogin(user.email, user.password);
            Gson gson = new Gson();
            String parsedJson = gson.toJson(user);

            RestFunctionHelpers restFunctionHelpers = new RestFunctionHelpers();
            Response response = restFunctionHelpers.generateToken(parsedJson);
            String tokenGenerated = response.jsonPath().get("jwtToken");
            Integer id = response.jsonPath().get("id");

            // Sending Get request
            restFunctionHelpers = new RestFunctionHelpers();
            response = restFunctionHelpers.sendingGetReqWithGeneratedToken(tokenGenerated, id);

            //Assertions
            assertEquals(id.toString(),response.jsonPath().get("id").toString());
            assertEquals("Srdjan", response.jsonPath().get("firstName").toString());
            assertEquals("Rados", response.jsonPath().get("lastName").toString());
            assertEquals("srdjan.rados@htecgroup.com", response.jsonPath().get("email").toString());
            assertEquals("{id=3, name=user}", response.jsonPath().get("role").toString());
        }

        @Test
        public void apiUpdateUserById() throws IOException
        {
            User user = new User();
            user.setValuesForValidUserToLogin(user.email, user.password);
            Gson gson = new Gson();
            String parsedJson = gson.toJson(user);

            RestFunctionHelpers restFunctionHelpers = new RestFunctionHelpers();
            Response response = restFunctionHelpers.generateToken(parsedJson);
            String tokenGenerated = response.jsonPath().get("jwtToken");
            Integer id = response.jsonPath().get("id");

            // Sending Update request
            user = new User();
            user.setValuesForUpdatingUser(user.firstName, user.lastName, user.password);
            gson = new Gson();
            parsedJson = gson.toJson(user);
            restFunctionHelpers = new RestFunctionHelpers();
            response = restFunctionHelpers.sendingPutReqWithGeneratedToken(parsedJson,tokenGenerated, id);

            //Assertions
            assertEquals(id.toString(),response.jsonPath().get("id").toString());
            assertEquals("Srdjan1", response.jsonPath().get("firstName").toString());
            assertEquals("Rados1", response.jsonPath().get("lastName").toString());
            assertEquals("{id=3, name=user}", response.jsonPath().get("role").toString());
        }
}
