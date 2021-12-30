package helpers.uiHelpers;

import helpers.excelHelpers.ExcelReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import pages.DashboardPage;
import pages.LoginPage;
import pages.LoginViaMicrosoft;
import pages.RegistrationPage;
import tests.frontendTests.LoginTest;

import java.io.IOException;
import java.util.Properties;


public class BaseTest
{
    public static WebDriver driver;
    public static Properties prop;
    public static WebDriverManager webDriverManager;
    public static WdWaitHelpers wdWaitHelpers;
    public static NavigateBrowserHelper navigateBrowser;
    public static ExcelReader excelReader;
    public static LoginPage loginPage;
    public static LoginTest loginTest;
    public static LoginViaMicrosoft loginViaMicrosoft;
    public static RegistrationPage regPage;
    public static DashboardPage dashboardPage;

    @Before
    public void initialize() throws IOException
    {
        webDriverManager = new WebDriverManager();
        driver = webDriverManager.initializeDriver();
        wdWaitHelpers = new WdWaitHelpers(driver);
        navigateBrowser = new NavigateBrowserHelper(driver, prop, wdWaitHelpers);
        driver.manage().window().maximize();
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        loginPage = new LoginPage(driver, wdWaitHelpers, excelReader, navigateBrowser);
        loginTest = new LoginTest();
        loginViaMicrosoft = new LoginViaMicrosoft(driver, wdWaitHelpers);
        regPage = new RegistrationPage(driver, wdWaitHelpers);
        dashboardPage = new DashboardPage(driver, wdWaitHelpers);
    }

    @After
    public void testTearDown() {
        driver.close();
        driver.quit();
    }
}