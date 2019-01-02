package biz.mercue.campusipr.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Banner;
import biz.mercue.campusipr.model.SysRolePermission;




@Repository("sysRolePermissionDao")
public class SysRolePermissionDaoImpl extends AbstractDao<String,  SysRolePermission> implements SysRolePermissionDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void deleteRolePermission(String roleId) {
		getSession().enableFilter("123");
	}
	@Override
	public void addSysRolePermission(SysRolePermission permission) {
		persist(permission);
	}
	
	
	

	
	

}
