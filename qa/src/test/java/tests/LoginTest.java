package tests;

import org.junit.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest
{
    @Test
    public void LoginPage() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        // Visualisation accpetance
        Thread.sleep(3000);
    }
}
