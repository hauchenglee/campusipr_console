package biz.mercue.campusipr.dao;

import java.util.List;

import biz.mercue.campusipr.model.Admin;



public interface AdminDao {
	
	Admin getById(String id);
	

	
	Admin getByEmail(String email);
	
	void createAdmin(Admin bean);

	void deleteAdmin(Admin bean);
	
	//List<Admin> getAvailableByBusinessId(String businessId);
	
	//List<Admin> getAllAdminList();
	
	//List<Admin> getByBusinessId(String businessId);
	
	List<Admin> getRoleBusinessAdminList(String roleId,String businessId,int page,int pageSize);
	int getRoleBusinessAdminCount(String roleId,String businessId);
	
//	List<Admin> getRoleAdminList(String roleId,int page,int pageSize);
//	int getRoleAdminCount(String roleId);
	
	List<Admin> searchRoleAdminList(String roleId,String businessId,String text,int page,int pageSize);
	int searchRoleAdminListCount(String roleId,String businessId,String text);
	


}
