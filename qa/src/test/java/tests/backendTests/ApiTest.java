package tests.backendTests;

import helpers.*;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Test;
import java.sql.*;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiTest extends BaseApiTest {

    @Test
    public void apiPostUserRegisteredCheck() {
        user.setValuesForValidUser(excelReader);
        String parsedJson = gson.toJson(user);

        // Sending Post request
        Response response = sendRequest.sendingPostReq("/register",parsedJson);

        //Assertions
        assertEquals("User registered", response.jsonPath().get("message").toString());
        assertEquals("201", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiPostNTCUserRegisteredCheck() {
        // Loop through methods
        for (int i = 2; i <= excelReader.getLastRowNumberFromSheet("apiTest"); i++) {
            user.setValuesForInvalidUser(i, excelReader);
            String parsedJson = gson.toJson(user);

            // Sending Post request
            Response response = sendRequest.sendingPostReq("/register",parsedJson);

            //Assertions
            String[] expectedMess = {"Record already exists.", "Email is not valid.", "Password is not valid."};
            String[] expectedStatus = {"400"};

            assertTrue(Arrays.toString(expectedMess).contains(response.jsonPath().get("message").toString()));
            assertTrue(Arrays.toString(expectedStatus).contains(response.jsonPath().get("status").toString()));
            System.out.println("---------------------------------");
        }
    }

    @Test
    public void apiPostUserLoginCheck() {
        user.setValuesForValidUserToLogin(excelReader);
        String parsedJson = gson.toJson(user);

        // Sending Post request
        Response response = sendRequest.sendingPostReq("/login",parsedJson);

        //Assertions
        assertTrue(response.getBody().asString().contains("id"));
        assertTrue(response.getBody().asString().contains("jwtToken"));
        assertTrue(response.getBody().asString().contains("jwtRefreshToken"));
        assertEquals("3", response.jsonPath().get("role").toString());
    }

    @Test
    public void apiPostNTCUserLoginCheck() {
        // Loop through methods
        for (int i = 2; i <= excelReader.getLastRowNumberFromSheet("apiTest"); i++) {
            user.setInvalidValuesForUserToLogin(i,excelReader);
            String parsedJson = gson.toJson(user);

            // Sending Post request
            Response response = sendRequest.sendingPostReq("/login",parsedJson);

            //Assertions
            String[] expectedMess = {"User not found.", "Authentication credentials not valid."};
            String[] expectedStatus = {"400"};

            assertTrue(Arrays.toString(expectedMess).contains(response.jsonPath().get("message").toString()));
            assertTrue(Arrays.toString(expectedStatus).contains(response.jsonPath().get("status").toString()));
            System.out.println("---------------------------------");
        }
    }

    @Test
    public void apiGetUserById() {
        user.setValuesForValidUserToLogin(excelReader);
        String parsedJson = gson.toJson(user);

        Response response = sendRequest.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");
        Integer id = response.jsonPath().get("id");

        // Sending Get request
        response = sendRequest.sendingGetReqWithGeneratedToken(tokenGenerated, id);

        //Assertions
        assertEquals(id.toString(), response.jsonPath().get("id").toString());
        assertEquals("Srdjan", response.jsonPath().get("firstName").toString());
        assertEquals("Rados", response.jsonPath().get("lastName").toString());
        assertEquals("srdjan.rados@htecgroup.com", response.jsonPath().get("email").toString());
        assertEquals("{id=3, name=user}", response.jsonPath().get("role").toString());
    }

    @Test
    public void apiUpdateUserById() {
        user.setValuesForValidUserToLogin(excelReader);
        String parsedJson = gson.toJson(user);

        Response response = sendRequest.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");
        Integer id = response.jsonPath().get("id");

        // Sending Update request
        user.setValuesForUpdatingUser(excelReader);
        parsedJson = gson.toJson(user);
        response = sendRequest.sendingPutReqWithGeneratedToken(parsedJson, tokenGenerated, id);

        //Assertions
        assertEquals(id.toString(), response.jsonPath().get("id").toString());
        assertEquals("Srdjan1", response.jsonPath().get("firstName").toString());
        assertEquals("Rados1", response.jsonPath().get("lastName").toString());
        assertEquals("{id=3, name=user}", response.jsonPath().get("role").toString());
    }

    @AfterClass
    public static void setUp() throws SQLException, ClassNotFoundException
    {
        Cleanup cleanup = new Cleanup();
        cleanup.cleanUp("srdjan.rados@htecgroup.com");
    }
}
