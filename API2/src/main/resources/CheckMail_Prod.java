package AP1_Prod;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class CheckMail_Prod extends GenericMethod {
	@Test
	public void checkemail_V1() throws EncryptedDocumentException, InvalidFormatException, IOException {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//using random email generator
		GenericMethod g=new GenericMethod();
			Response resp=	g.createapi();
			String email_value = resp.jsonPath().get("LoginRadius.Email[0].Value");	
				
				
				String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
				FileInputStream fis=new FileInputStream(path1);
				Workbook wb=WorkbookFactory.create(fis);
				Sheet sh=wb.getSheet("Sheet1");
				Row row=sh.getRow(8);
				String platform=row.getCell(0).getStringCellValue();
				String pId=row.getCell(1).getStringCellValue();
				String URL_checkemail=row.getCell(4).getStringCellValue();
				
				resp.prettyPrint();
				Response resp1=	RestAssured.
								given().
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								queryParam("platform",platform).
								queryParam("pId",pId).
								queryParam("email",email_value).
								when().
								post(URL_checkemail);
				resp1.then().assertThat().statusCode(200);
				
				resp1.prettyPrint();
				
				Boolean isExist  =	resp1. 
									then().
									extract().
									path("isExist");
				String str = String.valueOf(isExist);
				assertEquals(str,"true","isExist value is not as expected");
				String output=resp1.asString();
				
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
				
				Sheet sh1=wb1.getSheet("Sheet1");
				Row row2=sh1.getRow(8);
				row2.createCell(5);
				Cell cel2=	row2.getCell(5, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//cel2.setCellType(CellType.STRING);
				cel2.setCellValue(output);
				
				Row row3=sh1.getRow(8);
				row3.createCell(6);
				Cell cel3=	row3.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//cel3.setCellType(CellType.NUMERIC);
				cel3.setCellValue(resp1.statusCode());
				
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
				
				fos.close();
				
				

		
	}

}
