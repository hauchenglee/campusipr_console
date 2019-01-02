package biz.mercue.campusipr.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.cj.api.log.Log;

import biz.mercue.campusipr.dao.PermissionDao;
import biz.mercue.campusipr.dao.SysRolePermissionDao;
import biz.mercue.campusipr.model.Permission;


@Service("sysRolePermissionService")
@Transactional
public class SysRolePermissionServiceImpl implements SysRolePermissionService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private SysRolePermissionDao dao;

	@Override
	public void updatesysRole(String roleId, List<Permission> listPermission) {
		
		
	}
	
	

}
