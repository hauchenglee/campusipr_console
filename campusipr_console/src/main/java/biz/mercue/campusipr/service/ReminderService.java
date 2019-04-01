package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.ReminderTask;



public interface ReminderService {

	void addReminder(ReminderTask reminder);

	int updateReminder(ReminderTask reminder);

	void deleteReminder(ReminderTask reminder);

	ReminderTask getById(String id);
	
	List<ReminderTask> getAvailableReminderByPatentId(String patentId);

	List<ReminderTask> getAvailableReminder();

	void changeRemindertatus(String id, boolean status);

}
