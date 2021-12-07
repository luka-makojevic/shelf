package tests;

import helpers.BaseWdWaitHelpers;
import helpers.BaseWebDriverManager;
import helpers.ExcelReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import pages.ShelfLoginPage;
import pages.RegistrationPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class BaseTest
{
    public static WebDriver driver;
    public static BaseWebDriverManager baseWebDriverManager;
    public static BaseWdWaitHelpers baseWdWaitHelpers;
    public static ExcelReader excelReader;
    public static ShelfLoginPage shelfLoginPage;
    public static RegistrationPage regPage;
    public static Properties prop;

    @BeforeClass
    public static void initialize() throws IOException
    {
        baseWebDriverManager = new BaseWebDriverManager();
        driver = baseWebDriverManager.initializeDriver();
        baseWdWaitHelpers = new BaseWdWaitHelpers(driver);
        driver.manage().window().maximize();
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        shelfLoginPage = new ShelfLoginPage(driver);
        regPage = new RegistrationPage(driver);
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
        baseWdWaitHelpers.waitUrlToBe(pageUrl);
        return pageUrl;
    }

    /**
     * @author stefan.gajic
     */
    public void switchTab() {
        String oldTab = driver.getWindowHandle();
        ArrayList<String> newTab = new ArrayList<String>(driver.getWindowHandles());
        newTab.remove(oldTab);
        driver.switchTo().window(newTab.get(0));
    }

    @AfterClass
    public static void testTearDown() {
        driver.close();
        driver.quit();
    }
}
