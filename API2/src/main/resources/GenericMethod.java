package AP1_Prod;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class GenericMethod {
	
	
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
		
		
		resp.then().assertThat().statusCode(200);
		String act = resp.jsonPath().get("LoginRadius.Email[0].Value");
		  act.equals(emailid);

		
		return resp;
		
	}
	public String getSaltString() {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { 
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	public void writedata(String Sheet,int rownum,int cellnum,String str) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\eclipse-workspace\\API2\\testdataV1.xls";

		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);
		
		Sheet sh1=wb1.getSheet(Sheet);
		Row row2=sh1.getRow(rownum);
		row2.createCell(cellnum);
		Cell cel2=	row2.getCell(cellnum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellValue(str);
		
	}
	public  Response Authentication() throws EncryptedDocumentException, InvalidFormatException, IOException {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Sheet1");
		Row row=sh.getRow(3);
		String platform=row.getCell(0).getStringCellValue();
		String pId=row.getCell(1).getStringCellValue();
		String username=row.getCell(2).getStringCellValue();
		String password=row.getCell(3).getStringCellValue();
		String URL=row.getCell(4).getStringCellValue();
	
		
		BasicConfigurator.configure();
		Response resp=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						queryParam("platform",platform).
						queryParam("pId",pId).
						queryParam("username",username).
						queryParam("password",password).
						when().
						post(URL);
//		String str=	resp.asString();
		
		resp.then().assertThat().statusCode(200);
		
		resp.prettyPrint();
		
		String act=resp. 
				then().
				extract().path("Email[0].Value");
		assertEquals(username,act);
		
		return resp;
		
	}
	public static void addAttachment(Multipart multipart, String filename) 
	{
		try {
	    DataSource source = new FileDataSource(filename);
	    BodyPart messageBodyPart = new MimeBodyPart();        
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(filename);
	    multipart.addBodyPart(messageBodyPart);
		}
		catch(MessagingException me) {
			me.printStackTrace();
		}
	}
	
	}



