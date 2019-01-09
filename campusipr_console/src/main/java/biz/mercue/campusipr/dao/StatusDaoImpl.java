package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.Status;




@Repository("statusDao")
public class StatusDaoImpl extends AbstractDao<String,  Status> implements StatusDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Status getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public void create(Status status) {
		persist(status);
	}
	
	@Override
	public Status getByEventCode(String eventCode) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("event_code", eventCode));
		return (Status) criteria.uniqueResult();
	}

}
