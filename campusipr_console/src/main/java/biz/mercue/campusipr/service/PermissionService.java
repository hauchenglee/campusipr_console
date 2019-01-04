package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;



public interface PermissionService {
	List<Permission> getAllPermission();
	
	List<Permission> getPermissionListByRole(String roleId);
	
	List<Permission> getSettingPermissionByRole(String roleId);
	
	Permission getSettingPermissionByRoleAndModule(String roleId, String module);
	
	List<Permission> getPermissionListByAdmin(String adminId);
}
