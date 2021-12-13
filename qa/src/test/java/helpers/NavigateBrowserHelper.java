package helpers;

import org.openqa.selenium.WebDriver;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class NavigateBrowserHelper {
    WebDriver driver;
    Properties prop;
    BaseWdWaitHelpers baseWdWaitHelpers;

    public NavigateBrowserHelper(WebDriver driver, Properties prop, BaseWdWaitHelpers baseWdWaitHelpers) {
        this.driver = driver;
        this.prop = prop;
        this.baseWdWaitHelpers = baseWdWaitHelpers;
    }

    /**
     * Navigate to the desired page
     *
     * @param page
     * @author stefan.gajic
     */
    public void navigateToPageUrl(String page) throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String pageUrl = prop.getProperty(page);
        driver.navigate().to(pageUrl);
    }

    /**
     * Method for getting the URL of the desired page
     *
     * @param page
     * @return String
     * @author stefan.gajic
     */
    public String getPageUrl(String page) throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String pageUrl = prop.getProperty(page);
        baseWdWaitHelpers.waitUrlToBe(pageUrl);
        return pageUrl;
    }

    /**
     * Method for switching tabs in browser window
     *
     * @author stefan.gajic
     */
    public void switchTab() {
        String oldTab = driver.getWindowHandle();
        ArrayList<String> newTab = new ArrayList<>(driver.getWindowHandles());
        newTab.remove(oldTab);
        driver.switchTo().window(newTab.get(0));
    }
}
