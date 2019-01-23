package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.FieldMap;


public interface FieldMapDao {
	
	
	FieldMap getById(String id);

	void create(FieldMap bean);
	
	void delete(String id);


	List<FieldMap> getByBusiness(String businessId);
	
	List<FieldMap> getByAdmin(String adminId);
	
	
	List<FieldMap> getByTask(String taskId);
	



}
