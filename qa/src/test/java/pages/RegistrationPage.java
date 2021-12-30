package pages;

import helpers.uiHelpers.WdWaitHelpers;
import helpers.excelHelpers.ExcelReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage {

    WebDriver driver;
    WdWaitHelpers wdWaitHelpers;
    ExcelReader excelReader;
    String mail;
    String pass;
    String confirmPass;
    String first;
    String last;
    public By email = By.name("email");
    public By password = By.name("password");
    public By confirmPassword = By.name("confirmPassword");
    public By firstName = By.name("firstName");
    public By lastName = By.name("lastName");
    public By eyeIconPass = By.xpath("//*[@name= 'password']/following-sibling::img");
    public By eyeIconConfPass = By.xpath("//*[@name= 'confirmPassword']/following-sibling::img");
    public By signUpButton = By.xpath("//*[contains(text(),'Sign up')]");
    public By signIn = By.cssSelector("a[href = '/login']");
    public By checkBox = By.name("areTermsRead");
    public By termsOfService = By.cssSelector("a[href = '/shelf-terms-and-conditions']");
    public By shelfIcon = By.cssSelector("img[src = '/assets/images/logo.png']");
    public By passwordReqPopUp = By.xpath("//*[contains(text(),'Passwords must contain at least 8 characters and contain the following:')]");
    public By invalidEmailMessage = By.xpath("//*[contains(text(),'Invalid email format')]");
    public By email50CharactersMessage = By.xpath("//*[contains(text(),'Email can not be longer than 50 characters')]");
    public By invalidPasswordMessage = By.xpath("//*[contains(text(),'Invalid password format')]");
    public By password8charactersMessage = By.xpath("//*[contains(text(),'Password must have at least 8 characters')]");
    public By password50charactersMessage = By.xpath("//*[contains(text(),'Password can not be longer than 50 characters')]");
    public By invalidConfirmPassMessage = By.xpath("//*[contains(text(),'Passwords must match')]");
    public By firstName50charactersMessage = By.xpath("//*[contains(text(),'First name can not be longer than 50 characters')]");
    public By lastName50charactersMessage = By.xpath("//*[contains(text(),'Last name can not be longer than 50 characters')]");
    public By fieldRequiredMessage = By.xpath("//*[contains(text(),'This field is required')]");
    public By successfulRegMessage = By.xpath("//*[contains(text(),'A verification link has been sent to your email address.')]");
    public By recordExistsMessage = By.xpath("//*[contains(text(),'Record already exists.')]");

    public RegistrationPage(WebDriver driver, WdWaitHelpers wdWaitHelpers, ExcelReader excelReader) {
        this.driver = driver;
        this.wdWaitHelpers = wdWaitHelpers;
        this.excelReader = excelReader;
    }

    public void insertData(By locator, String data) {
        driver.findElement(locator).clear();
        driver.findElement(locator).click();
        driver.findElement(locator).sendKeys(data);
    }

    public void clickOnElement(By locator) {
        wdWaitHelpers.waitToBeClickable(driver.findElement(locator)).click();
    }

    public String getMessageText (By locator) {
        return wdWaitHelpers.waitToBeVisible(locator).getText();
    }

    /**
     * This method selects an element if element isn't already selected
     *
     * @author stefan.gajic
     */
    public void selectElement(By locator) {
        boolean selected = driver.findElement(locator).isSelected();
        if(selected==false) driver.findElement(locator).click();
    }

    /**
     * Method for loading valid and invalid data from Excel for filling out registration form
     *
     * @author stefan.gajic
     */
    public void getDataForRegistrationForm
    (int mailRow, int mailCell, int passRow, int passCell, int confPassRow, int confPassCell,
     int firstNameRow, int firstNameCell, int lastNameRow, int lastNameCell) {
        mail = excelReader.getStringData("RegistrationTest", mailRow, mailCell);
        pass = excelReader.getStringData("RegistrationTest", passRow, passCell);
        confirmPass = excelReader.getStringData("RegistrationTest", confPassRow, confPassCell);
        first = excelReader.getStringData("RegistrationTest", firstNameRow, firstNameCell);
        last = excelReader.getStringData("RegistrationTest", lastNameRow, lastNameCell);
    }

    public void registerUser () {
        insertData(email, mail);
        insertData(password, pass);
        insertData(confirmPassword, confirmPass);
        insertData(firstName, first);
        insertData(lastName, last);
        clickOnElement(eyeIconPass);
        clickOnElement(eyeIconConfPass);
        selectElement(checkBox);
        clickOnElement(signUpButton);
    }

    public void registerUserWithoutTosCheckBox () {
        insertData(email, mail);
        insertData(password, pass);
        insertData(confirmPassword, confirmPass);
        insertData(firstName, first);
        insertData(lastName, last);
        clickOnElement(eyeIconPass);
        clickOnElement(eyeIconConfPass);
        clickOnElement(signUpButton);
    }
}
