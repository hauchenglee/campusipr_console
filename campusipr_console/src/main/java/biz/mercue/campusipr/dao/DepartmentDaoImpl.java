package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Department;


@Repository("departmentDao")
public class DepartmentDaoImpl extends AbstractDao<String, Department> implements DepartmentDao{

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public Department getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Department department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(String patentId, String businessId) {
		String hql = "DELETE FROM Department d where d.patent.patent_id = :patentId and d.business_id = :businessId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patentId", patentId);
		query.setParameter("businessId", businessId);
		query.executeUpdate();
	}
	
	@Override
	public List<Department> getByPatentId(String patentId) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		return criteria.list();
	}
}
