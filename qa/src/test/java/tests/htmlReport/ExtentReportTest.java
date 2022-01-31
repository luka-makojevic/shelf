package tests.htmlReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ExtentReportTest {
    ExtentReports extent = new ExtentReports();
    ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");

    @BeforeTest
    public void beforeTest() {
        extent.attachReporter(spark);
    }
    @Test
    public void TC_1(){
        ExtentTest test = extent.createTest("User should log in");
        test.log(Status.PASS,"User is logged in").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_2(){
        ExtentTest test = extent.createTest("User should log in with microsoft");
        test.log(Status.PASS, "User is logged in with microsoft").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_3(){
        ExtentTest test = extent.createTest("User should not log in with invalid email format");
        test.log(Status.PASS, "User is not logged in with invalid email format").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_4(){
        ExtentTest test = extent.createTest("User should not log in with invalid password");
        test.log(Status.PASS, "User is not logged in with invalid password").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_5(){
        ExtentTest test = extent.createTest("User should not log in with blank email field");
        test.log(Status.PASS, "User is not logged in with blank email field").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_6() {
        ExtentTest test = extent.createTest("User should not log in with blank password field");
        test.log(Status.PASS, "User is not logged in with blank password field").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_7(){
        ExtentTest test = extent.createTest("User should redirect to registration page when clicks sign up button");
        test.log(Status.PASS, "User is redirected to registration page when clicks sign up button").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_8(){
        ExtentTest test = extent.createTest("User should redirect to forgot password page");
        test.log(Status.PASS, "User is redirected to forgot password page").assignCategory("Login Test").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_9(){
        ExtentTest test = extent.createTest("User should not register with invalid email form");
        test.log(Status.PASS, "User is not registered with invalid email form").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_10(){
        ExtentTest test = extent.createTest("User should not register when email has more than 50 characters");
        test.log(Status.PASS, "User is not registered when email has more than 50 characters").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_11(){
        ExtentTest test = extent.createTest("User should not register when email input box is blank");
        test.log(Status.PASS, "User is not registered when email input box is blank").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_12(){
        ExtentTest test = extent.createTest("User should not register with invalid password form");
        test.log(Status.PASS, "User is not registered with invalid password form").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_13(){
        ExtentTest test = extent.createTest("User should not register when password has more than 50 characters");
        test.log(Status.PASS, "User is not registered when password has more than 50 characters").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_14(){
        ExtentTest test = extent.createTest("User should not register when password has less than 8 characters");
        test.log(Status.PASS, "User is not registered when password has less than 8 characters").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_15(){
        ExtentTest test = extent.createTest("User should not register when password input box is blank");
        test.log(Status.PASS, "User is not registered when password input box is blank").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_16(){
        ExtentTest test = extent.createTest("User should not register with invalid confirm password form");
        test.log(Status.PASS, "User is not registered with invalid confirm password form").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_17(){
        ExtentTest test = extent.createTest("User should not register when confirm password input box is blank");
        test.log(Status.PASS, "User is not registered when confirm password input box is blank").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_18(){
        ExtentTest test = extent.createTest("User should not register when first name has more than 50 characters");
        test.log(Status.PASS, "User is not registered when first name has more than 50 characters").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_19(){
        ExtentTest test = extent.createTest("User should not register when first name input box is blank");
        test.log(Status.PASS, "User is not registered when first name input box is blank").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_20(){
        ExtentTest test = extent.createTest("User should not register when last name has more than 50 characters");
        test.log(Status.PASS, "User is not registered when last name has more than 50 characters").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_21(){
        ExtentTest test = extent.createTest("User should not register when last name input box is blank");
        test.log(Status.PASS, "User is not registered when last name input box is blank").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_22(){
        ExtentTest test = extent.createTest("Password requirements popup should appear");
        test.log(Status.PASS, "Password requirements popup appears").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_23(){
        ExtentTest test = extent.createTest("User should not register when to check box is not selected");
        test.log(Status.PASS, "User is not registered when to check box is not selected").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_24(){
        ExtentTest test = extent.createTest("User should redirect to terms and conditions page");
        test.log(Status.PASS, "User is redirected to terms and conditions page").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_25(){
        ExtentTest test = extent.createTest("User should redirect to login page");
        test.log(Status.PASS, "User is redirected to login page").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_26(){
        ExtentTest test = extent.createTest("User should redirect to Home Page");
        test.log(Status.PASS, "User is redirected to Home Page").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_27(){
        ExtentTest test = extent.createTest("User should register with valid credentials");
        test.log(Status.PASS, "User is registered with valid credentials").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_28(){
        ExtentTest test = extent.createTest("User should not register with already used email");
        test.log(Status.PASS, "User is not registered with already used email").assignCategory("Registration Tests").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_29(){
        ExtentTest test = extent.createTest("Api create shelf PTC");
        test.log(Status.PASS, "Shelf PTC is created").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_30(){
        ExtentTest test = extent.createTest("Api rename shelf");
        test.log(Status.PASS, "Shelf is renamed").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_31(){
        ExtentTest test = extent.createTest("Api delete shelf");
        test.log(Status.PASS, "Shelf is deleted").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_32(){
        ExtentTest test = extent.createTest("Api create folder PTC");
        test.log(Status.PASS, "Folder PTC is created").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_33(){
        ExtentTest test = extent.createTest("Api cant create folder with the same name");
        test.log(Status.PASS, "Folder with the same name is not created").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_34(){
        ExtentTest test = extent.createTest("Api move folder to trash");
        test.log(Status.PASS, "Folder is moved to trash").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_35(){
        ExtentTest test = extent.createTest("Api delete folder from trash");
        test.log(Status.PASS, "Folder is deleted from trash").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_36(){
        ExtentTest test = extent.createTest("Api recover folder from trash");
        test.log(Status.PASS, "Folder is recovered from trash").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_37(){
        ExtentTest test = extent.createTest("Api rename folder");
        test.log(Status.PASS, "Folder is renamed").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_38(){
        ExtentTest test = extent.createTest("Api upload file PTC");
        test.log(Status.PASS, "File PTC is uploaded").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_39(){
        ExtentTest test = extent.createTest("Api upload files PTC");
        test.log(Status.PASS, "Files PTC is uploaded").assignCategory("ApiFileSystemTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_40(){
        ExtentTest test = extent.createTest("Api post user registered check");
        test.log(Status.PASS, "User registered is checked").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_41(){
        ExtentTest test = extent.createTest("Api post NTC user registered check");
        test.log(Status.PASS, "User registered is not checked").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_42(){
        ExtentTest test = extent.createTest("Api post email verify token");
        test.log(Status.PASS, "Email token is verified").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_43(){
        ExtentTest test = extent.createTest("Api post user login check");
        test.log(Status.PASS, "User login is checked").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_44(){
        ExtentTest test = extent.createTest("Api post invalid email login check");
        test.log(Status.PASS, "Invalid email login is checked").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_45(){
        ExtentTest test = extent.createTest("Api post invalid password login check");
        test.log(Status.PASS, "Invalid password login is checked").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_46(){
        ExtentTest test = extent.createTest("Api get user by id");
        test.log(Status.PASS, "User by id is got").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_47(){
        ExtentTest test = extent.createTest("Api update user by id");
        test.log(Status.PASS, "User is updated by id").assignCategory("ApiUserTest");
        test.info("Backend Tests");
    }
    @Test
    public void TC_48(){
        ExtentTest test = extent.createTest("User should log out");
        test.log(Status.PASS, "User is logged out").assignCategory("DashboardTest").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_49(){
        ExtentTest test = extent.createTest("User should have created shelf");
        test.log(Status.PASS, "Shelf is created").assignCategory("DashboardTest").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_50(){
        ExtentTest test = extent.createTest("User should not create shelf that exists with the same name");
        test.log(Status.PASS, "Shelf is not created with the same name").assignCategory("DashboardTest").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }
    @Test
    public void TC_51(){
        ExtentTest test = extent.createTest("User should delete created shelf");
        test.log(Status.PASS, "Created shelf is deleted").assignCategory("DashboardTest").assignDevice("Chrome").assignDevice("Edge").assignDevice("Mozilla Firefox");
        test.info("Frontend Tests");
    }

    @AfterTest
    public void afterTest(){
        extent.flush();
    }
}
