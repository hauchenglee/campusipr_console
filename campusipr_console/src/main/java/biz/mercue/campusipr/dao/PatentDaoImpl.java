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

import biz.mercue.campusipr.model.Patent;




@Repository("patentDao")
public class PatentDaoImpl extends AbstractDao<String,  Patent> implements PatentDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public Patent getById(String id){
		return getByKey(id);
	}
	
	@Override
	public Patent getById(String businessId,String id) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("listBusiness","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.add(Restrictions.eq("patent_id", id));
		//criteria.addOrder(Order.desc("push_date"));
		//criteria.addOrder(Order.desc("push_type"));
		return (Patent) criteria.uniqueResult();
	}

	@Override
	public void create(Patent patent) {
		persist(patent);
	}
	
	@Override
	public void delete(String id) {
		Patent dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}

	@Override
	public List<Patent> getByBusinessId(String businessId,int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("listBusiness","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		//criteria.addOrder(Order.desc("push_date"));
		//criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	@Override
	public List<Patent> getByPatentIds(List<String> ids){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.in("patent_id", ids));
		//criteria.addOrder(Order.desc("push_date"));
		//criteria.addOrder(Order.desc("push_type"));
		return criteria.list();
	}
	
	

	
	
//	@Override
//	public List<Patent> getByAdminId(String adminId,int page,int pageSize){
//		log.info("123");
//		Criteria criteria =  createEntityCriteria();
//		criteria.add(Restrictions.eq("admin_id", adminId));
//		//criteria.addOrder(Order.desc("push_date"));
//		//criteria.addOrder(Order.desc("push_type"));
//			
//		return criteria.list();
//	}

	@Override
	public List<Patent> fieldSearchPatent(Patent patent) {
		Criteria criteria =  createEntityCriteria();
		//criteria.add(Restrictions.eq("admin_id", adminId));

		return criteria.list();
	}
	
	@Override
	public Patent getByPatentNo(String patentNo){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("patent_no", patentNo));
		return (Patent) criteria.uniqueResult();
	}

	

}
