package biz.mercue.seed.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.mercue.seed.model.PushTask;
import biz.mercue.seed.service.PushService;

@Component
public class PushSendJob  implements Job {


	private Logger log = Logger.getLogger(this.getClass().getName());


	@Autowired
	PushService pushService;




	public void execute(JobExecutionContext context) {

		log.info("do push task");

		//PushSender sender = new PushSender();

		try {
			
			JobKey key = context.getJobDetail().getKey();
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			
			
			String pushId = dataMap.getString("push_id");
			log.info("pushId:"+pushId +" executing");
			PushTask task = pushService.getById(pushId);
			
//
//				
//				List<BotAccount> botAccountList = new ArrayList<BotAccount>();
//				List<Message> messageList = new ArrayList<Message>();
//				
//				if (!(StringUtils.isNULL(task.getAccount_id()) || StringUtils.isNULL(task.getBot_id()))) {
//					
//					log.info("push bot: " + task.getBot_id() + " , account: " + task.getAccount_id());
//					BotAccount record = botAccountService.getByBotAccount(task.getBot_id(), task.getAccount_id());
//					botAccountList.add(record);
//					
//				} else if (!StringUtils.isNULL(task.getAccount_id()) && task.getBotList() != null && task.getBotList().size() > 0) {
//					
//					log.info("push account: " + task.getAccount_id() + " , bot count: " + task.getBotList().size());
//					List<String> botIdList = new ArrayList<String>();
//					for (Bot bot: task.getBotList()) {
//						botIdList.add(bot.getBot_id());
//					}
//					botAccountList = botAccountService.getByAccountAndBotList(botIdList, task.getAccount_id());
//					
//				} else if (task.getBotList() != null && task.getTagList() != null) {
//					
//					log.info("push bot counts: " + task.getBotList().size() + ", tag counts: " + task.getTagList().size() + 
//							", target gender: " + task.getTarget_gender() + ", upper age: " + task.getHighest_age() + 
//							", lower age: " + task.getLowest_age() + ", location: " + task.getPush_location());
//
//					List<String> botIdList = new ArrayList<String>();
//					List<String> tagIdList = new ArrayList<String>();
//
//					for(Bot bot: task.getBotList()) {
//						botIdList.add(bot.getBot_id());
//					}
//					for (Tag tag: task.getTagList()) {
//						tagIdList.add(tag.getTag_id());
//					}
//					
//					botAccountList = botAccountService.getByBotTagSql(botIdList, tagIdList, task.getTarget_gender(), task.getHighest_age(), task.getLowest_age(), task.getPush_location());
//
//				}
//				
//				log.info("push account counts: " + botAccountList.size());
//				for(BotAccount record: botAccountList) {
//					
//					if(record !=null) {
//						log.info("account :"+record.getAccount_id());
//						log.info("bot :"+record.getBot_id());
//					}
//					Account account = accountService.getById(record.getAccount_id());
//					
//					Bot bot = BotManager.getInstance().getBot(record.getBot_id());
//
//					if(account!=null && bot!=null) {
//						Message message = new Message();
//						message.setMessage_id(KeyGeneratorUtils.generateRandomString());
//						message.setAccount_id(account.getAccount_id());
//						message.setBot_id(bot.getBot_id());
//						message.setMessage_social_type(bot.getBot_social_type());
//						message.setBusiness_id(bot.getBusiness_id());
//						message.setMessage_date(task.getPush_date());
//						message.setSocial_user_id(account.getSocial_user_id());
//						message.setMessage_send_receive(Constants.MESSAGE_SEND);
//						
//						message.setAdmin_id("40w9dse0277455f634fw40439sd");
//						message.setAdmin_name("系統推播");
//						
//						messageList.add(message);
//					}
//					
//				}
//				
//				log.info("create message list finish");
//				messageList.forEach(message-> {
//					List<String> contentList= new ArrayList<String>();
//					switch (task.getPush_type()) {
//					case Constants.PUSH_TYPE_TEXT:
//						log.info("type text");
//						log.info("message template :"+task.getMessageTemplate().getMessage_template_id());
//						message.setMessage_type(Constants.PUSH_TYPE_TEXT);
//						message.setMessage_text(task.getMessageTemplate().getMessage_text());
//						break;
//					case Constants.PUSH_TYPE_MEDIA:
//						String extensionName = task.getPush_extension_name();
//						File mediaFile = new File(Constants.PICTURE_UPLOAD_PATH + File.separator + pushId + File.separator + "original." + extensionName);
//						if(extensionName.equals(Constants.MEDIA_TYPE_3GP) ||extensionName.equals(Constants.MEDIA_TYPE_MOV) || extensionName.equals(Constants.MEDIA_TYPE_MP4)) {
//							log.info("type video");
//							message.setMessage_type(Constants.SOCIAL_MESSAGE_TYPE_VIDEO);
//						}else if(extensionName.equals(Constants.MEDIA_TYPE_PNG) ||extensionName.equals(Constants.MEDIA_TYPE_JPEG) || extensionName.equals(Constants.MEDIA_TYPE_JPG)){
//							log.info("type image");
//							message.setMessage_type(Constants.SOCIAL_MESSAGE_TYPE_IMAGE);
//							File large = new File(Constants.PICTURE_UPLOAD_PATH + File.separator +pushId+ File.separator+ "large." + extensionName);
//							mediaFile = large;
//						}else {
//							message.setMessage_type(Constants.SOCIAL_MESSAGE_TYPE_FILE);
//						}
//						try {
//							MediaMessageConverter.MediaMessage2Type(message, mediaFile,pushId);
//						} catch (Exception e1) {
//							log.error("Exception:"+e1.getMessage());
//						}
//					
//						break;
//					case Constants.PUSH_TYPE_CAMPAIGN:
//						break;
//					case Constants.PUSH_TYPE_QUESTIONAIRE:
//						contentList = task.getContentList();
//						if (contentList != null && contentList.size() == 1) {
//							Questionnaire questionnaire = questionnaireService.getAvailableByBusinessId(contentList.get(0), task.getBusiness_id());
//							questionnaire.setAccount_id(message.getAccount_id());
//							questionnaire.setBot_id(message.getBot_id());
//							MessageConverter.questionnaire2Message(questionnaire, message);
//						}
//						break;
//					case Constants.PUSH_TYPE_PRODUCT:
//						contentList = task.getContentList();
//						List<Product> productList = new ArrayList<Product>();
//						contentList.forEach(id-> {
//							Product product = productService.getProductById(id);
//							productList.add(product);
//						});
//						MessageConverter.product2Message(productList, message);
//						
//						break;
//					case Constants.PUSH_TYPE_DM:
//						Dm dm = dmService.getDmById(task.getContent_ids());
//						log.info(dm.getDm_image_url() + ", " + dm.getDm_image_height() + ", " + dm.getDm_image_width());
//						MessageConverter.dm2Message(dm, message);
//						break;
//					default:
//						break;
//					}
//					log.info("push message, type: " + message.getMessage_type() + ", to acount: " + message.getAccount_id() + ", in bot: " + message.getBot_id());
//					if(Constants.SOCIAL_MESSAGE_TYPE_TEXT.equals(message.getMessage_type())) {
//						log.info("text:"+message.getMessage_text());
//					}
//					messageService.addMessage(message);
//					Admin admin = agentAccountService.getLatestAdmin(message.getAccount_id(), message.getBot_id());
//					if (admin != null && MercueWebSocketClient.getInstance().isAdminOnline(admin.getAdmin_id())) {
//						BeanResponseBody response = new BeanResponseBody();
//						response.setCode(Constants.AGENT_SEND_A_MESSAGE);
//						response.setBean(message);
//						String  result = ObjectMapperUtil.mapObjectWithView(response,  View.Public.class);	
//						try {
//							MercueWebSocketClient.getInstance().send(Constants.WEB_SOCKET_MESSAGE_SEND_URL +admin.getAdmin_id(), result);
//						} catch (Exception e) {
//							log.info("error: " + e.getMessage());
//							e.printStackTrace();
//						}
//					}
//					MercueWebSocketClient.getInstance().push(message);
//					
//				});
//				

			
			pushService.changePushStatus(pushId, true);
		} catch (Exception e) {
		
			log.error(e.getMessage());
		}
		
	}


}
