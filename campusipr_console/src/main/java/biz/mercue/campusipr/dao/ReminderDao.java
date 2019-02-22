package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.ReminderTask;

public interface ReminderDao {

	ReminderTask getById(String id);

	void create(ReminderTask reminder);
	
	void delete(String id);
	
	List<ReminderTask> getAvailableReminderByPatentId(String patentId);

	List<ReminderTask> getAvailableReminder();
}
