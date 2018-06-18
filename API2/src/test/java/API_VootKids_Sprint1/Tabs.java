package API_VootKids_Sprint1;

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
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class Tabs extends GenericMethod_VK
{
	static String key2test;
	static String Value2test;
	static String TestType;
	static String deviceType;
	static String os;
	static String Url;
	static String myValue;
	static int counter;
	static Boolean bool;
	static String myErrorArray="";
	static String mynullKeys="";
	static String expectedDatatype;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void tabs() throws IOException, EncryptedDocumentException, InvalidFormatException 
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//reading from excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Tabs");
		
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
				
		//started for loop
		for(int i=1; i<=rowCount;i++)
		{
			Row row = sh.getRow(i);
			//fetching the cell values
			TestType=row.getCell(0).getStringCellValue();
			Url=row.getCell(2).getStringCellValue();
			deviceType=row.getCell(3).getStringCellValue();
			os=row.getCell(4).getStringCellValue();
			key2test=row.getCell(5).getStringCellValue();
			Value2test=row.getCell(6).getStringCellValue();
			expectedDatatype=row.getCell(7).getStringCellValue();
			
			if(deviceType.equals("EMPTY"))
			{
				deviceType="";
			}
			if(deviceType.equals("NOTPASS"))
			{
				Tabs.NotPassDeviceType(i,Url);
				continue;
			}
			if(os.equals("EMPTY"))
			{
				os="";
			}
			if(os.equals("NOTPASS"))
			{
				Tabs.NotPassOS(i,Url);
				continue;
			}
		
			//posting the request
			BasicConfigurator.configure();
			Response resp1=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						headers(headers).
						params("deviceType",deviceType).
						params("os",os).
						when().
						get(Url);
			
			resp1.prettyPrint();
			resp1.then().assertThat().statusCode(200);//checking the statuscode=200
			
			if(TestType.equals("Positive"))//logic to test for positive TC
			{
				//declaration of Arraylist for null parameters keys
				ArrayList<String> nullParams = new ArrayList<String>();//storing the keys which have null values
				ArrayList<String> myDatatype = new ArrayList<String>();//storing the datatype of keys coming from response
				ArrayList<Integer> ErrorMyDatatype = new ArrayList<Integer>();//storing the array no. having unmatched datatype from expected
				
				String[] Keys = Value2test.split(",");//split function for separating the keys to test
				String[] KeysDatatype=expectedDatatype.split(",");//split function for separating the datatype to test
				//converting normal array to arrayList
				ArrayList<String> expectedDatatype = new ArrayList<String>(Arrays.asList(KeysDatatype));
				
				int sizeOfList = resp1.body().path(key2test+".size()");//taking the size of the items array
				String retrievDatatype="abc";//initialization of variable where we store datatype
				for(int k=0; k <sizeOfList; k++)
				{
					counter=1;//assigning flag=1 for not getting any garbage value
					mynullKeys="";//initialize to "" for using next iteration
					
					nullParams.removeAll(nullParams);//remove all elements for using next iteration[overriding issue]
					myDatatype.removeAll(myDatatype);//remove all elements for using next iteration[overriding issue]
					
					for (int j=0; j < Keys.length; j++)
					{
						myValue=String.valueOf(resp1.then().extract().path(key2test+"["+k+"]."+Keys[j]));//extracting the key value
						System.out.println(myValue);
						try//try to handle datatype whenever it is null 
						{
							retrievDatatype=resp1.jsonPath().get(key2test+"["+k+"]."+Keys[j]).getClass().getSimpleName();//extracting datatype
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
			   		if(bool==false)
					{
						ErrorMyDatatype.add(k+1);//ErrorMyDatatype array number
					}
				}
				for (Integer s : ErrorMyDatatype)//for each loop to traverse
				{
					myErrorArray += s + ","; //storing integer array elements to string variable
				}
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
			GenericMethod_VK.ExcelWriteBack(path1, "Tabs", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
		}
		GenericMethod_VK.write2Master(15, "Tabs", 9,path1);
		softAssert.assertAll();
		
	}
	public static void NotPassDeviceType(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("os",os).
			when().
			get(URL);
		
		resp1.prettyPrint();
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "Tabs", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	}
	public static void NotPassOS(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("deviceType",deviceType).
			when().
			get(URL);
		
		resp1.prettyPrint();
		resp1.then().assertThat().statusCode(200);
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//witing back to excel	
		GenericMethod_VK.ExcelWriteBack(path1, "Tabs", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	}
}

