package SamplePackage;

import java.awt.image.RescaleOp;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.attribute.ResolutionSyntax;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class MoviesMegaMenu {
	static String testType;
	static String uRL;
	static String assets2Test;
	static String assetsDataType;
	static String items2Test;
	static String itemsDataType;
	static String movieTypeValue;
	static String movieTypeDataValue;
	static String itemTypeValue;
	static String itemTypeDataValue;
	static String path = "C:\\Users\\ifocus\\git\\APIAutomation\\API2\\sampleSheet.xlsx";
	static int flag;
	static Response response;
	static SoftAssert softAssert = new SoftAssert();
	static String[] assets;
	static String[] items;
	static String[] assetsData;
	static String[] itemsData;
	static Boolean boolForAssets;
	static Boolean boolForItems;
	static ArrayList<String> expectedAssetsDataType;
	static ArrayList<String> actualAssetsDataType;
	static ArrayList<String> expectedItemsDataType;
	static ArrayList<String> actualItemsDAtaType;
	
	
	@Test
	public void megaMenuMovies() throws EncryptedDocumentException, InvalidFormatException, IOException {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("moviesMegaMenu");
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		System.out.println("Number of TestCases: "+rowCount);
		
		for(int i =1; i <= rowCount; i++) {
			Row row = sheet.getRow(i);
			testType = row.getCell(0).getStringCellValue();
			uRL = row.getCell(2).getStringCellValue();
			assets2Test = row.getCell(3).getStringCellValue();
			assetsDataType = row.getCell(4).getStringCellValue();
			items2Test = row.getCell(5).getStringCellValue();
			itemsDataType = row.getCell(6).getStringCellValue();
			
			BasicConfigurator.configure();
			response = RestAssured.
					   given().
					   relaxedHTTPSValidation().
					   contentType(ContentType.JSON).
					   accept(ContentType.JSON).
					   when().
					   get(uRL);
			
			response.then().assertThat().statusCode(200);
			response.prettyPrint();
			System.out.println("Response Printed");
			
			MoviesMegaMenu.checkTestType(i, response, path, assets2Test, assetsDataType, items2Test, itemsDataType);
			
		}
	}


	public static void checkTestType(int i, Response response, String path, String assets2Test,
			String assetsDataType, String items2Test, String itemsDataType) {
		if(testType.equals("Positive")) {
			assets = assets2Test.split(",");			
			int sizeOfAssets = assets.length;
			items = items2Test.split(",");
			int sizeOfItems = items.length;
			assetsData = assetsDataType.split(",");
			int sizeOfAssetsData = assetsData.length;
			itemsData = itemsDataType.split(",");
			int sizeOfItemData = itemsData.length; 
			
			int typeOfMovies = response.body().path("assets.size()");
			int numberOfMovies = response.body().path("assets[0].items.size");
			
			for(int j =0; j<typeOfMovies; j++) {
				movieTypeValue = String.valueOf(response.then().extract().path("assets["+j+"]"));
				System.out.println(movieTypeValue);
				actualAssetsDataType = new ArrayList<String>();
				actualAssetsDataType.removeAll(actualAssetsDataType);
				for(int k=0;k<sizeOfAssets;k++) {
					expectedAssetsDataType = new ArrayList<String>(Arrays.asList(assetsData));
					String strAssets = String.valueOf(response.then().extract().path("assets["+j+"]."+assets[k]));
					if(strAssets.equals("null"))
						continue;
				    movieTypeDataValue = response.jsonPath().get("assets["+j+"]."+assets[k]).getClass().getSimpleName();
                    System.out.println(movieTypeDataValue);
                    actualAssetsDataType.add(movieTypeDataValue);
				}
				System.out.println(expectedAssetsDataType);
				System.out.println(actualAssetsDataType);
				boolForAssets = actualAssetsDataType.equals(expectedAssetsDataType);
				System.out.println(boolForAssets);
				
				actualItemsDAtaType= new ArrayList<String>();
				actualItemsDAtaType.removeAll(actualItemsDAtaType);
				for(int l=0; l<numberOfMovies; l++) {
					itemTypeValue = String.valueOf(response.then().extract().path("assets["+j+"].items"));
					System.out.println(itemTypeValue);
					expectedItemsDataType = new ArrayList<String>(Arrays.asList(itemsData));
					for(int m=0; m<sizeOfItems; m++) {
						expectedItemsDataType = new ArrayList<String>(Arrays.asList(itemsDataType));
						String strItems = String.valueOf(response.then().extract().path("assets["+j+"].items["+l+"]."+items[m]));
						if(strItems.equals("null"))
							continue;
						itemTypeDataValue = response.jsonPath().get("assets["+j+"].items["+m+"]."+items[l]).getClass().getSimpleName();
						System.out.println(itemTypeDataValue);
						actualItemsDAtaType.add(itemTypeDataValue);
					}
					System.out.println(expectedItemsDataType);
					System.out.println(actualItemsDAtaType);
					boolForItems = actualItemsDAtaType.equals(expectedItemsDataType);
					System.out.println(boolForItems);
				}
				
			}
			
			
		}
		
	}
	
}
