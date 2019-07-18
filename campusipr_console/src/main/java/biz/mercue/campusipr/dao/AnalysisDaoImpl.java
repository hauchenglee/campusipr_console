package biz.mercue.campusipr.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.util.StringUtils;

@Repository("analysisDao")
public class AnalysisDaoImpl extends AbstractDao<String, Analysis> implements AnalysisDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	//預設：未申請完成的申請號數量
	public int countUnApplPatent(String businessId) {
		log.info("未申請完成的申請號數量");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id)"
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no like '%@%' "
				+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long)q.uniqueResult();
		log.info(count);
		return (int)count;
	}
	
	//預設：各年度專利申請數量
	public List<Analysis> countAllYearPatent(String businessId) {
		log.info("各年度的專利總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id), date_format(p.patent_appl_date, '%Y')"
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and lb.business_id = :businessId "
				+ "GROUP BY date_format(patent_appl_date, '%Y') ";
		
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().size());
		return q.list();
	}
	//預設：年度合計專利總數
	public int countPatentTotal(String businessId) {
		log.info("年度合計專利總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id)"
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long)q.uniqueResult();
		log.info(count);
		return (int)count;
	}
	//預設：年度合計專利家族總數
	public int countPatentFamilyTotal(String businessId) {
		log.info("年度合計專利家族總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct lf.patent_family_id)"
				+ "FROM Patent as p "
				+ "JOIN p.listFamily as lf "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and lf.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long)q.uniqueResult();
		log.info(count);
		return (int)count;
	}
	//預設：年度合計技術總數
	
	//預設：年度合計科系總數
	public int countDepartmentTotal(String businessId){
		log.info("年度合計科系總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct ld.department_name)"
				+ "FROM Patent as p "
				+ "JOIN p.listDepartment as ld "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and ld.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long)q.uniqueResult();
		log.info(count);
		return (int)count;
	}
	//預設：年度合計發明人總數-中
	public int countInventorTotal(String businessId){
		log.info("年度合計發明人總數-中");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name)"
				+ "FROM Patent as p "
				+ "JOIN p.listInventor as li "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long)q.uniqueResult();
		log.info(count);
		return (int)count;
	}
	//預設：年度合計發明人總數-英
	public int countInventorEnTotal(String businessId){
		log.info("年度合計發明人總數-英");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name_en)"
				+ "FROM Patent as p "
				+ "JOIN p.listInventor as li "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and li.inventor_name_en is not NULL "
				+ "and li.inventor_name is Null "
				+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long)q.uniqueResult();
		log.info(count);
		return (int)count;
	}
	
	//特定年度區間合計專利總數
	public List<String> countYearPatentTotal(String businessId, Long beginDate, Long endDate) {
		log.info("特定年度區間合計專利總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id)"
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "and lb.business_id = :businessId ";
				
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		q.setParameter("beginDate",beginDateFormat);     
		q.setParameter("endDate",endDateFormat);  
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//特定年度合計專利家族總數
	public List<String> countPatentFamily(String businessId, Long beginDate, Long endDate){
		log.info("特定年度年度合計專利家族總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct lf.patent_family_id)"
				+ "FROM Patent as p "
				+ "JOIN p.listFamily as lf "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "and lf.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		q.setParameter("beginDate",beginDateFormat);     
		q.setParameter("endDate",endDateFormat);  
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	//特定年度合計技術總數
	
	//特定年度合計科系總數
	public List<String> countDepartment(String businessId, Long beginDate, Long endDate){
		log.info("特定年度合計科系總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct ld.department_name)"
				+ "FROM Patent as p "
				+ "JOIN p.listDepartment as ld "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "and ld.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		q.setParameter("beginDate",beginDateFormat);     
		q.setParameter("endDate",endDateFormat);  
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//特定年度合計發明人總數-中
	public List<String> countInventor(String businessId, Long beginDate, Long endDate){
		log.info("特定年度合計發明人總數-中");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name)"
				+ "FROM Patent as p "
				+ "JOIN p.listInventor as li "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		q.setParameter("beginDate",beginDateFormat);     
		q.setParameter("endDate",endDateFormat);  
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//特定年度合計發明人總數-英
	public List<String> countInventorEn(String businessId, Long beginDate, Long endDate){
		log.info("特定年度合計發明人總數-英");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name_en)"
				+ "FROM Patent as p "
				+ "JOIN p.listInventor as li "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and li.inventor_name_en is not NULL "
				+ "and li.inventor_name is Null "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		q.setParameter("beginDate",beginDateFormat);     
		q.setParameter("endDate",endDateFormat);  
		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//特定年度區間的各年度的專利總數
	@Override
	public List<String> countYearPatent(String businessId, Long beginDate, Long endDate){
		log.info("特定年度區間的各年度專利總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id), date_format(p.patent_appl_date, '%Y')"
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE p.patent_appl_no Not like '%@%' "
				+ "and patent_appl_date IS NOT NULL "
				+ "and lb.business_id = :businessId "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "GROUP BY date_format(patent_appl_date, '%Y') ";
		
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		q.setParameter("beginDate",beginDateFormat);     
		q.setParameter("endDate",endDateFormat);  
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	
	public List<String> testPatent(String businessId){
		Session session = getSession();
		String queryStr = "SELECT date_format(p.patent_appl_date, '%Y'),count(distinct p.patent_id)"
				+ "FROM Patent as p "
				+ "JOIN p.listBusiness as lb "
				+ "WHERE lb.business_id = :businessId "
				+ "and p.patent_appl_no Not like '%@%' "
				+ "GROUP BY date_format(patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if(!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	
}
