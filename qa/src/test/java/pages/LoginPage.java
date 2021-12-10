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
    @FindBy(css = "sc-bBHxTw kIUaGY") WebElement eyeIcon;
    @FindBy(css = "form button") WebElement signInButton;
    @FindBy(css = "[href='/register']") WebElement signUpButton;
    @FindBy(linkText = "Reset password") WebElement resetPassword;
    public @FindBy(xpath = "//*[contains(text(),'Invalid email format')]") WebElement invalidEmailMessage;
    public @FindBy(xpath = "//*[contains(text(),'Password must have at least 8 characters')]") WebElement invalidPasswordMessage;
    public @FindBy(xpath = "//*[@id=\"root\"]/div[2]/div[1]/div/form/div[2]/div[1]/div") WebElement blankEmailFieldMessage;
    public @FindBy(xpath = "//*[@id=\"root\"]/div[2]/div[1]/div/form/div[2]/div[2]/div") WebElement blankPasswordFieldMessage;

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
    public void clickEyeIcon(){

        this.eyeIcon.click();
    }
    public void clickSignUpButton() {

        this.signUpButton.click();
    }
    public void clickResetPassword(){
        this.resetPassword.click();
    }

}
