package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContact;






@Component
public class MailSender {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	final String username = Constants.MAIL_USER_NAME;
	final String password = Constants.MAIL_PASSWORD;
	//final String username = "appmail";
	//final String password = "abcd@1234";
	
//	final String username = "appmail";
//	final String password = "Beyond@pp";

	
	private List<String> receivers = null;
	private String subject = null;
	private String content = null;
	private Properties props = null;
	private Session session = null;
	
	
	private String htmlContent = null;
	
	
	private Date sendDate =null;
	
	int condition =  -1;
	
	public static void main(String[] args){

		
		Constants.MAIL_USER_NAME = "contact@mercue.biz";
		Constants.MAIL_PASSWORD = "Mercue_5024";
		
		
		 Constants.URL_RESET_PASSWORD = "http://192.168.2.71:8080/impact/app/html/resetpwd.html";
		 
		 Constants.URL_ENABLE_PASSWORD = "http://192.168.2.71:8080/impact/app/html/enablepwd.html";
		 
		 Constants.URL_PATENT_CONTENT =  "http://192.168.2.71:8080/impact/html/patent-content/search?patent_id=";
		 
		 Constants.HTML_NEW_ACCOUNT = "/Users/leo/Desktop/webpage/accountactivation.html";
		 
		 Constants.HTML_ANNUITY_REMINDER = "/Users/leo/Desktop/webpage/paymentnotice.html";
		 
		 Admin admin = new Admin();
		 admin.setAdmin_name("Leo Huang");
		 admin.setAdmin_email("leohuang@mercue.biz");
		 admin.setToken(KeyGeneratorUtils.generateRandomString());
		 
		 
		 
		 List<PatentContact> listContact = new ArrayList<PatentContact>();
		 PatentContact contact = new PatentContact();
		 contact.setContact_email("leohuang@mercue.biz");
		 listContact.add(contact);
		 
		 Patent patent = new Patent();
		 patent.setPatent_id("0137021b10a46fb54e9f0fea9c21f172");
		 patent.setPatent_name("專利名稱");
		 patent.setPatent_appl_no("TW1234567");
		 patent.setCountry_name("中華民國");
		 patent.setAnnuity_date("2019/02/20");
		 
		 new MailSender().sendPatentAnnuityReminder(patent, listContact);
		
	}
	
	public MailSender(){
		props = new Properties();
		props.put("mail.smtp.host",  "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");

		
		
		session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
	}
	
	
	public void setReceiver(List<String> receiverList){
		this.receivers = receiverList;
	}
	
	public void setSubject(String strSubject){
		this.subject = strSubject;
	}
	
	public void setContent(String strContent){
		this.content = strContent;
	}
	
	public void setHtmlContent(String htmlContent){
		this.htmlContent = htmlContent;
	}
	
	public void setSendDate(Date sendDate){
		this.sendDate = sendDate;
	}
	
	public void setMailContent(String strSubject,String strContent){
		this.subject = strSubject;
		this.content = strContent;
	}
	
	public void sendSimpleMail(List<String> receiverList,String strSubject,String strContent){
		sendSimpleMail(receiverList,strSubject,strContent,null);
	}
	
	public void sendSimpleMail(List<String> receiverList,String strSubject,String strContent,Date sendDate){
		this.receivers = receiverList;
		this.subject = strSubject;
		this.content = strContent;
		this.sendDate = sendDate;
		sendMail();
	}
	
	public void sendHTMLMail(List<String> receiverList,String strSubject,String htmlContent){
		sendHTMLMail(receiverList,strSubject,htmlContent,null);
	}
	
	public void sendHTMLMail(List<String> receiverList,String strSubject,String htmlContent,Date sendDate){
		this.receivers = receiverList;
		this.subject = strSubject;
		this.htmlContent = htmlContent;
		this.sendDate = sendDate;
		sendMail();
	}
	
	
	
