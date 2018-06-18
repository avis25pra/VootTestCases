package API_VootKids_Sprint4;

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
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

import API_VootKids_Sprint1.GenericMethod_VK;

public class SetUserPreferences extends GenericMethod_VK
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String uId;
	static String language;
	static String  URL;
	static Integer counter;
	static SoftAssert softAssert = new SoftAssert();
	static Headers headers=GenericMethod_VK.headers();
	@Test
	public void setUserPreferences() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path4);
		System.out.println(path4);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("setUserPreferences");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		for(int i=1; i<=rowCount;i++)
		{
			//getting the values from excel sheet
		 	Row row = sh.getRow(i);
		 	TestType=row.getCell(0).getStringCellValue();
		 	URL=row.getCell(2).getStringCellValue();
		 	uId=row.getCell(3).getStringCellValue();
		 	language=row.getCell(4).getStringCellValue();
		 	key2test=row.getCell(5).getStringCellValue();
		 	Value2test=row.getCell(6).getStringCellValue();
		 	
		 	if(uId.equals("EMPTY"))
		 	{
		 		uId="";
		 	}
		 	if(uId.equals("NOTPASS"))
		 	{
		 		SetUserPreferences.NotPassUId(i,URL);
		 		continue;
		 	}
		 	if(language.equals("EMPTY"))
		 	{
		 		language="";
		 	}
		 	if(language.equals("NOTPASS"))
		 	{
		 		SetUserPreferences.NotPassLanguage(i,URL);
		 		continue;
		 	}
		 	
		 	//posting request
	        BasicConfigurator.configure();
			Response resp1=	RestAssured.
							given().
							queryParam("uId", uId).			
							queryParam("language", language).
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							post(URL);
			
			resp1.prettyPrint();//printing the response in the console
			resp1.then().assertThat().statusCode(200);//checking for status code=200
			
			myValue= resp1.jsonPath().get(key2test);//extracting the value
			softAssert.assertEquals(Value2test,myValue);
			
			//write logic
			FileInputStream fis1=new FileInputStream(path4);
			Workbook wb1=WorkbookFactory.create(fis1);
	
			Sheet sh1=wb1.getSheet("setUserPreferences");
			Row row1=sh1.getRow(i);
			row1.createCell(7);
			Cell cel1=row1.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(resp1.asString());
			
			Row row3=sh1.getRow(i);
			row3.createCell(8);
			Cell cel3=row3.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			//main logic to write pass fail logic based on keywords in the response	
			if(myValue.equals(Value2test))
			{
				cel3.setCellValue("Pass");
			}
			else 
			{
				cel3.setCellValue("Fail");
			}
			
			FileOutputStream fos=new FileOutputStream(path4);
			wb1.write(fos);
			fos.close();
		}
		GenericMethod_VK.write2Master(7, "setUserPreferences", 8,path4);
	 	softAssert.assertAll();
	}
	public static void NotPassUId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
        BasicConfigurator.configure();
		Response resp1=	RestAssured.
						given().
						queryParam("language", language).
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						headers(headers).
						when().
						post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue= resp1.jsonPath().get(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//calling generic method to write response and status
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,myValue,7,8,"setUserPreferences",path4);
		
	}
	public static void NotPassLanguage(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
        BasicConfigurator.configure();
		Response resp1=	RestAssured.
						given().
						queryParam("uId", uId).			
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						headers(headers).
						when().
						post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue= resp1.jsonPath().get(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//calling generic method to write response and status
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,myValue,7,8,"setUserPreferences",path4);
	}
}	
