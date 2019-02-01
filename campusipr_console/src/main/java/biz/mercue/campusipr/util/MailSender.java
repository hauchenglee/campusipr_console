package biz.mercue.campusipr.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;



@Component
public class MailSender {
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	final String username = "contact@mercue.biz";
	final String password = "Mercue_5024";
	//final String username = Constants.MAIL_USER_NAME;
	//final String password =  Constants.MAIL_PASSWORD;

	
	private String receiver = null;
	private String subject = null;
	private String content = null;
	private Properties props = null;
	private Session session = null;
	
	int condition =  -1;
	
	public MailSender(){
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", Constants.MAIL_STARTTLS);		
		props.put("mail.smtp.host",  Constants.MAIL_HOST);
		props.put("mail.smtp.port", Constants.MAIL_PORT);
		
		
		session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
	}
	
	
	public void setReceiver(String strReceiver){
		this.receiver = strReceiver;
	}
	
	public void setSubject(String strSubject){
		this.subject = strSubject;
	}
	
	public void setContent(String strContent){
		this.content = strContent;
	}
	
	public void setMailContent(String strSubject,String strContent){
		this.subject = strSubject;
		this.content = strContent;
	}
	
	public void sendMail(String strReceiver,String strSubject,String strContent){
		this.receiver = strReceiver;
		this.subject = strSubject;
		this.content = strContent;
		sendMail();
	}
	
	
	
	public void sendMail(){
		new Thread(){
	            public void run() {
				try {
								
					Message message = new MimeMessage(session);
					//message.setFrom(new InternetAddress("contact@mercue.biz"));
					message.setFrom(new InternetAddress(Constants.SYSTEM_EMAIL));
					message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receiver));
					message.setSubject(subject);
					message.setText(content);

					Transport.send(message);
					log.info("Done");
				} catch (MessagingException e) {
					log.info("MessagingException :"+e.getMessage());
					
				}
	       }
		
		}.start();
	}
	
}
