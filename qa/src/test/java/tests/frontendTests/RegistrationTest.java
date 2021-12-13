package tests.frontendTests;

import helpers.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class RegistrationTest extends BaseTest {

    @Before
    public void pageSetUp() throws IOException {
        navigateBrowser.navigateToPageUrl("registrationPageUrl");
    }

    @Test
    public void userShouldNotRegisterWithInvalidEmail() throws IOException {
        for (int i = 1; i <= excelReader.getLastRowNumberFromColumn("RegistrationTest", "InvalidEmail"); i++) {
            regPage.loadDataFromExcel(i,4,1,1,1,1,1,2,1,3);
            regPage.registerUser();
            Assert.assertEquals("Invalid email format", regPage.getMessageText(regPage.invalidEmailMessage));
        }
    }

    @Test
    public void userShouldNotRegisterWithInvalidPassword() throws IOException {
        for (int i = 1; i <= excelReader.getLastRowNumberFromColumn("RegistrationTest", "InvalidPassword"); i++) {
            regPage.loadDataFromExcel(1, 0, i, 5, i, 5, 1, 2, 1, 3);
            regPage.registerUser();
            Assert.assertEquals("Invalid password format", regPage.getMessageText(regPage.invalidPasswordMessage));
        }
    }

    @Test
    public void userShouldNotRegisterWithInvalidConfirmPassword() throws IOException {
        regPage.loadDataFromExcel(1, 0, 1, 1, 1, 5, 1, 2, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("Passwords must match", regPage.getMessageText(regPage.invalidConfirmPassMessage));
    }

    @Test
    public void userShouldNotRegisterWhenTosCheckBoxIsNotSelected() throws IOException {
        regPage.loadDataFromExcel(1, 0, 1, 1, 1, 1, 1, 2, 1, 3);
        regPage.registerUserWithoutTosCheckBox();
        Assert.assertEquals("This field is required", regPage.getMessageText(regPage.fieldRequiredMessage));
    }

    @Test
    public void userShouldBeRedirectedToTermsAndConditionsPage() throws IOException {
        regPage.clickOnElement(regPage.termsOfService);
        navigateBrowser.switchTab();
        Assert.assertEquals(navigateBrowser.getPageUrl("termsAndConditionsPageUrl"), driver.getCurrentUrl());
    }

    @Test
    public void userShouldBeRedirectedToLoginPage() throws IOException {
        regPage.clickOnElement(regPage.signIn);
        Assert.assertEquals(navigateBrowser.getPageUrl("loginPageUrl"), driver.getCurrentUrl());
    }

    @Test
    public void userShouldBeRedirectedToHomePage() throws IOException {
        regPage.clickOnElement(regPage.shelfIcon);
        Assert.assertEquals(navigateBrowser.getPageUrl("homeUrl"), driver.getCurrentUrl());
    }

    @Test
    public void userShouldRegisterWithValidCredentials() throws IOException {
        regPage.loadDataFromExcel(1,0,1,1,1,1,1,2,1,3);
        regPage.registerUser();
        Assert.assertEquals("A verification link has been sent to your email address.", regPage.getMessageText(regPage.successfulRegMessage));
    }

    @Test
    public void userShouldNotRegisterWithAlreadyUsedEmail() throws IOException {
        regPage.loadDataFromExcel(1,0,1,1,1,1,1,2,1,3);
        regPage.registerUser();
        Assert.assertEquals("Record already exists.", regPage.getMessageText(regPage.recordExistsMessage));
    }

}
