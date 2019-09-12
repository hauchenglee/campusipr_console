package biz.mercue.campusipr.service;


import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import biz.mercue.campusipr.dao.BusinessDao;
import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.MessageDao;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Message;



@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MessageDao mDao;
	
	@Autowired
	private AdminDao adminDao;

	@Autowired
	private BusinessDao businessDao;

	@Override
	public void addMessage(Message message, Admin admin) {
		try {
			message.setMessage_id(KeyGeneratorUtils.generateRandomString());
			Date now = new Date();
			message.setMessage_date(now.getTime());
			message.setIs_read(false);
			message.setAdmin(admin);
			message.setBusiness(admin.getBusiness());
			mDao.addMessage(message);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public ListQueryForm getMessagesList(String senderId, String receiverId, int page, int pageSize) {
		int count = mDao.getCountAdminMessages(senderId, receiverId);
		List<Message> messageList = mDao.getMessagesList(senderId, receiverId);
		return new ListQueryForm(count, pageSize, messageList);
	}

	@Override
	public List<Message> getMessagesList(String senderId, String receiverId) {
		List<Message> messageList = mDao.getMessagesList(senderId, receiverId);
		Collections.reverse(messageList);
		return messageList;
	}

	@Override
	public List<Message> getMessagesBeforeTime(String senderId, String receiverId, long timeStamp) {
		List<Message> previousMessage = mDao.getMessagesBeforeTime(senderId, receiverId, timeStamp);
		Collections.reverse(previousMessage);
		return previousMessage;
	}

	@Override
	public List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, long timeStamp) {
		return mDao.getMessagesBeforeAndEqualTime(senderId, receiverId, timeStamp);
	}

	@Override
	public List<Message> getMessagesAfterTime(String senderId, String receiverId, long timeStamp) {
		return mDao.getMessagesAfterTime(senderId, receiverId, timeStamp);
	}

	@Override
	public ListQueryForm getMessagesByAdminId(String senderId,String receiverId, int page, int pageSize) {
		int count = mDao.getCountAdminMessages(senderId, receiverId);
		log.info("admin :" + senderId + "/ target id : " + receiverId);
		log.info("count: " + count);
		List<Message> messageList = mDao.getAdminMessages(senderId, receiverId, page, pageSize);
		return new ListQueryForm(count, pageSize, messageList);
	}

	@Override
	public List<Message> searchText(String senderId, String receiverId, String text) {
		return mDao.searchText(senderId, receiverId, text);
	}

	@Override
	public List<Admin> getAllAdminAndNewestMessage(AdminToken adminToken) {
		try {
			List<Admin> adminList = new ArrayList<>();
			if (adminToken.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
//				log.info("user is platform: return all school member");
				adminList = adminDao.getSchoolAdminList();
				for (Admin school : adminList) {
					Message dbMessage = mDao.getNewestMessage(school.getAdmin_id(), Constants.BUSINESS_PLATFORM);
					if (dbMessage != null) {
						school.setMessage(dbMessage);
					} else {
						school.setMessage(null);
					}
				}
			} else {
//				log.info("user is school: return public platform");
				Business platformBusiness = businessDao.getById(Constants.BUSINESS_PLATFORM);
				Admin platformAdmin = new Admin();
				platformAdmin.setAdmin_id("public_platform");
				platformAdmin.setAdmin_name("中心平台");
				platformAdmin.setBusiness(platformBusiness);
				adminList.add(platformAdmin);
				for (Admin platform : adminList) {
					Message dbMessage = mDao.getNewestMessage(Constants.BUSINESS_PLATFORM, adminToken.getAdmin().getAdmin_id());
					if (dbMessage != null) {
						platform.setMessage(dbMessage);
					} else {
						platform.setMessage(null);
					}
				}
			}
			return adminList;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public void readMessage(String senderId, String receiverId) {
		mDao.readMessage(senderId, receiverId);
	}
}
