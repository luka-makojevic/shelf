package pages;

import helpers.BaseWebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BaseWebDriverManager
{
    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void navigateToShelfPage() { driver.get("https://www.google.com/"); }

    public void login()
    {
        navigateToShelfPage();
    }

}
