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

import biz.mercue.campusipr.model.Banner;




@Repository("bannerDao")
public class BannerDaoImpl extends AbstractDao<String,  Banner> implements BannerDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public Banner getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(Banner banner) {
		persist(banner);
	}
	
	@Override
	public void delete(String id) {
		Banner dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}

	@Override
	public List<Banner> getAll(){
		Criteria criteria =  createEntityCriteria();
		criteria.addOrder(Order.asc("banner_order"));
		return criteria.list();
	}
	
	@Override
	public List<Banner> getAvailable(){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		criteria.addOrder(Order.asc("banner_order"));
		//criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}

}
