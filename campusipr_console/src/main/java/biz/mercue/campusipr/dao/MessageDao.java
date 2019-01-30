package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Message;


public interface MessageDao {
	
	Message getById(String id);
	
	void addMessage(Message message);
	
	List<Message> getMessagesAfterTime(String adminId,String targetId, Date time);

	List<Message> getAdminMessages(String adminId,String targetId, int page, int pageSize);
	
	int  getCountAdminMessages(String adminId,String targetId);

	
	

}
