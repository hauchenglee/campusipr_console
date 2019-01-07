package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Role;


public interface RoleService {
	
	//List<Role> getAllBusinessRole(String businessId);
	List<Role> getAllRole();
	Role getById(String roleId);
	//int addRole(Role bean);
	int updateRole(Role bean);
	//int deleteRole(Role bean);
}
