package tests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class LoginTest extends BaseTest {

    @Before
    public void pageSetUp() throws IOException {

        navigateToPageUrl("loginPageUrl");
    }
    @Test
    public void userShouldBeLoggedIn() throws IOException {

        String email = excelReader.getStringData("LoginTest", 1,0);
        String password = excelReader.getStringData("LoginTest", 1, 1);
        loginPage.loginAsUser(email, password);
        baseWdWaitHelpers.waitUrlToBe(getPageUrl("dashboardPageUrl"));
        Assert.assertEquals(driver.getCurrentUrl(), getPageUrl("dashboardPageUrl"));
    }
    @Test
    public void userShouldBeLoggedInWithMicrosoft() throws IOException {
        String email = excelReader.getStringData("LoginTest", 1,0);
        String password = excelReader.getStringData("LoginTest", 1, 1);
        loginViaMicrosft.loginAsMicrosoftUser(email, password);
        baseWdWaitHelpers.waitUrlToBe(getPageUrl("dashboardPageUrl"));
        Assert.assertEquals(driver.getCurrentUrl(), getPageUrl("dashboardPageUrl"));
    }

    @Test
    public void userShouldNotLoginWithInvalidEmailFormat(){
      
        for (int i = 1; i < excelReader.getLastRowNumberFromSheet("LoginTest") -2; i++) {

            String email = excelReader.getStringData("LoginTest", i, 2);
            String password = excelReader.getStringData("LoginTest", 1, 1);
            loginPage.loginAsUser(email, password);
            String expectedMessage = "Authentication credentials not valid.";
            String message = loginPage.invalidEmailMessage.getText();
            Assert.assertTrue("Invalid email format", message.contains(expectedMessage));
        }
    }
    @Test
    public void userShouldNotLoginWithInvalidPassword() {

        for (int i = 1; i < excelReader.getLastRowNumberFromSheet("LoginTest") + 1; i++) {
          
            String username = excelReader.getStringData("LoginTest", 1, 0);
            String password = excelReader.getStringData("LoginTest", i, 3);
            loginPage.loginAsUser(username, password);
        }
        String expectedMessage = "Authentication credentials not valid.";
        String message = loginPage.invalidPasswordMessage.getText();
        Assert.assertTrue("Authentication credentials not valid.", message.contains(expectedMessage));
    }
    @Test
    public void userShouldNotLoginWithBlankEmailField(){

        String email = excelReader.getStringData("LoginTest", 3, 0);
        String password = excelReader.getStringData("LoginTest", 1, 1);
        loginPage.loginAsUser(email, password);
        String expectedMessage = "This field is required";
        String message = loginPage.blankEmailFieldMessage.getText();
        Assert.assertTrue("This field is required", message.contains(expectedMessage));
    }
    @Test
    public void userShouldNotLoginWithBlankPasswordField(){

        String email = excelReader.getStringData("LoginTest", 1, 0);
        String password = excelReader.getStringData("LoginTest", 2, 1);
        loginPage.loginAsUser(email, password);
        String expectedMessage = "This field is required";
        String message = loginPage.blankPasswordFieldMessage.getText();
        Assert.assertTrue("This field is required", message.contains(expectedMessage));
    }
    @Test
    public void userShouldRedirectedToRegistrationPageWhenClicksSignUpButton() throws IOException {
        loginPage.clickSignUpButton();
        baseWdWaitHelpers.waitUrlToBe(getPageUrl("registrationPageUrl"));
        Assert.assertEquals(driver.getCurrentUrl(), getPageUrl("registrationPageUrl"));
    }
    @Test
    public void userShouldRedirectToForgotPasswordPage() throws IOException {
        loginPage.clickResetPassword();
        baseWdWaitHelpers.waitUrlToBe(getPageUrl("forgotPassword"));
        Assert.assertEquals(driver.getCurrentUrl(), getPageUrl("forgotPassword"));
    }
    @After
    public void deleteCookies(){
        driver.manage().deleteAllCookies();
    }
}
