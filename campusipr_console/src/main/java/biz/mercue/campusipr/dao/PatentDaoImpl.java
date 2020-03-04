package biz.mercue.campusipr.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.util.Constants;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.util.StringUtils;




@Repository("patentDao")
public class PatentDaoImpl extends AbstractDao<String,  Patent> implements PatentDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public List<Object> demo(String patentId, String businessId) {
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), lsps.status_desc "
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as lsps "
				+ "where lb.business_id = :businessId "
				+ "and ls.create_date = "
				+ "( select max(ps.create_date) "
				+ "FROM PatentStatus as ps "
				+ "where p.patent_id = ps.primaryKey.patent.patent_id ) "
				+ "and lsps.status_desc = :statusDesc "
				+ "group by p.patent_appl_country";
		Session session = getSession();
		Query query = session.createQuery(queryStr);
		String statusDesc = "申請";
		query.setParameter("statusDesc", statusDesc);
		if (!StringUtils.isNULL(businessId)) {
			query.setParameter("businessId", businessId);
		}
		return query.list();
	}

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
	public Patent getByApplNoAndBusinessId(String applNo, String businessId) {
		String hql = "from Patent p inner join p.listBusiness as plb where p.patent_appl_no = :applNo and plb = :businessId";
		Session session = getSession();
		Query q = session.createQuery(hql);
		q.setParameter("applNo", applNo);
		q.setParameter("businessId", businessId);
		return (Patent) q.uniqueResult();
	}
	
	@Override
	public List<Patent> getPatentListByApplNo(String applNo) {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.like("patent_appl_no", applNo, MatchMode.START));
		return criteria.list();
	}

	@Override
	public int updatePatentApplNo(String patentId, String patentApplNo) {
		String hql = "Update Patent p set p.patent_appl_no = :patentApplNo where patent_id = :patent_id";
	    Session session = getSession();
	    Query query = session.createQuery(hql);
	    query.setParameter("patentApplNo", patentApplNo);
	    query.setParameter("patent_id", patentId);
	    return query.executeUpdate();
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
	public List<String> getBusinessIdsByPatentId(String patentId) {
		String hql = "select plb.business_id from Patent as p join p.listBusiness as plb where p.patent_id = :patentId";
		Session session = getSession();
		session.disableFilter("businessFilter");
		Query query = session.createQuery(hql);
		query.setParameter("patentId", patentId);
		return query.list();
	}

	@Override
	public List<Patent> getPatentList() {
		String hql = "select p from Patent as p";
		Session session = getSession();
		Query query = session.createQuery(hql);
		return query.list();
	}

	@Override
	public List<Patent> getByBusinessId(String businessId) {
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		return criteria.list();
	}

	@Override
	public List<Patent> getByBusinessId(String businessId,int page,int pageSize, String orderList, String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
		}
		if (businessId.equals(Constants.BUSINESS_PLATFORM)) {
			// platform
			queryStr += " JOIN p.listBusiness as lb WHERE (lb.business_id = :businessId)" +
					" or (lb.business_id not in :businessId and p.is_sync = 1)";
		} else {
			// school
			Filter filter = session.enableFilter("businessFilter");
			filter.setParameter("business_id",businessId);
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
		Session session = getSession();
		String queryStr = "SELECT count(distinct p) from Patent as p";
		if (businessId.equals(Constants.BUSINESS_PLATFORM)) {
			// platform
			queryStr += " JOIN p.listBusiness as lb WHERE (lb.business_id = :businessId)" +
					" or (lb.business_id not in :businessId and p.is_sync = 1)";
		} else {
			// school
			Filter filter = session.enableFilter("businessFilter");
			filter.setParameter("business_id", businessId);
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId";
		}
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
		return (int) count;
	}
	
	@Override
	public List<Patent> getByNotSyncPatent(String businessId){
		Calendar syncTime = Calendar.getInstance();
		syncTime.setTime(new Date());
		syncTime.add(Calendar.DATE, -7);
		
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("listBusiness","bs");
			criteria.add(Restrictions.eq("bs.business_id", businessId));
		}
		Criterion c1 = Restrictions.ge("sync_date", syncTime.getTime());
		Criterion c2 = Restrictions.isNull("sync_date");
		criteria.add(Restrictions.or(c1,c2));
		return criteria.list();
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
				" LEFT JOIN p.patentAbstract as pa " + 
				" LEFT JOIN p.patentClaim as pc " + 
				" LEFT JOIN p.patentDesc as pd " + 
				" LEFT JOIN p.listAssignee as las " + 
				" LEFT JOIN p.listApplicant as lap " + 
				" LEFT JOIN p.listInventor as lin ";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
		}
		if(!businessId.equals(Constants.BUSINESS_PLATFORM)) {
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId and (" +
					" (p.patent_name like :searchText or p.patent_name_en like :searchText or" + 
					" p.patent_appl_country like :searchText or p.patent_appl_no like :searchText or" + 
					" p.patent_notice_no like :searchText or p.patent_publish_no like :searchText or" + 
					" p.patent_no like :searchText or pa.context_abstract like :searchText) or" + 
					" p.patent_id in (SELECT lin.patent FROM lin where lin.inventor_name like :searchText or lin.inventor_name_en like :searchText) or" + 
					" p.patent_id in (SELECT las.patent FROM las where las.assignee_name like :searchText or las.assignee_name_en like :searchText ) or" + 
					" p.patent_id in (SELECT lap.patent FROM lap where lap.applicant_name like :searchText or lap.applicant_name_en like :searchText ))";
		}else {
			queryStr += " JOIN p.listBusiness as lb WHERE ((lb.business_id = :businessId) or (lb.business_id not in :businessId and p.is_sync = 1)) and" +
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
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
				" LEFT JOIN p.patentAbstract as pa " + 
				" LEFT JOIN p.patentClaim as pc " + 
				" LEFT JOIN p.patentDesc as pd " + 
				" LEFT JOIN p.listAssignee as las " + 
				" LEFT JOIN p.listApplicant as lap " + 
				" LEFT JOIN p.listInventor as lin ";
		if(!businessId.equals(Constants.BUSINESS_PLATFORM)) {
			queryStr += " JOIN p.listBusiness as lb WHERE lb.business_id = :businessId and (" +
					" (p.patent_name like :searchText or p.patent_name_en like :searchText or" + 
					" p.patent_appl_country like :searchText or p.patent_appl_no like :searchText or" + 
					" p.patent_notice_no like :searchText or p.patent_publish_no like :searchText or" + 
					" p.patent_no like :searchText or pa.context_abstract like :searchText) or" + 
					" p.patent_id in (SELECT lin.patent FROM lin where lin.inventor_name like :searchText or lin.inventor_name_en like :searchText) or" + 
					" p.patent_id in (SELECT las.patent FROM las where las.assignee_name like :searchText or las.assignee_name_en like :searchText )or" + 
					" p.patent_id in (SELECT lap.patent FROM lap where lap.applicant_name like :searchText or lap.applicant_name_en like :searchText ))";
		}else {
			queryStr += " JOIN p.listBusiness as lb WHERE ((lb.business_id = :businessId) or (lb.business_id not in :businessId and p.is_sync = 1)) and" +
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
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
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
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE p.patent_appl_country IN (:list)"
					 +" and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE p.patent_appl_country IN (:list)";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
			queryStr += " JOIN p.listBusiness as lb WHERE p.patent_appl_country IN (:list)"
					+" and lb.business_id = :businessId";
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
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
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
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE (ls."+fieldCode+"_name like :searchText "
					+ " or ls."+fieldCode+"_name_en like :searchText) "
					+" and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE ls."+fieldCode+"_name like :searchText "
					+ " or ls."+fieldCode+"_name_en like :searchText  ";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
	public List<Patent> searchFieldStatusListPatent(String searchText, String searchTextEn, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p ";
		queryStr +=  " LEFT JOIN p.listPatentStatus as ls"
					+ " LEFT JOIN ls.primaryKey.status as pk_aliase";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE"
					+ " lb.business_id = :businessId"
					+ " and ls.create_date = (\n" + 
					"    SELECT max(ps.create_date)\n" + 
					"    FROM PatentStatus as ps\n" + 
					"    LEFT JOIN ps.primaryKey.patent as mpkp\n" + 
					"    WHERE mpkp.patent_id = p.patent_id \n" + 
					")";
		}else {
			queryStr += " WHERE" +
			"    ls.create_date = (\n" + 
			"    SELECT max(ps.create_date)\n" + 
			"    FROM PatentStatus as ps\n" +  
			"    LEFT JOIN ps.primaryKey.patent as mpkp\n" + 
			"    WHERE mpkp.patent_id = p.patent_id \n" +  
			")";
		}
		queryStr += " and (";
		if (!StringUtils.isNULL(searchText)) {
			queryStr += " pk_aliase.status_desc = :searchText";
			if (!StringUtils.isNULL(searchTextEn)) {
				queryStr += " or";
			}
		}
		if (!StringUtils.isNULL(searchTextEn)) {
			queryStr += " pk_aliase.status_desc_en = :searchTextEn";
		}
		queryStr += " )";
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
		if (!StringUtils.isNULL(searchText)) {
			q.setParameter("searchText", searchText);
		}
		if (!StringUtils.isNULL(searchTextEn)) {
			q.setParameter("searchTextEn", searchTextEn);
		}
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}
	
	@Override
	public int countSearchFieldStatusPatent(String searchText, String searchTextEn, String businessId){
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id) from Patent as p ";
		queryStr +=  " LEFT JOIN p.listPatentStatus as ls"
					+ " LEFT JOIN ls.primaryKey.status as pk_aliase";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE"
					+ " lb.business_id = :businessId"
					+ " and ls.create_date = (\n" + 
					"    SELECT max(ps.create_date)\n" + 
					"    FROM PatentStatus as ps\n" + 
					"    LEFT JOIN ps.primaryKey.patent as mpkp\n" + 
					"    WHERE mpkp.patent_id = p.patent_id \n" + 
					")";
		}else {
			queryStr += " WHERE" +
			"    ls.create_date = (\n" + 
			"    SELECT max(ps.create_date)\n" + 
			"    FROM PatentStatus as ps\n" +  
			"    LEFT JOIN ps.primaryKey.patent as mpkp\n" + 
			"    WHERE mpkp.patent_id = p.patent_id \n" +  
			")";
		}

		queryStr += " and (";
		if (!StringUtils.isNULL(searchText)) {
			queryStr += " pk_aliase.status_desc = :searchText";
			if (!StringUtils.isNULL(searchTextEn)) {
				queryStr += " or";
			}
		}
		if (!StringUtils.isNULL(searchTextEn)) {
			queryStr += " pk_aliase.status_desc_en = :searchTextEn";
		}
		queryStr += " )";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		if (!StringUtils.isNULL(searchText)) {
			q.setParameter("searchText", searchText);
		}
		if (!StringUtils.isNULL(searchTextEn)) {
			q.setParameter("searchTextEn", searchTextEn);
		}
		long count = (long)q.uniqueResult();
		return (int)count;
	}

	@Override
	public List<Patent> searchFieldNoStatusListPatent(String businessId, int page, int pageSize){
		Session session = getSession();
		String platformBusinessId = Constants.BUSINESS_PLATFORM;
		String hql = "SELECT p FROM Patent as p JOIN p.listBusiness as lb ";
		if (StringUtils.isNULL(businessId)) {
			// platform
			hql += " WHERE ((lb.business_id = :businessId) or (lb.business_id not in :businessId and p.is_sync = 1))" +
					" and not exists (select 1 from PatentStatus as ps where ps.primaryKey.patent.patent_id = p.patent_id)";
		} else {
			// school
			hql += " WHERE lb.business_id = :businessId and not exists (select 1 from PatentStatus as ps where ps.primaryKey.patent.patent_id = p.patent_id)";
		}

		Query query = session.createQuery(hql);
		if (StringUtils.isNULL(businessId)) {
			// platform
			query.setParameter("businessId", platformBusinessId);
		} else {
			// school
			query.setParameter("businessId", businessId);
		}
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public int countSearchFieldNoStatusPatent(String businessId){
		Session session = getSession();
		String platformBusinessId = Constants.BUSINESS_PLATFORM;
		String hql = "SELECT count(distinct p.patent_id) FROM Patent as p JOIN p.listBusiness as lb ";
		if (StringUtils.isNULL(businessId)) {
			// platform
			hql += " WHERE ((lb.business_id = :businessId) or (lb.business_id not in :businessId and p.is_sync = 1))" +
					" and not exists (select 1 from PatentStatus as ps where ps.primaryKey.patent.patent_id = p.patent_id)";
		} else {
			// school
			hql += " WHERE lb.business_id = :businessId and not exists (select 1 from PatentStatus as ps where ps.primaryKey.patent.patent_id = p.patent_id)";
		}
		Query query = session.createQuery(hql);
		if (StringUtils.isNULL(businessId)) {
			// platform
			query.setParameter("businessId", platformBusinessId);
		} else {
			// school
			query.setParameter("businessId", businessId);
		}
		long count = (long)query.uniqueResult();
		return (int)count;
	}
	
	@Override
	public List<Patent> searchFieldExtensionListPatent(String searchText, String fieldCode, String businessId, int page, int pageSize, String orderList,String orderFieldCode,int is_asc){
		Session session = getSession();
		String queryStr = "SELECT p from Patent as p "
							+ " LEFT JOIN p.listExtension as ls";
		if (orderList != null) {
			queryStr +=  " LEFT JOIN p."+orderList+" as OLS";
			if ("listPatentStatus".equals(orderList)) {
				queryStr +=  " LEFT JOIN OLS.primaryKey.status as OPA";
			}
		}
		if(!StringUtils.isNULL(businessId)) {
			queryStr += " JOIN p.listBusiness as lb WHERE ls."+fieldCode+" like :searchText "
					+" and lb.business_id = :businessId";
		}else {
			queryStr += " WHERE ls."+fieldCode+" like :searchText ";
		}
		queryStr += " GROUP BY p";
		if (orderFieldCode != null) {
			if (orderList != null) {
				if (is_asc == 1) {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" ASC";
					} else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" ASC";
					}
				}else {
					if ("listPatentStatus".equals(orderList)) {
						queryStr +=  " ORDER BY OPA."+orderFieldCode+" DESC";
					}else {
						queryStr += " ORDER BY OLS."+orderFieldCode+" DESC";
					}
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
	public List<Patent> getByAdvancedSearchString(String hql, List<String> dataList, String businessId, int page, int pageSize) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		for (int i = 0; i < dataList.size(); i++) {
			String parameterName = "s" + i;
			query.setParameter(parameterName, dataList.get(i));
		}
		query.setParameter("bid", businessId);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
//		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // 可以用distinct(bean)代替
		return query.list();
	}

	@Override
	public int getCountByAdvancedSearchString(String hql, List<String> dataList, String businessId) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		for (int i = 0; i < dataList.size(); i++) {
			String parameterName = "s" + i;
			query.setParameter(parameterName, dataList.get(i));
		}
		query.setParameter("bid", businessId);
		long count = (long) query.uniqueResult();
		return (int) count;
	}

	@Override
	public List<Patent> getByAdvancedSearchDate(String hql, List<Date> dataList, String businessId, int page, int pageSize) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		for (int i = 0; i < dataList.size(); i++) {
			String parameterName = "d" + i;
			query.setParameter(parameterName, dataList.get(i));
		}
		query.setParameter("bid", businessId);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public int getCountByAdvancedSearchDate(String hql, List<Date> dataList, String businessId) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		for (int i = 0; i < dataList.size(); i++) {
			String parameterName = "d" + i;
			query.setParameter(parameterName, dataList.get(i));
		}
		query.setParameter("bid", businessId);
		long count = (long) query.uniqueResult();
		return (int) count;
	}

	@Override
	public Patent getByPatentNo(String patentNo) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("patent_no", patentNo));
		return (Patent) criteria.uniqueResult();
	}

	@Override
	public void deletePatentAnnuity(String patentId) {
		String hql = "Delete From Annuity where patent_id = :patent_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		query.executeUpdate();
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
	public void deletePatentContact(String patentId) {
		String hql = "Delete From PatentContact where patent_id = :patent_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		query.executeUpdate();
	}

	@Override
	public void deletePatentContactByKey(String contactId) {
		String hql = "Delete From PatentContact c where c.patent_contact_id = :contactId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("contactId", contactId);
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

	@Override
	public void deletePatentStatus(String patentId) {
		String hql = "Delete From PatentStatus where primaryKey.patent.patent_id = :patent_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		query.executeUpdate();
	}

	@Override
	public void deletePatentStatus(String patentId, String statusId) {
		String hql = "Delete From PatentStatus where primaryKey.patent.patent_id = :patent_id and primaryKey.status.status_id = :status_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		query.setParameter("status_id", statusId);
		query.executeUpdate();
	}

	@Override
	public void deletePatentStatus(String patentId, String statusId, Date createTime) {
		String hql = "Delete From PatentStatus where primaryKey.patent.patent_id = :patent_id and primaryKey.status.status_id = :status_id and create_date = :create_date";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		query.setParameter("status_id", statusId);
		query.setParameter("create_date", createTime);
		query.executeUpdate();
	}

	@Override
	public void deletePatentExtension(String extensionId) {
		String hql = "Delete From PatentExtension where extension_id = :extension_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("extension_id", extensionId);
		query.executeUpdate();
	}

	@Override
	public void deletePatentExtension(String patentId, String businessId) {
		String hql = "Delete From PatentExtension where patent_id = :patent_id";
		if (!StringUtils.isNULL(businessId)) {
			hql += " and business_id = :business_id";
		}
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		if (!StringUtils.isNULL(businessId)) {
			query.setParameter("business_id", businessId);
		}
		query.executeUpdate();
	}

	@Override
	public void deleteStatus(String statusId) {
		String hql = "Delete From Status where status_id = :status_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("status_id", statusId);
		query.executeUpdate();
	}

	@Override
	public void deleteCost(String costId) {
		String hql = "Delete From PatentCost where cost_id = :cost_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("cost_id", costId);
		query.executeUpdate();
	}

	@Override
	public void deleteHistory(String historyId) {
		String hql = "Delete From PatentEditHistory where history_id = :history_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("history_id", historyId);
		query.executeUpdate();
	}

	@Override
	public void deleteDepartment(String departmentId) {
		String hql = "Delete From Department where department_id = :departmentId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("departmentId", departmentId);
		query.executeUpdate();
	}

	@Override
	public List<String> getPatentIdByBusinessId(String businessId){
		String hql ="Select p.patent_id "
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "Where lb.business_id = :businessId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("businessId", businessId);
		return query.list();
	}
}
