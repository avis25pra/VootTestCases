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
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;


public class ZSendMail 
{
	@Test
	public void SendMail()
	{
		final String username = "abhilash.s@ifocussystec.com";
		final String password = "P455w0rd!!";

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

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("abhilash.s@ifocussystec.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("amit.mahato@ifocussystec.in"));
//			message.setRecipients(Message.RecipientType.TO,
//					InternetAddress.parse("vishnu26121993@gmail.com"));
			
			message.setSubject("AUTH API's Report");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
			
			BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText("This is message body");

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
//	         String filename = "C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\test-output\\emailable-report.html";
	         String filename1 = "C:\\Users\\ifocus.IFOCUSODC-PC47\\git\\API2\\testdataV1.xls";
//	         GenericMethod.addAttachment(multipart, filename);
	         GenericMethod_Auth.addAttachment(multipart, filename1);
	         // Send the complete message parts
	        
	         message.setContent(multipart);
			
			
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}
}
