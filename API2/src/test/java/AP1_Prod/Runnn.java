package AP1_Prod;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.annotations.Test;

public class Runnn //This will run the testng.xml 
{
	
	public static void main(String [] args) throws Exception
	{
		for(int i=0;i<1;i++)// Enter number of iteration you want
		{
			if(i>0)
			{
				Thread.sleep(000000);// enter minutes in miliseconds
				List<String> suites = new ArrayList<String>();
				suites.add("C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testng.xml"); //path of .xml file to be run-provide complete path

				TestNG tng = new TestNG();
				tng.setTestSuites(suites);

				tng.run(); //run test suite
			}
			else 
			{
				List<String> suites = new ArrayList<String>();
				suites.add("C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testng.xml"); //path of .xml file to be run-provide complete path

				TestNG tng = new TestNG();
				tng.setTestSuites(suites);

				tng.run(); //run test suite
			}
		}
	}
}
