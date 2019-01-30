package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
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
	public List<Patent> getByBusinessId(String businessId,int page,int pageSize, String orderList, String orderFieldCode,int is_asc){
		log.info("businessId:"+businessId);
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			Filter filter = session.enableFilter("businessFilter");
			filter.setParameter("business_id",businessId);
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
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
	public List<Patent> searchAllFieldPatent(String  searchText,String businessId,int page,int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p" + 
				" JOIN p.patentAbstract as pa " + 
				" JOIN p.patentClaim as pc " + 
				" JOIN p.patentDesc as pd " + 
				" JOIN p.listAssignee as las " + 
				" JOIN p.listApplicant as lap " + 
				" JOIN p.listInventor as lin ";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId and" +
					" (p.patent_name like :searchText or p.patent_name_en like :searchText or" + 
					" p.patent_appl_country like :searchText or p.patent_appl_no like :searchText or" + 
					" p.patent_notice_no like :searchText or p.patent_publish_no like :searchText or" + 
					" p.patent_no like :searchText or pa.context_abstract like :searchText) or" + 
					" p.patent_id in (SELECT lin.patent FROM lin where lin.inventor_name like :searchText or lin.inventor_name_en like :searchText) or" + 
					" p.patent_id in (SELECT las.patent FROM las where las.assignee_name like :searchText or las.assignee_name_en like :searchText )or" + 
					" p.patent_id in (SELECT lap.patent FROM lap where lap.applicant_name like :searchText or lap.applicant_name_en like :searchText )";
		}else {
			queryStr += " WHERE" + 
					" (p.patent_name like :searchText or p.patent_name_en like :searchText or" + 
					" p.patent_appl_country like :searchText or p.patent_appl_no like :searchText or" + 
					" p.patent_notice_no like :searchText or p.patent_publish_no like :searchText or" + 
					" p.patent_no like :searchText or pa.context_abstract like :searchText) or" + 
					" p.patent_id in (SELECT lin.patent FROM lin where lin.inventor_name like :searchText or lin.inventor_name_en like :searchText) or" + 
					" p.patent_id in (SELECT las.patent FROM las where las.assignee_name like :searchText or las.assignee_name_en like :searchText )or" + 
					" p.patent_id in (SELECT lap.patent FROM lap where lap.applicant_name like :searchText or lap.applicant_name_en like :searchText )";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("searchText", searchText);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}
	
	@Override
	public int countSearchAllFieldPatent(String searchText, String businessId) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id) from Patent as p" + 
				" JOIN p.patentAbstract as pa " + 
				" JOIN p.patentClaim as pc " + 
				" JOIN p.patentDesc as pd " + 
				" JOIN p.listAssignee as las " + 
				" JOIN p.listApplicant as lap " + 
				" JOIN p.listInventor as lin ";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId and" +
					" (p.patent_name like :searchText or p.patent_name_en like :searchText or" + 
					" p.patent_appl_country like :searchText or p.patent_appl_no like :searchText or" + 
					" p.patent_notice_no like :searchText or p.patent_publish_no like :searchText or" + 
					" p.patent_no like :searchText or pa.context_abstract like :searchText) or" + 
					" p.patent_id in (SELECT lin.patent FROM lin where lin.inventor_name like :searchText or lin.inventor_name_en like :searchText) or" + 
					" p.patent_id in (SELECT las.patent FROM las where las.assignee_name like :searchText or las.assignee_name_en like :searchText )or" + 
					" p.patent_id in (SELECT lap.patent FROM lap where lap.applicant_name like :searchText or lap.applicant_name_en like :searchText )";
		}else {
			queryStr += " WHERE" + 
					" (p.patent_name like :searchText or p.patent_name_en like :searchText or" + 
					" p.patent_appl_country like :searchText or p.patent_appl_no like :searchText or" + 
					" p.patent_notice_no like :searchText or p.patent_publish_no like :searchText or" + 
					" p.patent_no like :searchText or pa.context_abstract like :searchText) or" + 
					" p.patent_id in (SELECT lin.patent FROM lin where lin.inventor_name like :searchText or lin.inventor_name_en like :searchText) or" + 
					" p.patent_id in (SELECT las.patent FROM las where las.assignee_name like :searchText or las.assignee_name_en like :searchText )or" + 
					" p.patent_id in (SELECT lap.patent FROM lap where lap.applicant_name like :searchText or lap.applicant_name_en like :searchText )";
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("searchText", searchText);
		long count = (long)q.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldPatent(String searchText, String fieldCode, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId"
					+ " and " + fieldCode + " like :searchText";
		} else {
			queryStr += " WHERE " + fieldCode + " like :searchText";
		}
		
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("searchText", searchText);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}
	
	@Override
	public int countSearchFieldPatent(String searchText, String fieldCode,String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.add(Restrictions.like(fieldCode, searchText));
		criteria.setProjection(Projections.countDistinct("patent_id"));
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldCountryPatent(List<String> coutryIdList, String fieldCode, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p ";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE p.patent_appl_country IN (:list) and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE p.patent_appl_country IN (:list)";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
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
		String queryStr = "SELECT count(distinct p.patent_id) from Patent as p";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE p.patent_appl_country IN (:list) and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE p.patent_appl_country IN (:list)";
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
	public List<Patent> searchFieldPatent(Date startDate, Date endDate, String fieldCode, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p ";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE p."+fieldCode+" >= :startDate "
					+ " and p."+fieldCode+" <= :endDate "
					+"and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE p."+fieldCode+" >= :startDate "
					+ " and p."+fieldCode+" <= :endDate ";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
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
	public List<Patent> searchFieldHumanListPatent(String searchText,String fieldCode, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p ";
		String strList = "list"+fieldCode.substring(0, 1).toUpperCase() + fieldCode.substring(1);
		queryStr +=  " LEFT JOIN p."+strList+" as ls";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE ls."+fieldCode+"_name like :searchText "
					+ " or ls."+fieldCode+"_name_en like :searchText "
					+"and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE ls."+fieldCode+"_name like :searchText "
					+ " or ls."+fieldCode+"_name_en like :searchText  ";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("searchText", searchText);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
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
		criteria.setProjection(Projections.countDistinct("patent_id"));
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldStatusListPatent(String searchText, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p ";
		queryStr +=  " LEFT JOIN p.listStatus as ls";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE ls.status_id like :searchText "
					+ " or ls.status_desc like :searchText or ls.status_desc_en like :searchText"
					+"and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE ls.status_id like :searchText "
					+ " or ls.status_desc like :searchText or ls.status_desc_en like :searchText";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("searchText", searchText);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}
	
	@Override
	public int countSearchFieldStatusPatent(String searchText, String businessId){
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		criteria.createAlias("listStatus", "lS");
		Criterion re1 = Restrictions.like("lS.status_id", searchText);
		Criterion re2 = Restrictions.like("lS.status_desc", searchText);
		Criterion re3 = Restrictions.like("lS.status_desc_en", searchText);
		criteria.add(Restrictions.or(re1, re2, re3));
		criteria.setProjection(Projections.countDistinct("patent_id"));
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
	
	@Override
	public List<Patent> searchFieldExtensionListPatent(String searchText, String fieldCode, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p "
							+ " LEFT JOIN p.listExtension as ls";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE ls."+fieldCode+" like :searchText "
					+"and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE ls."+fieldCode+" like :searchText ";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
				}
			} else {
				if (is_asc == 1) {
					queryStr += " ORDER BY p."+orderFieldCode+" ASC";
				}else {
					queryStr += " ORDER BY p."+orderFieldCode+" DESC";
				}
			}
		}
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("searchText", searchText);
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
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
		criteria.setProjection(Projections.countDistinct("patent_id"));
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

	

}
