package API_VootKids_Sprint1;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class GetProfile extends GenericMethod_VK
{
	static String str;
	static String key2test;
	static String Value2test;
	static String TestType;
	static int sizeOfList;
	static String uId;
	static SoftAssert softAssert = new SoftAssert();
	@Test
	public void Get_ChildProfiles() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Reading the excel sheet
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		//Excel sheet name Create
		Sheet sh=wb.getSheet("GetChildProfiles");
		//count the no. of rows ignoring the 1st row
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		
	    //started for loop
	    for(int i=1; i<=rowCount;i++)
        {
	    	
            	Row row = sh.getRow(i);
            	//fetching the cell values
            	TestType=row.getCell(0).getStringCellValue();
            	uId=row.getCell(3).getStringCellValue();
            	String URL=row.getCell(2).getStringCellValue();
        		key2test=row.getCell(4).getStringCellValue();
        		Value2test=row.getCell(5).getStringCellValue();
        		if(uId.equals("AUTO")) 
        		{
        			uId="487c0a7652b8405ea8472cb3ddb52e25";
        		}
        		if(uId.equals("INVALID")) 
        		{
        			uId="abcdef";
        		}
        		if(uId.equals("EMPTY"))//assign uid="" when uid is empty
				{
        			uId="";
				}
        		else if(uId.equals("NOTPASS"))//calling function for when uid is not passed
				{
        			GetProfile.NotPassUid(i, URL);
            		continue;
				}

				BasicConfigurator.configure();
				Response resp1=	RestAssured.
								given().
								param("uId",uId).
								relaxedHTTPSValidation().
								contentType(ContentType.JSON).
								accept(ContentType.JSON).
								headers(headers).
								when().
								get(URL);
				
				resp1.prettyPrint();//printing the response
				resp1.then().assertThat().statusCode(200);//checking for status code=200
				if(TestType.equals("Positive"))
				{
					sizeOfList = resp1.body().path("profiles.size()");//taking the size of the array profiles
					System.out.println(sizeOfList);
					for (int j=0;j<sizeOfList;j++)
					{
						String list=resp1.jsonPath().get("profiles["+j+"].id");//checking whether the list having id key or not
						System.out.println(list);
						assertNotNull(list);
					}
				}
				
				else
				{
					str= resp1.jsonPath().get(key2test);
					softAssert.assertEquals(Value2test,str);
				}
				
				//writing back to excel
				FileInputStream fis1=new FileInputStream(path1);
				Workbook wb1=WorkbookFactory.create(fis1);
		
				Sheet sh1=wb1.getSheet("GetChildProfiles");
				Row row1=sh1.getRow(i);
				row1.createCell(6);
				Cell cel1=row1.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel1.setCellType(CellType.STRING);
				cel1.setCellValue(resp1.asString());//writing the response back to the excel sheet
		
				Row row3=sh1.getRow(i);
				row3.createCell(7);
				Cell cel3=row3.getCell(7, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if(TestType.equals("Positive"))
				{
					if(sizeOfList>6)//for positive scenarios checking the length of an array, and logic for writing pass/fail
					{
					 cel3.setCellValue("Fail");
					}
					else
					{
						cel3.setCellValue("Pass");
					}
					
				}
				else if(TestType.equals("Negative"))//logic for writing pass/fail for negative scenarios
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
	    	
        
	    	
	   GenericMethod_VK.write2Master(14,"GetChildProfiles",7,path1);
	    softAssert.assertAll();
        }
	    
	
	//function for not passing uid
	public static void NotPassUid(int i,String URL) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		BasicConfigurator.configure();
		Response resp1=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						accept(ContentType.JSON).
						headers(headers).
						when().
						get(URL);
		
		str=resp1.then().extract().path(key2test);
		softAssert.assertEquals(Value2test,str);
		
		GenericMethod_VK.writedata(i, Value2test,TestType, resp1,str,6,7,"GetChildProfiles",path1);
	}
}

	
        	

