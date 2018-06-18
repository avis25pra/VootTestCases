package API_VootKids_Sprint1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class ResetPin extends GenericMethod_VK
{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String oldPin;
	static String newPin;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void reset_Pin() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("ResetPIN");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String email=row.getCell(2).getStringCellValue();
            	oldPin=row.getCell(3).getStringCellValue();
            	newPin=row.getCell(4).getStringCellValue();
            	//calling function for auto generation pin
            	if(newPin.equals("AUTO"))
            	{
            		newPin=GenericMethod_VK.pinGenerator();
            	}
            	String URL=row.getCell(5).getStringCellValue();
        		key2test=row.getCell(6).getStringCellValue();
        		Value2test=row.getCell(7).getStringCellValue();
        		//assign email="" when email is empty
        		if(email.equals("EMPTY"))
				{
        			email="";
				}
        		//calling function when email is not pass
        		else if(email.equals("NOTPASS"))
				{
            		ResetPin.NotPassEmail(oldPin, newPin, i, URL);
            		continue;
				}
        		//assign oldpin="" when oldpin is empty
        		if(oldPin.equals("EMPTY"))
				{
        			oldPin="";
				}
        		//calling function when old pin is not passed
        		if(oldPin.equals("NOTPASS"))
				{
            		ResetPin.NotPassOldPin(email, newPin, i, URL);
            		continue;
				}
        		//assign newpin="" when newpin is empty
        		if(newPin.equals("EMPTY"))
				{
        			newPin="";
				}
        		//calling function when new pin is not passed
        		if(newPin.equals("NOTPASS"))
				{
            		ResetPin.NotPassNewPin(email, oldPin, i, URL);
            		continue;
				}
        		//assign values for oldpin and newpin when both are same
				if(oldPin.equals("SAMEPIN"))
				{
					oldPin="3456";
					newPin="3456";
				}
				
				//posting request
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					headers(headers).
					queryParam("email",email).
					queryParam("oldPin",oldPin).
					queryParam("newPin",newPin).
					when().
					post(URL);
				
				resp1.prettyPrint();//printing the response
				resp1.then().assertThat().statusCode(200);//checking the statuscode=200
				
				str=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,str);
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("ResetPIN");
				Row row1=sh1.getRow(i);
				row1.createCell(8);
				Cell cel1=row1.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(9);
				Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(str.equals(Value2test) )
				{
					cel3.setCellValue("Pass");
				}
				else 
				{
					cel3.setCellValue("Fail");
				}
				if(TestType.equals("Positive"))//logic for writing pass/fail for positive scenarios
				{
					Row row4=sh1.getRow(1);
					row4.createCell(3);
					Cell cel4=row4.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cel4.setCellType(CellType.STRING);
					cel4.setCellValue(newPin);
				}
				
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
				
		}
	    GenericMethod_VK.write2Master(7, "ResetPIN", 9,path1);
	    softAssert.assertAll();
	}
	//function for not passing email
	public static void NotPassEmail(String oldPin,String newPin,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("oldPin",oldPin).
			queryParam("newPin",newPin).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,8,9,"ResetPIN",path1);
	}
	//function for not passing oldpin
	public static void NotPassOldPin(String email,String newPin,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("newPin",newPin).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,8,9,"ResetPIN",path1);
	}
	//function for not passing newpin
	public static void NotPassNewPin(String email,String oldPin,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("oldPin",oldPin).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,8,9,"ResetPIN",path1);
	}
	
}
