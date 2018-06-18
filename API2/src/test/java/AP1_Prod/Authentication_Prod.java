package AP1_Prod;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class Authentication_Prod  extends GenericMethod_Auth
{
	String str;
	String Value2test;
	@Parameters({"path","platformName"})
	@Test
	public  void Authentication_Traditional(String path,String platformName) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		GenericMethod_Auth.platformname=platformName;
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Path of the sheet
		
		
		//Reading the excel sheet
	//	FileInputStream fis=new FileInputStream(path1);
		FileInputStream fis=new FileInputStream(path);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Authentication
		Sheet sh=wb.getSheet("Authentication");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		System.out.println(rowCount);
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
			  	String platform=row.getCell(1).getStringCellValue();
		      	String pId=row.getCell(2).getStringCellValue();
				String username=row.getCell(4).getStringCellValue();
				if(username.equals("EMPTY"))
				{
					username="";
				}
				String password=row.getCell(5).getStringCellValue();
				if(password.equals("EMPTY"))
				{
					password="";
				}
				String URL=row.getCell(6).getStringCellValue();
				String key2Test=row.getCell(7).getStringCellValue();
				 Value2test=row.getCell(8).getStringCellValue();
	
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
				resp.prettyPrint();
				resp.then().assertThat().statusCode(200);
				
				str=resp.then().extract().path(key2Test);
				System.out.println(str);
				softAssert.assertEquals(Value2test,str);
				
				//writing into the excel sheet
				FileInputStream fis1=new FileInputStream(path);
				Workbook wb1=WorkbookFactory.create(fis1);
				
				Sheet sh1=wb1.getSheet("Authentication");
				Row row1=sh1.getRow(i);
				row1.createCell(9);
				Cell cel1=row1.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(10);
				Cell cel3=row3.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(str.equals(Value2test)) 
				{
					cel3.setCellValue("Pass");
				}
				else 
				{
					cel3.setCellValue("Fail");
				}
		
				FileOutputStream fos=new FileOutputStream(path);
				wb1.write(fos);
		
				fos.close();
				
				}
	    softAssert.assertAll();
	    	
	}

}

