package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Status;




@Repository("statuDao")
public class StatusDaoImpl extends AbstractDao<String,  Status> implements StatusDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public Status getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(Status bean) {
		persist(bean);
	}
	
	@Override
	public void delete(String id) {
		Status dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	@Override
	public Status getByEventClass(String countryId,String eventClass){
		
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("country_id", countryId));
		criteria.add(Restrictions.eq("event_class", eventClass));
		return (Status) criteria.uniqueResult();
	}

	@Override
	public List<Status> getByCountry(String countryId){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("country_id", countryId));
		return criteria.list();
	}
	
	@Override
	public Status getByEventCode(String eventCode,String countryId) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("event_code", eventCode));
		criteria.add(Restrictions.eq("country_id", countryId));
		return (Status) criteria.uniqueResult();
	}
	
	@Override
	public Status getByEditCode(String StatusDesc) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("status_desc", StatusDesc));
		criteria.add(Restrictions.eq("status_from", "sys"));
		List list = criteria.list();
		Status status = null;
		if (!list.isEmpty()) {
			status = (Status) list.get(0);
		}
		return status;
	}
	
	
	@Override
	public List<Status> getEditable(){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("status_from", "sys"));
		return criteria.list();
	}
	
	@Override
	public void updateStatus(Status status) {
		String statusId = status.getStatus_id();
		String statusDesc = status.getStatus_desc();
		String statusDescEn = status.getStatus_desc_en();
		String statusColor = status.getStatus_color();
		String hql = "Update Status s set s.status_desc = :statusDesc, s.status_desc_en = :statusDescEn, s.status_color = :statusColor " +
				"where s.status_id = :statusId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("statusDesc", statusDesc);
		query.setParameter("statusDescEn", statusDescEn);
		query.setParameter("statusColor", statusColor);
		query.setParameter("statusId", statusId);
		query.executeUpdate();
	}
}