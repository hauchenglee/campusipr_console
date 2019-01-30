package biz.mercue.campusipr.service;


import java.util.Date;
import java.util.List;


import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Message;



public interface MessageService {
	

	void addMessage(Message message);
	

	List<Message> getMessagesAfterTime(String adminId,String targetId, Date time);

	ListQueryForm getMessagesByAdminId(String adminId, String targetId,int page, int pageSize);
	

}
