package biz.mercue.campusipr.service;


import java.util.List;
import java.util.Map;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Permission;



public interface AdminService {
	
	
	public Admin getById(String id);
	
	public Admin getByEmail(String email);
	
	public int login(String email,String password);
	
	public int logout(String adminId);
	
	public List<Admin> getListByBusinessId(String businessId);
	
	public Map<String,Admin> getMapByBusinessId(String businessId);
	
	public int createAdmin(Admin admin);

	public int updateAdmin(Admin admin);

	public int deleteAdmin(Admin admin);
	
	public List<Permission> getPermissionById(String adminId);
}
