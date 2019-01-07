package biz.mercue.campusipr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Role;



@Repository("roleDao")
public class RoleDaoImpl extends AbstractDao<String,  Role> implements RoleDao {

	@Override
	public Role getById(String id){

		return getByKey(id);
	}

	@Override
	public Role getById(String id,String businessId) {
		Criteria crit = createEntityCriteria();    
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		crit.add(Restrictions.eq("role_id", id));
		return (Role) crit.uniqueResult();
	}

	@Override
	public Role getByName(String businessId,String name) {
		Criteria crit = createEntityCriteria();    
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		crit.add(Restrictions.eq("role_name", name));
		return (Role) crit.uniqueResult();
	}
	
	@Override
	public List<Role> getAll(){
		Criteria crit = createEntityCriteria();  
		crit.addOrder(Order.desc("role_level"));
		return crit.list();
	}

	@Override
	public List<Role> getAllBusinessRole(String businessId) {
		Criteria crit = createEntityCriteria();    
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		return crit.list();
	}

	@Override
	public Role getByBusinessAndName(String businessId, String name) {
		Criteria crit = createEntityCriteria();
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		crit.add(Restrictions.eq("role_name", name));
		crit.setMaxResults(1);
		return (Role) crit.uniqueResult();
	}

	@Override
	public void addRole(Role role) {
		persist(role);
	}

	@Override
	public void deleteRole(Role role) {
		delete(role);
	}



}
