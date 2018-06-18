package AP1_Prod;



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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class GetProfileUser_Prod 
{
	@Test
	public void getprofile_positive() throws EncryptedDocumentException, InvalidFormatException, IOException 
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
		GenericMethod r=new GenericMethod();
		Response resp=	r.createapi();
		String ID = resp.jsonPath().get("LoginRadius.ID");
		System.out.println(ID);
		String email = resp.jsonPath().get("LoginRadius.Email[0].Value");
		
		//reading data for get 
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Sheet1");
		Row row=sh.getRow(2);
		String platform=row.getCell(0).getStringCellValue();
		String pId=row.getCell(1).getStringCellValue();
		String URL_getProfileUser=row.getCell(4).getStringCellValue();
		Response resp1=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						queryParam("platform",platform).
						queryParam("pId",pId).
						queryParam("user_id",ID).
						when().
						post(URL_getProfileUser);
						
						String str=resp1.asString();
						resp1.prettyPrint();
						resp1.then().assertThat().statusCode(200);
						
						String act = resp.jsonPath().get("LoginRadius.Email[0].Value");
						  act.equals(email);

		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);
		
		Sheet sh1=wb1.getSheet("Sheet1");
		Row row1=sh1.getRow(2);
		row1.createCell(5);
		Cell cel1=	row1.getCell(5, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(str);
		
		Row row2=sh1.getRow(1);
		row2.createCell(6);
		Cell cel2=	row1.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.NUMERIC);
		cel2.setCellValue(resp.statusCode());
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);
		
		fos.close();
		
	
		
		
		
	}

}
