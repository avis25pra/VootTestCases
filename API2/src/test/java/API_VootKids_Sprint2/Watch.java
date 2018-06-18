package API_VootKids_Sprint2;

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

public class Watch extends GenericMethod_VK {
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;
	static Integer counter;
	static String singleVar;
	static String URL;
	static String limit;
	static String offSet;
	static Boolean bool;
	static String expectedDatatype;
	static String myErrorArray="";
	static String mynullKeys="";
	static Headers headers=GenericMethod_VK.headers();
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void watch() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path2);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Watch");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		for(int i=1; i<=rowCount;i++)
	        {
				 //reading values from excel
				 Row row = sh.getRow(i);
	             TestType=row.getCell(0).getStringCellValue();	
	             URL=row.getCell(2).getStringCellValue();
	             limit=row.getCell(3).getStringCellValue();
	             offSet=row.getCell(4).getStringCellValue();
	             key2test=row.getCell(5).getStringCellValue();
	             Value2test=row.getCell(6).getStringCellValue();
	             expectedDatatype=row.getCell(7).getStringCellValue();
	             
	             if(limit.equals("EMPTY"))//when limit is empty initialize it to ""
	             {
	            	 limit="";
            	 }
	             if(limit.equals("NOTPASS"))
	             {
	            	 Watch.NotPasslimit(i, URL);//calling function when limit is not passed
	            	 continue;
	             }
	             if(offSet.equals("EMPTY"))//when offset is empty initialize it to ""
	             {
	            	 offSet="";
	             }
	             if(offSet.equals("NOTPASS"))
	             {
	            	 Watch.NotPassoffset(i, URL);//calling function when offset is not passed
	            	 continue;
	             }	

	             //posting request
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
             
	             resp1.prettyPrint();//printing the response in console
	             resp1.then().assertThat().statusCode(200);//checking the statusCode=200
	             
	             if(TestType.equals("Positive") && i<=7) 
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
	             }
	             else if(TestType.equals("Positive") && i>7)
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
	             //logic for negative scenarios
	             else if(TestType.equals("Negative")) 
	 			{
	 				myValue= resp1.jsonPath().get(key2test);
	 				softAssert.assertEquals(Value2test,myValue);
	 			}
	           //writing back to excel
	             GenericMethod_VK.ExcelWriteBack(path2, "Watch", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	        }
		GenericMethod_VK.write2Master(3, "Watch", 9,path2);
		softAssert.assertAll();
			
	}
	//function for not passing offset
	public static void NotPassoffset(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
							given().
							parameter("limit",limit).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);

		resp1.prettyPrint();//printing the response in console
		resp1.then().assertThat().statusCode(200);//checking the statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the values
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
        GenericMethod_VK.ExcelWriteBack(path2, "Watch", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	}
	//functon for not passing limit
	public static void NotPasslimit(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		BasicConfigurator.configure();
		 Response resp1=	RestAssured.
							given().
							parameter("offset",offSet).
							relaxedHTTPSValidation().
							contentType(ContentType.JSON).
							accept(ContentType.JSON).
							headers(headers).
							when().
							get(URL);

		resp1.prettyPrint();//printing response in console
		resp1.then().assertThat().statusCode(200);//checking the statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
        GenericMethod_VK.ExcelWriteBack(path2, "Watch", i, 8, resp1, 9, TestType, counter, bool, myValue, Value2test, 10, 11,mynullKeys,myErrorArray );
	}
	
}

			
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
		
	        
		
	


