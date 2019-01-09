package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;



public interface PermissionService {
	List<Permission> getAllPermission();
	
	List<Permission> getPermissionListByRole(String roleId);

	List<Permission> getSettingPermissionByRole(String roleId);
	
	List<Permission> getPermissionListByAdmin(String adminId);
	
	Permission getSettingPermissionByRoleAndModule(String roleId, String module);
	
	Permission getSettingPermissionByModule(String code, String module);
	
	Role getByRole(String roleId);
	
	int updateRolePermission(Role role);
}
