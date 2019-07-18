package biz.mercue.campusipr.dao;

import java.util.List;

import biz.mercue.campusipr.util.Constants;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.mysql.cj.api.log.Log;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.util.StringUtils;


@Repository("adminDao")
public class AdminDaoImpl extends AbstractDao<String,  Admin> implements AdminDao {

	
	private Logger log = Logger.getLogger(this.getClass().getName());
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
		log.info("roleId:"+roleId);
		log.info("businessId:"+businessId);
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		if(!StringUtils.isNULL(businessId)) {
			crit.createAlias("business", "business");
			crit.add(Restrictions.eq("business.business_id", businessId));
		}
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}
	
	@Override
	public int getRoleBusinessAdminCount(String roleId,String businessId) {
		log.info("roleId:"+roleId);
		log.info("businessId:"+businessId);
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		if(!StringUtils.isNULL(businessId)) {
			crit.createAlias("business", "business");
			crit.add(Restrictions.eq("business.business_id", businessId));
		}
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
	}
	
	
	@Override
	public List<Admin> searchRoleAdminList(String roleId,String businessId,String text,int page,int pageSize){
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		if(!StringUtils.isNULL(businessId)) {
			crit.createAlias("business", "business");
			crit.add(Restrictions.eq("business.business_id", businessId));
		}
		Criterion field1 = Restrictions.like("admin_name", "%"+text+"%");
		Criterion field2 = Restrictions.like("admin_email", "%"+text+"%");
		crit.add(Restrictions.or(field1, field2));
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}
	
	@Override
	public int searchRoleAdminListCount(String roleId,String businessId,String text) {
		Criteria crit = createEntityCriteria();	
		crit.createAlias("role", "role");
		crit.add(Restrictions.eq("role.role_id", roleId));
		if(!StringUtils.isNULL(businessId)) {
			crit.createAlias("business", "business");
			crit.add(Restrictions.eq("business.business_id", businessId));
		}
		
		Criterion field1 = Restrictions.like("admin_name", "%"+text+"%");
		Criterion field2 = Restrictions.like("admin_email", "%"+text+"%");
		crit.add(Restrictions.or(field1, field2));
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
	}

	
//	@Override
//	public List<Admin> getRoleAdminList(String roleId,int page,int pageSize){
//		Criteria crit = createEntityCriteria();	
//		crit.createAlias("role", "role");
//		crit.add(Restrictions.eq("role.role_id", roleId));
//		crit.setFirstResult((page - 1) * pageSize);
//		crit.setMaxResults(pageSize);
//		return crit.list();
//	}
//	@Override
//	public int getRoleAdminCount(String roleId) {
//		Criteria crit = createEntityCriteria();	
//		crit.createAlias("role", "role");
//		crit.add(Restrictions.eq("role.role_id", roleId));
//		crit.setProjection(Projections.rowCount());
//		long count = (long)crit.uniqueResult();
//		return (int)count;
//	}
	
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
	

	@Override
	public List<Admin> getAllAdminList(){
		Criteria criteria = createEntityCriteria();
		return criteria.list();
	}

	@Override
	public List<Admin> getPlatformAdminList() {
		String hql = "select adm from Admin adm where adm.role.role_id = :ROLE_PLATFORM_MANAGER or adm.role.role_id = :ROLE_PLATFORM_PATENT";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("ROLE_PLATFORM_MANAGER", Constants.ROLE_PLATFORM_MANAGER);
		query.setParameter("ROLE_PLATFORM_PATENT", Constants.ROLE_PLATFORM_PATENT);
		return query.list();
	}

	@Override
	public List<Admin> getSchoolAdminList() {
		String hql = "select adm from Admin adm where adm.role.role_id = :ROLE_BUSINESS_MANAGER" +
				" or adm.role.role_id = :ROLE_BUSINESS_PATENT" +
				" or adm.role.role_id = :ROLE_COMMON_USER";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("ROLE_BUSINESS_MANAGER", Constants.ROLE_BUSINESS_MANAGER);
		query.setParameter("ROLE_BUSINESS_PATENT", Constants.ROLE_BUSINESS_PATENT);
		query.setParameter("ROLE_COMMON_USER", Constants.ROLE_COMMON_USER);
		return query.list();
	}



}
