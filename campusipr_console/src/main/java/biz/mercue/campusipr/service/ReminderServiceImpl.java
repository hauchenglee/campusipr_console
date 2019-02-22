package biz.mercue.campusipr.service;



import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PushDao;
import biz.mercue.campusipr.dao.ReminderDao;
import biz.mercue.campusipr.model.MessageTemplate;
import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;


@Service("reminderService")
@Transactional
public class ReminderServiceImpl implements ReminderService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ReminderDao reminderDao;

	@Override
	public void addReminder(ReminderTask reminder) {
		reminderDao.create(reminder);
		
	}

	@Override
	public int updateReminder(ReminderTask reminder) {
		ReminderTask dbBean = reminderDao.getById(reminder.getTask_id());
		if(dbBean!=null){


			dbBean.setTask_type(reminder.getTask_type());
			dbBean.setTask_date(reminder.getTask_date());
			dbBean.setPatent_id(reminder.getPatent_id());
			dbBean.setIs_send(reminder.is_send());
			dbBean.setIs_remind(reminder.is_remind());
			dbBean.setReminder_day(reminder.getReminder_day());

		}
		return 0;
	}

	@Override
	public void deleteReminder(ReminderTask reminder) {
		reminderDao.delete(reminder.getTask_id());
		
	}

	@Override
	public ReminderTask getById(String id) {
		return reminderDao.getById(id);
	}
	
	@Override
	public List<ReminderTask> getAvailableReminderByPatentId(String patentId) {
		return reminderDao.getAvailableReminderByPatentId(patentId);
	}

	@Override
	public List<ReminderTask> getAvailableReminder() {
		return reminderDao.getAvailableReminder();
	}



	@Override
	public void changeRemindertatus(String id, boolean status) {
		ReminderTask dbBean = reminderDao.getById(id);
		if(dbBean!=null){
			dbBean.setIs_send(status);
		}
	}


	


}
