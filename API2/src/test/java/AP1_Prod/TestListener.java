package AP1_Prod;


import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestListener  implements ITestListener 
{

	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailure(ITestResult result) 
	{
		System.out.println("The name of the testcase failed is :"+result.getName());
		final String username = "ifocus.automation@gmail.com";
		final String password = "Ifocus@123";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try 
		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ifocus.automation@gmail.com"));
			
//			message.setRecipients(Message.RecipientType.TO,
//					InternetAddress.parse("Brijneet.Bhasin@viacom18.com,Aniruddha.Batchu@viacom18.com,Abhishek.Hardi@viacom18.com,Shubhank.Mauria@viacom18.com,Shriraj.Salunkhe@viacom18.com,Anubhav.Shrivastava@viacom18.com,Prashant.Singh@viacom18.com,Prajney.Sribhashyam@viacom18.com,suhas.bhat@ifocussystec.com,vootleads@ifocussystec.com,ifocus-v18-qa-delivery@ifocussystec.com"));
			
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("karanam.vishnuvardhan@ifocussystec.in,amit.mahato@ifocussystec.in"));
			
			
			
			message.setSubject("API Automation Failure");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("Hi Team,\n\n\t API Failed , Platform is "+GenericMethod_Auth.platformname.toString()+ "\n\t-"+result.getName() +" API failed. As "+result.getThrowable().getMessage().toString()+"."+"\n\n Thanks & Regards, \n iFocus API Automation Team.");
			// Create a multipar message
	        Multipart multipart = new MimeMultipart();
	        // Set text message part
	        multipart.addBodyPart(messageBodyPart);
	        // Part two is attachment
	        message.setContent(multipart);
	        Transport.send(message);
	        System.out.println("Done");

		} 
		catch (MessagingException e) 
		{
			throw new RuntimeException(e);
		}
		
	}

	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	

	

		
}