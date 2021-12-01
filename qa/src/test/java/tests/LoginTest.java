package tests;

        import org.junit.Before;
        import org.junit.Test;

public class LoginTest extends BaseTest {
    @Before
    public void pageSetUp() {

        driver.navigate().to("http://localhost:3000/login");
    }

    @Test
    public void invalidUsername() throws InterruptedException {

        for (int i = 1; i < excelReader.getLastRowNumberFromSheet("LoginTest"); i++) {

            String username = excelReader.getStringData("LoginTest", i, 2);
            String password = excelReader.getStringData("LoginTest", 1, 1);

            loginPage.loginAsUser(username, password);

        }
    }

    @Test
    public void invalidPassword() throws InterruptedException {

        for (int i = 1; i < excelReader.getLastRowNumberFromSheet("LoginTest"); i++) {

            String username = excelReader.getStringData("LoginTest", 1, 0);
            String password = excelReader.getStringData("LoginTest", i, 3);

            loginPage.loginAsUser(username, password);

        }
    }
}
