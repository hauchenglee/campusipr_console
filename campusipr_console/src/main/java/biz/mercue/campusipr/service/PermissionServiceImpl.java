package biz.mercue.campusipr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PermissionDao;
import biz.mercue.campusipr.model.Permission;


@Service("permissionService")
@Transactional
public class PermissionServiceImpl implements PermissionService{

	
	@Autowired
	private PermissionDao dao;
	
	@Override
	public List<Permission> getAllPermission() {
		// TODO Auto-generated method stub
		return dao.getAllPermission();
	}

}
