package helpers.uiHelpers;

import helpers.dbHelpers.Cleanup;
import helpers.excelHelpers.ExcelReader;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import pages.DashboardPage;
import pages.LoginPage;
import pages.LoginViaMicrosoft;
import pages.RegistrationPage;

import java.io.IOException;
import java.util.Properties;


public class BaseTest
{

    public WebDriver driver;
    public Properties prop;
    public WebDriverManager webDriverManager;
    public WdWaitHelpers wdWaitHelpers;
    public NavigateBrowserHelper navigateBrowser;
    public ExcelReader excelReader;
    public LoginPage loginPage;
    public LoginViaMicrosoft loginViaMicrosoft;
    public RegistrationPage regPage;
    public DashboardPage dashboardPage;
    public static Cleanup cleanup;

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
        loginViaMicrosoft = new LoginViaMicrosoft(driver, wdWaitHelpers);
        regPage = new RegistrationPage(driver, wdWaitHelpers, excelReader);
        cleanup = new Cleanup();
        dashboardPage = new DashboardPage(driver, wdWaitHelpers);
    }

    @After
    public void testTearDown() {
        driver.close();
        driver.quit();
    }
}