package biz.mercue.campusipr.dao;



import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import org.hibernate.criterion.Restrictions;
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
		return criteria.list();
	}

	@Override
	public List<AnnuityReminder> getByBusinessIds(List<String> businessIds) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.in("bs.business_id", businessIds));
		return criteria.list();
	}
	
	
}
