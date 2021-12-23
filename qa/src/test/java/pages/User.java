package pages;

import helpers.ExcelReader;

public class User
{
    public String email;
    public String password;
    public String firstName;
    public String lastName;

    public void setValuesForValidUser(ExcelReader excelReader) {
        email = excelReader.getStringData("apiTest",1,0);
        password = excelReader.getStringData("apiTest",1,1);
        firstName = excelReader.getStringData("apiTest",1,2);
        lastName = excelReader.getStringData("apiTest",1,3);
    }
    public void setValuesForInvalidUser(int i, ExcelReader excelReader) {
        email = excelReader.getStringData("apiTest", i,0);
        password = excelReader.getStringData("apiTest", i,1);
        firstName = excelReader.getStringData("apiTest", i,2);
        lastName = excelReader.getStringData("apiTest", i,3);
    }
    public void setValuesForValidUserToLogin(ExcelReader excelReader) {
        email = excelReader.getStringData("apiTest",1,0);
        password = excelReader.getStringData("apiTest",1,1);
    }

    public void setInvalidValuesForUserToLogin(int emalRow, int emailCell, int passRow, int passCell, ExcelReader excelReader) {
        email = excelReader.getStringData("apiTest",emalRow,emailCell);
        password = excelReader.getStringData("apiTest",passRow,passCell);
    }

    public void setValuesForUpdatingUser(ExcelReader excelReader) {
        firstName = excelReader.getStringData("apiTest",1,4);
        lastName = excelReader.getStringData("apiTest",1,5);
        password = excelReader.getStringData("apiTest",1,6);
    }
}
