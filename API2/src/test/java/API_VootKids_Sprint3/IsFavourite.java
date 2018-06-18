package API_VootKids_Sprint3;

import java.io.FileInputStream;
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

public class IsFavourite extends GenericMethod_VK
{
	static String myValue;
	static String isfav="";
	static String singleVar;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String mediaTypeId;
	static String uId;
	static String profileId;
	static String  URL;
	static String mediaId;
	static Boolean bool;
	static Integer counter;
	static String mynullKeys="";
	static String expectedDatatype;
	static String myErrorArray="";
	static SoftAssert softAssert = new SoftAssert();
	static Headers headers=GenericMethod_VK.headers();
	@Test
	public void isFavourite() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path3);
		System.out.println(path3);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("isFavourite");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		for(int i=1; i<=rowCount;i++)
        {
			//reading values from excel
			Row row = sh.getRow(i);
		 	TestType=row.getCell(0).getStringCellValue();	
		 	mediaId=row.getCell(3).getStringCellValue();
		 	uId=row.getCell(4).getStringCellValue();
		 	profileId=row.getCell(5).getStringCellValue();
		 	mediaTypeId=row.getCell(6).getStringCellValue();
		 	URL=row.getCell(2).getStringCellValue();
		 	key2test=row.getCell(7).getStringCellValue();
		 	Value2test=row.getCell(8).getStringCellValue();
		 	expectedDatatype=row.getCell(9).getStringCellValue();
		 	
		 	if(mediaId.equals("EMPTY"))//when mediaid is empty initialize it to ""
		 	{
		 		mediaId="";
		 	}
		 	if(mediaId.equals("NOTPASS"))
		 	{
		 		IsFavourite.NotPassMediaId(i, URL);//calling function when mediaid is not passed
		 		continue;
		 	}
		 	if(uId.equals("EMPTY"))//when uid is empty initialize it to ""
		 	{
		 		uId="";
		 	}
		 	if(uId.equals("NOTPASS"))
		 	{
		 		IsFavourite.NotPassuId(i, URL);//calling function when uid is not passed 
		 		continue;
		 	}
		 	if(profileId.equals("EMPTY"))//when prodileid is empty initialize it to ""
		 	{
			 	profileId="";
		 	}
		 	if(profileId.equals("NOTPASS"))
		 	{
		 		IsFavourite.NotPassprofileId(i, URL);//calling function when profileid is not passed
		 		continue;
		 	}
		 	if(mediaTypeId.equals("EMPTY"))//when mediatypeid is empty initialize it to ""
		 	{
		 		mediaTypeId="";
		 	}
		 	if(mediaTypeId.equals("NOTPASS"))
		 	{
		 		IsFavourite.NotPassMediaTypeId(i, URL);//calling function when mediatypeis is not passed
		 		continue;
		 	}
		 	//posting request to the server
		 	Response resp1=	RestAssured.
					given().
					param("mediaId",mediaId).
					param("uId",uId).
					param("profileId",profileId).
					param("mediaTypeId",mediaTypeId).
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					headers(headers).
					when().
					get(URL);
		 	
		 	resp1.prettyPrint();//printing response in the console
		 	resp1.then().assertThat().statusCode(200);//checking for status code=200
		 	
		 	if(TestType.equals("Positive"))
		 	{
		 		//declaration of Arraylist for null parameters keys
				ArrayList<String> nullParams = new ArrayList<String>();//storing the keys which have null values
				ArrayList<String> myDatatype = new ArrayList<String>();//storing the datatype of keys coming from response
				
				String[] Keys = Value2test.split(",");//split function for separating the keys to test
				String[] KeysDatatype=expectedDatatype.split(",");//split function for separating the datatype to test
				//converting normal array to arrayList
				ArrayList<String> expectedDatatype = new ArrayList<String>(Arrays.asList(KeysDatatype));
				String retrievDatatype="abc";//initialization of variable where we store datatype of mediaId
				
				for (int j=0;j<Keys.length;j++) 
				{
					counter=1;//assigning counter=1 for not getting any garbage value
					myValue=String.valueOf(resp1.jsonPath().get(key2test+"."+Keys[j]));//extracting key value
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
		 	}
		 	
		 	else if(TestType.equals("Negative"))
		 	{
		 		myValue= resp1.jsonPath().get(key2test);//extracting the value
		 		softAssert.assertEquals(Value2test,myValue);
		 	}
		 	//writing back to excel
			 GenericMethod_VK.ExcelWriteBack(path3, "isFavourite", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
        }
	 	GenericMethod_VK.write2Master(7, "isFavourite", 11,path3);
	 	softAssert.assertAll();
        
		
	}
	//fucntion when mediaid not passed
	public static void NotPassMediaId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("uId",uId).
				param("profileId",profileId).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);//extracting value for keys
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		 GenericMethod_VK.ExcelWriteBack(path3, "isFavourite", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	//function when uid is not passed
	public static void NotPassuId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("mediaId",mediaId).
				param("profileId",profileId).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);//extracting values for keys
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		 GenericMethod_VK.ExcelWriteBack(path3, "isFavourite", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	//function when profileId is not passed
	public static void NotPassprofileId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("mediaId",mediaId).
				param("uId",uId).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);//extracting values for keys
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		 GenericMethod_VK.ExcelWriteBack(path3, "isFavourite", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	//function when mediatypeid is not passed
	public static void NotPassMediaTypeId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("mediaId",mediaId).
				param("uId",uId).
				param("profileId",profileId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);//extracting values for keys
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		 GenericMethod_VK.ExcelWriteBack(path3, "isFavourite", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
}
