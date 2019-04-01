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
import biz.mercue.campusipr.model.SynchronizeTask;




@Repository("synchronizeTaskDao")
public class SynchronizeTaskDaoImpl extends AbstractDao<String,  SynchronizeTask> implements SynchronizeTaskDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public SynchronizeTask getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(SynchronizeTask reminder) {
		persist(reminder);
	}

	@Override
	public void delete(String id) {
		SynchronizeTask dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<SynchronizeTask> getAvailableSynchronize() {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("is_sync", false));
		criteria.addOrder(Order.asc("task_date"));
		return criteria.list();
	}
	
	@Override
	public SynchronizeTask getAvailableBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("is_sync", false));
		criteria.add(Restrictions.eq("business_id", businessId));
		criteria.addOrder(Order.asc("task_date"));
		return (SynchronizeTask) criteria.uniqueResult();
	}

}
