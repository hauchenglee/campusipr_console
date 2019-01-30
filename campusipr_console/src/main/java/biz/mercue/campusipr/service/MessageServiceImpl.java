package biz.mercue.campusipr.service;


import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
		mDao.addMessage(message);
	}

	
	

	
	@Override
	public List<Message> getMessagesAfterTime(String adminId,String targetId, Date time) {
		return mDao.getMessagesAfterTime(adminId, targetId, time);
	}



	
	
	@Override
	public ListQueryForm getMessagesByAdminId(String adminId,String targetId, int page, int pageSize) {
		
		int count = mDao.getCountAdminMessages(adminId,targetId);
		log.info("admin :"+adminId +"/ target id : "+targetId);
		log.info("count: "+count);
		List<Message> list = mDao.getAdminMessages(adminId, targetId, page, pageSize);
		ListQueryForm form = new ListQueryForm(count, pageSize, list);
		return form;
	}
	

}
