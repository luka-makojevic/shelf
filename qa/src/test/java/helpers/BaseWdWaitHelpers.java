package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


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
    public WebElement waitToBeVisible (By locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
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


}
