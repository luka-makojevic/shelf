package tests;

import helpers.BaseWebDriverManager;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public class BaseTest extends BaseWebDriverManager
{
    @Before
    public void initialize() throws IOException
    {
        driver = initializeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void testTearDown() {
        driver.close();
        driver.quit();
    }
}
