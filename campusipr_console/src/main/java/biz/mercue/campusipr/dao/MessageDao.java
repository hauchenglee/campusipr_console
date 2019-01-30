package biz.mercue.campusipr.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import biz.mercue.campusipr.model.Message;


public interface MessageDao {
	
	Message getById(String id);
	
	public List<Message> getAdminMessagesAfterTime(String id, Date time);

	void addMessage(Message message);

	List<Message> getAccountMessages(String botId, String accountId, int page, int range);

	Message getLatestMessage(String botId, String accountId);
	
	List<Message> getAccountsLastestMessage(List<String> adminIdList);
	
	List<Message> getAccountsLastestMessage(String adminId);
	
	void deleteMessage(Message message);
	
	int deleteByBusiess(String businessId);

	Message getByIdAndAccountId(String id,String account);
	
	List<Message> getOfflineMessage(String businessId, int page, int range);
	
	List<Message> getOfflineMessageByAccount(String botId, String accountId);
	
	List<Message> getAdminUnreadMessage(String adminId);
	
	List<Message> getHistoryMessage(String businessId, int page, int range);

	int getUnreadMessagesNum(String botId, String id);

	List<Message> getUnreadMessages(String botId, String id);

	List<Message> searchAccountMessages(String botId, String accountId, String keyword, int page, int range);
	
	int countMessages(String accountId, String botId);
	
	List<Message> getNotifyMessages(String botId, String accountId);
	
	Map<String, BigInteger> countPushMessages(String accountId, String botId);
	
	Map<String, BigInteger> countSendMessages(String accountId, String botId);
	
	Map<String, BigInteger> countReceiveMessages(String accountId, String botId);
}
