package tests.frontendTests;

import helpers.uiHelpers.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import java.io.IOException;

public class DashboardTest extends BaseTest {

    @Before
    public void loginBeforeTest() throws IOException {
        navigateBrowser.navigateToPageUrl("loginPageUrl");
        loginPage.successfulLoggIn();
    }

    @Test
    public void testAUserShouldBeLoggedOut() throws IOException {

        dashboardPage.clickProfilePicture();
        dashboardPage.clickLogOutButton();
        wdWaitHelpers.waitUrlToBe(navigateBrowser.getPageUrl("homeUrl"));
        Assert.assertEquals(driver.getCurrentUrl(), navigateBrowser.getPageUrl("homeUrl"));
    }
    @Test
    public void testBUserShouldHaveCreatedShelf() {
        dashboardPage.createShelf("New shelf");
        Assert.assertTrue(wdWaitHelpers.waitForElementPresence(By.xpath("//*[contains(text(),'New shelf')]")).isDisplayed());
    }
    @Test
    public void testCUserShouldNotCreateShelfThatExistsWithTheSameName() {
        dashboardPage.createShelf("New shelf");
        Assert.assertEquals("Shelf with the same name already exists.",
                wdWaitHelpers.waitToBeVisible(By.xpath("//*[contains(text(),'Shelf with the same name already exists.')]")).getText());
    }
    @Test
    public void testDUserShouldDeleteCreatedShelf(){
        dashboardPage.deleteShelf.click();
        wdWaitHelpers.waitToBeClickable(dashboardPage.deleteButton).click();
       Assert.assertTrue(wdWaitHelpers.waitToBeInvisible(By.xpath("//*[contains(text(),'New shelf')]")).booleanValue());
    }
}
