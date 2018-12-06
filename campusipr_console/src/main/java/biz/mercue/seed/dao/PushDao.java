package biz.mercue.seed.dao;


import java.util.List;

import biz.mercue.seed.model.PushTask;

public interface PushDao {

	PushTask getById(String id);

	void create(PushTask Push);
	
	void delete(String id);
	
	
	List<PushTask> getByBusinessId(String id);
	
	List<PushTask> getByAccount(String accountId);
	
	List<PushTask> getByAccountBussiness(String businessId,String accountId);
	
	List<PushTask> getByStatus(String businessId, boolean status);

	List<PushTask> getAvailableUnsendPush();
}
