package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.PushTask;




@Repository("pushDao")
public class PushDaoImpl extends AbstractDao<String,  PushTask> implements PushDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public PushTask getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(PushTask push) {
		push.setCreate_date(new Date());
		push.setUpdate_date(new Date());
		persist(push);
	}

	@Override
	public List<PushTask> getByBusinessId(String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("business_id", id));
		criteria.addOrder(Order.desc("push_date"));
		criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	
	@Override
	public List<PushTask> getByAccountBussiness(String businessId,String accountId){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.add(Restrictions.eq("business_id", accountId));
		criteria.addOrder(Order.desc("push_date"));
		criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	
	@Override
	public List<PushTask> getByAccount(String accountId){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("account_id", accountId));
		criteria.addOrder(Order.desc("push_date"));
		criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	

	
	@Override
	public List<PushTask> getByStatus(String businessId, boolean status) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("business_id", businessId));
		criteria.add(Restrictions.eq("is_send", status));
		criteria.addOrder(Order.desc("push_date"));
		criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	@Override
	public void delete(String id) {
		PushTask dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<PushTask> getAvailableUnsendPush() {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("is_send", false));
		criteria.add(Restrictions.eq("available", true));
		criteria.addOrder(Order.asc("push_date"));
		return criteria.list();
	}

}
