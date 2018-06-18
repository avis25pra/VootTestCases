package API_VootKids_Sprint1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

public class GenericMethod_VK 
{
	static Headers headers=GenericMethod_VK.headers();
	public static final String path1=System.getProperty("user.dir")+"\\VootKidsSprint1.xls";
	public static final String path2=System.getProperty("user.dir")+"\\VootKidsSprint2.xlsx";
	public static final String path3=System.getProperty("user.dir")+"\\VootKidsSprint3.xlsx";
	public static final String path4=System.getProperty("user.dir")+"\\VootKidsSprint4.xlsx";
	public static String platformname=""; //For different platform
	
	//function for Signup
	public  Response SignUp() throws EncryptedDocumentException, InvalidFormatException, IOException  
	{
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//Auto generated email
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 10) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String saltStr = salt.toString();
	    String email= saltStr+"@gmail.com";//generation of email
	    
	    RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		
		FileInputStream fis=new FileInputStream(path1);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet("SignUp");
		Row row=sh.getRow(1);
		//reading data from excel
		String password=row.getCell(3).getStringCellValue();
		String deviceId=row.getCell(4).getStringCellValue();
		String deviceBrand=row.getCell(5).getStringCellValue();
		String URL=row.getCell(6).getStringCellValue();
		String key2test=row.getCell(7).getStringCellValue();
	
		//posting request
		BasicConfigurator.configure();
		Response resp=	RestAssured.
						given().
						relaxedHTTPSValidation().
						contentType(ContentType.JSON).
						headers(headers).
						accept(ContentType.JSON).
						queryParam("email",email).
						queryParam("password",password).
						queryParam("deviceId",deviceId).
						queryParam("deviceBrand",deviceBrand).
						when().
						post(URL);
		
