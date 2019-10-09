package biz.mercue.campusipr.dao;



import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.AnnuityReminder;




@Repository("annuityReminderDao")
public class AnnuityReminderDaoImpl extends AbstractDao<String,  AnnuityReminder> implements AnnuityReminderDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	
	@Override
	public AnnuityReminder getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public void create(AnnuityReminder reminder) {
		persist(reminder);
	}
		
	
	@Override
	public List<AnnuityReminder> getByBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.addOrder(Order.desc("email_day"));
		return criteria.list();
	}

	@Override
	public List<AnnuityReminder> getAvailableByBusinessId(String businessId) {
		String hql = "select ar from AnnuityReminder as ar join ar.business as bus " +
				"where bus.business_id = :businessId and ar.available = :available " +
				"order by ar.email_day desc";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("businessId", businessId);
		query.setParameter("available", true);
		return query.list();
	}

	@Override
	public List<AnnuityReminder> getByBusinessIds(List<String> businessIds) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.in("bs.business_id", businessIds));
		return criteria.list();
	}
	
	
}
