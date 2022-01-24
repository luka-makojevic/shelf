package helpers.uiHelpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WebDriverManager
{

    public WebDriver driver;
    public Properties prop;


    public WebDriver initializeDriver(Boolean enableHeadless) throws IOException {

        // Create global property file
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String browserName = prop.getProperty("browser");
        System.out.println(browserName);

        if (browserName.equals("chrome")) {
            io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setHeadless(enableHeadless);
            driver = new ChromeDriver(chromeOptions);
        } else if (browserName.equals("firefox")) {
            io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setHeadless(enableHeadless);
            driver = new FirefoxDriver(firefoxOptions);
        }
         else if (browserName.equals("edge")) {
        io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup();
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setHeadless(enableHeadless);
        driver = new EdgeDriver(edgeOptions);
        }
        return driver;
    }
}
