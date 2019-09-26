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
		String hql = "select ps from PatentStatus ps where ps.primaryKey.patent.patent_id = :patentId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patentId", patentId);
		return query.list();
	}

	@Override
	public List<String> getStatusIds(String patentId) {
		String hql = "select ps.primaryKey.status.status_id from PatentStatus ps" +
				" join ps.primaryKey.status as jst" +
				" where ps.primaryKey.patent.patent_id = :patentId and jst.status_from = :s_from";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patentId", patentId);
		query.setParameter("s_from", "user");
		return query.list();
	}

	@Override
	public String checkStatusIdExist(String patentId, String statusId) {
		String queryStr = "select ps.primaryKey.status.status_id from PatentStatus as ps" +
				" join ps.primaryKey.status as jst" +
				" where ps.primaryKey.patent.patent_id = :patentId and jst.status_id = :statusId";
		Session session = getSession();
		Query query = session.createQuery(queryStr);
		query.setParameter("patentId", patentId);
		query.setParameter("statusId", statusId);
		return (String) query.uniqueResult();
	}

	@Override
	public void create(PatentStatus ps) {
		persist(ps);
	}

	@Override
	public void updateStatusPatent(List<String> statusId, String updateId) {
		String hql = "update PatentStatus ps set ps.primaryKey.patent.patent_id = :updateId" +
				" where ps.primaryKey.status.status_id in (:statusId)";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("updateId", updateId);
		query.setParameter("statusId", statusId);
		query.executeUpdate();
	}
}
