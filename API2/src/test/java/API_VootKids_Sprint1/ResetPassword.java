package API_VootKids_Sprint1;

import static org.hamcrest.Matchers.is;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

public class ResetPassword  extends GenericMethod_VK
{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void reset_Password() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("ResetPassword");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	String email=row.getCell(2).getStringCellValue();
            	String oldPassword=row.getCell(3).getStringCellValue();
            	String newPassword=row.getCell(4).getStringCellValue();
            	String URL=row.getCell(5).getStringCellValue();
        		key2test=row.getCell(6).getStringCellValue();
        		Value2test=row.getCell(7).getStringCellValue();
        		if(email.equals("EMPTY"))
				{
        			email="";
				}
        		if(oldPassword.equals("EMPTY"))
				{
        			oldPassword="";
				}
        		if(newPassword.equals("AUTO"))
				{
        			newPassword=GenericMethod_VK.passwordGenerator();
				}
        		if(newPassword.equals("EMPTY"))
				{
        			newPassword="";
				}
        		
        		//function calling where keys are not passed
        		if(email.equals("NOTPASS"))
				{
            		ResetPassword.NotPassEmail(oldPassword, newPassword, i, URL);
            		continue;
				}
        		//calling function when old password is not passed
        		if(oldPassword.equals("NOTPASS"))
				{
            		ResetPassword.NotPassOldPassword(email, newPassword, i, URL);
            		continue;
				}
        		//calling function when new password is not passed
        		if(newPassword.equals("NOTPASS"))
				{
            		ResetPassword.NotPassNewPassword(email, oldPassword, i, URL);
            		continue;
				}
        		//when old password and new password are same
        		if(oldPassword.equals("SAMEPASS"))
        		{
        			oldPassword="ABCDEFG";
        			newPassword="ABCDEFG";
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
					queryParam("oldPassword",oldPassword).
					queryParam("newPassword",newPassword).
					when().
					post(URL);
				
				resp1.prettyPrint();//printing the response
				resp1.then().assertThat().statusCode(200);//checking for status code=200
				
				str=resp1.then().extract().path(key2test);
				softAssert.assertEquals(Value2test,str);
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("ResetPassword");
				Row row1=sh1.getRow(i);
				row1.createCell(8);
				Cell cel1=row1.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(9);
				Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(str.equals(Value2test) )//logic for writing pass/fail
				{
					cel3.setCellValue("Pass");
				}
				else 
				{
					cel3.setCellValue("Fail");
				}
				
				if(TestType.equals("Positive"))//writing new password in place of old password for next time execution 
				{
					resp1.then().assertThat().statusCode(200);
					Row row2=sh1.getRow(1);
					row2.createCell(3);
					Cell cel2=row2.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cel2.setCellType(CellType.STRING);
					cel2.setCellValue(newPassword);
				}
				
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
				
		}
	    GenericMethod_VK.write2Master(5,"ResetPassword",9,path1);
	    softAssert.assertAll();
	}
	//function for not passing email
	public static void NotPassEmail(String oldPassword,String newPassword,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("oldPassword",oldPassword).
			queryParam("newPassword",newPassword).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,8,9,"ResetPassword",path1);
	}
	//function for not passing old password
	public static void NotPassOldPassword(String email,String newPassword,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("newPassword",newPassword).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,8,9,"ResetPassword",path1);
	}
	//function for not passing new password
	public static void NotPassNewPassword(String email,String oldPassword,int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
			given().
			relaxedHTTPSValidation().
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			headers(headers).
			queryParam("email",email).
			queryParam("oldPassword",oldPassword).
			when().
			post(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,8,9,"ResetPassword",path1);
	}
	
}
		

