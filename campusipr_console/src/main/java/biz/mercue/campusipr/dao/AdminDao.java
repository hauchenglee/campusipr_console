package biz.mercue.campusipr.dao;

import java.util.List;

import biz.mercue.campusipr.model.Admin;



public interface AdminDao {
	
	Admin getById(String id);
	
	List<Admin> getByBusinessId(String businessId);
	
	Admin getByEmail(String email);
	
	void createAdmin(Admin bean);

	void deleteAdmin(Admin bean);
	
	List<Admin> getAvailableByBusinessId(String businessId);
	


}
