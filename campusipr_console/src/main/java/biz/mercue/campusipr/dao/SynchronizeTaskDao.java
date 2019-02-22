package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.SynchronizeTask;

public interface SynchronizeTaskDao {

	SynchronizeTask getById(String id);

	void create(SynchronizeTask reminder);
	
	void delete(String id);

	List<SynchronizeTask> getAvailableSynchronize();
	
	SynchronizeTask getAvailableBusinessId(String businessId);
}
