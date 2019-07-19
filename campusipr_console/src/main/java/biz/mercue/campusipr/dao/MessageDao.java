package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Message;


public interface MessageDao {
	
	Message getById(String id);
	
	void addMessage(Message message);

	List<Message> getMessagesList(String senderId, String receiverId);

	List<Message> getMessagesBeforeTime(String senderId, String receiverId, long timeStamp);

	List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, long timeStamp);

	List<Message> getMessagesAfterTime(String senderId, String receiverId, long timeStamp);

	List<Message> getAdminMessages(String adminId,String targetId, int page, int pageSize);

	int  getCountAdminMessages(String adminId,String targetId);

	List<Message> searchText(String senderId, String receiverId, String text);

    Message getNewestMessage(String senderId);

	void readMessage(String senderId, String receiverId);
}
