package biz.mercue.campusipr.dao;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Permission;



@Repository("permissionDao")
public class PermissionDaoImpl extends AbstractDao<String,  Permission> implements PermissionDao {



	public List<Permission> getAllPermission() {
		Criteria crit = createEntityCriteria();	
		crit.addOrder(Order.asc("permission_group_order"));
		crit.addOrder(Order.asc("permission_order"));
		return crit.list();
	}
	
	public Permission getByRoleIdAndModule(String roleId,String module) {
		Criteria criteria = createEntityCriteria();	
	
		criteria.add(Restrictions.eq("role_id", roleId));
		criteria.add(Restrictions.eq("permission_name", module));
		return (Permission) criteria.uniqueResult();
	}

	
	public List<Permission> getRolePermission(String roleId) {
		Criteria crit = createEntityCriteria();	
		//TODO
		crit.addOrder(Order.asc("permission_group_order"));
		crit.addOrder(Order.asc("permission_order"));
		return crit.list();
	}
}
