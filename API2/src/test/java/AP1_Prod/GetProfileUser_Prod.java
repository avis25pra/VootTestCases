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

public class GetProfileUser_Prod extends GenericMethod_Auth
{
	String str;
	String Value2test;
	@Parameters({"path","platformName"})
	@Test
	public void getprofile_SocialLogin(String path,String platformName) throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		GenericMethod_Auth.platformname=platformName;
		SoftAssert softAssert = new SoftAssert();
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		
		//reading data for get 
		FileInputStream fis=new FileInputStream(path);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("GetProfileUser");
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
		for(int i=1; i<=rowCount;i++)
		{
			Row row=sh.getRow(i);
			String platform=row.getCell(1).getStringCellValue();
			String pId=row.getCell(2).getStringCellValue();
			String ID=row.getCell(4).getStringCellValue();
			if(ID.equals("EMPTY"))
			{
				ID="";
			}
			String key2Test=row.getCell(6).getStringCellValue();
			 Value2test=row.getCell(7).getStringCellValue();
			String URL_getProfileUser=row.getCell(5).getStringCellValue();
			
			Response resp1=	RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).accept(ContentType.JSON).
							queryParam("platform",platform).
							queryParam("pId",pId).
							queryParam("user_id",ID).
							when().
							post(URL_getProfileUser);
						
							
							resp1.prettyPrint();
							resp1.then().assertThat().statusCode(200);
						
							str = resp1.jsonPath().get(key2Test);
							softAssert.assertEquals(Value2test,str);

			FileInputStream fis1=new FileInputStream(path);
			Workbook wb1=WorkbookFactory.create(fis1);
		
			Sheet sh1=wb1.getSheet("GetProfileUser");
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
		
			FileOutputStream fos=new FileOutputStream(path);
			wb1.write(fos);
		
			fos.close();
		}
		
		softAssert.assertAll();
		
		
		
	}

}
