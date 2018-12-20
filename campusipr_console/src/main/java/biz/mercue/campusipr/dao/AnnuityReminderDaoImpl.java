package biz.mercue.campusipr.dao;



import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.AnnuityReminder;




@Repository("annuityReminderDao")
public class AnnuityReminderDaoImpl extends AbstractDao<String,  AnnuityReminder> implements AnnuityReminderDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	
	@Override
	public void create(AnnuityReminder reminder) {
		persist(reminder);
	}
		
	
	@Override
	public AnnuityReminder getByBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		return (AnnuityReminder)criteria.uniqueResult();
	}
	
	
}
