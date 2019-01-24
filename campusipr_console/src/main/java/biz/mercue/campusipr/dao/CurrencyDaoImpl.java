package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;


import biz.mercue.campusipr.model.Currency;




@Repository("currencyDao")
public class CurrencyDaoImpl extends AbstractDao<String,  Currency> implements CurrencyDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	
	@Override
	public List<Currency> getAll(){
		Criteria criteria =  createEntityCriteria();
		return criteria.list();
	}

	
	

	

}
