package SamplePackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Sample_Authentication {
	static String testType;
	static String platform;
	static String pId;
	static String userName;
	static String password;
	static String uRL;
	static String key2Test;
	static String value2Test;
	static String str;
	static String path = "C:\\Users\\ifocus\\git\\APIAutomation\\API2\\sampleSheet.xlsx";
	static Boolean bool;
	static Response response;
	static SoftAssert softAssert = new SoftAssert();
	
	
	@Test
	public void authentication() throws EncryptedDocumentException, InvalidFormatException, IOException {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("Authentication");
		
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		System.out.println("Number of TestCases: "+rowCount);
		for(int i = 1; i <= rowCount; i++) {
			Row row= sheet.getRow(i);
			testType = row.getCell(0).getStringCellValue();
			platform = row.getCell(1).getStringCellValue();
			pId = row.getCell(2).getStringCellValue();
			userName = row.getCell(4).getStringCellValue();
			password = row.getCell(5).getStringCellValue();
			uRL = row.getCell(6).getStringCellValue();
			key2Test = row.getCell(7).getStringCellValue();
			value2Test = row.getCell(8).getStringCellValue();
			
			if(userName.equals("EMPTY")) {
				userName="";
			}
			if(password.equals("EMPTY")) {
				password="";
			}
			
			BasicConfigurator.configure();
			response = RestAssured.
					   given().
					   relaxedHTTPSValidation().
					   contentType(ContentType.JSON).
					   accept(ContentType.JSON).
					   queryParam("pId", pId).
					   queryParam("platform", platform).
					   queryParam("username", userName).
					   queryParam("password", password).
					   when().
					   post(uRL);
			response.then().assertThat().statusCode(200);
			response.prettyPrint();
			System.out.println("Response Printed");
			
			Sample_Authentication.checkTestType(i, response, testType, path, key2Test, value2Test);
		}
		
	}


	public static void checkTestType(int i, Response response, String testType, String path, String key2Test, String value2Test) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		if(testType.equals("Positive")) 
		{
			str = String.valueOf(response.then().extract().path("LoginRadius.Email[0].Value"));
			//System.exit(0);
			System.out.println(str);
			softAssert.assertEquals(value2Test, str);			
		}
		else if(testType.equals("Negative")) 
		{
			str = String.valueOf(response.then().extract().path(key2Test));
			softAssert.assertEquals(value2Test, str);
		}
		
		Sample_Authentication.writeResponse(i, response, testType, key2Test, value2Test, path);
		
	}


	public static void writeResponse(int i, Response response, String testType, String key2Test, String value2Test,
			String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("Authentication");
		Row row = sheet.getRow(i);
		
		row.createCell(9);
		Cell cellOutput = row.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cellOutput.setCellValue(response.asString());
		
		row.createCell(10);
		Cell cellStaus = row.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(str.equals(value2Test)) 
		{
		 	cellStaus.setCellValue("Pass");
		}
		else
		{
			cellStaus.setCellValue("Fail");
		}
		
		
		FileOutputStream fos = new FileOutputStream(path);
		workbook.write(fos);
		workbook.close();
	}

}
