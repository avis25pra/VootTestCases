package AP1_Prod;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


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

public class ChangePassword_Prod extends GenericMethod_Auth
{
	String str;
	String Value2test;
	@Test
	public void changePassword () throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		GenericMethod_Auth g=new GenericMethod_Auth();
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Password Change
		Sheet sh=wb.getSheet("PasswordChange");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		

	    //started for loop
	    for(int i=1; i<=rowCount;i++)
	    {
	    	//calling generic class method createapi()
	    		Response resp=g.createapi();
	    	
	    		Row row=sh.getRow(i);
	    		String platform=row.getCell(1).getStringCellValue();
	    		String pId=row.getCell(2).getStringCellValue();
	    		String Uid=row.getCell(4).getStringCellValue();
	    		if(Uid.equals("NA"))
	    		{
	    			Uid = resp.jsonPath().get("LoginRadius.Uid");
	    		}
	    		else if(Uid.equals("EMPTY"))
	    		{
	    			Uid="";
	    		}
	    		String old_password=row.getCell(5).getStringCellValue();
	    		if(old_password.equals("EMPTY"))
	    		{
	    			old_password ="";
	    		}
	    		String new_password=row.getCell(6).getStringCellValue();
	    		if(new_password.equals("EMPTY"))
	    		{
	    			new_password ="";
	    		}
	    		String URL_changePassword =row.getCell(7).getStringCellValue();
	    		String TestType =row.getCell(0).getStringCellValue();
	    		String key2Test=row.getCell(8).getStringCellValue();
	    		 Value2test=row.getCell(9).getStringCellValue();
		
	    		Response resp1=	RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).accept(ContentType.JSON).
				queryParam("platform",platform).
				queryParam("pId",pId).
				queryParam("old_password",old_password).
				queryParam("new_password",new_password).
				queryParam("account_id",Uid).
				when().
				post(URL_changePassword);
				
	    		resp1.then().assertThat().statusCode(200);
	    		resp1.prettyPrint();
	    		
	    		if(TestType.equals("Positive"))
	    		{
	    			Boolean isPosted=resp1.then().extract().path(key2Test);
	    			str=String.valueOf(isPosted);
	    			softAssert.assertEquals(Value2test,str);
	    		}
	    		else
	    		{
	    			str=resp1.then().extract().path(key2Test);
	    			softAssert.assertEquals(Value2test,str);
	    		}
				
				
				FileInputStream fis2=new FileInputStream(path1);
				Workbook wb2=WorkbookFactory.create(fis2);
			
				Sheet sh2=wb2.getSheet("PasswordChange");
					

					
					Row row2=sh2.getRow(i);
					row2.createCell(10);
					Cell cel2=	row2.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					//cel2.setCellType(CellType.STRING);
					cel2.setCellValue(resp1.asString());
				
					Row row3=sh2.getRow(i);
					row3.createCell(11);
					Cell cel3=row3.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if(str.equals(Value2test)) {
					cel3.setCellValue("Pass");
					}
					else {
						cel3.setCellValue("Fail");
					}
				
					FileOutputStream fos=new FileOutputStream(path1);
					wb2.write(fos);
				
					fos.close();
					
					
					
					
				}
	   
	    softAssert.assertAll();
	    
	    	
		
	
		
	    }
	}
		

