package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Business;


public interface BusinessDao {

	Business getById(String id);

	void create(Business bean);
	
	void delete(String id);
	
	List<Business> getAll(int page,int pageSize);
	int getAllCount();

	List<Business> getAvailable(int page,int pageSize);
	
	
	List<Business> search(String text,int page,int pageSize);
	int searchCount(String text);
		
	List<Business> getByName(String name);
	
	List<Business> getByFuzzyName(String name);

}
