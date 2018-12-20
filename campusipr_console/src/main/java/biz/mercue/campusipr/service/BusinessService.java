package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Business;



public interface BusinessService {

	void addBusiness(Business business);
	
	int saveAddBusiness(Business business);

	int updateBusiness(Business business);

	void deleteBusiness(Business business);
	
	List<Business> getAll();

	List<Business> getAvailable(int page,int pageSize);
	
	Business getById(String id);
	
	
	List<Business> getByName(String name);
	
	List<Business> getByFuzzyName(String name);

}
