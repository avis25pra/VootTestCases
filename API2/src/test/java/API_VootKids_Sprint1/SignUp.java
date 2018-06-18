package API_VootKids_Sprint1;

import static org.hamcrest.Matchers.is;

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
import org.hamcrest.core.IsNull;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import API_VootKids_Sprint1.GenericMethod_VK;

public class SignUp extends GenericMethod_VK
{
	static String password;
	static String deviceId;
	static String deviceBrand;
	static String URL;
	static String email;
	static String expectedDatatype;
	static String myValue;
	static int counter;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String mynullKeys;
	static Boolean bool;
	static String myErrorArray="";
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void SignUp_Kids() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("SignUp");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	email=row.getCell(2).getStringCellValue();
            	password=row.getCell(3).getStringCellValue();
            	deviceId=row.getCell(4).getStringCellValue();
        		deviceBrand=row.getCell(5).getStringCellValue();
        		URL=row.getCell(6).getStringCellValue();
        		key2test=row.getCell(7).getStringCellValue();
        		Value2test=row.getCell(8).getStringCellValue();
        		expectedDatatype=row.getCell(9).getStringCellValue();
            	
            	//if email is passed as empty
            	if(email.equals("EMPTY"))
				{
					email="";
				}
            	//Code to get autogenerated email
				else if(email.equals("AUTO"))
				{
					email=GenericMethod_VK.emailGenerator();
					System.out.println(email);
				}
            	//If email not pass in request
				else if(email.equals("NOTPASS"))
				{
					SignUp.NotPassEmail(i, URL);
					continue;
				}
            	//When password is empty
            	if(password.equals("EMPTY"))
				{
            		password="";
				}
            	//When password is not passed
            	else if(password.equals("NOTPASS"))
				{
            		SignUp.NotPassPassword(i, URL);
            		continue;
				}
            	//If device id is passed as empty
            	if(deviceId.equals("EMPTY"))
				{
        			deviceId="";
				}
            	//When deviceId is not passed
            	else if(deviceId.equals("NOTPASS"))
				{
        			SignUp.NotPassdeviceId(i, URL);
        			continue;
				}
            	//If device brand is empty
            	if(deviceBrand.equals("EMPTY"))
				{
        			deviceBrand="";
				}
            	//If device brand is not passed
            	if(deviceBrand.equals("NOTPASS"))
				{
        			SignUp.NotPassdeviceBrand(i, URL);
        			continue;
				}
            	
            	//Posting the request
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					headers(headers).
					queryParam("email",email).
					queryParam("password",password).
					queryParam("deviceId",deviceId).
					queryParam("deviceBrand",deviceBrand).
					when().
					post(URL);
				
				
				resp1.prettyPrint();//printing the response
				resp1.then().assertThat().statusCode(200);//Checking the status code
				
				if(TestType.equals("Positive"))//logic to test for positive TC
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
						myValue=String.valueOf(resp1.then().extract().path(Keys[j]));//extracting the key value
						System.out.println(myValue);
						try//try to handle datatype whenever it is null 
						{
							retrievDatatype=resp1.jsonPath().get(Keys[j]).getClass().getSimpleName();//extracting datatype
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
				else if(TestType.equals("Negative")) //Logic for negative TC
				{
					myValue=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,myValue);
				}
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "SignUp", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
				
		}
	    GenericMethod_VK.write2Master(1, "SignUp", 11,path1);
	    softAssert.assertAll();
	    
	    
	}
	//Funtion for not passing email 
	public static void NotPassEmail(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("password",password).
			queryParam("deviceId",deviceId).
			queryParam("deviceBrand",deviceBrand).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "SignUp", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	//Funtion for not passing password
	public static void NotPassPassword(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("deviceId",deviceId).
			queryParam("deviceBrand",deviceBrand).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "SignUp", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	//Function for not passing deviceId
	public static void NotPassdeviceId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("password",password).
			queryParam("deviceBrand",deviceBrand).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "SignUp", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	//Function for not passing devicebrand
	public static void NotPassdeviceBrand(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("password",password).
			queryParam("deviceId",deviceId).
			
			when().
			post(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "SignUp", i, 10, resp1, 11, TestType, counter, bool, myValue, Value2test, 12, 13,mynullKeys,myErrorArray );
	}
	
	
}
