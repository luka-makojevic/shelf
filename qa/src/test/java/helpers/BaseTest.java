package helpers;

import helpers.BaseWdWaitHelpers;
import helpers.BaseWebDriverManager;
import helpers.ExcelReader;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest
{
    public WebDriver driver;
    public WebDriverWait wait;
    public BaseWebDriverManager baseWebDriverManager;
    public BaseWdWaitHelpers baseWdWaitHelpers;
    public ExcelReader excelReader;
    public LoginPage loginPage;
    public static Properties prop;

    @Before
    public void initialize() throws IOException
    {
        baseWebDriverManager = new BaseWebDriverManager();
        driver = baseWebDriverManager.initializeDriver();
        baseWdWaitHelpers = new BaseWdWaitHelpers(driver);
        driver.manage().window().maximize();
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        loginPage = new LoginPage(driver);
    }
    /**
     * @author stefan.gajic
     */
    public void navigateToPageUrl(String url) throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String pageUrl = prop.getProperty(url);
        driver.navigate().to(pageUrl);
    }
    /**
     * @author stefan.gajic
     */
    public String getPageUrl(String url) throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String pageUrl = prop.getProperty(url);
        return pageUrl;
    }
/*
    @After
    public void testTearDown() {
        driver.close();
        driver.quit();
    }

 */
}