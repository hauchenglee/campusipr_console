package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
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
	public List<Patent> getAllByBusinessId(String businessId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("listBusiness","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
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
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Patent> searchPatent(String  searchText,String businessId,int page,int pageSize){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("listBusiness","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		Criterion re1 = Restrictions.like("patent_name", searchText);
		Criterion re2 = Restrictions.like("patent_name_en", searchText);
		Criterion re3 = Restrictions.like("patent_appl_country", searchText);
		Criterion re4 = Restrictions.like("patent_appl_no", searchText);
		Criterion re5 = Restrictions.like("patent_notice_no", searchText);
		Criterion re6 = Restrictions.like("patent_publish_no", searchText);
		Criterion re7 = Restrictions.like("patent_no", searchText);
		criteria.createAlias("patentContext", "pc", CriteriaSpecification.LEFT_JOIN);
		Criterion re14 = Restrictions.like("pc.context_abstract", searchText);
		Criterion re15 = Restrictions.like("pc.context_desc", searchText);
		Criterion re16 = Restrictions.like("pc.context_claim", searchText);
		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6,re7,re14,re15,re16));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
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

	@Override
	public int searchCountPatent(String searchText, String businessId) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("listBusiness","bs");
		criteria.add(Restrictions.eq("bs.business_id", businessId));
		Criterion re1 = Restrictions.like("patent_name", searchText);
		Criterion re2 = Restrictions.like("patent_name_en", searchText);
		Criterion re3 = Restrictions.like("patent_appl_country", searchText);
		Criterion re4 = Restrictions.like("patent_appl_no", searchText);
		Criterion re5 = Restrictions.like("patent_notice_no", searchText);
		Criterion re6 = Restrictions.like("patent_publish_no", searchText);
		Criterion re7 = Restrictions.like("patent_no", searchText);
		criteria.createAlias("patentContext", "pc", CriteriaSpecification.LEFT_JOIN);
		Criterion re14 = Restrictions.like("pc.context_abstract", searchText);
		Criterion re15 = Restrictions.like("pc.context_desc", searchText);
		Criterion re16 = Restrictions.like("pc.context_claim", searchText);
		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6,re7,re14,re15,re16));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}

	

}
