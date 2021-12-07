package pages;

import helpers.BaseWdWaitHelpers;
import helpers.ExcelReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class RegistrationPage {

    WebDriver driver;
    BaseWdWaitHelpers wait;
    ExcelReader excelReader;
    String mail;
    String pass;
    String confirmPass;
    String first;
    String last;
    By email = By.name("email");
    By password = By.name("password");
    By confirmPassword = By.name("confirmPassword");
    By firstName = By.name("firstName");
    By lastName = By.name("lastName");
    By eyeIconPass = By.xpath("//*[@name= 'password']/following-sibling::img");
    By eyeIconConfPass = By.xpath("//*[@name= 'confirmPassword']/following-sibling::img");
    By signUpButton = By.cssSelector("button[class = 'sc-iqseJM sc-crHmcD fgBWLQ giSLaK']");
    public By signIn = By.cssSelector("a[href = '/login']");
    public By checkBox = By.name("areTermsRead");
    public By termsOfService = By.cssSelector("a[href = '/shelf-terms-and-conditions']");
    public By shelfIcon = By.cssSelector("img[src = '/assets/images/logo.png']");
    public By invalidEmailMessage = By.xpath("//*[contains(text(),'Invalid email format')]");
    public By invalidPasswordMessage = By.xpath("//*[contains(text(),'Invalid password format')]");
    public By invalidConfirmPassMessage = By.xpath("//*[contains(text(),'Passwords must match')]");
    public By fieldRequiredMessage = By.xpath("//*[contains(text(),'This field is required')]");
    public By successfulRegMessage = By.xpath("//*[contains(text(),'A verification link has been sent to your email address.')]");
    public By recordExistsMessage = By.xpath("//*[contains(text(),'Record already exists.')]");


    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void insertData(By locator, String data) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(data);
    }

    public void clickOnElement(By locator) {
        wait = new BaseWdWaitHelpers(driver);
        wait.waitToBeClickable(driver.findElement(locator)).click();
    }

    public String getMessageText (By locator) {
        wait = new BaseWdWaitHelpers(driver);
        String message = wait.waitToBeVisible(locator).getText();
        return message;
    }

    public void loadDataFromExcel
            (int mailRow, int mailCell, int passRow, int passCell, int confPassRow, int confPassCell,
             int firstNameRow, int firstNameCell, int lastNameRow, int lastNameCell) throws IOException {
        excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
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
        clickOnElement(checkBox);
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
