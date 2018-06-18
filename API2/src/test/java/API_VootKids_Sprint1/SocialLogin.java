package API_VootKids_Sprint1;

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
import com.jayway.restassured.response.Response;

public class SocialLogin extends GenericMethod_VK
{	
	static String deviceId;
	static String uId;
	static String deviceBrand;
	static String URL;
	static int counter;
	static String expectedDatatype;
	static Boolean bool;
	static String mynullKeys;
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String myErrorArray="";
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void Social_Login() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("SocialLogin");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	uId=row.getCell(2).getStringCellValue();
            	deviceId=row.getCell(3).getStringCellValue();
            	deviceBrand=row.getCell(4).getStringCellValue();
            	URL=row.getCell(5).getStringCellValue();
        		key2test=row.getCell(6).getStringCellValue();
        		Value2test=row.getCell(7).getStringCellValue();
        		expectedDatatype=row.getCell(8).getStringCellValue();
        		
        		if(uId.equals("EMPTY")) //passing Uid as empty
				{
        			uId="";
				}
        		else if(uId.equals("NOTPASS")) //calling function when uid is not passed in request
				{
            		SocialLogin.NotPassUid(i, URL);
            		continue;
				}
        		if(deviceId.equals("EMPTY"))//passing deviceId as empty
				{
            		deviceId="";
				}
        		else if(deviceId.equals("NOTPASS"))//calling function when deviceId is not passed in request
				{
            		SocialLogin.NotPassdeviceId(i, URL);
            		continue;
				}
        		if(deviceBrand.equals("EMPTY")) //passing empty value when deviceBrand is empty
        		{
        			deviceBrand="";
        		}
        		else if(deviceBrand.equals("NOTPASS"))//calling function for not passing devicebrand
        		{
        			SocialLogin.NotPassdevicebrand(i, URL);
        			continue;
        		}
				
        		//Posting request to the server
        		BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					headers(headers).
					queryParam("uId",uId).
					queryParam("deviceId",deviceId).
					queryParam("deviceBrand",deviceBrand).
					when().
					get(URL);
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200); //checking for status code=200 in response
				
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
				GenericMethod_VK.ExcelWriteBack(path1, "SocialLogin", i, 9, resp1, 10, TestType, counter, bool, myValue, Value2test, 11, 12,mynullKeys,myErrorArray);
		}
	    //writing back to master
	    GenericMethod_VK.write2Master(3,"SocialLogin",10,path1);//calling generic method for writing in master sheet
	    softAssert.assertAll();
	    
	}
	//function for not passing Uid
	public static void NotPassUid(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("deviceId",deviceId).
			queryParam("deviceBrand",deviceBrand).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		GenericMethod_VK.ExcelWriteBack(path1, "SocialLogin", i, 9, resp1, 10, TestType, counter, bool, myValue, Value2test, 11, 12,mynullKeys,myErrorArray);
 
	}
	//function for not passing deviceId
	public static void NotPassdeviceId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("uId",uId).
			queryParam("deviceBrand",deviceBrand).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		GenericMethod_VK.ExcelWriteBack(path1, "SocialLogin", i, 9, resp1, 10, TestType, counter, bool, myValue, Value2test, 11, 12,mynullKeys,myErrorArray);

	}
	//function for not passing deviceBrand
	public static void NotPassdevicebrand(int i,String URL)throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("uId",uId).
			queryParam("deviceId",deviceId).
			when().
			get(URL);
		
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		GenericMethod_VK.ExcelWriteBack(path1, "SocialLogin", i, 9, resp1, 10, TestType, counter, bool, myValue, Value2test, 11, 12,mynullKeys,myErrorArray);

	}
}
