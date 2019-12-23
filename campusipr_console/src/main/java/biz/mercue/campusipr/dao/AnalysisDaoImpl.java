package biz.mercue.campusipr.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;

@Repository("analysisDao")
public class AnalysisDaoImpl extends AbstractDao<String, Analysis> implements AnalysisDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	// 所有日期都依申請日選擇??

	// 已公開未公告申請案
	@Override
	public int countNoticePatentByYear(String businessId, Long beginDate, Long endDate) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct patent_appl_no)" 
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls " 
						+ "JOIN ls.primaryKey.status as lsps "
						+ "where (p.patent_publish_no is NULL or p.patent_publish_no = '') " 
						+ "and patent_notice_no is not null "
						+ "and lsps.status_desc = :statusNotice " 
						+ "and p.is_sync = 1 "
						+ "and p.patent_appl_country in ('tw','us','cn') ";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += "and lb.business_id = :businessId ";
		}
		if (beginDate != null && endDate != null) {
			queryStr += "and (date_format(p.patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		// + "and ls.create_date = "
		// + "( select MAX(ls.create_date) "
		// + "FROM p.listPatentStatus as ls "
		// + "JOIN ls.primaryKey.status as lsps "
		// + "where p.patent_id = ls.primaryKey.patent.patent_id "
		// + "and lsps.status_desc in (:statusPublish, :statusNotice, :statusApp)) ";
		Query q = session.createQuery(queryStr);
		if(beginDate!= null && endDate!= null) {	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		String statusNotice = "公開";
		q.setParameter("statusNotice", statusNotice);
		// String statusApp = "申請";
		// String statusPublish = "公告";
		// q.setParameter("statusApp", statusApp);
		// q.setParameter("statusPublish", statusPublish);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
		// log.info(count);
		return (int) count;
	}

	// 已公告專利
	@Override
	public int countPublishPatentByYear(String businessId, Long beginDate, Long endDate) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct patent_appl_no)" 
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls " 
						+ "JOIN ls.primaryKey.status as lsps "
						+ "where lsps.status_desc = :statusDesc " 
						+ "and p.is_sync = 1 "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and (p.patent_publish_no is not NULL and p.patent_publish_date is not NULL)";
		if(!StringUtils.isNULL(businessId)) {
			queryStr += "and lb.business_id = :businessId ";
		}
		if (beginDate != null && endDate != null) {
			queryStr += "and (date_format(p.patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}

		Query q = session.createQuery(queryStr);
		if (beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);

			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		String statusDesc = "公告";
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("statusDesc", statusDesc);
		long count = (long) q.uniqueResult();
		// log.info(count);
		return (int) count;
	}

	// 未公開/公告申請案
	@Override
	public int countApplPatentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("依年份：未公開/公告申請案");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id) " 
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "WHERE lb.business_id = :businessId "
						+ "AND p.is_sync = 1 "
						+ "AND p.patent_appl_date IS NOT NULL " 
						+ "AND p.patent_appl_date NOT IN ('')  "
						+ "AND p.patent_appl_no NOT IN ('')  " 
						+ "AND p.patent_appl_no IS NOT NULL "
						+ "AND (p.patent_notice_no Is NULL or p.patent_notice_no = '') "
						+ "AND (p.patent_publish_no Is NULL or p.patent_publish_no = '') "
						+ "AND p.patent_appl_country in ('tw','us','cn') ";
		
		if (beginDate != null && endDate != null) {
			queryStr += "and (date_format(p.patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		
		Query q = session.createQuery(queryStr);
		
		if(beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			log.info(beginDateFormat + ", " + endDateFormat);
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 專利家族總數-有跟同步專利成為家族的才會列入計算
	@Override
	public int countPatentFamilyByYear(String businessId, Long beginDate, Long endDate) {
		// log.info("依年份：專利家族總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct lf.patent_family_id) "
				+ "FROM Patent as p "
				+ "JOIN p.listFamily as lf "
				+ "WHERE lf.business_id = :businessId "
				+ "and p.is_sync = 1 ";
		if(beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		Query q = session.createQuery(queryStr);
		if(beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
		// log.info(count);
		return (int) count;
	}

	// 技術總數
	@Override
	public int countTech(String businessId) {
		Session session = getSession();
		String queryStr = "SELECT count(*) " 
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "WHERE p.is_sync = 0 " 
						+ "and lb.business_id = :businessId "
						+ "AND (p.patent_appl_no Is NULL or p.patent_appl_no ='') "
						+ "AND (p.patent_notice_no Is NULL or p.patent_notice_no = '') "
						+ "AND (p.patent_publish_no Is NULL or p.patent_publish_no = '') "
						+ "AND p.patent_appl_country in ('tw','us','cn') ";
		// String queryStr = "SELECT count(distinct p.patent_id) "
		// + "FROM Patent as p "
		// + "JOIN p.listBusiness as lb "
		// + "JOIN p.listInventor as li "
		// + "JOIN p.listDepartment as ld "
		// + "JOIN p.listAssignee as la "
		// + "WHERE lb.business_id = :businessId "
		// + "AND p.patent_appl_no IS NULL "
		// + "AND p.patent_notice_no IS NULL "
		// + "AND p.patent_publish_no IS NULL "
		// + "AND (p.patent_name IS NOT NULL or "
		// + "p.patent_name_en IS NOT NULL or "
		// + "li.inventor_id IS NOT NULL or "
		// + "la.assignee_id IS NOT NULL or "
		// + "ld.department_id IS NOT NULL) ";
		Query q = session.createQuery(queryStr);

		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
		return (int) count;
	}

	// 科系總數
	@Override
	public int countDepartmentByYear(String businessId, Long beginDate, Long endDate) {
		// log.info("依年份：科系總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct ld.department_name) " 
						+ "FROM Patent as p "
						+ "JOIN p.listDepartment as ld "
						+ "WHERE ld.business_id = :businessId "
						+ "AND p.patent_appl_country in ('tw','us','cn') "
						+ "and p.is_sync = 1 ";
		if(beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		Query q = session.createQuery(queryStr);
		
		if(beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
		// log.info(count);
		return (int) count;
	}

	// 發明人總數-中
	@Override
	public int countInventorByYear(String businessId, Long beginDate, Long endDate) {
		// log.info("依年份：發明人總數-中");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name) " 
				+ "FROM Patent as p "
				+ "JOIN p.listInventor as li " 
				+ "JOIN p.listBusiness as lb "
				+ "WHERE lb.business_id = :businessId " 
				+ "AND p.patent_appl_country in ('tw','us','cn') "
				+ "and p.is_sync = 1 ";
		
		if(beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		
		Query q = session.createQuery(queryStr);
		
		if(beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
		// log.info(count);
		return (int) count;
	}

	// 發明人總數-英
	@Override
	public int countInventorEnByYear(String businessId, Long beginDate, Long endDate) {
		// log.info("依年份：發明人總數-英");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name_en) " 
				+ "FROM Patent as p "
				+ "JOIN p.listInventor as li " 
				+ "JOIN p.listBusiness as lb " 
				+ "WHERE li.inventor_name_en is not NULL "
				+ "and li.inventor_name is Null "
				+ "and lb.business_id = :businessId " 
				+ "AND p.patent_appl_country in ('tw','us','cn') "
				+ "and p.is_sync = 1 ";
		
		if(beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		
		if(beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		long count = (long) q.uniqueResult();
		// log.info(count);
		return (int) count;
	}

	// 學校端 歷年專利申請數量 & 平台端 歷年專利申請數量
	@Override
	public List<Analysis> overYearPatent(String businessId, Long beginDate, Long endDate) {
//		log.info("歷年專利申請數量");
		Session session = getSession();
		
		String queryStr = "SELECT count(distinct patent_appl_no), date_format(p.patent_appl_date, '%Y') "
						+ "From Patent as p "
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls " 
						+ "JOIN ls.primaryKey.status as lsps "
						+ "WHERE p.is_sync = 1 "
						+ "and p.patent_appl_country in ('tw','us','cn') ";
		if (!StringUtils.isNULL(businessId)) {
			queryStr += "AND lb.business_id = :businessId ";
		}
						//申請
		queryStr += "AND ((p.patent_appl_date IS NOT NULL " 
					+ "AND p.patent_appl_date NOT IN ('')  "
					+ "AND p.patent_appl_no NOT IN ('')  " 
					+ "AND p.patent_appl_no IS NOT NULL "
					+ "AND (p.patent_notice_no Is NULL or p.patent_notice_no = '') "
					+ "AND (p.patent_publish_no Is NULL or p.patent_publish_no = '') "
					+ "and lsps.status_desc = :statusApp)"
					// 公開
					+ "OR ((p.patent_publish_no is NULL or p.patent_publish_no = '')" + "and patent_notice_no is not null "
					+ "and lsps.status_desc = :statusNotice)"
					// 公告
					+ "OR(lsps.status_desc = :statusPublish "
					+ "and (p.patent_publish_no is not NULL and p.patent_publish_date is not NULL))) ";
		
		if(beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}			
							
						
			queryStr += "GROUP BY date_format(patent_appl_date, '%Y') "
						+ "Order by date_format(patent_appl_date, '%Y') asc ";
		
//		queryStr = "SELECT count(distinct patent_appl_no), date_format(p.patent_appl_date, '%Y') "
//						+ "FROM Patent as p " 
//						+ "JOIN p.listBusiness as lb " 
//						+ "WHERE p.is_sync = 1 "
//						+ "and lb.business_id = :businessId " 
//						+ "OR (p.is_sync = 0 " 
//						+ "and lb.business_id = :businessId "
//						+ "AND p.patent_appl_country in ('tw','us','cn') " 
//						+ "AND p.patent_appl_no IS NOT NULL "
//						+ "AND p.patent_appl_date IS NOT NULL " 
//						+ "AND p.patent_appl_date NOT IN ('') "
//						+ "AND p.patent_appl_no NOT IN ('')  " 
//						+ "AND (p.patent_notice_no Is NULL or p.patent_notice_no = '') "
//						+ "AND (p.patent_publish_no Is NULL or p.patent_publish_no = '')) "
//						+ "GROUP BY date_format(patent_appl_date, '%Y') " 
//						+ "Order by date_format(patent_appl_date, '%Y') asc";

		Query q = session.createQuery(queryStr);
		
		if(beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		 String statusApp = "申請";
		 String statusNotice = "公開";
		 String statusPublish = "公告";
		 q.setParameter("statusApp", statusApp);
		 q.setParameter("statusNotice", statusNotice);
		 q.setParameter("statusPublish", statusPublish);
//		log.info(q.list().size());
		return q.list();
	}

	// 學校端 國家分析 & 平台端 國家分析
	// 預設：各個國家專利總數，各專利狀態只會讀取已同步的專利
	@Override
	public List<Analysis> eachCountry(String businessId, Long beginDate, Long endDate, String status) {
		Session session = getSession();
		String statusDesc =null;
		String queryStr = "SELECT p.patent_appl_country,  count(distinct p.patent_id) " 
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls " 
						+ "JOIN ls.primaryKey.status as lsps ";
		
		if(StringUtils.isNULL(businessId) ||  businessId.equals(Constants.BUSINESS_PLATFORM)) {
			queryStr += "WHERE";
			
		}
		else if(!StringUtils.isNULL(businessId) && !businessId.equals(Constants.BUSINESS_PLATFORM)){
			queryStr += "WHERE lb.business_id = :businessId "
					 +  "AND ";
		}
		
		if(status.equals("申請")) {
			queryStr += " p.patent_appl_date IS NOT NULL " 
					+ "AND p.patent_appl_date NOT IN ('')  "
					+ "AND p.patent_appl_no NOT IN ('')  " 
					+ "AND p.patent_appl_no IS NOT NULL "
					+ "AND (p.patent_notice_no Is NULL or p.patent_notice_no = '') "
					+ "AND (p.patent_publish_no Is NULL or p.patent_publish_no = '') "
					//對齊專利列表狀態需要
					+ "AND lsps.status_desc = :statusDesc "
					//同步
					+ "AND p.is_sync = 1 ";
			statusDesc = status;
		}
		
		if(status.equals("公開")) {
			queryStr += " (p.patent_publish_no is NULL or p.patent_publish_no = '') " 
					 + "AND patent_notice_no is not null "
					//對齊專利列表狀態需要
					 + "AND lsps.status_desc = :statusDesc "
					//同步
					 + "AND p.is_sync = 1 ";
			statusDesc = status;
		}
		
		if(status.equals("公告")) {
			queryStr += " p.patent_appl_country in ('tw','us','cn') "
					 + "and (p.patent_publish_no is not NULL and p.patent_publish_date is not NULL) "
					//對齊專利列表狀態需要
					 + "and lsps.status_desc = :statusDesc " 
					//同步
					 + "and p.is_sync = 1 ";
			statusDesc = status;
		}
		//(改三者相加)
//		if(status.equals("總數")) {
//			queryStr += "p.is_sync = 1 ";
//		}
		
		if (beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		queryStr +="AND p.patent_appl_country in ('tw','us','cn') " 
				 + "GROUP by p.patent_appl_country";

		Query q = session.createQuery(queryStr);
		
//		對齊專利列表狀態需要
		if(statusDesc !=null) {
			q.setParameter("statusDesc", statusDesc);
		}
		
		if (beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		
		if (!StringUtils.isNULL(businessId) && !businessId.equals(Constants.BUSINESS_PLATFORM)) {
			q.setParameter("businessId", businessId);
		}
		
		return q.list();
	}

	// 各國歷年專利申請數量
	@Override
	public List<Analysis> countSingleCountryByYear(String businessId, Long beginDate, Long endDate, String countryId) {
//		log.info("單一國家的各年份總數(國家可選)");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct patent_appl_no), date_format(patent_appl_date, '%Y') "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb " 
						+ "where p.patent_appl_country in ('tw','us','cn') " 
						+ "and p.patent_appl_country = :countryId " 
						+ "and p.is_sync = 1 ";
		if(!StringUtils.isNULL(businessId)){
			queryStr += "and lb.business_id = :businessId ";
		}
		if (beginDate != null && endDate != null) {
			queryStr += "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		}
		
		queryStr += "group by date_format(patent_appl_date, '%Y')";
		
		Query q = session.createQuery(queryStr);
		
		if (beginDate != null && endDate != null) {		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		q.setParameter("countryId", countryId);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// GetDefaultYear
	@Override
	public List<Analysis> getEachDepartmentDefaultYear(String businessId) {
//		log.info("GetDefaultYear");
		Session session = getSession();
		String queryStr = "SELECT count(distinct patent_appl_no), date_format(p.patent_appl_date, '%Y') "
				+ "FROM Patent as p " + "JOIN p.listBusiness as lb " + "JOIN p.listDepartment as ld "
				+ "where lb.business_id = :businessId " + "and p.is_sync = 1 "
				+ "group by date_format(p.patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	
	//學校端 科系分析
	@Override
	public List<Analysis> eachDepartment(String businessId, Long beginDate, Long endDate, String countryId){
		Session session = getSession();
		String queryStr = "SELECT distinct(ld.department_name), count(distinct p.patent_id) " 
				+ "FROM Patent as p "
				+ "JOIN p.listDepartment as ld " 
				+ "where ld.business_id = :businessId ";
		if(countryId.equals("總數")) {
			queryStr+= "AND p.patent_appl_country in ('tw','us','cn') ";
		}
		
		if(countryId.equals("TW")) {
			queryStr+= "and p.patent_appl_country = 'TW' " ;
		}		
		
		if(countryId.equals("CN")) {
			queryStr+= "and p.patent_appl_country = 'CN' " ;
		}
		
		if(countryId.equals("US")) {
			queryStr+= "and p.patent_appl_country = 'CN' " ;
		}
		
		if(beginDate!= null && endDate != null) {
			queryStr+= "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) " ;
		}
		
		queryStr+= "and p.is_sync = 1 "
				+ "group by ld.department_name";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		
		if (beginDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		
		log.info(businessId);
		return q.list();
	}
	
	
	//平台端
	// 取得學校清單
	@Override
	public List<String> getSchoolList() {
		Session session = getSession();
		// 如果只要有建立過patent的學校
		// String queryStr = "SELECT distinct lb.business_id "
		// + "FROM Patent as p "
		// + "JOIN p.listBusiness as lb "
		// + "where lb.business_id != :role ";
		String queryStr = "SELECT distinct b.business_id, b.business_name " 
						+ "FROM Business as b "
						+ "where b.business_id != :role ";
		Query q = session.createQuery(queryStr);
		q.setParameter("role", Constants.BUSINESS_PLATFORM);
		// log.info(q.list().size());
		return q.list();
	}

	@Override
	public List<Analysis> getDefaultYear() {
		Session session = getSession();
		String queryStr = "SELECT MIN(date_format(patent_appl_date, '%Y')), MAX(date_format(patent_appl_date, '%Y')) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb " 
						+ "where lb.business_id != :role "
						+ "AND p.is_sync = 1 ";
		Query q = session.createQuery(queryStr);
		q.setParameter("role", Constants.BUSINESS_PLATFORM);
		// log.info(q.list().size());
		return q.list();
	}
	
	// 專利組合
	@Override
	public int countPorfolio (Long beginDate, Long endDate) {
		Session session = getSession();
		// 如果只要計算大於0的專利組合
//		String queryStr = "SELECT count(distinct lpf.portfolio_id)"
//						+ "FROM Patent as p " 
//						+ "JOIN p.listPortfolio as lpf " 
//						+ "WHERE (date_format(lpf.create_date, '%Y') between :beginDate and :endDate) "
//						+ "and p.is_sync = 1 ";
		// 以Portfolio為主
		String queryStr = "SELECT count(distinct pfo.portfolio_id)" 
						+ "FROM Portfolio as pfo ";
		if (beginDate != null && endDate != null) {
			queryStr += "WHERE (date_format(pfo.create_date, '%Y') between :beginDate and :endDate) ";
		} else {
			queryStr += "WHERE pfo.create_date IS NOT NULL";
		}
		Query q = session.createQuery(queryStr);
		if(beginDate != null && endDate != null) {		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		long count = (long) q.uniqueResult();
		log.info("如果是0，表示未有資料，或create_date皆是NULL: " + count);
		return (int) count;
	}

	// 學校數By Year(business_id 去掉智財中心)
	@Override
	public int countSchool(Long beginDate, Long endDate) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct b.business_id) " 
						+ "FROM Business as b "
						+ "WHERE b.business_id != :role ";
		if(beginDate!= null && endDate != null) {
			queryStr += "AND (date_format(b.create_date, '%Y') between :beginDate and :endDate) ";
		}
		else {
			queryStr += "AND b.create_date IS NOT NULL " ;
		}
		
		Query q = session.createQuery(queryStr);
		
		if(beginDate!= null && endDate != null) {			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
		}
		q.setParameter("role", Constants.BUSINESS_PLATFORM);

		long count = (long) q.uniqueResult();
		return (int) count;
	}

	//國家分析 申請、公開、公告
	@Override
	public List<Analysis> eachSchoolStatus(String status, Long beginDate, Long endDate){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();

		schoolList.addAll(getSchoolList());
		Object[][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String statusDesc = null;
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_appl_no), p.patent_appl_country, lsps.status_desc "
					+ "FROM Patent as p " 
					+ "JOIN p.listBusiness as lb " 
					+ "JOIN p.listPatentStatus as ls "
					+ "JOIN ls.primaryKey.status as lsps " 
					+ "where p.is_sync = 1 ";
			if (!StringUtils.isNULL(businessId)) {
				queryStr += "AND lb.business_id = :businessId ";
			}
			if(status.equals("申請")) {
				queryStr += "AND p.patent_appl_date IS NOT NULL " 
						+ "AND p.patent_appl_date NOT IN ('')  "
						+ "AND p.patent_appl_no NOT IN ('')  " 
						+ "AND p.patent_appl_no IS NOT NULL "
						+ "AND (p.patent_notice_no Is NULL or p.patent_notice_no = '') "
						+ "AND (p.patent_publish_no Is NULL or p.patent_publish_no = '') "
						+ "AND lsps.status_desc = :statusDesc ";
				statusDesc = status;
			}
			
			if(status.equals("公開")) {
				queryStr += "AND (p.patent_publish_no is NULL or p.patent_publish_no = '') " 
						+ "AND patent_notice_no is not null "
						+ "AND lsps.status_desc = :statusDesc ";
				statusDesc = status;
			}
			
			if(status.equals("公告")) {
				queryStr += "AND p.patent_appl_country in ('tw','us','cn') "
						+ "and (p.patent_publish_no is not NULL and p.patent_publish_date is not NULL) "
						+ "and lsps.status_desc = :statusDesc " ;
				statusDesc = status;
			}
			
			if(beginDate!= null && endDate != null) {
				queryStr+= "AND (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) " ;
			}
			queryStr+= "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			
			if (beginDate != null && endDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Timestamp bd = new Timestamp(beginDate);
				Timestamp ed = new Timestamp(endDate);
				String beginDateFormat = sdf.format(bd);
				String endDateFormat = sdf.format(ed);
				
				q.setParameter("beginDate", beginDateFormat);
				q.setParameter("endDate", endDateFormat);
			}
			if(statusDesc !=null) {
				q.setParameter("statusDesc", statusDesc);
			}
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}
	
	// For Excel Export 各國家總合By Select School
	@Override
	public List<Analysis> countSchoolPatentTotalByYear(JSONArray schoolArray, Long beginDate, Long endDate) {
		int listInx = 0;
		String businessName = null;
		List<Analysis> schoolData = new ArrayList<Analysis>();

		Session session = getSession();
		for (listInx = 0; listInx < schoolArray.length(); listInx++) {
			businessName = schoolArray.optString(listInx);
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_appl_no), p.patent_appl_country "
					+ "FROM Patent as p " + "JOIN p.listBusiness as lb " + "where lb.business_name = :businessName "
					+ "AND p.is_sync = 1 "
					+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
					+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);

			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
			if (!StringUtils.isNULL(businessName)) {
				q.setParameter("businessName", businessName);
			}
//				log.info(q.list().isEmpty());
			schoolData.addAll(q.list());
		}
//			log.info(schoolData);
		return schoolData;
	}

	// For Excel Export 各狀態各國家By Select Status and school
	@Override
	public List<Analysis> countSchoolPatentStatusByYear(JSONArray statusDesc, JSONArray schoolArray, Long beginDate,
			Long endDate) {
		int schoolInx = 0;
		int statusInx = 0;
		String businessName = null;
		String status = null;
		List<Analysis> schoolData = new ArrayList<Analysis>();

		Session session = getSession();
//				log.info(statusDesc.length());
		for (statusInx = 0; statusInx < statusDesc.length(); statusInx++) {
			status = statusDesc.optString(statusInx);
			for (schoolInx = 0; schoolInx < schoolArray.length(); schoolInx++) {
				businessName = schoolArray.optString(schoolInx);
				String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_appl_no), p.patent_appl_country, lsps.status_desc "
						+ "FROM Patent as p " + "JOIN p.listBusiness as lb " + "JOIN p.listPatentStatus as ls "
						+ "JOIN ls.primaryKey.status as lsps " + "where lb.business_name = :businessName "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "AND p.is_sync = 1  " + "and lsps.status_desc = :status " + "and ls.create_date = "
						+ "( select MAX(ls.create_date) " + "FROM p.listPatentStatus as ls "
						+ "where p.patent_id = ls.primaryKey.patent.patent_id ) " + "GROUP BY p.patent_appl_country ";
				Query q = session.createQuery(queryStr);
				q.setParameter("status", status);
				if (!StringUtils.isNULL(businessName)) {
					q.setParameter("businessName", businessName);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				
				Timestamp bd = new Timestamp(beginDate);
				Timestamp ed = new Timestamp(endDate);
				String beginDateFormat = sdf.format(bd);
				String endDateFormat = sdf.format(ed);
//						log.info("開始年: "+beginDateFormat+"，結束年: "+endDateFormat);
				q.setParameter("beginDate", beginDateFormat);
				q.setParameter("endDate", endDateFormat);
//						log.info(q.list().isEmpty());
//						log.info(schoolArray);
//						log.info(statusDesc);
				schoolData.addAll(q.list());
			}
		}
		return schoolData;
	}

	// 各校各國家總和By Year
	@Override
	public List<Analysis> countSchoolPatentTotalByYear(Long beginDate, Long endDate) {
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
	
		schoolList.addAll(getSchoolList());
		Object[][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
	
		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_appl_no), p.patent_appl_country "
					+ "FROM Patent as p " + "JOIN p.listBusiness as lb " + "where lb.business_id = :businessId "
					+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
					+ "AND p.is_sync = 1 " + "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
	
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}

}
