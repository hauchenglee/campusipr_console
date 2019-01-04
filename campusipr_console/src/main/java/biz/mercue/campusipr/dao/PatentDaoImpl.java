package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
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
		return (Patent) criteria.uniqueResult();
	}
	
	
	@Override
	public Patent getByApplNo(String applNo) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("patent_appl_no", applNo));
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
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	
	@Override
	public int  getCountByBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("listBusiness","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> getByPatentIds(List<String> ids,String businessId){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.in("patent_id", ids));
		return criteria.list();
	}
	
	@Override
	public List<Patent> searchPatent(String  searchText,String businessId,int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	

	@Override
	public List<Patent> fieldSearchPatent(Patent patent,String businessId,int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public Patent getByPatentNo(String patentNo){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("patent_no", patentNo));
		return (Patent) criteria.uniqueResult();
	}

	

}
