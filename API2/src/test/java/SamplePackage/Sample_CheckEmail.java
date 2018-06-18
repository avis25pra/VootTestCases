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
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Sample_CheckEmail {
	
	static String str;
	static String path = "C:\\Users\\ifocus\\git\\APIAutomation\\API2\\sampleSheet.xlsx";
	static String uRL;
	static String key2Test;
	static String value2Test;
	static String email;
	static String testType;
	static String pId;
	static String platform;
	static Boolean bool;
	static Response response;
	static SoftAssert softAssert = new SoftAssert();
	
	@Test
	public void emailChecker() throws EncryptedDocumentException, InvalidFormatException, IOException{
		
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("checkEmail");
		
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		System.out.println("Number of Test Cases: "+rowCount);
		
		for(int i=1; i<=rowCount; i++) {
			Row row = sheet.getRow(i);
			testType = row.getCell(0).getStringCellValue();
			platform = row.getCell(1).getStringCellValue();
			pId = row.getCell(2).getStringCellValue();
			email = row.getCell(4).getStringCellValue();
			uRL = row.getCell(5).getStringCellValue();
			key2Test = row.getCell(6).getStringCellValue();
			value2Test = row.getCell(7).getStringCellValue();
			
			BasicConfigurator.configure();
			response = RestAssured.
					   given().
					   relaxedHTTPSValidation().
					   accept(ContentType.JSON).
					   contentType(ContentType.JSON).
					   queryParam("platform", platform).
					   queryParam("PId", pId).
					   queryParam("ëmail", email).
					   when().
					   post(uRL);
			
			response.then().assertThat().statusCode(200);
			response.prettyPrint();
			System.out.println("Response Printed");
			
			Sample_CheckEmail.checkTestType(response, i, path, testType, uRL, key2Test, value2Test);
			softAssert.assertAll();
			
		}
		
	}

	public static void checkTestType(Response response, int i, String path, String testType, String uRL, String key2Test, String value2Test) throws EncryptedDocumentException, InvalidFormatException, IOException {
		if(testType.equals("Positive")) {
			bool = response.then().extract().path("isExist");
			str = String.valueOf(bool);
			softAssert.assertEquals(value2Test, str);
		}else if(testType.equals("Negative")) {
			str = String.valueOf(response.then().extract().path(key2Test));
			softAssert.assertEquals(value2Test, str);
		}
		
		Sample_CheckEmail.write2Sheet(i, response, path, key2Test, value2Test, str);
		
	}

	public static void write2Sheet(int i, Response response, String path, String key2Test,
			String value2Test, String str) throws EncryptedDocumentException, InvalidFormatException, IOException {
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("checkEmail");
		
		Row write2Response = sheet.getRow(i);
		write2Response.createCell(8);
		Cell cellOutput = write2Response.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cellOutput.setCellValue(response.asString());
		
		write2Response.createCell(9);
		Cell cellStatus = write2Response.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(str.equals(value2Test)) {
			cellStatus.setCellValue("Pass");
		}else {
			cellStatus.setCellValue("Fail");
		}
		
		FileOutputStream fos = new FileOutputStream(path);
		workbook.write(fos);
		workbook.close();
	}

}
