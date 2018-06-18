package SamplePackage;

import java.awt.RenderingHints.Key;
import java.awt.image.RescaleOp;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.lf5.PassingLogRecordFilter;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.omg.CORBA.RepositoryIdHelper;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Sample_KUserRegistraton {
	static String testType;
	static String platform;
	static String pId;
	static String email;
	static String uId;
	static String uRL;
	static String firstName;
	static String lastName;
	static String udid;
	static String deviceBrand;
	static String key2Test;
	static String value2Test;
	static String str;
	static String path = "C:\\Users\\ifocus\\git\\APIAutomation\\API2\\sampleSheet.xlsx";
	static int flag;
	static Boolean bool;
	static Response response;
	static SoftAssert softAssert = new SoftAssert();
	
	
	@Test
	public void kUserRegistration() throws EncryptedDocumentException, InvalidFormatException, IOException {
		RestAssured.config = RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("kUserReg");
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		System.out.println("Number of TestCases: "+rowCount);
		
		for(int i = 1; i <= rowCount; i++) {
			Row row = sheet.getRow(i);
			testType = row.getCell(0).getStringCellValue();
			platform = row.getCell(1).getStringCellValue();
			pId = row.getCell(2).getStringCellValue();
			email = row.getCell(4).getStringCellValue();
			uId = row.getCell(5).getStringCellValue();
			firstName= row.getCell(6).getStringCellValue();
			lastName = row.getCell(7).getStringCellValue();
			udid = row.getCell(8).getStringCellValue();
			deviceBrand = row.getCell(9).getStringCellValue();
			uRL = row.getCell(10).getStringCellValue();
			key2Test = row.getCell(11).getStringCellValue();
			value2Test = row.getCell(12).getStringCellValue();
			
			if(email.equals("AUTO")) {
				String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
		        StringBuilder salt = new StringBuilder();
		        Random rnd = new Random();
		        while (salt.length() < 10) { 
		            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
		            salt.append(SALTCHARS.charAt(index));
		        }
		        String saltStr = salt.toString();
		       email= saltStr+"@gmail.com";
			}
			if(uId.equals("AUTO")) {
				String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
		        StringBuilder salt = new StringBuilder();
		        Random rnd = new Random();
		        while (salt.length() < 10) { 
		            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
		            salt.append(SALTCHARS.charAt(index));
		        }
		        uId  = salt.toString();    
			}
			if(email.equals("EMPTY")) {
				email="";
			}
			if(uId.equals("EMPTY")) {
				uId="";
			}
			if(email.equals("NOT PASS")) {
				Sample_KUserRegistraton.emailNotPassed(i, response, key2Test, value2Test, path);
				continue;
			}
			if(uId.equals("NOT PASS")) {
				Sample_KUserRegistraton.uIdNotPassed(i, response, key2Test, value2Test, path);
				continue;
			}
			if(udid.equals("NOT PASS")) {
				Sample_KUserRegistraton.udIdNotPassed(i, response, key2Test, value2Test, path);
				continue;
			}if(deviceBrand.equals("NOT PASS")) {
				Sample_KUserRegistraton.deviceBrandNotPassed(i, response, key2Test, value2Test, path);
				continue;
			}
						
			BasicConfigurator.configure();
			response = RestAssured.
					   given().
					   relaxedHTTPSValidation().
					   contentType(ContentType.JSON).
					   accept(ContentType.JSON).
					   queryParam("platform", platform).
					   queryParam("pId", pId).
					   queryParam("email", email).
					   queryParam("UID", uId).
					   queryParam("firstname", firstName).
					   queryParam("lastname", lastName).
					   queryParam("UDID", udid).
					   queryParam("deviceBrand", deviceBrand).
					   when().
					   post(uRL);
			response.then().assertThat().statusCode(200);
			response.prettyPrint();
			System.out.println("Response Printed");
			
			Sample_KUserRegistraton.checkTestType(i, response, key2Test, value2Test, path);
			softAssert.assertAll();		   
		}
	}


	public static void deviceBrandNotPassed(int i, Response response, String key2Test, String value2Test,
			String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		System.out.println("DeviceBrand is Not Passed");
		BasicConfigurator.configure();
		response = RestAssured.
				   given().
				   relaxedHTTPSValidation().
				   contentType(ContentType.JSON).
				   accept(ContentType.JSON).
				   queryParam("platform", platform).
				   queryParam("pId", pId).
				   queryParam("email", email).
				   queryParam("UID", uId).
				   queryParam("firstname", firstName).
				   queryParam("lastname", lastName).
				   queryParam("UDID", udid).
				   when().
				   post(uRL);
		response.then().assertThat().statusCode(200);
		response.prettyPrint();
		System.out.println("Response Printed");
		
		Sample_KUserRegistraton.checkTestType(i, response, key2Test, value2Test, path);
		
	}


	public static void udIdNotPassed(int i, Response response, String key2Test, String value2Test, String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		System.out.println("UDID is Not Passed");
		BasicConfigurator.configure();
		response = RestAssured.
				   given().
				   relaxedHTTPSValidation().
				   contentType(ContentType.JSON).
				   accept(ContentType.JSON).
				   queryParam("platform", platform).
				   queryParam("pId", pId).
				   queryParam("email", email).
				   queryParam("UID", uId).
				   queryParam("firstname", firstName).
				   queryParam("lastname", lastName).
				   queryParam("deviceBrand", deviceBrand).
				   when().
				   post(uRL);
		response.then().assertThat().statusCode(200);
		response.prettyPrint();
		System.out.println("Response Printed");
		
		Sample_KUserRegistraton.checkTestType(i, response, key2Test, value2Test, path);
		
	}


	public static void uIdNotPassed(int i, Response response2, String key2Test2, String value2Test2, String path2) throws EncryptedDocumentException, InvalidFormatException, IOException {
		System.out.println("uId is Not Passed");
		BasicConfigurator.configure();
		response = RestAssured.
				   given().
				   relaxedHTTPSValidation().
				   contentType(ContentType.JSON).
				   accept(ContentType.JSON).
				   queryParam("platform", platform).
				   queryParam("pId", pId).
				   queryParam("email", email).
				   queryParam("firstname", firstName).
				   queryParam("lastname", lastName).
				   queryParam("UDID", udid).
				   queryParam("deviceBrand", deviceBrand).
				   when().
				   post(uRL);
		response.then().assertThat().statusCode(200);
		response.prettyPrint();
		System.out.println("Response Printed");
		
		Sample_KUserRegistraton.checkTestType(i, response, key2Test, value2Test, path);
		
	}


	public static void emailNotPassed(int i, Response response, String key2Test, String value2Test, String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		System.out.println("Email is Not Passed");
		BasicConfigurator.configure();
		response = RestAssured.
				   given().
				   relaxedHTTPSValidation().
				   contentType(ContentType.JSON).
				   accept(ContentType.JSON).
				   queryParam("platform", platform).
				   queryParam("pId", pId).
				   queryParam("UID", uId).
				   queryParam("firstname", firstName).
				   queryParam("lastname", lastName).
				   queryParam("UDID", udid).
				   queryParam("deviceBrand", deviceBrand).
				   when().
				   post(uRL);
		response.then().assertThat().statusCode(200);
		response.prettyPrint();
		System.out.println("Response Printed");
		
		Sample_KUserRegistraton.checkTestType(i, response, key2Test, value2Test, path);
		
	}


	public static void checkTestType(int i, Response response, String key2Test, String value2Test, String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		if(testType.equals("Positive")) {
			
			String[] Values = value2Test.split(",");
			ArrayList<String> expectedDataType = new ArrayList<String>(Arrays.asList(Values));
			String[] Keys = key2Test.split(",");
			ArrayList<String> actualResults = new ArrayList<String>(Arrays.asList(Keys));
			ArrayList<String> myDataType = new ArrayList<String>();
			int sizeOfKeys = Keys.length;
			
			for(int j=0;j<sizeOfKeys;j++) {
				str = response.jsonPath().get(Keys[j]).getClass().getSimpleName();
				myDataType.add(str);				
			}
			System.out.println(myDataType);
			System.out.println(expectedDataType);
			bool = myDataType.equals(expectedDataType);			
		}else if(testType.equals("Negative")) {
			str = response.then().extract().path(key2Test);
			bool = str.equals(value2Test);
		}
		Sample_KUserRegistraton.write2Sheet(i, response, key2Test, value2Test, path, bool);
	}


	public static void write2Sheet(int i, Response response, String key2Test, String value2Test, String path,
			Boolean bool) throws EncryptedDocumentException, InvalidFormatException, IOException {
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("kUserReg");
		Row row = sheet.getRow(i);
		
		row.getCell(13);
		Cell cellOutput = row.getCell(13, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cellOutput.setCellValue(response.asString());
		 
		row.getCell(14);
		Cell cellStatus = row.getCell(14, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(bool==true) {
			cellStatus.setCellValue("Pass");
		}else {
			cellStatus.setCellValue("Fail");
		}
		FileOutputStream fos = new FileOutputStream(path);
		workbook.write(fos);
		workbook.close();
	}

}
