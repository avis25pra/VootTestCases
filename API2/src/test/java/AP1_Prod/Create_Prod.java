package AP1_Prod;



import static org.testng.Assert.assertEquals;

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
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class Create_Prod extends GenericMethod_Auth
{
	String str;
	String Value2test;
	@Test
	public void create_new() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		GenericMethod_Auth g=new GenericMethod_Auth();
		Response resp=	g.new_createapi();
		resp.prettyPrint();
		
		
		
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("Create1");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		System.out.println(rowCount);
	    //started for loop
	    for(int i=2; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
			  	String platform=row.getCell(1).getStringCellValue();
		      	String pId=row.getCell(2).getStringCellValue();
				String email=row.getCell(4).getStringCellValue();
				//when email is empty
				if(email.equals("EMPTY"))
				{
					email="";
				}
				if(email.equals("NA"))
				{
					GenericMethod_Auth g1= new GenericMethod_Auth();
					email=g1.getSaltString()+"@gmail.com";
				}
				String password=row.getCell(5).getStringCellValue();
				//when password is empty
				if(password.equals("EMPTY"))
				{
					password="";
				}
				String firstname=row.getCell(6).getStringCellValue();
				//when firstname is empty
				if(firstname.equals("EMPTY"))
				{
					firstname="";
				}
				String lastname=row.getCell(7).getStringCellValue();
				//when lastname is empty
				if(lastname.equals("EMPTY"))
				{
					lastname="";
				}
				String URL=row.getCell(8).getStringCellValue();
				String key2Test=row.getCell(9).getStringCellValue();
				
				Value2test=row.getCell(10).getStringCellValue();
	
				BasicConfigurator.configure();
				Response resp1=	RestAssured.
					given().
					relaxedHTTPSValidation().
					contentType(ContentType.JSON).
					accept(ContentType.JSON).
					queryParam("platform",platform).
					queryParam("pId",pId).
					queryParam("emailid",email).
					queryParam("password",password).
					queryParam("firstname",firstname).
					queryParam("lastname",lastname).
					when().
					post(URL);
				
				resp1.then().assertThat().statusCode(200);
				
				//printing the response
				resp1.prettyPrint();
				
				str=resp1.then().extract().path(key2Test);
				softAssert.assertEquals(str,Value2test);
				
				//code to write the output and status code in excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("Create1");
				Row row1=sh1.getRow(i);
				row1.createCell(11);
				Cell cel1=row1.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(12);
				Cell cel3=row3.getCell(12, MissingCellPolicy.CREATE_NULL_AS_BLANK);
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

