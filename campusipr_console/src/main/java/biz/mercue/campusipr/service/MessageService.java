package biz.mercue.campusipr.service;


import java.util.Date;
import java.util.List;


import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Message;



public interface MessageService {
	

	void addMessage(Message message);

	ListQueryForm getMessagesList(String senderId, String receiverId, int page, int pageSize);

	List<Message> getMessagesList(String senderId, String receiverId);

	List<Message> getMessagesBeforeTime(String senderId, String receiverId, long timeStamp);

	List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, long timeStamp);

	List<Message> getMessagesAfterTime(String senderId, String receiverId, long timeStamp);

	ListQueryForm getMessagesByAdminId(String adminId, String targetId,int page, int pageSize);


	List<Message> searchText(String senderId, String receiverId, String text);

    List<Admin> getAllAdminAndNewestMessage(AdminToken adminToken);

	void readMessage(String senderId, String receiverId);
}