	public void sendMail(){
		new Thread(){
	            public void run() {
				try {
					log.info("before message");	
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("contact@mercue.biz"));
					//String address = "leohuang@mercue.biz,leo731121@hotmail.com";
					InternetAddress[] iAdressArray = new InternetAddress[receivers.size()];
					for(int i = 0 ; i< receivers.size() ; i ++) {
						iAdressArray[i] = new InternetAddress(receivers.get(i));
					}

					
					message.setRecipients(Message.RecipientType.TO,iAdressArray);
					message.setSubject(subject);
					if(!StringUtils.isNULL(content)) {
						message.setText(content);
					}
					
					if(!StringUtils.isNULL(htmlContent)) {
						message.setContent(htmlContent,"text/html;charset=UTF-8");
					}
					
					
					if(sendDate !=null) {
						message.setSentDate(sendDate);
					}
					
					log.info("before send");	
					Transport.send(message);
					log.info("Done");
				} catch (MessagingException e) {
					log.error("MessagingException :"+e.getMessage());
					
				}
	       }
		
		}.start();
	}
	
	public void sendForgetPassword(Admin admin) {
		
		if(admin != null) {
			String html = FileUtils.readHtml(Constants.HTML_FORGET_PASSWORD);
			
			String htmlContent =html.replaceAll("@admin_name", admin.getAdmin_name());
			htmlContent =htmlContent.replaceAll("@forget_link", Constants.URL_RESET_PASSWORD + "?token=" +admin.getToken());
			List<String> list = new ArrayList<String>();
			list.add(admin.getAdmin_email());
			sendHTMLMail(list, "忘記密碼", htmlContent);
		}
		
	}
	
	public void sendActiveAccount(Admin admin) {
		
		if(admin != null) {
			String html = FileUtils.readHtml(Constants.HTML_NEW_ACCOUNT);
			
			String htmlContent =html.replaceAll("@admin_name", admin.getAdmin_name());
			htmlContent =htmlContent.replaceAll("@active_link", Constants.URL_ENABLE_PASSWORD + "?token=" +admin.getToken()+"&email="+admin.getAdmin_email());
			List<String> list = new ArrayList<String>();
			list.add(admin.getAdmin_email());
			sendHTMLMail(list, "邀請使用", htmlContent);
		}
		
	}
	
	
	public void sendPatentAnnuityReminder(Patent patent,List<PatentContact> listContact) {
		
		if(patent != null) {
			String html = FileUtils.readHtml(Constants.HTML_ANNUITY_REMINDER);
			
			String htmlContent = html.replaceAll("@patent_name", patent.getPatent_name());
			htmlContent = htmlContent.replaceAll("@country_name", patent.getCountry_name());
			htmlContent = htmlContent.replaceAll("@patent_appl_no", patent.getPatent_appl_no());
			htmlContent = htmlContent.replaceAll("@annuity_date", patent.getAnnuity_date());
			htmlContent =htmlContent.replaceAll("@patent_link", Constants.URL_PATENT_CONTENT  + patent.getPatent_id());
			List<String> list = new ArrayList<String>();
			for(PatentContact contact : listContact) {
				list.add(contact.getContact_email());
			}

			sendHTMLMail(list, "專利繳費通知 "+patent.getCountry_name() + " " +patent.getPatent_appl_no(), htmlContent);
		}
		
	}
	
	public void sendPatentMutipleChange(Business business, List<Patent> listPatent) {
		
		if(business != null) {
			String html = FileUtils.readHtml(Constants.HTML_MULTIPLE_PATENT_CHANGE);
			if (!StringUtils.isNULL(business.getContact_name())) {
				String htmlContent = html.replaceAll("@admin_name", business.getContact_name());
				
				if (listPatent.size() > 0) {
					htmlContent = html.replaceAll("@patent_country_1", listPatent.get(0).getCountry_name());
					htmlContent = html.replaceAll("@patent_appl_num_1", listPatent.get(0).getPatent_appl_no());
					htmlContent =htmlContent.replaceAll("@patent_link_1", Constants.URL_PATENT_CONTENT  + listPatent.get(0).getPatent_id());
				}
				if (listPatent.size() > 1) {
					htmlContent = html.replaceAll("@patent_country_2", listPatent.get(1).getCountry_name());
					htmlContent = html.replaceAll("@patent_appl_num_2", listPatent.get(1).getPatent_appl_no());
					htmlContent =htmlContent.replaceAll("@patent_link_2", Constants.URL_PATENT_CONTENT  + listPatent.get(1).getPatent_id());
				}
				List<String> list = new ArrayList<String>();
				list.add(business.getContact_email());
				sendHTMLMail(list, "專利同步通知", htmlContent);
			}

		}
		
	}
	
}