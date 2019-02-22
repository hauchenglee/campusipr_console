package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.model.SynchronizeBusiness;




@Repository("synchronizeBusinessDao")
public class SynchronizeBusinessDaoImpl extends AbstractDao<String,  SynchronizeBusiness> implements SynchronizeBusinessDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public SynchronizeBusiness getByBusinessId(String businessId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","business");
		criteria.add(Restrictions.eq("business.business_id", businessId));
		return (SynchronizeBusiness) criteria.uniqueResult();
	}

	@Override
	public void create(SynchronizeBusiness sync) {
		persist(sync);
	}

	@Override
	public void delete(String id) {
		SynchronizeBusiness dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<SynchronizeBusiness> getAllSyncTask(){
		Criteria criteria =  createEntityCriteria();
		criteria.addOrder(Order.asc("random_time"));
		return criteria.list();
	}

}
