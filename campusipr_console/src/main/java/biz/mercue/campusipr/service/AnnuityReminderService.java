package biz.mercue.campusipr.service;


import biz.mercue.campusipr.model.AnnuityReminder;




public interface AnnuityReminderService {



    void create(AnnuityReminder reminder);
	
    void update(AnnuityReminder reminder);
	
	AnnuityReminder getByBusinessId(String businessId);


}
