package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;



public interface SysRolePermissionService {
	
	void updatesysRole(String roleId, List<Permission> listPermission );
}
