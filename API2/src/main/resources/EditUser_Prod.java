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
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.response.Response;


public class EditUser_Prod extends GenericMethod {
	@Test
	public void edituser() throws EncryptedDocumentException, InvalidFormatException, IOException {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		GenericMethod g=new GenericMethod();
		Response resp=g.createapi();
		String ID = resp.jsonPath().get("LoginRadius.ID");
		
		
		String path1="C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("Sheet1");
		Row row=sh.getRow(9);
		String platform=row.getCell(0).getStringCellValue();
		String pId=row.getCell(1).getStringCellValue();
		String URL_EditUser=row.getCell(4).getStringCellValue();
		Response resp1=	RestAssured.
						given().
						queryParam("platform",platform).
						queryParam("pId",pId).
						queryParam("user_id",ID).
						when().
						post(URL_EditUser);
		resp1.then().assertThat().statusCode(200);
		resp1.prettyPrint();
		Boolean isPosted  =	resp1.then().extract().path("isPosted");
							String str = String.valueOf(isPosted);
							assertEquals(str,"false","isExist value is not as expected");
							String output=resp1.asString();
		
		
		FileInputStream fis1=new FileInputStream(path1);
		Workbook wb1=WorkbookFactory.create(fis1);
		
		Sheet sh1=wb1.getSheet("Sheet1");
		Row row1=sh1.getRow(9);
		row1.createCell(5);
		Cell cel1=	row1.getCell(5, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(output);
		
		Row row2=sh1.getRow(1);
		row2.createCell(6);
		Cell cel2=	row1.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.NUMERIC);
		cel2.setCellValue(resp1.statusCode());
		
		FileOutputStream fos=new FileOutputStream(path1);
		wb1.write(fos);
		
		fos.close();
		
		
		
		
		
	}

}

