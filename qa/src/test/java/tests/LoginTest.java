package tests;

import org.junit.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest
{
    @Test
    public void loginPage()
    {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
    }
}
