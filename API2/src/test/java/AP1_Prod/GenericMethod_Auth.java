package AP1_Prod;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Parameters;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;



public class GenericMethod_Auth
{
	String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
	public static String platformname="";
	public  Response createapi() throws EncryptedDocumentException, InvalidFormatException, IOException  {
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
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Create1");
		Row row=sh.getRow(1);
		String platform=row.getCell(1).getStringCellValue();
		String pId=row.getCell(2).getStringCellValue();
		String password=row.getCell(5).getStringCellValue();
		String firstname=row.getCell(6).getStringCellValue();
		String lastname=row.getCell(7).getStringCellValue();
		String URL=row.getCell(8).getStringCellValue();
	
		
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
		
		
		resp.then().assertThat().statusCode(200);
		String act = resp.jsonPath().get("LoginRadius.Email[0].Value");
		act.equals(emailid);

		
		return resp;
		
	}
	public  Response new_createapi() throws EncryptedDocumentException, InvalidFormatException, IOException  
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890.";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 10)
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    
	    String saltStr = salt.toString();
	    String emailid= saltStr+"@gmail.com";
		
	    FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Create1");
		Row row=sh.getRow(1);
		String platform=row.getCell(1).getStringCellValue();
		String pId=row.getCell(2).getStringCellValue();
		String password=row.getCell(5).getStringCellValue();
		String firstname=row.getCell(6).getStringCellValue();
		String lastname=row.getCell(7).getStringCellValue();
		String URL=row.getCell(8).getStringCellValue();
	
		
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
		
		resp.then().assertThat().statusCode(200);
		String act = resp.jsonPath().get("LoginRadius.Email[0].Value");
		act.equals(emailid);
		
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);
		
		Sheet sh1=wb1.getSheet("Create1");
		Row row1=sh1.getRow(1);
		row1.createCell(11);
		Cell cel1=	row1.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp.asString());
		
		Row row2=sh1.getRow(1);
		row2.createCell(12);
		Cell cel2=	row2.getCell(12, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.NUMERIC);
		cel2.setCellValue("Pass");
		
		Row row3=sh1.getRow(1);
		row3.createCell(4);
		Cell cel3=	row3.getCell(4, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel3.setCellType(CellType.STRING);
		cel3.setCellValue(act);
		
		Row row4=sh1.getRow(1);
		row4.createCell(10);
		Cell cel4=	row4.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel4.setCellType(CellType.STRING);
		cel4.setCellValue(act);
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);
		
		fos.close();
		
		return resp;
		
	}
	
	public String getSaltString() 
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { 
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	public static void addAttachment(Multipart multipart, String filename) 
		{
			try 
			{
				DataSource source = new FileDataSource(filename);
				BodyPart messageBodyPart = new MimeBodyPart();        
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
			}
			catch(MessagingException me) 
			{
				me.printStackTrace();
			}
		}
	
}



