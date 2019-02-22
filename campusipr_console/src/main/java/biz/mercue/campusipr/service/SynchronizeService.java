package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.SynchronizeTask;



public interface SynchronizeService {

	void addSync(SynchronizeTask sync);

	int updateSync(SynchronizeTask sync);

	void deleteSync(SynchronizeTask sync);

	SynchronizeTask getById(String id);

	List<SynchronizeTask> getAvailableSynchronize();

	void changeSyncStatus(String id, boolean status);

}
