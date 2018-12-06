package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.PushTask;



public interface PushService {

	void addPush(PushTask push);

	int updatePush(PushTask push);

	void deletePush(PushTask push);

	List<PushTask> getByBusinessId(String businessId);
	
	List<PushTask> getByAccount(String accountId);
	
	List<PushTask> getByAccountBussiness(String businessId, String accountId);

	List<PushTask> getByStatus(String businessId, boolean status);

	PushTask getById(String id);

	List<PushTask> getAvailableUnsendPush();

	void changePushStatus(String id, boolean status);


}