		resp.then().assertThat().statusCode(200);//checking for status code=200
		return resp;
		
	}
	//function for email generator
	public static String emailGenerator()
	{
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz123456789";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 10) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String saltStr = salt.toString();
	    String email= saltStr+"@gmail.com";
	    return email;
	}
	//function for pin generator
	public static String pinGenerator()
	{
		String SALTCHARS = "1234567890";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 4) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String pin = salt.toString();
	   
	    return pin;
	}
	//function for password generator
	public static String passwordGenerator()
	{
		String SALTCHARS = "abcdefghijklmnopqrstuvwxyz";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 7) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String password = salt.toString();
	   
	    return password;
	}
	//functon to write data back to excel after execution
	public static void writedata(int i,String Value2test, String TestType, Response resp1,String str,int celnum1,int celnum2,String sheetname,String path) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		FileInputStream fis1=new FileInputStream(path);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet(sheetname);
		Row row1=sh1.getRow(i);
		row1.createCell(celnum1);
		Cell cel1=row1.getCell(celnum1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.STRING);
		cel1.setCellValue(resp1.asString());

		Row row3=sh1.getRow(i);
		row3.createCell(celnum2);
		Cell cel3=row3.getCell(celnum2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		if(TestType.equals("Negative")|| TestType.equals("Positive"))
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
	//function for taking both the value double and integer in response
	public static <T> boolean oneOfEquals(T a, T b, T expected) 
	{
	    return a.equals(expected) || b.equals(expected);
	}
	//function for header
	public static Headers headers() 
	{
		Header h1= new Header("peppaPig", "M2ZseFBKUTE5YnFaY0xoMQ==qNG9f8sLNOc1mff/2lng2H3+/yXCgAxwfxXJ38cN3PtE33CD/tF7vSrL+1Es6qiEY1f8S0z1iPfvkLKRgvMhoEImWpfBao1noFXTpMMQOvJ/Tp/+ocKsB4A1E25vSzURHtv1ecpG+HX5KgKQnCUnww==");
	    Header h2 = new Header("platform", "android");
	    List<Header> list = new ArrayList<Header>();
	    
	    list.add(h1);
	    list.add(h2);
	    Headers header = new Headers(list);
	    
	    return header;
	}
	//function for write2master
	public static void write2Master(int row,String sheetname,int columnum,String path) throws EncryptedDocumentException, InvalidFormatException, IOException,NullPointerException
	{
	
		int countPass=0;
		int countFail=0;
		FileInputStream fis=new FileInputStream(path);
		Workbook wb=WorkbookFactory.create(fis);
		Sheet sh=wb.getSheet(sheetname);
		//count the rows
		int rowCount = sh.getLastRowNum()-sh.getFirstRowNum();
		for(int i=1; i<=rowCount;i++)
        {
			Row row4= sh.getRow(i);
			String status=row4.getCell(columnum).getStringCellValue();
			if(status.equals("Pass"))
			{
				countPass=countPass+1;
			}
			else
			{
				countFail=countFail+1;
			}
        }
		
		FileInputStream fis1=new FileInputStream(path);
		Workbook wb1=WorkbookFactory.create(fis1);

		Sheet sh1=wb1.getSheet("Master");
		
		Row row3=sh1.getRow(row);
		row3.createCell(2);
		Cell cel3=row3.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel3.setCellType(CellType.NUMERIC);
		cel3.setCellValue(rowCount);
		
		Row row1=sh1.getRow(row);
		row1.createCell(3);
		Cell cel1=row1.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel1.setCellType(CellType.NUMERIC);
		cel1.setCellValue(countPass);
		
		Row row2=sh1.getRow(row);
		row2.createCell(4);
		Cell cel2=row1.getCell(4, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cel2.setCellType(CellType.NUMERIC);
		cel2.setCellValue(countFail);
		
		FileOutputStream fos=new FileOutputStream(path);
		wb1.write(fos);

		fos.close();
		}
		
		public static void ExcelWriteBack(String path,String sheetname,int row,int respCol,Response resp,int statusCol,String TestType,int counter,Boolean bool,String myValue,String Value2test,int datatypeCol,int nullCol,String nullValues,String myErrorArray) throws EncryptedDocumentException, InvalidFormatException, IOException,NullPointerException
		{
			String errorArray="";
			if(myErrorArray.equals(""))
			{
				errorArray="";
			}
			else
			{
				errorArray=", Error in Array no: ["+myErrorArray+"]";
			}
			
			//code to write the output and status code in excel
			FileInputStream fis1=new FileInputStream(path);
			Workbook wb1=WorkbookFactory.create(fis1);
	
			Sheet sh1=wb1.getSheet(sheetname);
			Row row1=sh1.getRow(row);
			row1.createCell(respCol);
			Cell cel1=row1.getCell(respCol, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(resp.asString());//writing the response back to the excel
	
			Row row3=sh1.getRow(row);
			row3.createCell(statusCol);
			Cell cel3=row3.getCell(statusCol, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if(TestType.equals("Positive")) //logic to write pass/fail for positive TC
			{
				if(counter==0 || bool==false)
				{
					cel3.setCellValue("Fail");
				}
				else 
				{
					cel3.setCellValue("Pass");
				}
			}
			if(TestType.equals("Negative")) ////Logic for writing pass/fail for negative TC
			{	
				if(myValue.equals(Value2test) )
				{
					cel3.setCellValue("Pass");
				}
				else 
				{
					cel3.setCellValue("Fail");
				}
			}
			//created cell to write pass/fail for datatype validation
			if(TestType.equals("Positive"))
			{
				Row row2=sh1.getRow(row);
				row2.createCell(datatypeCol);
				Cell cel2=row2.getCell(datatypeCol, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cel2.setCellType(CellType.STRING);
				if(bool==false)
				{
					cel2.setCellValue("Fail"+errorArray);
				}
				else 
				{
					cel2.setCellValue("Pass");
				}
			}
			//created cell to write null keys 
			if(TestType.equals("Positive"))//logic to write parameters name which have null values
			{
				if(counter==0)
				{	
					Row row4=sh1.getRow(row);
					row4.createCell(nullCol);
					Cell cel4=row4.getCell(nullCol, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cel4.setCellType(CellType.STRING);
					cel4.setCellValue("Keys having null values: "+ nullValues);
				}
			}
			
			FileOutputStream fos=new FileOutputStream(path);
			wb1.write(fos);
			fos.close();
		}
	
		
	}





