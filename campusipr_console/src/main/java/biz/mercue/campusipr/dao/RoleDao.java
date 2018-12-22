package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Role;



public interface RoleDao {
	
	Role getById(String id);
	
	Role getById(String id,String businessId);
	
	Role getByName(String businessId,String name);
	
	List<Role> getAllBusinessRole(String businessId);
	
	void addRole(Role role);
	
	void deleteRole(Role role);

	Role getByBusinessAndName(String businessId, String name);
	


}
