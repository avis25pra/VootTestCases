package API_VootKids_Sprint1;

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

public class UpdateProfile extends GenericMethod_VK
{
	static String myValue;
	static String key2test;
	static String Value2test;
	static String TestType;
	static String uId;
	static String profileId;
	static String dob;
	static String name;
	static String icon;
	static String color;
	static String pin;
	static String Url;
	static int StatusCode;
	static String expectedDatatype;
	static Boolean bool;
	static String mynullKeys;
	static int counter;
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
			Sheet sh=wb.getSheet("UpdateProfile");
			//count the no. of rows ignoring the 1st row
			int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
	       //started for loop
			for(int i=1; i<=1;i++)
	        {
		    
		    	Row row = sh.getRow(i);
            	//fetching the cell values
		    	TestType=row.getCell(0).getStringCellValue();
		    	uId=row.getCell(2).getStringCellValue();
		    	//assign uid="" when uid is empty
		    	if(uId.equals("EMPTY")) 
		    	{
		    		uId="";
		    	}
		    	profileId=row.getCell(3).getStringCellValue();
		    	//assign childprofileId="" when it is empty
		    	if(profileId.equals("EMPTY")) 
		    	{
		    		profileId="";
		    	}
		    	name=row.getCell(4).getStringCellValue();
		    	//assign name="" when it is empty
		    	if(name.equals("EMPTY")) 
		    	{
		    		name="";
		    	}
		    	dob=row.getCell(5).getStringCellValue();
		    	icon=row.getCell(6).getStringCellValue();
		    	//assign icon="" when it is empty
		    	if(icon.equals("EMPTY")) 
		    	{
		    		icon="";
		    	}
		    	color=row.getCell(7).getStringCellValue();
		    	//assign color="" when it is empty
		    	if(color.equals("EMPTY")) 
		    	{
		    		color="";
		    	}
		    	pin=row.getCell(8).getStringCellValue();
		    	
		    	Url=row.getCell(9).getStringCellValue();
		    	key2test=row.getCell(10).getStringCellValue();
		    	Value2test=row.getCell(11).getStringCellValue();
		    	expectedDatatype=row.getCell(12).getStringCellValue();
		    	
		    	//calling function when pin is empty
		    	if(pin.equals("EMPTY")) 
		    	{
		    		UpdateProfile.PinIsEmpty(i);
		    		continue;
		    	}
				
		    	String[] x = {"story"};
				String[] y = {"359118"};
				
				//setting the values for skills and favCharacters
				Preferences preferences=new Preferences();
				preferences.setSkills(x);
				preferences.setFavCharacters(y);
		    	
		    	//assigning values of icon and color 
				buddy buddy=new buddy();
				buddy.setIcon(icon);
				buddy.setColor(color);
				
				//assigning the values of name,dob and pin
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
				if(dob.equals("NULL"))
				{
					profile.setDob("NULL");
				}
				if(dob.equals("SPECIALCHAR"))
				{
					profile.setDob("@@@");
				}
				
				profile.setPin(pin);
				profile.setBuddy(buddy);
				profile.setPreferences(preferences);
		
				request request=new request();
				request.setProfileId(profileId);
				request.setProfile(profile);
				
				
				//Calling function when uid not pass
				if(uId.equals("NOTPASS"))
				{
					UpdateProfile.UidNotPassed(i);
					continue;
				}
				//calling function when childprofileid is not passed
				if(profileId.equals("NOTPASS"))
				{
					UpdateProfile.notPassChildProfileId(i);
					continue;
				}
				
				//calling function when name is not passed
				if(name.equals("NOTPASS"))
				{
					UpdateProfile.NameNotPassed(i);
					continue;
				}
				//calling function when dob is not passed
				if(dob.equals("NOTPASS"))
				{
					UpdateProfile.DOBNotPassed(i);
					continue;
				}
				//calling function when icon is not passed
				if(icon.equals("NOTPASS"))
				{
					UpdateProfile.IconNotPassed(i);
					continue;
				}
				//calling function when color is not passed
				if(color.equals("NOTPASS"))
				{
					UpdateProfile.ColorNotPassed(i);
					continue;
				}
				
				//posting request
				Response resp1=	RestAssured.
								given().
								body(request).
								queryParam("uId", uId).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								post(Url);
				
				
				
				resp1.prettyPrint(); //printing the response
				resp1.then().assertThat().statusCode(200);//checking for statuscode=200
				
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
				GenericMethod_VK.ExcelWriteBack(path1, "UpdateProfile", i, 13, resp1, 14, TestType, counter, bool, myValue, Value2test, 15, 16,mynullKeys,myErrorArray);
	        }	
//			GenericMethod_VK.write2Master(10, "UpdateProfile",14,path1);
		    softAssert.assertAll();
	        }
		//fucntion for icon not passed
		public static void IconNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setProfileId(profileId);
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
									post(Url);
					
		}
		//function for uid not passed
		public static void UidNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setProfileId(profileId);
					request1.setProfile(profile1);
					
					Response resp1=	RestAssured.
									given().
									body(request1).
									relaxedHTTPSValidation().
									contentType(ContentType.JSON).
									accept(ContentType.JSON).
									headers(headers).
									when().
									post(Url);
					
		}
		//function for childprofileId not pass
		public static void notPassChildProfileId(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
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
									post(Url);
		}
		//function for name not pass
		public static void NameNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setProfileId(profileId);
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
									post(Url);
		}
		//function for dob not pass
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
					request1.setProfileId(profileId);
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
									post(Url);
					
		}
		//function for color not pass
		public static void ColorNotPassed(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setPin(pin);
					profile1.setDob("1992-02-14");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setProfileId(profileId);
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
									post(Url);
					
		}
		//fucntion for pin not passed
		public static void PinIsEmpty(int i) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
					RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));	
					buddy buddy1=new buddy();
					buddy1.setIcon(icon);
					buddy1.setColor(color);
					
					profile profile1=new profile();
					profile1.setName(name);
					profile1.setDob("1992-02-14");
					profile1.setPin("");
					profile1.setBuddy(buddy1);
					
					request request1=new request();
					request1.setProfileId(profileId);
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
									post(Url);
					resp1.prettyPrint();
		}
}

	
	

		
		
		
		
	


