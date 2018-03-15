package staging.rcibsp;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelReader implements Reader {

    private final String[] EXTENSIONS = new String[]{"xlsx", "xls"};
    private String sheetName = "Feuil1";

    public List<Map<String, String>> read(String filePath, Country country) throws IOException{
    	
        List<Map<String, String>> result = new ArrayList();
	    //Create an object of File class to open xlsx file
	
	    File file =    new File(filePath);
	    //Create an object of FileInputStream class to read excel file
	
	    FileInputStream inputStream = new FileInputStream(file);
	
	    Workbook workBook = null;
	
	    //Find the file extension by splitting file name in substring  and getting only extension name
	
	    String fileExtensionName = filePath.substring(filePath.indexOf("."));
	
	    //Check condition if the file is xlsx file
	
	    if(fileExtensionName.equals(".xlsx")){
	
		    //If it is xlsx file then create object of XSSFWorkbook class
		
		    workBook = new XSSFWorkbook(inputStream);
	
	    }

	    //Check condition if the file is xls file
	
	    else if(fileExtensionName.equals(".xls")){
	
	        //If it is xls file then create object of XSSFWorkbook class
	
	        workBook = new HSSFWorkbook(inputStream);
	
	    }
	
	    //Read sheet inside the workbook by its name
	   
	    Sheet sheet = workBook.getSheetAt(0); //workBook.getSheet(sheetName);
	
	    //Find number of rows in excel file
	
	    int rowCount = sheet.getLastRowNum()- sheet.getFirstRowNum();
	
	    //Create a loop over all the rows of excel file to read it
	    for (int i = 1; i < rowCount+1; i++) {
	
	        Row row = sheet.getRow(i);
	        Map<String, String>  item = new HashMap<String, String>();
	        //Create a loop to print cell values in a row
	        if (row == null){
	        	continue;
	        }
	        int colIndex;
	        
	        for (int j = 0; j < row.getLastCellNum(); j++) {
	            //Print Excel data in console
	        	Cell cell =  row.getCell(j);
	        	if (cell == null){
	        		continue;
	        	}
	            colIndex = cell.getColumnIndex();
	            
	        	if (cell != null){
	        		 generateMap(item, colIndex, cell, country);
	        	}
	           
	        }
	        if ( item !=null && !item.isEmpty()){
		        result.add(item);
	        }
	       }
	    return result;

    }

    private void generateMap(Map<String, String> result, int colIndex,  Cell cell, Country country) {
	     switch(country){
	     	case DE:
	     		generateMapDE(result, colIndex, cell);
	     		break;
	     	case UK:
	     		generateMapUK(result, colIndex, cell);
	     		break;
	     	case PL:
	     		generateMapPL(result, colIndex, cell);
	     		break;
	     	case BR:
	     		generateMapBR(result, colIndex, cell);
	     		break;
	     }
    }

	private void generateMapDE(Map<String, String> result, int colIndex,  Cell cell) {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		switch(colIndex){
			case 0: 
				if (!cell.getStringCellValue().isEmpty()){
					result.put("email", cell.getStringCellValue()); 
				}
				break;
			case 1:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("password", cell.getStringCellValue());
				}
				break;
			case 2:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("sexe", cell.getStringCellValue());
				}
				break;
			case 3: 
				result.put("firstname", cell.getStringCellValue()); 
				break;
			case 4:
				result.put("lastname", cell.getStringCellValue());
				break;
			case 5:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("confirm password", cell.getStringCellValue());
				}
				break;
			case 6:
				result.put("birthday",   sf.format( cell.getDateCellValue()));
				break;
				
			case 7: 
				result.put("home phone",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 8:
				result.put("phone number", Long.toString((long)cell.getNumericCellValue())); 
				break;
			case 9: 
				result.put("Street", cell.getStringCellValue()); 
				break;
			case 10:
				result.put("Postal Code",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 11:
				result.put("City", cell.getStringCellValue());
				break;
			case 12:
				result.put("Country", cell.getStringCellValue());
				break;
				
			case 13:
				result.put("Vehicle Insurance Date begin",   sf.format( cell.getDateCellValue()));
				break;
			case 14:
				// autofill field
				break;
			case 15:
				result.put("VIN", Long.toString((long)cell.getNumericCellValue())); 
				break;
			case 16: 
				result.put("HSN", Long.toString((long)cell.getNumericCellValue()));  
				break;
			case 17: 
				result.put("TSN", Long.toString((long)cell.getNumericCellValue())); 
				break;
			case 18: 
				result.put("Licence plate", cell.getStringCellValue()); 
				break;
			case 19:
				result.put("Brand", cell.getStringCellValue());
				break;
			case 20:
				result.put("Vehicle Insurance Date",   sf.format( cell.getDateCellValue()));
				break;
			case 21: 
				result.put("Card number",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 22:
				// autofill field
				break;
			case 23:
				 result.put("Expiry date",   sf.format( cell.getDateCellValue()));
				break;
			case 24:
				result.put("Security number", Long.toString((long)cell.getNumericCellValue()));
				break;
		}
	}
	
	private void generateMapPL(Map<String, String> result, int colIndex,  Cell cell) {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		switch(colIndex){
			case 0: 
				result.put("firstname", cell.getStringCellValue()); 
				break;
			case 1:
				result.put("lastname", cell.getStringCellValue());
				break;
			case 2:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("email", cell.getStringCellValue()); 
				}
			case 3: 
				if (!cell.getStringCellValue().isEmpty()){
					result.put("password", cell.getStringCellValue());
				}
				break;
			case 4:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("confirm password", cell.getStringCellValue());
				}
				break;
			case 5:
				result.put("Pesel",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 6:
				result.put("birthday",   sf.format( cell.getDateCellValue()));
				break;
				
			case 7: 
				result.put("home phone",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 8:
				result.put("Postal Code",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 9: 
				result.put("City", cell.getStringCellValue());
				break;
			case 10:
				result.put("Street", cell.getStringCellValue()); 
				break;
			case 11:
				result.put("Street Number", Long.toString((long)cell.getNumericCellValue()));
				break;
			case 12:
				result.put("Vehicle Insurance Date",   sf.format( cell.getDateCellValue()));
				break;
			case 13:
				result.put("VIN",   cell.getStringCellValue()); 
				break;
			case 14: 
				result.put("Registration number", cell.getStringCellValue()); 
				break;
			case 15: 
				result.put("Date first registration",   sf.format( cell.getDateCellValue()));
				break;
			case 16: 
				result.put("Manufacturing Year",    Long.toString((long)cell.getNumericCellValue()));
				break;
			case 17:
				result.put("brand", cell.getStringCellValue());
				break;
			case 18:
				result.put("Model", cell.getStringCellValue());
				break;
			case 19:
				//System.out.println("Case 18"+cell.getCellTypeEnum());
				result.put("Card number",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 20: 
				 result.put("Expiry Month",   Long.toString((long)cell.getNumericCellValue()));
				break;
			case 21:
				 result.put("Expiry Year",   Long.toString((long)cell.getNumericCellValue()));
					break;
			case 22:
				result.put("Security number", Long.toString((long)cell.getNumericCellValue()));
				break;
		}
	}
	
	private void generateMapBR(Map<String, String> result, int colIndex,  Cell cell) {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		switch(colIndex){
			case 0: 
				result.put("lastname", cell.getStringCellValue()); 
				break;
			case 1:
				result.put("firstname", cell.getStringCellValue());
				break;
			case 2:
				result.put("CPF",   cell.getStringCellValue());
				break;
			case 3:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("email", cell.getStringCellValue()); 
				}
			case 4: 
				if (!cell.getStringCellValue().isEmpty()){
					result.put("password", cell.getStringCellValue());
				}
				break;
			case 5:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("confirm password", cell.getStringCellValue());
				}
				break;
			case 6:
				result.put("Model",  cell.getStringCellValue());
				break;
			case 7:
				result.put("Souscription duration",  cell.getStringCellValue());
				break;
			case 8:
				result.put("Mileage covered",   cell.getStringCellValue());
				break;
			case 9:
				result.put("Actual Mileage",   Long.toString((long)cell.getNumericCellValue()));
				break;
			case 10:
				result.put("Car registration date",   sf.format( cell.getDateCellValue()));
				break;
				
			case 11: 
				result.put("home phone",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 12: 
				result.put("mobile phone",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 13:
				result.put("status", cell.getStringCellValue());
				break;
			case 14:
				result.put("birthday",   sf.format( cell.getDateCellValue()));
				break;
			case 15:
				result.put("Street", cell.getStringCellValue()); 
				break;
			case 16:
				result.put("Street Number", Long.toString((long)cell.getNumericCellValue()));
				break;
			case 17: 
				result.put("CEP",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 18: 
				result.put("City", cell.getStringCellValue());
				break;
			case 19: 
				result.put("Country", cell.getStringCellValue());
				break;
			case 20:
				result.put("Neighborhood", cell.getStringCellValue());
				break;
			case 21: 
				result.put("Etat", cell.getStringCellValue());
				break;
			case 22:
				result.put("VIN", cell.getStringCellValue());
				break;
			case 23: 
				result.put("Registration number", cell.getStringCellValue()); 
				break;
			case 24:
				result.put("Card number",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 25: 
				 result.put("Expiry Month",   Long.toString((long)cell.getNumericCellValue()));
				break;
			case 26:
				 result.put("Expiry Year",   Long.toString((long)cell.getNumericCellValue()));
					break;
			case 27:
				result.put("Security number", Long.toString((long)cell.getNumericCellValue()));
				break;
		}
	}
	
	private void generateMapUK(Map<String, String> result, int colIndex,  Cell cell) {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("EEE, MM dd HH:mm:ss yyyy");
		switch(colIndex){
			case 0: 
				result.put("firstname", cell.getStringCellValue()); 
				break;
			case 1:
				result.put("lastname", cell.getStringCellValue());
				break;
			case 2:
				
				result.put("date of birth",   sf.format( cell.getDateCellValue()));
				break;
			case 3: 
				if (!cell.getStringCellValue().isEmpty()){
					result.put("email", cell.getStringCellValue()); 
				}
				break;
			case 4:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("password", cell.getStringCellValue());
				}
				break;
			case 5:
				if (!cell.getStringCellValue().isEmpty()){
					result.put("confirm password", cell.getStringCellValue());
				}
				break;
			case 6: 
				result.put("phone number", Long.toString((long)cell.getNumericCellValue())); 
				break;
			case 7:
				result.put("Property number / Name",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 8: 
				result.put("Street", cell.getStringCellValue()); 
				break;
			case 9:
				result.put("Postal Code", cell.getStringCellValue());
				break;
			case 10:
				result.put("City", cell.getStringCellValue());
				break;
			case 11:
				result.put("Country", cell.getStringCellValue());
				break;
			case 12:
				result.put("VIN", cell.getStringCellValue());
				break;
			case 13: 
				result.put("Registration", cell.getStringCellValue()); 
				break;
			case 14:
				result.put("Brand", cell.getStringCellValue());
				break;
			case 15:
				result.put("Model", cell.getStringCellValue());
				break;
			case 16:
				result.put("Vehicle Insurance Date",   sf.format( cell.getDateCellValue()));
				break;
			case 17: 
				result.put("Card number",  Long.toString((long)cell.getNumericCellValue()));
				break;
			case 18:
				 result.put("Expiry date",   sf.format( cell.getDateCellValue()));
				break;
			case 19:
				result.put("Security number", Long.toString((long)cell.getNumericCellValue()));
				break;
		}
    
	}
    //Main function is calling readExcel function to read data from excel file

    public static void main(String...strings) throws IOException{

	    //Create an object of ReadExcelFile class
	
	    ExcelReader objExcelFile = new ExcelReader();
	
	    //Prepare the path of excel file
	
	    String filePath = System.getProperty("user.dir")+"\\src\\excelExportAndFileIO\\jeudetestFormated.xlsx";
	
	    //Call read file method of the class to read data
	
	    List<Map<String, String>> result = objExcelFile.read(filePath, Country.UK);

    }



	public boolean hasExtension(String extension) {
		// TODO Auto-generated method stub
	    return Arrays.asList(EXTENSIONS).contains(extension);
		
	}

}