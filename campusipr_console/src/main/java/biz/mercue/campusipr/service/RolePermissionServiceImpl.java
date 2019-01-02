package biz.mercue.campusipr.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.cj.api.log.Log;

import biz.mercue.campusipr.dao.PermissionDao;
import biz.mercue.campusipr.model.Permission;


@Service("rolePermissionService")
@Transactional
public class RolePermissionServiceImpl implements PermissionService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private PermissionDao dao;
	
	@Override
	public List<Permission> getAllPermission() {
		// TODO Auto-generated method stub
		return dao.getAllPermission();
	}
	
	@Override
	public List<Permission> getPermissionListByRole(String roleId){
		return dao.getAllPermission();
	}
	
	
	
	@Override
	public Permission getPermissionByRole(String roleId, String module) {
		
		log.info("getPermissionByRole");
		
		log.info("roleId "+roleId);
		log.info("module "+module);
		
		return dao.getByRoleIdAndModule(roleId, module);
	}

}
