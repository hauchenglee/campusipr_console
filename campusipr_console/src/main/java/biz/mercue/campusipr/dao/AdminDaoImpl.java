package biz.mercue.campusipr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Admin;


@Repository("adminDao")
public class AdminDaoImpl extends AbstractDao<String,  Admin> implements AdminDao {

	@Override
	public Admin getById(String id) {

		return getByKey(id);
	}
	
//	@Override
//	public List<Admin> getByBusinessId(String businessId) {
//		Criteria crit = createEntityCriteria();	
//		crit.createAlias("business", "business");
//		crit.add(Restrictions.eq("business.business_id", businessId));
//		return  crit.list();
//	}
	
	
	@Override
	public List<Admin> getRoleBusinessAdminList(String roleId,String businessId,int page,int pageSize){
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}
	
	@Override
	public int getRoleBusinessAdminCount(String roleId,String businessId) {
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		crit.createAlias("business", "business");
		crit.add(Restrictions.eq("business.business_id", businessId));
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Admin> getRoleAdminList(String roleId,int page,int pageSize){
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}
	@Override
	public int getRoleAdminCount(String roleId) {
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
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
	
	
//	@Override
//	public List<Admin> getAvailableByBusinessId(String businessId) {
//		
//		Criteria criteria = createEntityCriteria();	
//		criteria.createAlias("business", "business");
//		criteria.add(Restrictions.eq("business.business_id", businessId));
//
//		
//		return criteria.list();
//	}
	
	
//	@Override
//	public List<Admin> getAllAdminList(){
//		Criteria criteria = createEntityCriteria();	
//		return criteria.list();
//	}




}
