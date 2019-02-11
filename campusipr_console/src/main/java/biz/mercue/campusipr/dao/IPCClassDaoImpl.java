package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;




@Repository("IPCClassDao")
public class IPCClassDaoImpl extends AbstractDao<String,  IPCClass> implements IPCClassDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public IPCClass getByIdAndVersion(String id) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("ipc_class_id", id));
		return (IPCClass) criteria.uniqueResult();
	}
	
	@Override
	public void create(IPCClass ipc) {
		persist(ipc);
	}
	
}
