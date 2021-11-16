package tests;

import au.com.bytecode.opencsv.CSVReader;
import org.junit.Test;
import org.openqa.selenium.By;
import pages.RegistrationPage;

import java.io.FileReader;
import java.io.IOException;

public class RegistrationTest extends BaseTest
{
    @Test
    public void RegistrationPage() throws InterruptedException, IOException {
        RegistrationPage regPage = new RegistrationPage(driver);
        regPage.login();

        CSVReader reader = new CSVReader(new FileReader(".\\datafiles\\data.csv"));

        System.out.println("url:"+driver.getCurrentUrl());

        String csvCell[];

        while ((csvCell = reader.readNext()) != null) {
            String Fname = csvCell[0];
            String Lname = csvCell[1];
            String Email = csvCell[2];
            String Mob = csvCell[3];
            String company = csvCell[4];

            driver.findElement(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[1]")).sendKeys(Fname);
            driver.findElement(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[2]")).sendKeys(Lname);
            driver.findElement(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[3]")).sendKeys(Email);
            driver.findElement(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[4]")).sendKeys(Mob);
            driver.findElement(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[5]")).sendKeys(company);
            driver.findElement(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[6]")).click();
//            wdWait.until(ExpectedConditions.urlToBe(newUrl));
//            System.out.println("fname"+Fname);
//            wdWait.until(ExpectedConditions.urlToBe("http://only-testing-blog.blogspot.com/2014/05/form.html?FirstName="+Fname+"&"+"LastName="+Lname+"&"+"EmailID="+Email+"&"+"MobNo="+Mob+"&"+"Company="+company));
//            String newUrl = driver.getCurrentUrl();
//            System.out.println("new url:" + newUrl);
//            wdWait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"post-body-8228718889842861683\"]/div[1]/form/input[1]"),""));
            Thread.sleep(3000);
            driver.switchTo().alert().accept();

        }

    }
}
