package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;




@Repository("patentStatusDao")
public class PatentStatusDaoImpl extends AbstractDao<String,  PatentStatus> implements PatentStatusDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public PatentStatus getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public PatentStatus getByStatusAndPatent(String patentId, String StatusId, Date createTime) {
		// TODO Auto-generated method stub
		Session session = getSession();
		String queryStr = "SELECT ps from PatentStatus as ps "
				    + " LEFT JOIN ps.primaryKey.patent as p_aliase"
					+ " LEFT JOIN ps.primaryKey.status as s_aliase"
				    + " Where p_aliase.patent_id = :patent_id and s_aliase.status_id = :status_id"
					+ " and create_date = :create_date";
		Query q = session.createQuery(queryStr);
		q.setParameter("patent_id", patentId);
		q.setParameter("status_id", StatusId);
		q.setParameter("create_date", createTime);
		return (PatentStatus) q.uniqueResult();
	}
	
	@Override
	public List<PatentStatus> getByPatent(String patentId) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("patent_id", patentId));
		return criteria.list();
	}
	
	@Override
	public void create(PatentStatus ps) {
		persist(ps);
	}
	
}
