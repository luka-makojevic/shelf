package helpers.excelHelpers;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    File file;
    FileInputStream fis;
    XSSFWorkbook wb;
    XSSFSheet sheet;
    XSSFRow row;
    XSSFCell cell; //column

    public ExcelReader(String filePath) throws IOException {
        this.file = new File(filePath);
        this.fis = new FileInputStream(file);
        this.wb = new XSSFWorkbook(fis);
    }

    public String getStringData(String sheetName, int rowNumber, int cellNumber) {
        this.sheet = wb.getSheet(sheetName);
        this.row = sheet.getRow(rowNumber);
        this.cell = row.getCell(cellNumber);
        return this.cell.getStringCellValue();
    }

    public int getIntegerData(String sheetName, int rowNumber, int cellNumber) {
        this.sheet = wb.getSheet(sheetName);
        this.row = sheet.getRow(rowNumber);
        this.cell = row.getCell(cellNumber);
        return (int) this.cell.getNumericCellValue();
    }

    public int getLastRowNumber() {
        return sheet.getLastRowNum();
    }

    public int getLastRowNumberFromSheet(String sheetName) {
        this.sheet = wb.getSheet(sheetName);
        return sheet.getLastRowNum();
    }


    /** Method for getting the last row number from a specific column in Excel sheet
     *
     * @param  sheetName
     * @param  columnName
     * @return int
     * @author stefan.gajic
     */
    public int getLastRowNumberFromColumn(String sheetName, String columnName) {
        int rowNum = 0;
        int colNum = 0;
        this.sheet = wb.getSheet(sheetName);
        this.row = sheet.getRow(rowNum);

        // Selects desired column by its name
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().equals(columnName)) {
                colNum = i;}
        }
        //Determines last row number from desired column
        for(int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (row.getCell(colNum).getStringCellValue().equals("")) {
                rowNum = i-1;
                break;
            } else {
                rowNum = sheet.getLastRowNum();}
        }
        return rowNum;
    }

}
