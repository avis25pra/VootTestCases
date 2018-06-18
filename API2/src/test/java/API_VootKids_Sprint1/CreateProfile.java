package API_VootKids_Sprint1;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

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
import org.hamcrest.core.IsNull;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class CreateProfile extends GenericMethod_VK
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String uId;
	static String ks;
	static String deviceId;
	static String deviceBrand;
	static String dob;
	static String name;
	static String icon;
	static String color;
	static String pin;
	static String URL;
	static String expectedDatatype;
	static Boolean bool;
	static String mynullKeys;
	static int counter;
	static String myErrorArray="";
	static SoftAssert softAssert = new SoftAssert();
		@Test
		public void Create_Profiles() throws EncryptedDocumentException, InvalidFormatException, IOException
		{
			
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			//Points to be remember-
			//1. If the cell is AUTO , this means we taking key values from running SignUp api internally
			//2. If the cell is EMPTY then the value be <key>=<""> like this.
			//3. If the cell is NA then the TC is for not mandatory parameters.
			
			//Reading the excel sheet
			FileInputStream fis=new FileInputStream(path1);
			Workbook wb=WorkbookFactory.create(fis);
			//Excel sheet name Create
			Sheet sh=wb.getSheet("CreateProfile");
			//count the no. of rows ignoring the 1st row
			int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
			//started for loop
		    for(int i=1; i<=1;i++)
	        {
		    	GenericMethod_VK g=new GenericMethod_VK();
				Response resp=	g.SignUp();
		    	Row row = sh.getRow(i);
            	//fetching the cell values
		    	TestType=row.getCell(0).getStringCellValue();
		    	uId=row.getCell(2).getStringCellValue();
		    	key2test=row.getCell(12).getStringCellValue();
				Value2test=row.getCell(13).getStringCellValue();
				expectedDatatype=row.getCell(14).getStringCellValue();
				
		    	if(uId.equals("AUTO"))
		    	{
		    		uId=resp.then().extract().path("Uid");
		    	}
		    	else if(uId.equals("EMPTY"))
		    	{
		    		uId="";
		    	}
		    	else if(uId.equals("NA"))
		    	{
		    		CreateProfile.NotMandatory(i);
		    		continue;
		    	}
		    	ks=row.getCell(3).getStringCellValue();
		    	if(ks.equals("AUTO"))
		    	{
		    		ks=resp.then().extract().path("ks");
		    	}
		    	else if(ks.equals("EMPTY"))
		    	{
		    		ks="";
		    	}
		    	
		    	deviceId=row.getCell(4).getStringCellValue();
		    	if(deviceId.equals("EMPTY"))
		    	{
		    		deviceId="";
		    	}
		    	
		    	deviceBrand=row.getCell(5).getStringCellValue();
		    	if(deviceBrand.equals("EMPTY"))
		    	{
		    		deviceBrand="";
		    	}
		    	
		    	name=row.getCell(6).getStringCellValue();
		    	if(name.equals("EMPTY"))
		    	{
		    		name="";
		    	}
		    	
				dob=row.getCell(7).getStringCellValue();
				
				icon=row.getCell(8).getStringCellValue();
				if(icon.equals("EMPTY"))
		    	{
					icon="";
		    	}
				
				color=row.getCell(9).getStringCellValue();
				if(color.equals("EMPTY"))
		    	{
					color="";
		    	}
				
				pin=row.getCell(10).getStringCellValue();
				URL=row.getCell(11).getStringCellValue();
				
				String[] x = {"story"};
				String[] y = {"359118"};
				
				//setting the values for skills and favCharacters
				Preferences preferences=new Preferences();
				preferences.setSkills(x);
				preferences.setFavCharacters(y);
				
				//setting the values for icon and color
				buddy buddy=new buddy();
				buddy.setIcon(icon);
				buddy.setColor(color);
				
				//setting the values for name,dob and pin
				profile profile=new profile();
				profile.setName(name);
				if(dob.equals("NA"))
				{
					profile.setDob("2001-02-14");
				}
				if(dob.equals("ALPHACHAR"))
				{
					profile.setDob("gha324");
				}
				if(dob.equals("INVALID"))
				{
					profile.setDob("19992-2-234");
				}
				if(dob.equals("EMPTY"))
				{
					profile.setDob("");
				}
				if(dob.equals("null"))
				{
					profile.setDob("NULL");
				}
				if(dob.equals("SPECIALCHAR"))
				{
					profile.setDob("@@@");
				}
				
				//Calling functions for not passing elements
				if(uId.equals("NOTPASS")) 
		    	{
		    		CreateProfile.UidNotPassed(i);
		    		continue;
		    	}
				if(ks.equals("NOTPASS")) 
		    	{
		    		CreateProfile.KSNotPassed(i);
		    		continue;
		    	}
				if(deviceId.equals("NOTPASS")) 
		    	{
		    		CreateProfile.DeviceIdNotPassed(i);
		    		continue;
		    	}
				if(deviceBrand.equals("NOTPASS"))
		    	{
		    		CreateProfile.DeviceBrandNotPassed(i);
		    		continue;
		    	}
				if(name.equals("NOTPASS"))
		    	{
		    		CreateProfile.NameNotPassed(i);
		    		continue;
		    	}
				if(dob.equals("NOTPASS")) 
				{
					CreateProfile.DOBNotPassed(i);
					continue;
					
				}
				if(icon.equals("NOTPASS"))
		    	{
					CreateProfile.IconNotPassed(i);
					continue;
		    	}
				if(color.equals("NOTPASS")) 
				{
					CreateProfile.ColorNotPassed(i);
					continue;
				}
				if(pin.equals("EMPTY")) 
				{
					CreateProfile.PinIsEmpty(i);
					continue;
				}
				
				profile.setPin(pin);
				profile.setBuddy(buddy);
				profile.setPreferences(preferences);
				
				//setting the values for ks,deviceId and deviceBrand
				request request=new request();
				request.setParentKS(ks);
				request.setDeviceId(deviceId);
				request.setDeviceBrand(deviceBrand);
				request.setProfile(profile);
				
				
			//Posting the request	
			Response resp1=	RestAssured.
								given().
								body(request).
								queryParam("uId", uId).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				
				
				resp1.prettyPrint();//print the response in console
				resp1.then().assertThat().statusCode(200);//checking the statuscode=200
				
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
			   		for (String d : nullParams)
					{
						mynullKeys += d + ",";
					}
				}
				else if(TestType.equals("Negative")) //Logic for negative TC
				{
					myValue=resp1.then().extract().path(key2test);
					softAssert.assertEquals(Value2test,myValue);
				}
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	        }	
//		    GenericMethod_VK.write2Master(11, "CreateProfile", 16,path1); //calling generic method for writing in master sheet
		    softAssert.assertAll();
		}
		
	        
	//function for not passing icon 
	public static void IconNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								body(request1).
								queryParam("uId", uId).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);	
	}
	//function for passing non mandatory parameters
	public static void NotMandatory(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				
				Response resp1=	RestAssured.
								given().
								relaxedHTTPSValidation().
								queryParam("pin", 1223).
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);	
	}
	//function for not passing Uid
	public static void UidNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);			
	}
	//function for not passing ks token
	public static void KSNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId",uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}	
	//function for not passing deviceId
	public static void DeviceIdNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId",uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}
	//function for not passing devicebrand 
	public static void DeviceBrandNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId",uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}
	//function for not passing name 
	public static void NameNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId", uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}
	//function for not passing dob
	public static void DOBNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId", uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}
	//function for not passinf color
	public static void ColorNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setPin(pin);
				profile1.setDob("2001-02-14");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId", uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}
	//function for passing as empty pin
	public static void PinIsEmpty(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
				RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
				buddy buddy1=new buddy();
				buddy1.setIcon(icon);
				buddy1.setColor(color);
				
				profile profile1=new profile();
				profile1.setName(name);
				profile1.setDob("2001-02-14");
				profile1.setPin("");
				profile1.setBuddy(buddy1);
				
				request request1=new request();
				request1.setParentKS(ks);
				request1.setDeviceId(deviceId);
				request1.setDeviceBrand(deviceBrand);
				request1.setProfile(profile1);
				
				Response resp1=	RestAssured.
								given().
								queryParam("uId", uId).
								body(request1).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(URL);
				resp1.prettyPrint();
				
				myValue=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,myValue);
				//witing back to excel	
				GenericMethod_VK.ExcelWriteBack(path1, "CreateProfile", i, 15, resp1, 16, TestType, counter, bool, myValue, Value2test, 17, 18,mynullKeys,myErrorArray);
	}
}

	
	

		
		
		
		
	


