package biz.mercue.campusipr.dao;




import java.util.List;

import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.model.Business;


public interface AnnuityReminderDao {

	AnnuityReminder getById(String id);

	void create(AnnuityReminder reminder);
		
	List<AnnuityReminder> getByBusinessId(String businessId);

	List<AnnuityReminder> getAvailableByBusinessId(String businessId);

	List<AnnuityReminder> getByBusinessIds(List<String> businessIds);

}
