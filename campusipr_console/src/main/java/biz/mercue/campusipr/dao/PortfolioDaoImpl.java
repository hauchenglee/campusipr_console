package biz.mercue.campusipr.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Portfolio;




@Repository("portfolioDao")
public class PortfolioDaoImpl extends AbstractDao<String,  Portfolio> implements PortfolioDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public Portfolio getById(String id){
		return getByKey(id);
	}
	
	@Override
	public Portfolio getById(String businessId,String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.add(Restrictions.eq("portfolio_id", id));
		//criteria.addOrder(Order.desc("push_date"));
		//criteria.addOrder(Order.desc("push_type"));
		return (Portfolio) criteria.uniqueResult();
	}

	@Override
	public void create(Portfolio portfolio) {
		persist(portfolio);
	}
	
	@Override
	public void delete(String id) {
		Portfolio dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}

	@Override
	public List<Portfolio> getByBusinessId(String id,int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		//criteria.add(Restrictions.eq("business_id", id));
		//criteria.addOrder(Order.desc("push_date"));
		//criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	

	
	

	

}
