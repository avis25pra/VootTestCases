package AP1_Prod;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;

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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.response.Response;

public class KUserLogin_Prod extends GenericMethod_Auth
{
	String str;
	String Value2test;
	@Parameters({"path","platformName"})
	@Test
	public void kuserlogin_SocialLogin(String path,String platformName) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		GenericMethod_Auth.platformname=platformName;
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//reading data
		
//		String path= "C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\Pwa.xls";
		FileInputStream fis=new FileInputStream(path);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("KUserLogin");
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		for(int i=1; i<=rowCount;i++)
		{
			Row row=sh.getRow(i);
			String TestType=row.getCell(0).getStringCellValue();	
			String platform=row.getCell(1).getStringCellValue();
			String pId=row.getCell(2).getStringCellValue();
			String email =row.getCell(5).getStringCellValue();
			if(email.equals("EMPTY")) 
			{
				email="";
			}
			String UID =row.getCell(4).getStringCellValue();
			if(UID.equals("EMPTY")) 
			{
				UID="";
			}
			String URL =row.getCell(6).getStringCellValue();
			String key2Test =row.getCell(7).getStringCellValue();
			Value2test =row.getCell(8).getStringCellValue();
			
			BasicConfigurator.configure();
			Response resp=	RestAssured.
							given().
							queryParam("platform",platform).
							queryParam("pId",pId).
							queryParam("email",email).
							queryParam("UID",UID).
							when().
							post(URL);
			
			resp.then().assertThat().statusCode(200);
			FileInputStream fis1=new FileInputStream(path);
			Workbook wb1=WorkbookFactory.create(fis1);
			
			Sheet sh1=wb1.getSheet("KUserLogin");
			
			if(TestType.equals("Positive"))
			{
				String[] Keys = key2Test.split(",");
				for (int j=0; j < Keys.length; j++)
				{
					resp.then().body("$", hasKey(Keys[j]));
					resp.then().body(Keys[j], is(IsNull.notNullValue()));
					
				}
			}
			else if(TestType.equals("Negative"))
			{
				str = resp.jsonPath().get(key2Test);
				softAssert.assertEquals(Value2test,str);
			}
			resp.prettyPrint();
			Row row1=sh1.getRow(i);
			row1.createCell(9);
			Cell cel1=	row1.getCell(9, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(resp.asString());
			
			Row row3=sh1.getRow(i);
			row3.createCell(10);
			Cell cel3=row3.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if(TestType.equals("Negative"))
			{	
				if(str.equals(Value2test) )
				{
					cel3.setCellValue("Pass");
				}
				else 
				{
					cel3.setCellValue("Fail");
				}
			}
			FileOutputStream fos=new FileOutputStream(path);
			wb1.write(fos);
			
			fos.close();
		}
		softAssert.assertAll();
	}
}
			
			
			
			
			
			



	




