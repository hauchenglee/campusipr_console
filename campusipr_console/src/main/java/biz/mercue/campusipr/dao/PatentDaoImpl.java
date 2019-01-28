package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.util.StringUtils;




@Repository("patentDao")
public class PatentDaoImpl extends AbstractDao<String,  Patent> implements PatentDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public Patent getById(String id){
		return getByKey(id);
	}
	
	@Override
	public Patent getById(String businessId,String id) {
		Criteria criteria =null;
		if(!StringUtils.isNULL(businessId)) {
			Session session = getSession();
			Filter filter = session.enableFilter("businessFilter");
			filter.setParameter("business_id",businessId);
			criteria = session.createCriteria(Patent.class);
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}else {
			criteria = createEntityCriteria();
		}
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
	public List<Patent> getByFamily(String familyId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("family","family");
		criteria.add(Restrictions.eq("family.patent_family_id", familyId));
		return criteria.list();
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
	public List<Patent> getByBusinessId(String businessId,int page,int pageSize,String orderFieldId,int is_asc){
		log.info("businessId:"+businessId);
		Criteria criteria =null;
		if(!StringUtils.isNULL(businessId)) {
			Session session = getSession();
			Filter filter = session.enableFilter("businessFilter");
			filter.setParameter("business_id",businessId);
			criteria = session.createCriteria(Patent.class);
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}else {
			criteria = createEntityCriteria();
		}
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public List<Patent> getAllByBusinessId(String businessId){
		Criteria criteria =null;
		if(!StringUtils.isNULL(businessId)) {
			Session session = getSession();
			Filter filter = session.enableFilter("businessFilter");
			filter.setParameter("business_id",businessId);
			criteria = session.createCriteria(Patent.class);
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}else {
			criteria = createEntityCriteria();
		}
		return criteria.list();
	}
	
	
	@Override
	public int  getCountByBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> getByPatentIds(List<String> ids,String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.add(Restrictions.in("patent_id", ids));
		return criteria.list();
	}
	
	@Override
	public List<Patent> searchAllFieldPatent(String  searchText,String businessId,int page,int pageSize,String orderFieldCode,int is_asc){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		Criterion re1 = Restrictions.like("patent_name", searchText);
		Criterion re2 = Restrictions.like("patent_name_en", searchText);
		Criterion re3 = Restrictions.like("patent_appl_country", searchText);
		Criterion re4 = Restrictions.like("patent_appl_no", searchText);
		Criterion re5 = Restrictions.like("patent_notice_no", searchText);
		Criterion re6 = Restrictions.like("patent_publish_no", searchText);
		Criterion re7 = Restrictions.like("patent_no", searchText);
		criteria.createAlias("patentAbstract", "pa");
		Criterion re8 = Restrictions.like("pa.context_abstract", searchText);
		criteria.createAlias("patentClaim", "pc");
		Criterion re9 = Restrictions.like("pc.context_claim", searchText);
		criteria.createAlias("patentDesc", "pd");
		Criterion re10 = Restrictions.like("pd.context_desc", searchText);
		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6,re7,re8,re9,re10));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int countSearchAllFieldPatent(String searchText, String businessId) {
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		Criterion re1 = Restrictions.like("patent_name", searchText);
		Criterion re2 = Restrictions.like("patent_name_en", searchText);
		Criterion re3 = Restrictions.like("patent_appl_country", searchText);
		Criterion re4 = Restrictions.like("patent_appl_no", searchText);
		Criterion re5 = Restrictions.like("patent_notice_no", searchText);
		Criterion re6 = Restrictions.like("patent_publish_no", searchText);
		Criterion re7 = Restrictions.like("patent_no", searchText);
		criteria.createAlias("patentAbstract", "pa");
		Criterion re8 = Restrictions.like("pa.context_abstract", searchText);
		criteria.createAlias("patentClaim", "pc");
		Criterion re9 = Restrictions.like("pc.context_claim", searchText);
		criteria.createAlias("patentDesc", "pd");
		Criterion re10 = Restrictions.like("pd.context_desc", searchText);
		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6,re7,re8,re9,re10));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldPatent(String searchText, String fieldCode, String businessId, int page, int pageSize,String orderFieldId,int is_asc){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.add(Restrictions.like(fieldCode, searchText));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int countSearchFieldPatent(String searchText, String fieldCode,String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.add(Restrictions.like(fieldCode, searchText));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldCountryPatent(List<String> coutryIdList, String fieldCode, String businessId, int page, int pageSize,String orderFieldId,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent p ";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += "JOIN p.listBusiness lb WHERE p.patent_appl_country IN (:list) and lb.business_id = :businessId";
		}else {
			queryStr += "WHERE p.patent_appl_country IN (:list)";
		}
		Query q = session.createQuery(queryStr);
		
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("list", coutryIdList);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}
	
	@Override
	public int countSearchFieldCountryPatent(List<String> coutryIdList, String fieldCode,String businessId){
		Session session = getSession();
		String queryStr = "SELECT count(p) from Patent p ";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += "JOIN p.listBusiness lb WHERE p.patent_appl_country IN (:list) and lb.business_id = :businessId";
		}else {
			queryStr += "WHERE p.patent_appl_country IN (:list)";
		}
		Query q = session.createQuery(queryStr);
		
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("list", coutryIdList);
		long count = (long)q.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldPatent(Date startDate, Date endDate, String fieldCode, String businessId, int page, int pageSize,String orderFieldCode,int is_asc){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.add(Restrictions.ge(fieldCode, startDate));
		criteria.add(Restrictions.le(fieldCode, endDate));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int countSearchFieldPatent(Date startDate, Date endDate, String fieldCode,String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.add(Restrictions.ge(fieldCode, startDate));
		criteria.add(Restrictions.le(fieldCode, endDate));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldHumanListPatent(String searchText,String fieldCode, String businessId, int page, int pageSize,String orderFieldCode,int is_asc){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		String strList = "list"+fieldCode.substring(0, 1).toUpperCase() + fieldCode.substring(1);
		criteria.createAlias(strList, "ls");
		Criterion re1 = Restrictions.like("ls."+fieldCode+"_name", searchText);
		Criterion re2 = Restrictions.like("ls."+fieldCode+"_name_en", searchText);
		criteria.add(Restrictions.or(re1,re2));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int countSearchFieldHumanListPatent(String searchText,String fieldCode, String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		String strList = "list"+fieldCode.substring(0, 1).toUpperCase() + fieldCode.substring(1);
		criteria.createAlias(strList, "ls");
		Criterion re1 = Restrictions.like("ls."+fieldCode+"_name", searchText);
		Criterion re2 = Restrictions.like("ls."+fieldCode+"_name_en", searchText);
		criteria.add(Restrictions.or(re1,re2));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
//	
//	@Override
//	public List<Patent> searchFieldApplicantListPatent(String searchText, String businessId, int page, int pageSize){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("listApplicant", "lApp");
//		Criterion re1 = Restrictions.like("lApp.applicant_name", searchText);
//		Criterion re2 = Restrictions.like("lApp.applicant_name_en", searchText);
//		Criterion re3 = Restrictions.like("lApp.applicant_address", searchText);
//		Criterion re4 = Restrictions.like("lApp.applicant_address_en", searchText);
//		Criterion re5 = Restrictions.like("lApp.country_id", searchText);
//		Criterion re6 = Restrictions.like("lApp.country_name", searchText);
//		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		criteria.setFirstResult((page - 1) * pageSize);
//		criteria.setMaxResults(pageSize);
//		return criteria.list();
//	}
//	
//	@Override
//	public int countSearchFieldApplicantPatent(String searchText, String businessId){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("listApplicant", "lApp");
//		Criterion re1 = Restrictions.like("lApp.applicant_name", searchText);
//		Criterion re2 = Restrictions.like("lApp.applicant_name_en", searchText);
//		Criterion re3 = Restrictions.like("lApp.applicant_address", searchText);
//		Criterion re4 = Restrictions.like("lApp.applicant_address_en", searchText);
//		Criterion re5 = Restrictions.like("lApp.country_id", searchText);
//		Criterion re6 = Restrictions.like("lApp.country_name", searchText);
//		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6));
//		criteria.setProjection(Projections.rowCount());
//		long count = (long)criteria.uniqueResult();
//		return (int)count;
//	}
//	
//	@Override
//	public List<Patent> searchFieldInventorListPatent(String searchText, String businessId, int page, int pageSize){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("listInventor", "lIn");
//		Criterion re1 = Restrictions.like("lIn.inventor_name", searchText);
//		Criterion re2 = Restrictions.like("lIn.inventor_name_en", searchText);
//		Criterion re3 = Restrictions.like("lIn.country_id", searchText);
//		Criterion re4 = Restrictions.like("lIn.country_name", searchText);
//		criteria.add(Restrictions.or(re1,re2,re3,re4));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		criteria.setFirstResult((page - 1) * pageSize);
//		criteria.setMaxResults(pageSize);
//		return criteria.list();
//	}
	
//	@Override
//	public int countSearchFieldInventorPatent(String searchText, String businessId){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("listInventor", "lIn");
//		Criterion re1 = Restrictions.like("lIn.inventor_name", searchText);
//		Criterion re2 = Restrictions.like("lIn.inventor_name_en", searchText);
//		Criterion re3 = Restrictions.like("lIn.country_id", searchText);
//		Criterion re4 = Restrictions.like("lIn.country_name", searchText);
//		criteria.add(Restrictions.or(re1,re2,re3,re4));
//		criteria.setProjection(Projections.rowCount());
//		long count = (long)criteria.uniqueResult();
//		return (int)count;
//	}
	
	@Override
	public List<Patent> searchFieldStatusListPatent(String searchText, String businessId, int page, int pageSize,String orderFieldCode,int is_asc){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.createAlias("listStatus", "lS");
		Criterion re2 = Restrictions.like("lS.status_desc", searchText);
		Criterion re3 = Restrictions.like("lS.status_desc_en", searchText);
		criteria.add(Restrictions.or( re2, re3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int countSearchFieldStatusPatent(String searchText, String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.createAlias("listStatus", "lS");
		Criterion re2 = Restrictions.like("lS.status_desc", searchText);
		Criterion re3 = Restrictions.like("lS.status_desc_en", searchText);
		criteria.add(Restrictions.or(re2, re3));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
//	@Override
//	public List<Patent> searchFieldCostListPatent(String searchText, String businessId, int page, int pageSize){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("listCost", "lC");
//		criteria.add(Restrictions.like("lC.cost_name", searchText));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		criteria.setFirstResult((page - 1) * pageSize);
//		criteria.setMaxResults(pageSize);
//		return criteria.list();
//	}
//	
//	@Override
//	public int countSearchFieldCostPatent(String searchText, String businessId){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("listCost", "lC");
//		criteria.add(Restrictions.like("lC.cost_name", searchText));
//		criteria.setProjection(Projections.rowCount());
//		long count = (long)criteria.uniqueResult();
//		return (int)count;
//	}
	
//	@Override
//	public List<Patent> searchFieldFamilyListPatent(String searchText, String businessId, int page, int pageSize){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("family", "family");
//		criteria.add(Restrictions.like("family.country_list", searchText));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		criteria.setFirstResult((page - 1) * pageSize);
//		criteria.setMaxResults(pageSize);
//		return criteria.list();
//	}
//	
//	@Override
//	public int countSearchFieldFamilyPatent(String searchText, String businessId){
//		Criteria criteria =  createEntityCriteria();
//		if(!StringUtils.isNULL(businessId)) {
//			criteria.createAlias("listBusiness","bs");
//			criteria.add(Restrictions.eq("bs.business_id", businessId));
//		}
//		criteria.createAlias("family", "family");
//		criteria.add(Restrictions.like("family.country_list", searchText));
//		criteria.setProjection(Projections.rowCount());
//		long count = (long)criteria.uniqueResult();
//		return (int)count;
//	}
	
	@Override
	public List<Patent> searchFieldExtensionListPatent(String searchText, String fieldCode, String businessId, int page, int pageSize,String orderFieldCode,int is_asc){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.createAlias("listExtension", "listExtension");
		criteria.add(Restrictions.like("listExtension."+fieldCode, searchText));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}
	
	@Override
	public int countSearchFieldExtensionPatent(String searchText, String fieldCode, String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.createAlias("listExtension", "listExtension");
		criteria.add(Restrictions.like("listExtension."+fieldCode, searchText));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public Patent getByPatentNo(String patentNo){
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("patent_no", patentNo));
		return (Patent) criteria.uniqueResult();
	}
	
	
	   @Override
	   public void deletePatentCost(String patentId) {
	       String hql = "Delete From PatentCost where patent_id = :patent_id";
	       Session session = getSession();
	       Query query = session.createQuery(hql);
	       query.setParameter("patent_id", patentId);
	       query.executeUpdate();
	   }
	   
	   
	   @Override
	   public void deleteInventor(String patentId) {
	       String hql = "Delete From Inventor where patent_id = :patent_id";
	       Session session = getSession();
	       Query query = session.createQuery(hql);
	       query.setParameter("patent_id", patentId);
	       query.executeUpdate();
	   }
	   
	   
	   @Override
	   public void deleteAssignee(String patentId) {
	       String hql = "Delete From Assignee where patent_id = :patent_id";
	       Session session = getSession();
	       Query query = session.createQuery(hql);
	       query.setParameter("patent_id", patentId);
	       query.executeUpdate();
	   }
	   
	   
	   
	   @Override
	   public void deleteApplicant(String patentId) {
	       String hql = "Delete From Applicant where patent_id = :patent_id";
	       Session session = getSession();
	       Query query = session.createQuery(hql);
	       query.setParameter("patent_id", patentId);
	       query.executeUpdate();
	   }


//	@Override
//	public int searchCountPatent(String searchText, String businessId) {
//		Criteria criteria =  createEntityCriteria();
//		criteria.createAlias("listBusiness","bs");
//		criteria.add(Restrictions.eq("bs.business_id", businessId));
//		Criterion re1 = Restrictions.like("patent_name", searchText);
//		Criterion re2 = Restrictions.like("patent_name_en", searchText);
//		Criterion re3 = Restrictions.like("patent_appl_country", searchText);
//		Criterion re4 = Restrictions.like("patent_appl_no", searchText);
//		Criterion re5 = Restrictions.like("patent_notice_no", searchText);
//		Criterion re6 = Restrictions.like("patent_publish_no", searchText);
//		Criterion re7 = Restrictions.like("patent_no", searchText);
//		criteria.createAlias("patentContext", "pc", CriteriaSpecification.LEFT_JOIN);
//		Criterion re14 = Restrictions.like("pc.context_abstract", searchText);
//		Criterion re15 = Restrictions.like("pc.context_desc", searchText);
//		Criterion re16 = Restrictions.like("pc.context_claim", searchText);
//		criteria.add(Restrictions.or(re1,re2,re3,re4,re5,re6,re7,re14,re15,re16));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		criteria.setProjection(Projections.rowCount());
//		long count = (long)criteria.uniqueResult();
//		return (int)count;
//	}
	




	

}