package tests;

        import org.junit.Before;
        import org.junit.Test;

public class LoginTest extends BaseTest
{
    @Before
    public void pageSetUp(){

        driver.navigate().to("http://localhost:3000/");
    }

    @Test
    public void invalidUsername() throws InterruptedException {

        for (int i = 1; i < excelReader.getLastRowNumber(); i++) {

            String username = excelReader.getStringData("LoginTest", i, 2);
            String password = excelReader.getStringData("LoginTest", 1, 1);

            shelfLoginPage.insertUsername(username);
            shelfLoginPage.insertPassword(password);
            shelfLoginPage.clickSignInButton();


        }
    }
}
