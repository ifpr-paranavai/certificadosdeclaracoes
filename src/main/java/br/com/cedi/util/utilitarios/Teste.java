package br.com.cedi.util.utilitarios;

import java.util.Properties;

import javax.mail.Authenticator;
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

public class Teste {

	public static void main(String[] args) {
		try {	Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getInstance(prop, new Authenticator() {	
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("sistemasifprparanavai@gmail.com", "");
		    }
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("sistemasifprparanavai@gmail.com"));
		message.setRecipients(
		  Message.RecipientType.TO, InternetAddress.parse("frankwco@gmail.com"));
		message.setSubject("Mail Subject");

		String msg = "This is my first email using JavaMailer";

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 e.getMessage();
		}
		System.out.println(true);
	}

}
