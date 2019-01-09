package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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

}
