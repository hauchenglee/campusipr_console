package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.SynchronizeBusiness;

public interface SynchronizeBusinessDao {

	SynchronizeBusiness getByBusinessId(String id);

	void create(SynchronizeBusiness sync);
	
	void delete(String id);
	
	List<SynchronizeBusiness> getAllSyncTask();
	
}
