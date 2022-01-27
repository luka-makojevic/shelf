package pages;

import helpers.uiHelpers.WdWaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.Iterator;
import java.util.Set;

public class LoginViaMicrosoft
{
    WebDriver driver;
    WdWaitHelpers wdWaitHelpers;

    public LoginViaMicrosoft(WebDriver driver, WdWaitHelpers wdWaitHelpers)
    {
        this.driver = driver;
        this.wdWaitHelpers = wdWaitHelpers;
        PageFactory.initElements(driver, this);
    }

    @FindBy (xpath = "//*[contains(text(),'Sign in with Microsoft')]") WebElement signInWithMic;
    @FindBy(id = "i0116") WebElement email;
    @FindBy(xpath = "//input[@value='Next']") WebElement nextButton;
    @FindBy(xpath = "//input[@value='Sign in']") WebElement signInButton;
    @FindBy(id = "i0118") WebElement pass;
    @FindBy(id = "idBtn_Back") WebElement noButton;

    public void loginAsMicrosoftUser(String username,String password) {

        signInWithMic.click();

        String oldTab = driver.getWindowHandle();

        // Handling new open window(tab)
        Set<String> handles = driver.getWindowHandles();
        Iterator it = handles.iterator();
        String newHandle = null;
        String handle= null;
        while (it.hasNext()) {
                handle = it.next().toString();
                if (oldTab.contentEquals(handle)) {
                } else {
                    newHandle = handle;
                }
        }

        // Switch to the new tab
        driver.switchTo().window(newHandle);

        // Actions on new tab
        wdWaitHelpers.waitForAllElementsToBePresence(By.className("outer"));
        email.sendKeys(username);
        wdWaitHelpers.waitToBeClickable(nextButton);
        nextButton.click();
        wdWaitHelpers.waitForAllElementsToBePresence(By.className("outer"));
        pass.sendKeys(password);
        wdWaitHelpers.waitToBeClickable(signInButton);
        signInButton.click();
        wdWaitHelpers.waitToBeClickable(noButton);
        noButton.click();

        // Switch to the previous tab
        driver.switchTo().window(oldTab);
    }
}
