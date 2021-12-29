package tests.frontendTests;

import helpers.uiHelpers.BaseTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationTest extends BaseTest {

    @Before
    public void pageSetUp() throws IOException {
        navigateBrowser.navigateToPageUrl("registrationPageUrl");
    }

    @Test
    public void userShouldNotRegisterWithInvalidEmailForm() {
        for (int i = 1; i <= excelReader.getLastRowNumberFromColumn("RegistrationTest", "InvalidEmail")-1; i++) {
            regPage.getDataForRegistrationForm(i,4,1,1,1,1,1,2,1,3);
            regPage.registerUser();
            Assert.assertEquals("Invalid email format", regPage.getMessageText(regPage.invalidEmailMessage));
        }
    }

    @Test
    public void userShouldNotRegisterWhenEmailHasMoreThan50Characters() {
        regPage.getDataForRegistrationForm(5,4,1,1,1,1,1,2,1,3);
        regPage.registerUser();
        Assert.assertEquals("Email can not be longer than 50 characters", regPage.getMessageText(regPage.email50CharactersMessage));
    }

    @Test
    public void userShouldNotRegisterWhenEmailInputBoxIsBlank() {
        regPage.getDataForRegistrationForm(6,4,1,1,1,1,1,2,1,3);
        regPage.registerUser();
        Assert.assertEquals("This field is required", regPage.getMessageText(regPage.fieldRequiredMessage));
    }

    @Test
    public void userShouldNotRegisterWithInvalidPasswordForm() {
        for (int i = 1; i <= excelReader.getLastRowNumberFromColumn("RegistrationTest", "InvalidPassword")-2; i++) {
            regPage.getDataForRegistrationForm(1, 0, i, 5, i, 5, 1, 2, 1, 3);
            regPage.registerUser();
            Assert.assertEquals("Invalid password format", regPage.getMessageText(regPage.invalidPasswordMessage));
        }
    }

    @Test
    public void userShouldNotRegisterWhenPasswordHasMoreThan50Characters() {
        regPage.getDataForRegistrationForm(1, 0, 6, 5, 6, 5, 1, 2, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("Password can not be longer than 50 characters", regPage.getMessageText(regPage.password50charactersMessage));
    }

    @Test
    public void userShouldNotRegisterWhenPasswordHasLessThan8Characters() {
        regPage.getDataForRegistrationForm(1, 0, 5, 5, 5, 5, 1, 2, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("Password must have at least 8 characters", regPage.getMessageText(regPage.password8charactersMessage));
    }

    @Test
    public void userShouldNotRegisterWhenPasswordInputBoxIsBlank() {
        regPage.getDataForRegistrationForm(1, 0, 7, 5, 1, 1, 1, 2, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("This field is required", regPage.getMessageText(regPage.fieldRequiredMessage));
        Assert.assertEquals("Passwords must match", regPage.getMessageText(regPage.invalidConfirmPassMessage));
    }

    @Test
    public void userShouldNotRegisterWithInvalidConfirmPasswordForm() {
        for (int i = 1; i <= excelReader.getLastRowNumberFromColumn("RegistrationTest", "InvalidPassword"); i++) {
            regPage.getDataForRegistrationForm(1, 0, 1, 1, i, 5, 1, 2, 1, 3);
            regPage.registerUser();
            Assert.assertEquals("Passwords must match", regPage.getMessageText(regPage.invalidConfirmPassMessage));
        }
    }

    @Test
    public void userShouldNotRegisterWhenConfirmPasswordInputBoxIsBlank() {
        regPage.getDataForRegistrationForm(1, 0, 1, 1, 7, 5, 1, 2, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("This field is required", regPage.getMessageText(regPage.fieldRequiredMessage));
    }

    @Test
    public void userShouldNotRegisterWhenFirstNameHasMoreThan50Characters() {
        regPage.getDataForRegistrationForm(1, 0, 1, 1, 1, 1, 1, 6, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("First name can not be longer than 50 characters", regPage.getMessageText(regPage.firstName50charactersMessage));
    }

    @Test
    public void userShouldNotRegisterWhenFirstNameInputBoxIsBlank() {
        regPage.getDataForRegistrationForm(1, 0, 1, 1, 1, 1, 2, 6, 1, 3);
        regPage.registerUser();
        Assert.assertEquals("This field is required", regPage.getMessageText(regPage.fieldRequiredMessage));
    }

    @Test
    public void userShouldNotRegisterWhenLastNameHasMoreThan50Characters() {
        regPage.getDataForRegistrationForm(1, 0, 1, 1, 1, 1, 1, 2, 1, 7);
        regPage.registerUser();
        Assert.assertEquals("Last name can not be longer than 50 characters", regPage.getMessageText(regPage.lastName50charactersMessage));
    }

    @Test
    public void userShouldNotRegisterWhenLastNameInputBoxIsBlank() {
        regPage.getDataForRegistrationForm(1, 0, 1, 1, 1, 1, 1, 2, 2, 7);
        regPage.registerUser();
        Assert.assertEquals("This field is required", regPage.getMessageText(regPage.fieldRequiredMessage));
    }

    @Test
    public void passwordRequirementsPopUpShouldAppear() {
        regPage.clickOnElement(regPage.password);
        Assert.assertTrue(wdWaitHelpers.waitForElementPresence(regPage.passwordReqPopUp).isDisplayed());
    }

    @Test
    public void userShouldNotRegisterWhenTosCheckBoxIsNotSelected() {
        regPage.getDataForRegistrationForm(1, 0, 1, 1, 1, 1, 1, 2, 1, 3);
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
    public void userShouldRegisterWithValidCredentials() {
        regPage.getDataForRegistrationForm(1,0,1,1,1,1,1,2,1,3);
        regPage.registerUser();
        Assert.assertEquals("A verification link has been sent to your email address.", regPage.getMessageText(regPage.successfulRegMessage));
    }

    @Test
    public void userShouldNotRegisterWithAlreadyUsedEmail() {
        regPage.getDataForRegistrationForm(1,0,1,1,1,1,1,2,1,3);
        regPage.registerUser();
        Assert.assertEquals("Record already exists.", regPage.getMessageText(regPage.recordExistsMessage));
    }

    @AfterClass
    public static void deleteUser() throws SQLException, ClassNotFoundException
    {
         cleanup.cleanUp("stefanzgajic@gmail.com");
    }
}
