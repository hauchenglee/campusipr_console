package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Message;

import java.util.List;

public interface MessageService {
    void addMessage(Message message, Admin admin);

    ListQueryForm getMessagesList(String senderId, String receiverId, int page, int pageSize);

    List<Message> getMessagesList(String senderId, String receiverId);

    List<Message> getMessagesBeforeTime(String senderId, String receiverId, long timeStamp);

    List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, long timeStamp);

    List<Message> getMessagesAfterTime(String senderId, String receiverId, long timeStamp);

    ListQueryForm getMessagesByAdminId(String adminId, String targetId, int page, int pageSize);

    List<Message> searchText(String senderId, String receiverId, String text);

    List<Admin> getAllAdminAndNewestMessage(AdminToken adminToken);

    void readMessage(String senderId, String receiverId);
}
