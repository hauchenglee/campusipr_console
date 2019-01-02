package biz.mercue.campusipr.service;


import java.util.List;
import java.util.Map;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Permission;



public interface AdminService {
	
	
	public Admin getById(String id);
	
	public Admin getByEmail(String email);
	
	public int login(String email,String password);
	
	public int logout(String adminId);
	

	
	//public Map<String,Admin> getMapByBusinessId(String businessId);
	
	public int createAdmin(Admin admin);

	public int updateAdmin(Admin admin);

	public int deleteAdmin(Admin admin);
	
	public int updatePassword(String adminId,String password);
	
	
	
	//public ListQueryForm getListByBusinessId(String businessId);
	
	//public List<Admin> getAllBusiness();
	
	public ListQueryForm getRoleBusinessAdminList(String roleId,String businessId,int page);

	
	public ListQueryForm getRoleAdminList(String roleId,int page);
	
	//public List<Permission> getPermissionById(String adminId);
}
