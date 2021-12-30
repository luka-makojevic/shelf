package pages;

        import helpers.uiHelpers.WdWaitHelpers;
        import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.support.FindBy;
        import org.openqa.selenium.support.PageFactory;

public class DashboardPage {

    WebDriver driver;
    WdWaitHelpers wdWaitHelpers;

    public DashboardPage(WebDriver driver, WdWaitHelpers wdWaitHelpers) {
        this.driver = driver;
        this.wdWaitHelpers = wdWaitHelpers;
        PageFactory.initElements(driver, this);
    }
    @FindBy (css = ".sc-eCImPb.glXnND") WebElement profilePicture;
    @FindBy (xpath = "//*[contains(text(),'log out')]") WebElement logOutButton;
    @FindBy (xpath = "//*[contains(text(),'Create shelf')]") WebElement createShelfButton;
    @FindBy (name = "name") WebElement untitledShelf;
    public @FindBy (css = ".sc-egiyK.hTudgd") WebElement createButton;
    public @FindBy(css = ".sc-khQegj.fjGrKw") WebElement deleteShelf;
    public @FindBy (css = ".sc-egiyK.hTudgd") WebElement deleteButton;

    public void clickProfilePicture(){
        this.profilePicture.click();
    }
    public void clickLogOutButton(){
        this.logOutButton.click();
    }
    public void createShelf(String shelfName){
        this.createShelfButton.click();
        this.untitledShelf.sendKeys(shelfName);
        this.createButton.click();
    }
}
