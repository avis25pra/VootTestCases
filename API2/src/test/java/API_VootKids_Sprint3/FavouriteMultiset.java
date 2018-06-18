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

public class FavouriteMultiset extends GenericMethod_VK 
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String mediaTypeId;
	static String uId;
	static String profileId;
	static String isFavourite;
	static String  URL;
	static Integer counter;
	static String mediaIds;
	static String isfav="";
	static String mynullKeys="";
	static String expectedDatatype;
	static String myErrorArray="";
	static Boolean bool;
	static SoftAssert softAssert = new SoftAssert();
	static Headers headers=GenericMethod_VK.headers();
	@Test
	public void isFavMultiset() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		BasicConfigurator.configure();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path3);
		System.out.println(path3);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("favMultiset");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		for(int i=1; i<=rowCount;i++)
        {
			Row row = sh.getRow(i);
		 	TestType=row.getCell(0).getStringCellValue();
		 	URL=row.getCell(2).getStringCellValue();
		 	profileId=row.getCell(3).getStringCellValue();
		 	uId=row.getCell(4).getStringCellValue();
		 	mediaIds=row.getCell(5).getStringCellValue();
		 	System.out.println(mediaIds);
		 	isFavourite=row.getCell(6).getStringCellValue();
		 	mediaTypeId=row.getCell(7).getStringCellValue();
		 	key2test=row.getCell(8).getStringCellValue();
		 	Value2test=row.getCell(9).getStringCellValue();
		 	expectedDatatype=row.getCell(10).getStringCellValue();
		 	
		 	if(mediaIds.equals("EMPTY"))//when mediaid is empty initialize it to ""
		 	{
		 		mediaIds="";
		 	}
		 	if(mediaIds.equals("NOTPASS"))
		 	{
		 		FavouriteMultiset.NotPassMediaId(i, URL);//calling function when mediaids are not passed
		 		continue;
		 	}
		 	if(uId.equals("EMPTY"))//when uid is not passed initialize it to ""
		 	{
		 		uId="";
		 	}
		 	if(uId.equals("NOTPASS"))
		 	{
		 		FavouriteMultiset.NotPassuId(i, URL); //calling function when uid is not passed
		 		continue;
		 	}
		 	if(profileId.equals("EMPTY"))//when profileid is empty initialize it to ""
		 	{
			 	profileId="";
		 	}
		 	if(profileId.equals("NOTPASS"))
		 	{
		 		FavouriteMultiset.NotPassprofileId(i, URL);//calling function when profileid is not passed
		 		continue;
		 	}
		 	if(mediaTypeId.equals("EMPTY"))//when mediatypeid is empty initialize it to ""
		 	{
		 		mediaTypeId="";
		 	}
		 	if(mediaTypeId.equals("NOTPASS"))
		 	{
		 		FavouriteMultiset.NotPassMediaTypeId(i, URL);//calling function when mediatypeis is not passed
		 		continue;
		 	}
		 	if(isFavourite.equals("EMPTY"))//when isfavourite is empty initialize it to ""
		 	{
		 		isFavourite="";
		 	}
		 	if(isFavourite.equals("NOTPASS"))
		 	{
		 		FavouriteMultiset.NotPassisFavourite(i, URL);//calling function when isfavourite is not passed
		 		continue;
		 	}
		 	//posting request
		 	Response resp1=	RestAssured.
					given().
					queryParam("mediaIds[]",mediaIds).
					queryParam("isFavourite",isFavourite).
					queryParam("uId",uId).
					queryParam("profileId",profileId).
					queryParam("mediaTypeId",mediaTypeId).
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					headers(headers).
					when().
					post(URL);
		 	
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
				String retrievDatatype="abc";//initialization of variable where we store datatype of mediaId
				
				for (int j=0;j<Keys.length;j++) 
				{
					counter=1;//assigning counter=1 for not getting any garbage value
					if(Keys[j].equals("mediaIds"))
					{
						myValue=String.valueOf(resp1.jsonPath().get(key2test+"."+Keys[j]+"[0]"));//extracting key value
						softAssert.assertEquals(mediaIds,myValue);
						retrievDatatype=resp1.jsonPath().get(key2test+"."+Keys[j]+"[0]").getClass().getSimpleName();//extracting mediaId datatype
					}
					if(Keys[j].equals("isFavourite"))
					{
						isfav=String.valueOf(resp1.jsonPath().get(key2test+"."+Keys[j]));//extracting key value
						softAssert.assertEquals(isfav,isFavourite);
						retrievDatatype=resp1.jsonPath().get(key2test+"."+Keys[j]).getClass().getSimpleName();//extracting fav datatype
					}
					myDatatype.add(retrievDatatype);//append the elements into arraylist
					if(myValue.equals("null")||isfav.equals("null")) 
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
			 GenericMethod_VK.ExcelWriteBack(path3, "favMultiset", i, 11, resp1, 12, TestType, counter, bool, myValue, Value2test, 13, 14,mynullKeys,myErrorArray );
		
        }
	 	GenericMethod_VK.write2Master(6, "favMultiset", 12,path3);
	 	softAssert.assertAll();
        
		
	}
	//function when mediaid is not passed
	public static void NotPassMediaId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				queryParam("uId",uId).
				queryParam("isFavourite",isFavourite).
				queryParam("profileId",profileId).
				queryParam("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		//writing back to excel
		GenericMethod_VK.ExcelWriteBack(path3, "favMultiset", i, 11, resp1, 12, TestType, counter, bool, myValue, Value2test, 13, 14,mynullKeys,myErrorArray );
	}
	//function when uid is not passed
	public static void NotPassuId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				queryParam("mediaIds[]",mediaIds).
				queryParam("profileId",profileId).
				queryParam("mediaTypeId",mediaTypeId).
				queryParam("isFavourite",isFavourite).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		GenericMethod_VK.ExcelWriteBack(path3, "favMultiset", i, 11, resp1, 12, TestType, counter, bool, myValue, Value2test, 13, 14,mynullKeys,myErrorArray );
	}
	//function when profileId is not passed
	public static void NotPassprofileId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				queryParam("mediaIds[]",mediaIds).
				queryParam("uId",uId).
				queryParam("mediaTypeId",mediaTypeId).
				queryParam("isFavourite",isFavourite).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		GenericMethod_VK.ExcelWriteBack(path3, "favMultiset", i, 11, resp1, 12, TestType, counter, bool, myValue, Value2test, 13, 14,mynullKeys,myErrorArray );
	}
	//function when isfavourite is not passed
	public static void NotPassisFavourite(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				queryParam("mediaIds[]",mediaIds).
				queryParam("uId",uId).
				queryParam("profileId",profileId).
				queryParam("mediaTypeId",mediaTypeId).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		GenericMethod_VK.ExcelWriteBack(path3, "favMultiset", i, 11, resp1, 12, TestType, counter, bool, myValue, Value2test, 13, 14,mynullKeys,myErrorArray );
	}
	//function when mediatypeid is not passed
	public static void NotPassMediaTypeId(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Response resp1=	RestAssured.
				given().
				queryParam("mediaIds[]",mediaIds).
				queryParam("uId",uId).
				queryParam("profileId",profileId).
				queryParam("isFavourite",isFavourite).
				relaxedHTTPSValidation().
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				headers(headers).
				when().
				post(URL);
		
		resp1.then().assertThat().statusCode(200);//checking for status code=200
		myValue=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,myValue);
		
		//writing back to excel
		GenericMethod_VK.ExcelWriteBack(path3, "favMultiset", i, 11, resp1, 12, TestType, counter, bool, myValue, Value2test, 13, 14,mynullKeys,myErrorArray );
	}

}
