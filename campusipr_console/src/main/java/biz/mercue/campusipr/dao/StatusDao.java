package biz.mercue.campusipr.dao;


import java.util.List;



import biz.mercue.campusipr.model.Status;


public interface StatusDao {

	Status getById(String id);
	
	Status getByEventClass(String countryId,String eventClass);
	
	Status getByEventCode(String eventCode);

	void create(Status bean);
	
	void delete(String id);
	
	
	List<Status> getByCountry(String countryId);
	



}
