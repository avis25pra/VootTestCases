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
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.response.Response;


public class EditUser_Prod extends GenericMethod_Auth 
{
		String str;
		String Value2test;
		@Test
		public void edituser() throws EncryptedDocumentException, InvalidFormatException, IOException 
		{
			SoftAssert softAssert = new SoftAssert();
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			FileInputStream fis=new FileInputStream(path1);
			Workbook wb=WorkbookFactory.create(fis);
			Sheet sh=wb.getSheet("EditUser");
			int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
			
			for(int i=1; i<=rowCount;i++)
			{
				
				Row row=sh.getRow(i);
				String TestType=row.getCell(0).getStringCellValue();
				String platform=row.getCell(1).getStringCellValue();
				String pId=row.getCell(2).getStringCellValue();
				String user_id=row.getCell(4).getStringCellValue();
				if(user_id.equals("EMPTY"))
				{
					user_id="";
				}
				String firstname=row.getCell(5).getStringCellValue();
				if(firstname.equals("EMPTY"))
				{
					firstname="";
				}
				String lastname=row.getCell(6).getStringCellValue();
				if(lastname.equals("EMPTY"))
				{
					lastname="";
				}
				String key2Test=row.getCell(8).getStringCellValue();
				String Value2test=row.getCell(9).getStringCellValue();
				String URL_EditUser=row.getCell(7).getStringCellValue();
				Response resp1=	RestAssured.
						given().
						queryParam("platform",platform).
						queryParam("pId",pId).
						queryParam("user_id",user_id).
						queryParam("firstname",firstname).
						queryParam("lastname",lastname).
						when().
						post(URL_EditUser);
				
				resp1.then().assertThat().statusCode(200);
				resp1.prettyPrint();
				if(TestType.equals("Positive"))
	    		{
	    			Boolean isPosted=resp1.then().extract().path(key2Test);
	    			str=String.valueOf(isPosted);
	    			softAssert.assertEquals(Value2test,str);
	    		}
	    		else
	    		{
	    			str=resp1.then().extract().path(key2Test);
	    			softAssert.assertEquals(Value2test,str);
	    		}
				
				FileInputStream fis2=new FileInputStream(path1);
				Workbook wb2=WorkbookFactory.create(fis2);
			
					Sheet sh2=wb2.getSheet("EditUser");

					Row row2=sh2.getRow(i);
					row2.createCell(10);
					Cell cel2=	row2.getCell(10, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					//cel2.setCellType(CellType.STRING);
					cel2.setCellValue(resp1.asString());
				
					Row row3=sh2.getRow(i);
					row3.createCell(11);
					Cell cel3=row3.getCell(11, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if(str.equals(Value2test)) {
					cel3.setCellValue("Pass");
					}
					else {
						cel3.setCellValue("Fail");
					}
			
				
					FileOutputStream fos=new FileOutputStream(path1);
					wb2.write(fos);
				
					fos.close();
				
							
			}	
			softAssert.assertAll();
		
		
		
		
		
	}

}

