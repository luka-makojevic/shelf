package pages;

import helpers.excelHelpers.ExcelReader;
import helpers.uiHelpers.NavigateBrowserHelper;
import helpers.uiHelpers.WdWaitHelpers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class LoginPage {

    WebDriver driver;
    WdWaitHelpers wdWaitHelpers;
    ExcelReader excelReader;
    NavigateBrowserHelper navigateBrowser;

    public LoginPage(WebDriver driver, WdWaitHelpers wdWaitHelpers, ExcelReader excelReader,
                     NavigateBrowserHelper navigateBrowser) {
        this.driver = driver;
        this.wdWaitHelpers = wdWaitHelpers;
        this.excelReader = excelReader;
        this.navigateBrowser = navigateBrowser;
        PageFactory.initElements(driver, this);
    }
    @FindBy(name = "email") WebElement email;
    @FindBy(name = "password") WebElement password;
    @FindBy(css = "form button") WebElement signInButton;
    @FindBy(css = "[href='/register']") WebElement signUpButton;
    @FindBy(linkText = "Reset password") WebElement resetPassword;
    public @FindBy(xpath = "//*[contains(text(),'Authentication credentials not valid')]") WebElement errorMessage;
    public @FindBy(xpath = "//*[@name='email']/following-sibling::p[text()='This field is required']") WebElement blankEmailFieldMessage;
    public @FindBy(xpath = "//*[@name='password']/following-sibling::p[text()='This field is required']") WebElement blankPasswordFieldMessage;

    /**
     * @author lidija.veljkovic
     */
    public void loginAsUser(String username, String password){
        this.email.clear();
        this.email.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);
        this.signInButton.click();
    }
    /**
     * Method for loading valid data from Excel for filling out loggIn form
     *
     * @author lidija.veljkovic
     */
    public void successfulLoggIn() throws IOException {
        String email = excelReader.getStringData("LoginTest", 2,0);
        String password = excelReader.getStringData("LoginTest", 2, 1);
        loginAsUser(email, password);
        wdWaitHelpers.waitUrlToBe(navigateBrowser.getPageUrl("dashboardPageUrl"));
    }
    public void clickSignUpButton() {
        this.signUpButton.click();
    }
    public void clickResetPassword(){
        this.resetPassword.click();
    }

}