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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class CheckMail_Prod extends GenericMethod_Auth 

{
		String str;
		String Value2test;
		@Parameters({"path","platformName"})
		@Test
		public void checkemail_Traditional(String path, String platformName) throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
			SoftAssert softAssert = new SoftAssert();
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
			GenericMethod_Auth.platformname=platformName;
			//FileInputStream fis=new FileInputStream(path1);
			FileInputStream fis=new FileInputStream(path);
			Workbook wb=WorkbookFactory.create(fis);
			//Excel sheet name Password Change
			Sheet sh=wb.getSheet("CheckMail");
			//count the no. of rows ignoring the 1st row
			int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
			//started for loop
			for(int i=1; i<=1;i++)
			{
			
				Row row=sh.getRow(i);
				String platform=row.getCell(1).getStringCellValue();
				String pId=row.getCell(2).getStringCellValue();
				String email=row.getCell(4).getStringCellValue();
				if(email.equals("EMPTY"))
				{
					email = "";
				}
				
				String URL_checkEmail =row.getCell(5).getStringCellValue();
				String TestType =row.getCell(0).getStringCellValue();
				String key2Test=row.getCell(6).getStringCellValue();
				Value2test=row.getCell(7).getStringCellValue();
		
				Response resp1=	RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).accept(ContentType.JSON).
				queryParam("platform",platform).
				queryParam("pId",pId).
				queryParam("email",email).
				when().
				post(URL_checkEmail);
				
	    		resp1.then().assertThat().statusCode(200);
	    		resp1.prettyPrint();
	    		
	    		if(TestType.equals("Positive") || email.equals("Invalid"))
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
	    		
				
				FileInputStream fis2=new FileInputStream(path);
				Workbook wb2=WorkbookFactory.create(fis2);
			
					Sheet sh2=wb2.getSheet("CheckMail");

					Row row2=sh2.getRow(i);
					row2.createCell(8);
					Cell cel2=	row2.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					//cel2.setCellType(CellType.STRING);
					cel2.setCellValue(resp1.asString());
				
					Row row3=sh2.getRow(i);
					row3.createCell(9);
					Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if(str.equals(Value2test)) {
					cel3.setCellValue("Pass");
					}
					else {
						cel3.setCellValue("Fail");
					}
				
					FileOutputStream fos=new FileOutputStream(path);
					wb2.write(fos);
				
					fos.close();
					
					
					
					
				}
			softAssert.assertAll();
			
				
			}
		
	}


