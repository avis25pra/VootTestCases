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

public class SetPassword_Prod extends GenericMethod_Auth
{
	String str;
	String Value2test;
	
	@Test
	public void SetPassword () throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("SetPassword");
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		for(int i=1; i<=rowCount;i++)
		{
			Row row=sh.getRow(i);
			String TestType=row.getCell(0).getStringCellValue();
			String platform=row.getCell(1).getStringCellValue();
			String pId=row.getCell(2).getStringCellValue();
			String email=row.getCell(4).getStringCellValue();
			if(email.equals("EMPTY"))
			{
				email="";
			}
			String key2Test=row.getCell(6).getStringCellValue();
			Value2test=row.getCell(7).getStringCellValue();
			String URL_setPassword =row.getCell(5).getStringCellValue();
		
			Response resp1=	RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).accept(ContentType.JSON).
				queryParam("platform",platform).
				queryParam("pId",pId).
				queryParam("email",email).
				when().
				post(URL_setPassword);
				resp1.then().assertThat().statusCode(200);
				
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
				
				resp1.prettyPrint();
				
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
				
				Sheet sh1=wb1.getSheet("SetPassword");
				Row row1=sh1.getRow(i);
				row1.createCell(8);
				Cell cel1=	row1.getCell(8, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
				
				Row row3=sh1.getRow(i);
				row3.createCell(9);
				Cell cel3=row3.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(str.equals(Value2test)) {
					
				cel3.setCellValue("Pass");
				}
				else {
					
					cel3.setCellValue("Fail");
				}
			
				
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
				
				fos.close();
				
				
		}
		softAssert.assertAll();
		
				
		
	}
}
