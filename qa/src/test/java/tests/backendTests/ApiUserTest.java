package tests.backendTests;

import helpers.apiHelpers.BaseApiTest;
import helpers.dbHelpers.DbQueryHelpers;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Test;
import java.sql.*;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiUserTest extends BaseApiTest {

    @Test
    public void apiPostUserRegisteredCheck() {
        user.setValuesForValidUser(excelReader);
        String parsedJson = gson.toJson(user);

        // Sending Post request
        Response response = sendAuhtorizedRequests.sendingPostReq("/register",parsedJson);

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
            Response response = sendAuhtorizedRequests.sendingPostReq("/register",parsedJson);

            //Assertions
            String[] expectedMess = {"Record already exists.", "Email is not valid.", "Password is not valid."};
            String[] expectedStatus = {"400"};

            assertTrue(Arrays.toString(expectedMess).contains(response.jsonPath().get("message").toString()));
            assertTrue(Arrays.toString(expectedStatus).contains(response.jsonPath().get("status").toString()));
            System.out.println("---------------------------------");
        }
    }

    @Test
    public void apiPostEmailVerifyToken() throws SQLException, ClassNotFoundException {

//        user.setValuesForValidUser(excelReader);
//        String parsedJson = gson.toJson(user);
//        sendAuhtorizedRequests.sendingPostReq("/register", parsedJson);
        String sql = null;

        ResultSet rs = sheldDBServer.testDB(DbQueryHelpers.fetchEmailTokenVerify("srdjan.rados@htecgroup.com"));
        while (rs.next()) {
            sql = rs.getString("token");
        }

        String token = responseToJson.setToken(sql);
        String parsedJson = gson.toJson(token);
        Response response = sendAuhtorizedRequests.sendingPostReqForEmailVerifyToken("/tokens/confirmation", parsedJson);

        //Assertions
        assertEquals("Email confirmed", response.jsonPath().get("message").toString());
        assertEquals("200", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiPostUserLoginCheck() {
        user.setValuesForValidUserToLogin(excelReader);
        String parsedJson = gson.toJson(user);

        // Sending Post request
        Response response = sendAuhtorizedRequests.sendingPostReq("/login",parsedJson);
        Integer id = response.jsonPath().get("id");
        String jwtToken = response.jsonPath().get("jwtToken");
        String jwtRefreshToken = response.jsonPath().get("jwtRefreshToken");

        //Assertions
        assertEquals(id.toString(), response.jsonPath().get("id").toString());
        assertEquals(jwtToken, response.jsonPath().get("jwtToken").toString());
        assertEquals(jwtRefreshToken, response.jsonPath().get("jwtRefreshToken").toString());
        assertEquals("3", response.jsonPath().get("role").toString());
    }

    @Test
    public void apiPostInvalidEmailLoginCheck() {
            user.setInvalidValuesForUserToLogin(2, 0, 2, 1, excelReader);
            String parsedJson = gson.toJson(user);

            // Sending Post request
            Response response = sendAuhtorizedRequests.sendingPostReq("/login",parsedJson);

            //Assertions
            assertEquals("User not found.", response.jsonPath().get("message").toString());
            assertEquals("400", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiPostInvalidPasswordLoginCheck() {
        user.setInvalidValuesForUserToLogin(3, 0, 3, 1, excelReader);
        String parsedJson = gson.toJson(user);

        // Sending Post request
        Response response = sendAuhtorizedRequests.sendingPostReq("/login",parsedJson);

        //Assertions
        assertEquals("Authentication credentials not valid.", response.jsonPath().get("message").toString());
        assertEquals("400", response.jsonPath().get("status").toString());
    }

    @Test
    public void apiGetUserById() {
        user.setValuesForValidUserToLogin(excelReader);
        String parsedJson = gson.toJson(user);

        Response response = sendAuhtorizedRequests.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");
        Integer id = response.jsonPath().get("id");

        // Sending Get request
        response = sendAuhtorizedRequests.sendingGetReqWithGeneratedToken(tokenGenerated, id);

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

        Response response = sendAuhtorizedRequests.sendingPostReq("/login", parsedJson);
        String tokenGenerated = response.jsonPath().get("jwtToken");
        Integer id = response.jsonPath().get("id");

        // Sending Update request
        user.setValuesForUpdatingUser(excelReader);
        parsedJson = gson.toJson(user);
        response = sendAuhtorizedRequests.sendingPutReqWithGeneratedToken(parsedJson, tokenGenerated, id);

        //Assertions
        assertEquals(id.toString(), response.jsonPath().get("id").toString());
        assertEquals("Srdjan1", response.jsonPath().get("firstName").toString());
        assertEquals("Rados1", response.jsonPath().get("lastName").toString());
        assertEquals("{id=3, name=user}", response.jsonPath().get("role").toString());
    }

    @AfterClass
    public static void setUp() throws SQLException, ClassNotFoundException
    {
        cleanup.cleanUp("srdjan.rados@htecgroup.com");
    }
}
