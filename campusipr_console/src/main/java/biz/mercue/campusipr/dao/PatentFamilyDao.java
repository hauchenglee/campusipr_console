package biz.mercue.campusipr.dao;



import biz.mercue.campusipr.model.PatentFamily;

public interface PatentFamilyDao {

	PatentFamily getById(String id);
	


	void create(PatentFamily family);
	
	void delete(String id);
	
	

	
	
	

}
