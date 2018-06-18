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

public class Avatars extends GenericMethod_VK
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String limit;
	static String offSet;
	static String  URL;
	static Integer counter;
	static String singleVar;
	static Boolean bool;
	static String os;
	static String deviceType;
	static String myErrorArray="";
	static String mynullKeys="";
	static String expectedDatatype;
	static SoftAssert softAssert = new SoftAssert();
	static Headers headers=GenericMethod_VK.headers();
	@Test
	public void avatars() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path3);
		System.out.println(path3);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Avatar");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		System.out.println(rowCount-3);
		 for(int i=1; i<=rowCount;i++)
	     {
			 //reading values from excel sheet
			 Row row = sh.getRow(i);
			 TestType=row.getCell(0).getStringCellValue();	
			 URL=row.getCell(2).getStringCellValue();
			 os=row.getCell(3).getStringCellValue();
			 deviceType=row.getCell(4).getStringCellValue();
			 key2test=row.getCell(5).getStringCellValue();
			 Value2test=row.getCell(6).getStringCellValue();
			 expectedDatatype=row.getCell(7).getStringCellValue();
			 
			 if(os.equals("EMPTY"))//when os is empty initialize it to ""
			 {
				 os="";
			 }
			 if(os.equals("NOTPASS"))
			 {
				 Avatars.NotPassOS(i, URL);//calling function when os is not passed 
				 continue;
			 }
			 if(deviceType.equals("EMPTY"))//when deviceType is empty initialize it to ""
			 {
				 deviceType="";
			 }
			 if(deviceType.equals("NOTPASS"))
			 {
				 Avatars.NotPassDeviceType(i, URL);//calling function when deviceType is not passed
				 continue;
			 }
			 
			 //sending request
			 Response resp1=RestAssured.
						given().
						param("os",os).
						param("deviceType",deviceType).
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						headers(headers).
						
						when().
						get(URL);
			 
			 resp1.prettyPrint();//printing the response in console
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
			//writing back to excel
             GenericMethod_VK.ExcelWriteBack(path3, "Avatar", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	     }
		 GenericMethod_VK.write2Master(2, "Avatar", 9,path3);
		 softAssert.assertAll();
	}
	//function for not passing OS
	public static void NotPassOS(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().
				param("deviceType",deviceType).
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
        GenericMethod_VK.ExcelWriteBack(path3, "Avatar", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	}
	//function for not passing DeviceType
	public static void NotPassDeviceType(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
				given().
				param("os",os).
			
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
        GenericMethod_VK.ExcelWriteBack(path3, "Avatar", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	}
}
