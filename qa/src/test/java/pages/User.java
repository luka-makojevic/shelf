package pages;

import helpers.ExcelReader;
import java.io.IOException;

public class User
{
    public String email;
    public String password;
    public String firstName;
    public String lastName;

    public User setValuesForValidUser(String email,String password, String firstName, String lastName) throws IOException
    {
        ExcelReader excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");
        this.email = email = excelReader.getStringData("apiTest",1,0);
        this.password = password = excelReader.getStringData("apiTest",1,1);
        this.firstName = firstName = excelReader.getStringData("apiTest",1,2);
        this.lastName = lastName = excelReader.getStringData("apiTest",1,3);
        return this;
    }

    public User setValuesForInvalidUser(String email, String password, String firstName, String lastName) throws IOException
    {
        ExcelReader excelReader = new ExcelReader("src/main/resources/ExcelRead.xlsx");

        for (int i = 2; i < excelReader.getLastRowNumberFromSheet("apiTest"); i++)
        {
            this.email = email = excelReader.getStringData("apiTest",i,0);
            this.password = password = excelReader.getStringData("apiTest",i,1);
            this.firstName = firstName = excelReader.getStringData("apiTest",i,2);
            this.lastName = lastName = excelReader.getStringData("apiTest",i,3);
        }
        return this;
    }
}
