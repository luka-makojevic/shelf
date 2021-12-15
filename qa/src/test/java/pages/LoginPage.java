package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
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
    public void clickSignUpButton() {

        this.signUpButton.click();
    }
    public void clickResetPassword(){
        this.resetPassword.click();
    }

}