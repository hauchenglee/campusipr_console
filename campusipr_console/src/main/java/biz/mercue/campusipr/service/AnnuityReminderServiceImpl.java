package biz.mercue.campusipr.service;



import java.util.Date;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AnnuityReminderDao;
import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.util.Constants;


@Service("annuityReminderService")
@Transactional
public class AnnuityReminderServiceImpl implements AnnuityReminderService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private AnnuityReminderDao annuityReminderDao;
	
	

	@Override
	public void create(AnnuityReminder reminder) {
		reminder.setCreate_date(new Date());
		annuityReminderDao.create(reminder);
    }
	
	@Override
	public int update(AnnuityReminder reminder) {
		AnnuityReminder dbBean = annuityReminderDao.getByBusinessId(reminder.getBusiness().getBusiness_id());

		if(dbBean!=null){
			dbBean.setEmail_day(reminder.getEmail_day());
			dbBean.setPhone_day(reminder.getPhone_day());
			dbBean.setAvailable(reminder.isAvailable());
			dbBean.setUpdate_date(new Date());
			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
    }
	
	@Override
	public AnnuityReminder getByBusinessId(String businessId) {
		return annuityReminderDao.getByBusinessId(businessId);
	}


}
