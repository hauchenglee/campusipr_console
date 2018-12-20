package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Country;




@Repository("countryDao")
public class CountryDaoImpl extends AbstractDao<String,  Country> implements CountryDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	


	@Override
	public List<Country> getListByLanguage(String lang){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("country_lang", lang));
		criteria.addOrder(Order.asc("country_order"));
		return criteria.list();
	}
	
	

	
	
	@Override
	public Country getByLanguage(String countryId,String lang){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("country_id", countryId));
		criteria.add(Restrictions.eq("country_lang", lang));
		return (Country) criteria.uniqueResult();
	}
	
	

	

}
