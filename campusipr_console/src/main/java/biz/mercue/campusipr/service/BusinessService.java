package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;



public interface BusinessService {

	void addBusiness(Business business);
	
	int safeAddBusiness(Business business);

	int updateBusiness(Business business);

	void deleteBusiness(Business business);
	
	ListQueryForm getAll(int page);
	
	ListQueryForm search(String text,int page);

	List<Business> getAvailable(int page,int pageSize);
	
	Business getById(String id);
	
	
	List<Business> getByName(String name);
	
	List<Business> getByFuzzyName(String name);

}
