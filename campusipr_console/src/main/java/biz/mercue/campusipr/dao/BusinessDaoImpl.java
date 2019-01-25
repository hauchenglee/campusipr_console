package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Business;




@Repository("businessDao")
public class BusinessDaoImpl extends AbstractDao<String,  Business> implements BusinessDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public Business getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(Business bean) {
		persist(bean);
	}
	
	@Override
	public void delete(String id) {
		Business dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}

	@Override
	public List<Business> search(String searchText,int page,int pageSize){
		log.info("searchText:"+searchText);
		searchText = "%"+searchText+"%";
		Criteria criteria =  createEntityCriteria();
		Criterion c1 = Restrictions.like("business_name", searchText);
		Criterion c2 = Restrictions.like("business_name_en", searchText);
		Criterion c3 = Restrictions.like("business_alias", searchText);
		Criterion c4 = Restrictions.like("business_alias_en", searchText);
		criteria.add(Restrictions.or(c1,c2,c3,c4));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public List<Business> search(String searchText){
		log.info("searchText:"+searchText);
		searchText = "%"+searchText+"%";
		Criteria criteria =  createEntityCriteria();
		Criterion c1 = Restrictions.like("business_name", searchText);
		Criterion c2 = Restrictions.like("business_name_en", searchText);
		Criterion c3 = Restrictions.like("business_alias", searchText);
		Criterion c4 = Restrictions.like("business_alias_en", searchText);
		criteria.add(Restrictions.or(c1,c2,c3,c4));
		return criteria.list();
	}
	
	@Override
	public int searchCount(String searchText) {
		searchText = "%"+searchText+"%";
		Criteria criteria =  createEntityCriteria();
		Criterion c1 = Restrictions.like("business_name", searchText);
		Criterion c2 = Restrictions.like("business_name_en", searchText);
		Criterion c3 = Restrictions.like("business_alias", searchText);
		Criterion c4 = Restrictions.like("business_alias_en", searchText);
		criteria.add(Restrictions.or(c1,c2,c3,c4));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	
	@Override
	public List<Business> getAll(int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int getAllCount() {
		Criteria criteria =  createEntityCriteria();
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Business> getAvailable(int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public List<Business> getByName(String name) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		//criteria.addOrder(Order.asc("banner_order"));
		//criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	@Override
	public List<Business> getByFuzzyName(String name){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		//criteria.addOrder(Order.asc("banner_order"));
		//criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}


}
