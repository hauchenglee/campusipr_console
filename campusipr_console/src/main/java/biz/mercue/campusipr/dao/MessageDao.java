package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Message;

import java.util.List;

public interface MessageDao {
    Message getById(String id);

    void addMessage(Message message);

    List<Message> getMessagesList(String senderId, String receiverId);

    List<Message> getMessagesBeforeTime(String senderId, String receiverId, long timeStamp);

    List<Message> getMessagesBeforeAndEqualTime(String senderId, String receiverId, long timeStamp);

    List<Message> getMessagesAfterTime(String senderId, String receiverId, long timeStamp);

    List<Message> getAdminMessages(String adminId, String targetId, int page, int pageSize);

    int getCountAdminMessages(String adminId, String targetId);

    List<Message> searchText(String senderId, String receiverId, String text);

    Message getNewestMessage(String senderId, String receiverId);

    void readMessage(String senderId, String receiverId);
}
