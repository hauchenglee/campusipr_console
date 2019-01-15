package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.FieldMap;




@Repository("fieldMapDao")
public class FieldMapDaoImpl extends AbstractDao<String,  FieldMap> implements FieldMapDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	
	
	@Override
	public FieldMap getById(String id){
		return getByKey(id);
	}
	
	
	@Override
	public void create(FieldMap bean) {
		persist(bean);
	}
	
	@Override
	public void delete(String id) {
		FieldMap dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<FieldMap> getByBusiness(String businessId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("task","task");
		criteria.createAlias("task.business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	@Override
	public List<FieldMap> getByAdmin(String adminId){
		Criteria criteria =  createEntityCriteria();
		//criteria.createAlias("task","task");
		criteria.createAlias("task.admin","admin");
		criteria.add(Restrictions.eq("admin.admin_id", adminId));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	


	
	
	

	
	
	

	

}
