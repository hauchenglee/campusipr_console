package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.SynchronizeBusiness;



public interface SynchronizeBusinessService {

	void addSyncBusiness(SynchronizeBusiness sync);

	int updateSyncBusiness(SynchronizeBusiness sync);

	void deleteSyncBusiness(SynchronizeBusiness sync);

	SynchronizeBusiness getByBusinessId(String id);

}
