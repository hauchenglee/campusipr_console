package biz.mercue.campusipr.dao;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
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

}
