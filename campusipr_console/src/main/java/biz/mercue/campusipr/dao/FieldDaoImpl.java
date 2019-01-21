package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.PatentField;




@Repository("fieldDao")
public class FieldDaoImpl extends AbstractDao<String,  PatentField> implements FieldDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public PatentField getById(String id) {

		return getByKey(id);
	}
	
	@Override
	public List<PatentField> getSearableFields(){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("searchable", true));
		criteria.addOrder(Order.asc("field_order"));
		return criteria.list();
	}
	
	@Override
	public List<PatentField> getAllFields(){
		Criteria criteria =  createEntityCriteria();
		criteria.addOrder(Order.asc("field_order"));
		return criteria.list();
	}
	
	@Override
	public PatentField getByFieldCode(String code) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("filed_code", code));
		criteria.addOrder(Order.asc("field_order"));
		return (PatentField) criteria.uniqueResult();
	}
	
	
	@Override
	public PatentField getBySearchCode(String code) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("filed_search_code", code));
		criteria.addOrder(Order.asc("field_order"));
		return (PatentField) criteria.uniqueResult();
	}

	
	
	

	
	
	

	

}
