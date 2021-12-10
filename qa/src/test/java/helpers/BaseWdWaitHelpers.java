package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class BaseWdWaitHelpers {

    WebDriver driver;
    WebDriverWait wait;

    public BaseWdWaitHelpers(WebDriver driver) {
        this.driver = driver;
    }


    /**
     * Wait for element to be clickable
     *
     * @param element
     * @return WebElement after wait
     * @author stefan.gajic
     */
    public WebElement waitToBeClickable (WebElement element) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be visible
     *
     * @param locator
     * @return WebElement after wait
     * @author stefan.gajic
     */
    public WebElement waitToBeVisible (WebElement locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.visibilityOfElementLocated((By) locator));
    }

    /**
     * Wait for element to be invisible
     *
     * @param locator
     * @return Boolean after wait
     * @author stefan.gajic
     */
    public Boolean waitToBeInvisible (By locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be selected
     *
     * @param element
     * @return Boolean after wait
     * @author stefan.gajic
     */
    public Boolean waitToBeSelected (WebElement element) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.elementToBeSelected(element));
    }

    /**
     * Wait for element to be present
     *
     * @param locator
     * @return Webelement after wait
     * @author stefan.gajic
     */
    public WebElement waitForElementPresence (By locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for URL of the current page to be a specific url
     *
     * @param url
     * @return Boolean after wait
     * @author lidija.veljkovic
     */

    public Boolean  waitUrlToBe(String url){
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.urlToBe(url));
    }

    /**
     *
     * @param locator
     * @return List<WebElement> after wait
     * @author shelfsrdjan
     */
    public List<WebElement> waitForAllElementsToBePresence (By locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

}
