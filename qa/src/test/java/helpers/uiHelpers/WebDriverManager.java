package helpers.uiHelpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WebDriverManager
{

    public WebDriver driver;
    public Properties prop;

    public WebDriver initializeDriver() throws IOException {

        // Create global property file
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String browserName = prop.getProperty("browser");
        System.out.println(browserName);

        if (browserName.equals("chrome")) {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browserName.equals("firefox")) {
            io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
         else if (browserName.equals("ie")) {
        io.github.bonigarcia.wdm.WebDriverManager.iedriver().setup();
        driver = new InternetExplorerDriver();
}
        return driver;
    }
}
