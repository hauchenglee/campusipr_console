package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.model.Status;



public interface StatusService {

	Status getById(String id);
	
	Status getByEventClass(String countryId,String eventClass);

	
	int patchAddStatus(List<Status> list);
	
	int addStatus(Status bean);
	
	int update(Status bean);
	
	int delete(Status bean);
	
	
	List<Status> getByCountry(String countryId);


}
