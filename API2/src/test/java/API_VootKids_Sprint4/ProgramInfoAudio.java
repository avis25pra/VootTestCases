package API_VootKids_Sprint4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

public class ProgramInfoAudio extends GenericMethod_VK 
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String limit;
	static String offSet;
	static String  URL;
	static Integer counter;
	static Boolean bool;
	static String mediaId;
	static String mediaTypeId;
	static String myErrorArray="";
	static String mynullKeys="";
	static String expectedDatatype;
	static SoftAssert softAssert = new SoftAssert();
	static Headers headers=GenericMethod_VK.headers();
	@Test
	public void programInfoAudio() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path4);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("ProgramInfoAudio");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		 for(int i=1; i<=rowCount;i++)
		 {
			//getting the values from excel sheet
			 Row row = sh.getRow(i);
			 TestType=row.getCell(0).getStringCellValue();
			 URL=row.getCell(2).getStringCellValue();
			 mediaId=row.getCell(3).getStringCellValue();
			 mediaTypeId=row.getCell(4).getStringCellValue();
			 limit=row.getCell(5).getStringCellValue();
			 offSet=row.getCell(6).getStringCellValue();
			 key2test=row.getCell(7).getStringCellValue();
			 Value2test=row.getCell(8).getStringCellValue();
			 expectedDatatype=row.getCell(9).getStringCellValue();
			 
			 if(mediaId.equals("EMPTY"))
			 {
				 mediaId="";
			 }
			 if(mediaId.equals("NOTPASS"))
			 {
				 ProgramInfoAudio.NotPassMediaId(i,URL);
				 continue;
			 }
			 if(mediaTypeId.equals("EMPTY"))
			 {
				 mediaTypeId="";
			 }
			 if(mediaTypeId.equals("NOTPASS"))
			 {
				 ProgramInfoAudio.NotPassMediaTypeId(i,URL);
				 continue;
			 }
			 if(limit.equals("EMPTY"))
			 {
				 limit="";
			 }
			 if(offSet.equals("EMPTY"))
			 {
				 offSet="";
			 }
			 if(limit.equals("NA") && offSet.equals("NA"))
			 {
				 ProgramInfoAudio.MandatoryParams(i,URL);
				 continue;
			 }
			 if(mediaId.equals("NA") && mediaTypeId.equals("NA"))
			 {
				 ProgramInfoAudio.NonMandatoryParams(i,URL);
				 continue;
			 }
			 
			 //posting request
			 BasicConfigurator.configure();
			 Response resp1=RestAssured.
								given().
								param("mediaId",mediaId).
								param("mediaTypeId",mediaTypeId).
								param("limit",limit).
								param("offSet",offSet).				
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								get(URL);
			 
			 resp1.prettyPrint();//printing the response in the console
			 resp1.then().assertThat().statusCode(200);//checking for status code=200
			 
			 if(TestType.equals("Positive"))
			 {
				 ProgramInfoAudio.PositiveScenarios(i, resp1);
				 continue;
			 }
			 else if(TestType.equals("Negative"))
			 {
				 myValue= resp1.jsonPath().get(key2test);//extracting the value
				 softAssert.assertEquals(Value2test,myValue);
			 }
			//writing back to excel
             GenericMethod_VK.ExcelWriteBack(path4, "ProgramInfoAudio", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
		 }
		 	GenericMethod_VK.write2Master(1, "ProgramInfoAudio", 11,path4);
		 	softAssert.assertAll();
	}
	public static void NotPassMediaId(int i, String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		 BasicConfigurator.configure();
		 Response resp1=RestAssured.
							given().
							param("mediaTypeId",mediaTypeId).
							param("limit",limit).
							param("offSet",offSet).				
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);
		 
		 resp1.then().assertThat().statusCode(200);//checking for status code=200
		 myValue=resp1.then().extract().path(key2test);
		 softAssert.assertEquals(Value2test,myValue);
		 
		//writing back to excel
         GenericMethod_VK.ExcelWriteBack(path4, "ProgramInfoAudio", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	public static void NotPassMediaTypeId(int i, String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		 BasicConfigurator.configure();
		 Response resp1=RestAssured.
							given().
							param("mediaId",mediaId).
							param("limit",limit).
							param("offSet",offSet).				
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);
		 
		 resp1.then().assertThat().statusCode(200);//checking for status code=200
		 myValue=resp1.then().extract().path(key2test);
		 softAssert.assertEquals(Value2test,myValue);
		 
		//writing back to excel
         GenericMethod_VK.ExcelWriteBack(path4, "ProgramInfoAudio", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	public static void NotPassLimit(int i, String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		 BasicConfigurator.configure();
		 Response resp1=RestAssured.
							given().
							param("mediaId",mediaId).
							param("mediaTypeId",mediaTypeId).
							param("offSet",offSet).				
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);
		 
		 resp1.then().assertThat().statusCode(200);//checking for status code=200
		 myValue=resp1.then().extract().path(key2test);
		 softAssert.assertEquals(Value2test,myValue);
		 
		//writing back to excel
         GenericMethod_VK.ExcelWriteBack(path4, "ProgramInfoAudio", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	public static void MandatoryParams (int i, String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		 BasicConfigurator.configure();
		 Response resp1=RestAssured.
							given().
							param("mediaId",mediaId).
							param("mediaTypeId",mediaTypeId).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);
		 
		 resp1.prettyPrint();//printing the response in the console
		 resp1.then().assertThat().statusCode(200);//checking for status code=200
		
		ProgramInfoAudio.PositiveScenarios(i, resp1);
		
	}
	public static void NonMandatoryParams (int i, String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		 BasicConfigurator.configure();
		 Response resp1=RestAssured.
							given().
							param("limit",limit).
							param("offSet",offSet).				
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);
		 
		 resp1.then().assertThat().statusCode(200);//checking for status code=200
		 myValue=resp1.then().extract().path(key2test);
		 softAssert.assertEquals(Value2test,myValue);
		 
		//writing back to excel
         GenericMethod_VK.ExcelWriteBack(path4, "ProgramInfoAudio", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	public static void PositiveScenarios(int i, Response resp1) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//declaration of Arraylist for null parameters keys
		ArrayList<String> nullParams = new ArrayList<String>();//storing the keys which have null values
		ArrayList<String> myDatatype = new ArrayList<String>();//storing the datatype of keys coming from response
		
		String[] Keys = Value2test.split(",");//split function for separating the keys to test
		String[] KeysDatatype=expectedDatatype.split(",");//split function for separating the datatype to test
		//converting normal array to arrayList
		ArrayList<String> expectedDatatype = new ArrayList<String>(Arrays.asList(KeysDatatype));
		
		counter=1;//assigning flag=1 for not getting any garbage value
		String retrievDatatype="abc";//initialization of variable where we store datatype
		mynullKeys="";//initialize to "" for using next iteration
		
		nullParams.removeAll(nullParams);//remove all elements for using next iteration[overriding issue]
		myDatatype.removeAll(myDatatype);//remove all elements for using next iteration[overriding issue]
		
		for (int j=0; j < Keys.length; j++)
		{
			myValue=String.valueOf(resp1.then().extract().path(key2test+"."+Keys[j]));//extracting the key value
			try//try to handle datatype whenever it is null 
			{
				retrievDatatype=resp1.jsonPath().get(key2test+"."+Keys[j]).getClass().getSimpleName();//extracting datatype
			}
			catch(Exception e)
			{
				retrievDatatype="String"; //as null is literal and its datatype is string, so assigning it to string
			}
			myDatatype.add(retrievDatatype);//append the elements into arraylist
			if(myValue.equals("null")) 
			{
				counter=0;
				nullParams.add(Keys[j]);//appending the elements 
				softAssert.assertEquals(myValue,"SomeValue");//for failing the TC
			}
		}
		System.out.println(myDatatype);//print the arraylist of response datatype
   		System.out.println(expectedDatatype);//print the arraylist of mastHeadTray datatype
   		bool=myDatatype.equals(expectedDatatype);//Comparing both actual and Expected data types.
   		
   		//coverting arraylist to string
   		for (String x : nullParams)
		{
			mynullKeys += x + ",";
		}
	
   		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path4, "ProgramInfoAudio", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
}
