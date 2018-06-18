package AP1_Prod;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
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
import org.hamcrest.core.IsNull;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;


public class GetAuthKey_Prod extends GenericMethod_Auth
{
	String str;
	String Value2test;
	@Test
	public void getauthkey_positive() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//generic method
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("AuthKey");
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
		//reading data for authkey
		for(int i=1; i<=rowCount;i++)
		{
				Row row1=sh.getRow(i);
				String UID=row1.getCell(2).getStringCellValue();
				if(UID.equals("EMPTY"))
				{
					UID="";
				}
				String URL_getauthkey=row1.getCell(3).getStringCellValue();
				String TestType=row1.getCell(0).getStringCellValue();
				String key2Test=row1.getCell(4).getStringCellValue();
				Value2test=row1.getCell(5).getStringCellValue();
				Response resp1=	RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).accept(ContentType.JSON).
								queryParam("UID",UID).
								when().
								post(URL_getauthkey);
				
				resp1.prettyPrint();
				
				if(TestType.equals("Positive"))
				{
					resp1.then().assertThat().statusCode(200);
					resp1.then().body("$", hasKey("token"));
					resp1.then().body("token", is(IsNull.notNullValue()));
				}
				else if(TestType.equals("Negative"))
				{
					
					str=resp1.then().extract().path(key2Test);
					softAssert.assertEquals(Value2test,str);
				}
				
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("AuthKey");
				
				Row row2=sh1.getRow(i);
				row2.createCell(6);
				Cell cel2=	row2.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//cel2.setCellType(CellType.STRING);
				cel2.setCellValue(resp1.asString());
		
				Row row3=sh1.getRow(i);
				row3.createCell(7);
				Cell cel3=row3.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
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
		
		
				FileOutputStream fos=new FileOutputStream(path1);
				wb1.write(fos);
		
				fos.close();
		}
		softAssert.assertAll();	
			
	
}

}