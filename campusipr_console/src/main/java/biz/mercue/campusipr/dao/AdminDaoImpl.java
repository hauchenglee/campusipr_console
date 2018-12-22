package biz.mercue.campusipr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Admin;


@Repository("adminDao")
public class AdminDaoImpl extends AbstractDao<String,  Admin> implements AdminDao {

	@Override
	public Admin getById(String id) {

		return getByKey(id);
	}
	
	@Override
	public List<Admin> getByBusinessId(String businessId) {
		Criteria crit = createEntityCriteria();	
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		return  crit.list();
	}
	
	@Override
	public Admin getByEmail(String email) {
		
		Criteria criteria = createEntityCriteria();	
		criteria.add(Restrictions.eq("admin_email", email));
		return (Admin) criteria.uniqueResult();
	}
	
	@Override
	public void createAdmin(Admin bean) {

		persist(bean);
	}
	
	@Override
	public void deleteAdmin(Admin bean) {

		delete(bean);
	}
	@Override
	public List<Admin> getAvailableByBusinessId(String businessId) {
		
		Criteria criteria = createEntityCriteria();	
		criteria.createAlias("business", "business");
		criteria.add(Restrictions.eq("business.business_id", businessId));

		
		return criteria.list();
	}
	




}
