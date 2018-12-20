package biz.mercue.campusipr.dao;




import biz.mercue.campusipr.model.AnnuityReminder;


public interface AnnuityReminderDao {



	void create(AnnuityReminder reminder);
		
	
	AnnuityReminder getByBusinessId(String businessId);
	

}
