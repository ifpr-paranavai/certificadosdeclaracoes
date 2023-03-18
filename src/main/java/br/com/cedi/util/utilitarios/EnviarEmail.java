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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;



import com.google.gson.JsonObject;

public class EnviarEmail {
	public static Boolean enviarEmail(String destinatario, String titulo, String mensagem) {

		
		Boolean flag = false;
		try {	Properties prop = new Properties();
		prop.put("mail.smtp.host", "nao-responda2.ifpr.edu.br");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        //prop.put("mail.smtp.socketFactory.port", "587");
        //prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.starttls.enable","true");
        //prop.put("mail.smtp.ssl.trust", "nao-responda2.ifpr.edu.br");
		//prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getInstance(prop, new Authenticator() {	
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("certificados.paranavai@nao-responda2.ifpr.edu.br", "");
		    }
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("certificados.paranavai@nao-responda2.ifpr.edu.br"));
		message.setRecipients(
		  Message.RecipientType.TO, InternetAddress.parse(destinatario));
		message.setSubject(titulo);

		String msg = mensagem;

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

		

	}
}
