package tests;

import helpers.BaseWdWaitHelpers;
import helpers.BaseWebDriverManager;
import helpers.ExcelReader;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ShelfLoginPage;

import java.io.IOException;

    public class BaseTest
{
    public WebDriver driver;
    public WebDriverWait wait;
    public BaseWebDriverManager baseWebDriverManager;
    public BaseWdWaitHelpers baseWdWaitHelpers;
    public ExcelReader excelReader;
    public ShelfLoginPage shelfLoginPage;

    @Before
    public void initialize() throws IOException
    {
        baseWebDriverManager = new BaseWebDriverManager();
        driver = baseWebDriverManager.initializeDriver();
        baseWdWaitHelpers = new BaseWdWaitHelpers(driver);
        driver.manage().window().maximize();
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        shelfLoginPage = new ShelfLoginPage(driver);
    }

    @After
    public void testTearDown() {
        driver.close();
        driver.quit();
    }
}
