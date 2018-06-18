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

public class CreatePin extends GenericMethod_VK
{
	static String myValue;
	static int num;
	static String URL;
	static String pin;
	static String numberAsString;
	static String email;
	static String key2test;
	static String Value2test;
	static String expectedDatatype;
	static String TestType;
	static Boolean bool;
	static int counter;
	static String mynullKeys;
	static String myErrorArray="";
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void CreatePin1() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		GenericMethod_VK g=new GenericMethod_VK();
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("CreatePin");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    		Response resp=	g.SignUp();
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	email=row.getCell(2).getStringCellValue();
            	pin=row.getCell(3).getStringCellValue();
            	URL=row.getCell(4).getStringCellValue();
        		key2test=row.getCell(5).getStringCellValue();
        		Value2test=row.getCell(6).getStringCellValue();
        		expectedDatatype=row.getCell(7).getStringCellValue();
        		
        		//if email is AUTO extrtact email from SignUp API
        		if(email.equals("AUTO"))
        		{
        			email=resp.then().extract().path("Email");
        		}
        		//if email is invalid 
        		else if(email.equals("INVALID"))
        		{
        			email="john14.doe33333333333333333333333@mailinator.com";
        		}
        		//if email is empty
        		else if(email.equals("EMPTY"))
				{
        			email="";
				}
        		//if email is null pass assign email="null"
        		else if(email.equals("NULL")) {
        			email="null";
        		}
        		//calling function for not passing email
        		else if(email.equals("NOTPASS"))
				{
            		CreatePin.NotPassemail(pin, i, URL);
            		continue;
				}
        		//assign pin="" when email is empty
        		if(pin.equals("EMPTY"))
				{
        			pin="";
				}
        		//calling function pin is not pass
        		if(pin.equals("NOTPASS"))
				{
            		CreatePin.NotPassPin(email, i, URL);
            		continue;
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
					queryParam("pin",pin).
					when().
					post(URL);
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200);//checking the status code as 200
				
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
				GenericMethod_VK.ExcelWriteBack(path1, "CreatePin", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray);
				
		}
	    GenericMethod_VK.write2Master(9, "CreatePin", 9,path1);
	    softAssert.assertAll();
	    
	}
	//function for not passing email
	public static void NotPassemail(String pin,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("pin",pin).
			when().
			post(URL);
		
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "CreatePin", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray);
	}
	//function for not passing pin
	public static void NotPassPin(String Uid,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			when().
			post(URL);
		
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "CreatePin", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray);
	}
	
	
}
