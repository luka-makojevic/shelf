package helpers.uiHelpers;

import org.openqa.selenium.WebDriver;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class NavigateBrowserHelper {
    WebDriver driver;
    Properties prop;
    WdWaitHelpers wdWaitHelpers;

    public NavigateBrowserHelper(WebDriver driver, Properties prop, WdWaitHelpers wdWaitHelpers) {
        this.driver = driver;
        this.prop = prop;
        this.wdWaitHelpers = wdWaitHelpers;
    }

    /**
     //     * Method for reading data from property file
     //     *
     //     * @param page
     //     * @return String
     //     * @author stefan.gajic
     //     */
    public String getProperty (String page) throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream (".\\datafiles\\data.properties");
        prop.load(fis);
        String property = prop.getProperty(page);
        return property;
    }

    /**
     //     * Navigate to the desired page
     //     *
     //     * @param page
     //     * @author stefan.gajic
     //     */
    public void navigateToPageUrl(String page) throws IOException {
        driver.navigate().to(getProperty(page));
    }

    /**
     //     * Method for getting the URL of the desired page
     //     *
     //     * @param page
     //     * @return String
     //     * @author stefan.gajic
     //     */
    public String getPageUrl(String page) throws IOException {
        wdWaitHelpers.waitUrlToBe(getProperty(page));
        return getProperty(page);
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
