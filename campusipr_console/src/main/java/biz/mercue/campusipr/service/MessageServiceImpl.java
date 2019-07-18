package biz.mercue.campusipr.service;


import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
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

	@Override
	public void addMessage(Message message) {
		try {
			message.setMessage_id(KeyGeneratorUtils.generateRandomString());
			message.setMessage_date(new Date());
			mDao.addMessage(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
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
		return mDao.getMessagesList(senderId, receiverId);
	}

	@Override
	public List<Message> getMessagesBeforeTime(String senderId, String receiverId, Date timeStamp) {
		return mDao.getMessagesBeforeTime(senderId, receiverId, timeStamp);
	}

	@Override
	public List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, Date timeStamp) {
		return mDao.getMessagesBeforeAndEqualTime(senderId, receiverId, timeStamp);
	}

	@Override
	public List<Message> getMessagesAfterTime(String senderId, String receiverId, Date timeStamp) {
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
			List<Admin> adminList;
			if (adminToken.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
				log.info("user is platform: return all school member");
				adminList = adminDao.getSchoolAdminList();
				for (Admin school : adminList) {
					Message dbMessage = mDao.getNewestMessage(school.getAdmin_id());
					school.setMessage(dbMessage);
				}
			} else {
				log.info("user is school: return all platform member");
				adminList = adminDao.getPlatformAdminList();
				for (Admin platform : adminList) {
					Message dbMessage = mDao.getNewestMessage(platform.getAdmin_id());
					platform.setMessage(dbMessage);
				}
			}
			return adminList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void readMessage(String senderId, String receiverId) {
		mDao.readMessage(senderId, receiverId);
	}
}
