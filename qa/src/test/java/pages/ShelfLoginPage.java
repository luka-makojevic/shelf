package pages;

        import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.support.FindBy;
        import org.openqa.selenium.support.PageFactory;

public class ShelfLoginPage {

    WebDriver driver;
    WebElement username;
    WebElement password;
    WebElement eyeIcon;
    WebElement signInButton;

    public ShelfLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement getUsername() {

        return driver.findElement(By.name("email"));
    }

    public void insertUsername(String username) {
        this.getUsername().clear();
        this.getUsername().sendKeys(username);
    }

    public WebElement getPassword() {

        return driver.findElement(By.name("password"));
    }

    public void insertPassword(String password) {
        this.getPassword().clear();
        this.getPassword().sendKeys(password);
    }
    public WebElement getEyeIcon(){

        return driver.findElement(By.cssSelector(".sc-bBHxTw.kIUaGY"));
    }
    public void clickEyeIcon(){
        this.getEyeIcon().click();
    }

    public WebElement getSignInButton(){

        return driver.findElement(By.cssSelector("form button"));
    }
    public void clickSignInButton() {

        this.getSignInButton().click();
    }

    public WebElement getSignUpButton(){

        return driver.findElement(By.cssSelector("[href='/register']"));
    }
    public void clickSignUpButton() {

        this.getSignUpButton().click();
    }

}
