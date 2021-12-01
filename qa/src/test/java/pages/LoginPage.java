package pages;

        import org.openqa.selenium.By;
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

    @FindBy(name = "email") WebElement username;
    @FindBy(name = "password") WebElement password;
    @FindBy(css = "sc-bBHxTw kIUaGY") WebElement eyeIcon;
    @FindBy(css = "form button") WebElement signInButton;
    @FindBy(css = "[href='/register']") WebElement signUpButton;

    public void loginAsUser(String username, String password){
        this.username.clear();
        this.username.sendKeys(username);
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

}
