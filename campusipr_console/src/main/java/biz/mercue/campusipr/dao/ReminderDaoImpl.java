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




@Repository("reminderDao")
public class ReminderDaoImpl extends AbstractDao<String,  ReminderTask> implements ReminderDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public ReminderTask getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(ReminderTask reminder) {
		persist(reminder);
	}

	@Override
	public void delete(String id) {
		ReminderTask dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<ReminderTask> getAvailableReminderByPatentId(String patentId) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("patent_id", patentId));
		criteria.add(Restrictions.eq("is_send", false));
		criteria.add(Restrictions.eq("is_remind", true));
		criteria.addOrder(Order.asc("task_date"));
		return criteria.list();
	}
	
	@Override
	public List<ReminderTask> getAvailableReminder() {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("is_send", false));
		criteria.add(Restrictions.eq("is_remind", true));
		criteria.addOrder(Order.asc("task_date"));
		return criteria.list();
	}

}
