package AP1_Prod;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Random;

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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class Create_Prod {
	@Test
	public void createapi() throws EncryptedDocumentException, InvalidFormatException, IOException  {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		 String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
	        StringBuilder salt = new StringBuilder();
	        Random rnd = new Random();
	        while (salt.length() < 10) { 
	            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	            salt.append(SALTCHARS.charAt(index));
	        }
	        String saltStr = salt.toString();
	       String emailid= saltStr+"@gmail.com";
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Sheet1");
		Row row=sh.getRow(4);
		String platform=row.getCell(0).getStringCellValue();
		String pId=row.getCell(1).getStringCellValue();
		String password=row.getCell(3).getStringCellValue();
		String firstname=row.getCell(3).getStringCellValue();
		String lastname=row.getCell(3).getStringCellValue();
		String URL=row.getCell(4).getStringCellValue();
	
		
		BasicConfigurator.configure();
		Response resp=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						queryParam("platform",platform).
						queryParam("pId",pId).
						queryParam("emailid",emailid).
						queryParam("password",password).
						queryParam("firstname",firstname).
						queryParam("lastname",lastname).
						when().
						post(URL);
		
		resp.prettyPrint();
		String str=	resp.asString();
		resp.then().assertThat().statusCode(200);
		String act = resp.jsonPath().get("LoginRadius.Email[0].Value");
		  act.equals(emailid);
		resp.prettyPrint();
		
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);
		
		Sheet sh1=wb1.getSheet("Sheet1");
		Row row1=sh1.getRow(4);
		row1.createCell(5);
		Cell cel1=	row1.getCell(5, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(str);
		
		Row row2=sh1.getRow(4);
		row2.createCell(6);
		Cell cel2=	row1.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.NUMERIC);
		cel2.setCellValue(resp.statusCode());
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);
		
		fos.close();

		
		
		
	}
}
