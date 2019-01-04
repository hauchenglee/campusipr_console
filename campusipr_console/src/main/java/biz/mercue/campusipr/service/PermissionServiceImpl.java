package biz.mercue.campusipr.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antlr.collections.impl.LList;
import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.PermissionDao;
import biz.mercue.campusipr.dao.RoleDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;


@Service("permissionService")
@Transactional
public class PermissionServiceImpl implements PermissionService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private AdminDao admindao;
	
	@Autowired
	private RoleDao roledao;
	
	@Autowired
	private PermissionDao dao;
	
	@Override
	public List<Permission> getAllPermission() {
		// TODO Auto-generated method stub
		return dao.getAllPermission();
	}
	
	@Override
	public List<Permission> getPermissionListByRole(String roleId){
		List<Permission> list = null;
		Role role =roledao.getById(roleId);
		if(role != null) {
			list = role.getPermissionList();
			log.info("size :"+ list.size());
		}
		if(list == null) {
			list = new ArrayList<Permission>();
		}
		return list;
	}
	
	
	
	@Override
	public List<Permission> getPermissionListByAdmin(String adminId){
		List<Permission> list = null;
		Admin admin =admindao.getById(adminId);
		if(admin != null) {
			Role role = admin.getRole();
			if(role !=null) {
				list = role.getPermissionList();
				log.info("size :"+ list.size());
			}
		}
		if(list == null) {
			list = new ArrayList<Permission>();
		}
		return list;
	}
	
	@Override
	public List<Permission> getSettingPermissionByRole(String roleId) {
		
		log.info("getPermissionByRole");
		
		log.info("roleId "+roleId);
		
		return dao.getRolePermission(roleId);
	}
	
	
	
	@Override
	public Permission getSettingPermissionByRoleAndModule(String roleId, String module) {
		
		log.info("getPermissionByRoleAndModule");
		
		log.info("roleId "+roleId);
		log.info("module "+module);
		
		return dao.getByRoleIdAndModule(roleId, module);
	}
	
	

}
