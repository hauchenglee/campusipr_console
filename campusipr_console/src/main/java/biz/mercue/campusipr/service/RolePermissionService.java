package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;



public interface RolePermissionService {
	List<Permission> getAllPermission();
	
	
	
	List<Permission> getPermissionListByRole(String roleId);
	
	Permission getPermissionByRole(String roleId, String module);
}
