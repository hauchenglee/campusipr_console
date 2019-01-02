package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
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
	public List<Portfolio> getByBusinessId(String businessId,int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int getCountByBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("business","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	

	
	

	

}
