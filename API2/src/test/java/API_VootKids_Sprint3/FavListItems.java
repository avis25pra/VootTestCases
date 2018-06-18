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

public class FavListItems extends GenericMethod_VK 
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;	
	static String type;
	static String offSet;
	static String  URL;
	static Integer counter;
	static String singleVar;
	static Boolean bool;
	static String limit;
	static String mediaTypeId;
	static String profileId;
	static String uId;
	static String mediaIds;
	static String myErrorArray="";
	static String mynullKeys="";
	static String expectedDatatype;
	static SoftAssert softAssert = new SoftAssert();
	static Headers headers=GenericMethod_VK.headers();
	@Test
	public void favListItems() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path3);
		System.out.println(path3);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("favListItems");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		 for(int i=1; i<=1;i++)
		 {
			 Row row = sh.getRow(i);
			 TestType=row.getCell(0).getStringCellValue();	
			 URL=row.getCell(2).getStringCellValue();
			 profileId=row.getCell(3).getStringCellValue();
			 uId=row.getCell(4).getStringCellValue();
			 limit=row.getCell(5).getStringCellValue();
			 offSet=row.getCell(6).getStringCellValue();
			 type=row.getCell(7).getStringCellValue();
			 mediaTypeId=row.getCell(8).getStringCellValue();
			 mediaIds=row.getCell(9).getStringCellValue();
			 key2test=row.getCell(10).getStringCellValue();
			 Value2test=row.getCell(11).getStringCellValue();
			 expectedDatatype=row.getCell(12).getStringCellValue();
			 
			 if(profileId.equals("EMPTY"))//when profileId is empty initialize to ""
			 {
				 profileId="";
			 }
			 if(profileId.equals("NOTPASS"))
			 {
				 FavListItems.NotPassProfileId(i,URL);//calling function when profileId is not passed
				 continue;
			 }
			 if(uId.equals("EMPTY"))//when uid is empty initialize to ""
			 {
				 uId="";
			 }
			 if(uId.equals("NOTPASS"))
			 {
				 FavListItems.NotPassUId(i,URL);//calling function when uId is not passed
				 continue;
			 }
			 if(limit.equals("EMPTY"))//when limit is empty initialize to ""
			 {
				 limit="";
			 }
			 if(limit.equals("NOTPASS"))
			 {
				 FavListItems.NotPassLimit(i,URL);//calling function when limit is not passed
				 continue;
			 }
			 if(offSet.equals("EMPTY"))//when offset is empty initialize to ""
			 {
				 offSet="";
			 }
			 if(offSet.equals("NOTPASS"))
			 {
				 FavListItems.NotPassOffset(i,URL);//calling function when offSet is not passed
				 continue;
			 }
			 if(type.equals("EMPTY"))//when type is empty initialize to ""
			 {
				 type="";
			 }
			 if(type.equals("NOTPASS"))
			 {
				 FavListItems.NotPassType(i,URL);//calling function when type is not passed
				 continue;
			 }
			 if(mediaTypeId.equals("EMPTY"))//when mediatypeId is empty initialize to ""
			 {
				 mediaTypeId="";
			 }
			 if(mediaTypeId.equals("NOTPASS"))
			 {
				 FavListItems.NotPassMediaTypeId(i,URL);//calling function when mediaTypeId is not passed
				 continue;
			 }
			 if(mediaIds.equals("EMPTY"))//when mediaId is empty initialize to ""
			 {
				 mediaIds="";
			 }
			 if(mediaIds.equals("NOTPASS"))
			 {
				 FavListItems.NotPassMediaIds(i,URL);//calling function when mediaIds is not passed
				 continue;
			 }
			 if(mediaTypeId.equals("NA") && mediaIds.equals("NA"))
			 {
				 FavListItems.MandatoryParams(i,URL);//calling function for passing only mandatory params
				 continue;
			 }
			 if(mediaIds.equals("NA"))
			 {
				 mediaIds="1";//initializing to 1 because we mediaIds are not validating
			 }
			 if(mediaIds.equals("2Medias"))
			 {
				 FavListItems.TwoMediaIds(i,URL);//calling function when 2 mediaIds are passed
				 continue;
			 }
			 if(uId.equals("NA") && limit.equals("NA") && offSet.equals("NA") && type.equals("NA"))
			 {
				 FavListItems.NonMandatory(i,URL);
				 continue;
			 }
			 //posting request
			 Response resp1=RestAssured.
						given().
						param("profileId",profileId).
						param("uId",uId).
						param("limit",limit).
						param("offSet",offSet).
						param("type",type).
						param("mediaTypeId",mediaTypeId).
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						headers(headers).
						when().
						get(URL);
			 
			 resp1.prettyPrint();//printing the response on consol
			 resp1.then().assertThat().statusCode(200);//checking for status code=200
			 
			 if(TestType.equals("Positive"))
			 {
				FavListItems.PositiveScenarios(resp1,i);//calling functio when TC is positive
				continue;
			 }
			 //logic for negative scenarios
			 else if(TestType.equals("Negative")) 
			 {
				 myValue= resp1.jsonPath().get(key2test);//extracting the value
				 softAssert.assertEquals(Value2test,myValue);
			 }
			//writing back to excel
			 GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray );
					
		 }
		 GenericMethod_VK.write2Master(4, "favListItems", 13,path3);
		 softAssert.assertAll();
		 }

	//function for positive scenarios		 
	public static void PositiveScenarios(Response resp1 ,int i) throws EncryptedDocumentException, InvalidFormatException, IOException
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
	   		//writing back to excel
	   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray );
	}
	//function when only mandatory params are passed
	public static void MandatoryParams(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("limit",limit).
				param("offSet",offSet).
				param("type",type).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		FavListItems.PositiveScenarios(resp1, i);
	}
	//function when 2 mediaIds are passed
	public static void TwoMediaIds(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("limit",limit).
				param("offSet",offSet).
				param("type",type).
				param("mediaTypeId",mediaTypeId).
				param("mediaIds",562367,562366).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		FavListItems.PositiveScenarios(resp1, i);
	}
	//function when profileId is not passed
	public static void NotPassProfileId (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("uId",uId).
				param("limit",limit).
				param("offSet",offSet).
				param("type",type).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function when Uid is not passed
	public static void NotPassUId (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("limit",limit).
				param("offSet",offSet).
				param("type",type).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function when offset is not passed
	public static void NotPassOffset (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("limit",limit).
				param("type",type).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function when Type is not passed
	public static void NotPassType (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("limit",limit).
				param("offSet",offSet).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function when limit is not passed
	public static void NotPassLimit (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("offSet",offSet).
				param("type",type).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function when MediaTypeId is not passed
	public static void NotPassMediaTypeId (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		//posting request
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("limit",limit).
				param("offSet",offSet).
				param("type",type).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function when mediaIds are not passed
	public static void NotPassMediaIds (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				param("profileId",profileId).
				param("uId",uId).
				param("limit",limit).
				param("offSet",offSet).
				param("type",type).
				param("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
	//function for passing non mandatory params
	public static void NonMandatory (int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				param("mediaTypeId",mediaTypeId).
				param("mediaIds",mediaIds).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				get(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for statusCode=200
		myValue=resp1.then().extract().path(key2test);//extracting the value
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
   		GenericMethod_VK.ExcelWriteBack(path3, "favListItems", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray ); 
	}
}
