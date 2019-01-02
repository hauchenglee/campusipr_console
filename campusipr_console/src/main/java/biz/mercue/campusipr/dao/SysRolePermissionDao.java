package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.SysRolePermission;


public interface SysRolePermissionDao {

	void deleteRolePermission(String roleId);
	void addSysRolePermission(SysRolePermission permission);

}
