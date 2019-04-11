package biz.mercue.campusipr.service;


import java.util.List;

import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.model.SynchronizeBusiness;




public interface AnnuityReminderService {

    void create(AnnuityReminder reminder);
	
//    int update(AnnuityReminder reminder);
    
    int update(List<AnnuityReminder> reminders, String bussinessId);
	
    List<AnnuityReminder> getByBusinessId(String businessId);
	
}
