package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Permission;



public interface PermissionDao {
	
	List<Permission> getAllPermission();
	List<Permission> getRolePermission(String roleId);
	
	Permission getByRoleIdAndModule(String roleId,String module);

}
