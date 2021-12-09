package tests;

import helpers.BaseWdWaitHelpers;
import helpers.BaseWebDriverManager;
import helpers.ExcelReader;
import helpers.NavigateBrowserHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ShelfLoginPage;
import pages.RegistrationPage;

import java.io.IOException;
import java.util.Properties;


public class BaseTest
{
    public static WebDriver driver;
    public static Properties prop;
    public static WebDriverWait wait;
    public static BaseWebDriverManager baseWebDriverManager;
    public static BaseWdWaitHelpers baseWdWaitHelpers;
    public static NavigateBrowserHelper navigateBrowser;
    public static ExcelReader excelReader;
    public static ShelfLoginPage shelfLoginPage;
    public static RegistrationPage regPage;

    @BeforeClass
    public static void initialize() throws IOException
    {
        baseWebDriverManager = new BaseWebDriverManager();
        driver = baseWebDriverManager.initializeDriver();
        baseWdWaitHelpers = new BaseWdWaitHelpers(driver);
        navigateBrowser = new NavigateBrowserHelper(driver, prop, baseWdWaitHelpers);
        driver.manage().window().maximize();
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        shelfLoginPage = new ShelfLoginPage(driver);
        regPage = new RegistrationPage(driver, baseWdWaitHelpers);
    }

    @AfterClass
    public static void testTearDown() {
        driver.close();
        driver.quit();
    }
}
