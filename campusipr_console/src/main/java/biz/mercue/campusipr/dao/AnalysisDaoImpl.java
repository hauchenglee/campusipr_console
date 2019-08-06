package biz.mercue.campusipr.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;

@Repository("analysisDao")
public class AnalysisDaoImpl extends AbstractDao<String, Analysis> implements AnalysisDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	// 預設：未官方同步專利
	@Override
	public int countUnApplPatent(String businessId) {
//		log.info("未申請完成的申請號數量");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id)" 
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "WHERE (p.patent_appl_no like '%@%' or p.patent_appl_no IS NULL)"
						+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 預設：專利申請總數
		@Override
		public int countAllPatent(String businessId) {
	//		log.info("專利申請總數");
			Session session = getSession();
			String queryStr = "SELECT count(distinct p.patent_appl_no)" 
							+ "FROM Patent as p " 
							+ "JOIN p.listBusiness as lb "
							+ "WHERE p.patent_appl_no Not like '%@%' " 
							+ "and patent_appl_date IS NOT NULL "
							+ "and lb.business_id = :businessId ";
			Query q = session.createQuery(queryStr);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			long count = (long) q.uniqueResult();
	//		log.info(count);
			return (int) count;
		}

	// 預設：歷年專利申請數量
	@Override
	public List<Analysis> countYearPatent(String businessId) {
//		log.info("歷年專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id), date_format(p.patent_appl_date, '%Y')"
						+ "FROM Patent as p " + "JOIN p.listBusiness as lb " 
						+ "WHERE p.patent_appl_no Not like '%@%' "
						+ "and patent_appl_date IS NOT NULL " 
						+ "and lb.business_id = :businessId "
						+ "GROUP BY date_format(patent_appl_date, '%Y') "
						+ "Order by date_format(patent_appl_date, '%Y') asc";

		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().size());
		return q.list();
	}

	// 預設：專利家族總數
	@Override
	public int countPatentFamily(String businessId) {
//		log.info("年度合計專利家族總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct lf.patent_family_id)" 
						+ "FROM Patent as p "
						+ "JOIN p.listFamily as lf " 
						+ "WHERE lf.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	// 預設：年度合計技術總數

	// 預設：科系總數
	@Override
	public int countDepartment(String businessId) {
//		log.info("年度合計科系總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct ld.department_name)" 
						+ "FROM Patent as p "
						+ "JOIN p.listDepartment as ld " 
						+ "WHERE ld.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 預設：發明人總數-中
	@Override
	public int countInventor(String businessId) {
//		log.info("年度合計發明人總數-中");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name)" 
						+ "FROM Patent as p " 
						+ "JOIN p.listInventor as li "
						+ "JOIN p.listBusiness as lb " 
						+ "WHERE lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 預設：發明人總數-英
	@Override
	public int countInventorEn(String businessId) {
//		log.info("年度合計發明人總數-英");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name_en)" 
						+ "FROM Patent as p "
						+ "JOIN p.listInventor as li " 
						+ "JOIN p.listBusiness as lb " 
						+ "WHERE li.inventor_name_en is not NULL "
						+ "and li.inventor_name is Null " 
						+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 特定年度區間專利申請總數
	@Override
	public int countAllPatentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度區間專利申請總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_appl_no)" 
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

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 特定年度專利家族總數
	@Override
	public int countPatentFamilyByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度專利家族總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct lf.patent_family_id)" 
						+ "FROM Patent as p "
						+ "JOIN p.listFamily as lf " 
						+ "WHERE (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and lf.business_id = :businessId ";
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
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	// 特定年度合計技術總數

	// 特定年度科系總數
	@Override
	public int countDepartmentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度科系總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct ld.department_name)" 
						+ "FROM Patent as p "
						+ "JOIN p.listDepartment as ld " 
						+ "WHERE (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and ld.business_id = :businessId ";
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
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 特定年度發明人總數-中
	@Override
	public int countInventorByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度發明人總數-中");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name)" 
						+ "FROM Patent as p " 
						+ "JOIN p.listInventor as li "
						+ "JOIN p.listBusiness as lb " 
						+ "WHERE (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and lb.business_id = :businessId ";
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
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

//	 特定年度發明人總數-英
	@Override
	public int countInventorEnByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度發明人總數-英");
		Session session = getSession();
		String queryStr = "SELECT count(distinct li.inventor_name_en)" 
						+ "FROM Patent as p "
						+ "JOIN p.listInventor as li " 
						+ "JOIN p.listBusiness as lb " 
						+ "WHERE li.inventor_name_en is not NULL "
						+ "and li.inventor_name is Null "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}

	// 特定年度區間歷年專利申請數量
	@Override
	public List<Analysis> countYearPatentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度區間歷年專利申請數量");
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

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 預設：各個國家專利總數
	// 國家部分因目前資料庫國家混亂，故有設定"and p.patent_appl_country in ('tw','us','cn')"，之後有乾淨的資料庫後可拿掉
	@Override
	public List<Analysis> countCountry(String businessId) {
//		log.info("各個國家的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id ) " 
					+ "FROM Patent as p "
					+ "JOIN p.listBusiness as lb " 
					+ "where lb.business_id = :businessId "
					+ "and p.patent_appl_country in ('tw','us','cn') "
					+ "group by p.patent_appl_country";
			
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)&& !businessId.equals(Constants.BUSINESS_PLATFORM)) {
			q.setParameter("businessId", businessId);
		}

		return q.list();
	}
	// 預設：各個國家在申請狀態的專利總數
	@Override
	public List<Analysis> countCountryApplStatus(String businessId) {
//		log.info("各個國家在申請狀態的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), lsps.status_desc "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls "
						+ "JOIN ls.primaryKey.status as lsps "
						+ "where lb.business_id = :businessId "
						+ "and lsps.status_desc = :statusDesc "
						+ "and p.patent_appl_date IS NOT NULL "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and ls.create_date = "
							+ "( select MAX(ls.create_date) "
							+ "FROM p.listPatentStatus as ls "
							+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
						+ "group by p.patent_appl_country";

		Query q = session.createQuery(queryStr);
		String statusDesc = "申請";
		q.setParameter("statusDesc", statusDesc);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}


	
	// 預設：各個國家在公開狀態的專利總數
	@Override
	public List<Analysis> countCountryNoticeStatus(String businessId) {
//		log.info("各個國家在公開狀態的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), lsps.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as lsps "
				+ "where lb.business_id = :businessId "
				+ "and lsps.status_desc = :statusDesc "
				+ "and p.patent_appl_date IS NOT NULL "
				+ "and p.patent_appl_country in ('tw','us','cn') "
				+ "and ls.create_date = "
					+ "( select MAX(ls.create_date) "
					+ "FROM p.listPatentStatus as ls "
					+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
				+ "group by p.patent_appl_country";
		
		Query q = session.createQuery(queryStr);
		String statusDesc = "公開";
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("statusDesc", statusDesc);
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 預設：各個國家在公告狀態的專利總數
	@Override
	public List<Analysis> countCountryPublishStatus(String businessId) {
//		log.info("各個國家在公告狀態的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), lsps.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as lsps "
				+ "where lb.business_id = :businessId "
				+ "and lsps.status_desc = :statusDesc "
				+ "and p.patent_appl_date IS NOT NULL "
				+ "and p.patent_appl_country in ('tw','us','cn') "
				+ "and ls.create_date = "
					+ "( select MAX(ls.create_date) "
					+ "FROM p.listPatentStatus as ls "
					+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
				+ "group by p.patent_appl_country";
		
		Query q = session.createQuery(queryStr);
		String statusDesc = "公告";
		q.setParameter("statusDesc", statusDesc);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 預設：單一國家的各年份總數(國家可選)
	@Override
	public List<Analysis> countSingleCountry(String businessId, String countryId) {
//		log.info("單一國家的各年份總數(國家可選)");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), date_format(patent_appl_date, '%Y') "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_no IS NOT NULL "
						+ "and p.patent_appl_date IS NOT NULL "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and p.patent_appl_country = :countryId "
						+ "group by date_format(patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("countryId", countryId);
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各個國家在特定日期區間的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id )"
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "group by p.patent_appl_country";
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
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在申請狀態的特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryApplStatusByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各個國家在特定日期區間的申請狀態專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as s "
				+ "where lb.business_id = :businessId "
				+ "and s.status_desc = :statusDesc "
				+ "and p.patent_appl_country in ('tw','us','cn') "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "and ls.create_date = ( select MAX(ls.create_date) " 
									  + "FROM p.listPatentStatus as ls " 
									  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
				+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		
		String statusDesc = "申請";
		q.setParameter("statusDesc", statusDesc);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在公開狀態的特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryNoticeStatusByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各個國家在特定日期區間的公開狀態專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls "
						+ "JOIN ls.primaryKey.status as s "
						+ "where lb.business_id = :businessId "
						+ "and s.status_desc = :statusDesc "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and ls.create_date = (select MAX(ls.create_date) " 
											  + "FROM p.listPatentStatus as ls " 
											  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
						+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		
		String statusDesc = "公開";
		q.setParameter("statusDesc", statusDesc);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在公告狀態的特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryPublishStatusByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各個國家在特定日期區間的公各狀態專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls "
						+ "JOIN ls.primaryKey.status as s "
						+ "where lb.business_id = :businessId "
						+ "and s.status_desc = :statusDesc "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and ls.create_date = ( select MAX(ls.create_date) " 
											  + "FROM  p.listPatentStatus as ls " 
											  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
						+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		
		String statusDesc = "公告";
		q.setParameter("statusDesc", statusDesc);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	// 單一國家在特定日期區間的各年份總數(國家可選)
	@Override
	public List<Analysis> countSingleCountryByYear(String businessId, Long beginDate, Long endDate, String countryId) {
//		log.info("單一國家的各年份總數(國家可選)");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), date_format(patent_appl_date, '%Y') "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country in ('tw','us','cn') "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and p.patent_appl_country = :countryId "
						+ "group by date_format(patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		q.setParameter("countryId", countryId);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	//GetDefaultYear
	@Override
	public List<Analysis> getEachDepartmentDefaultYear(String businessId) {
//		log.info("GetDefaultYear");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id), date_format(p.patent_appl_date, '%Y') "
						+ "FROM Patent as p "
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld " 
						+ "where lb.business_id = :businessId "
						+ "AND p.patent_appl_date IS NOT NULL "
						+ "group by date_format(p.patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//預設：各科系專利申請數量
	@Override
	public List<Analysis> countEachDepartment(String businessId) {
//		log.info("各科系專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT distinct(ld.department_name), count(distinct p.patent_id) "
						+ "FROM Patent as p "
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld " 
						+ "where lb.business_id = :businessId "
						+ "AND p.patent_appl_date IS NOT NULL "
						+ "group by ld.department_name";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//預設：各科系在臺灣的專利申請數量
	@Override
	public List<Analysis> countTWEachDepartment(String businessId) {
//		log.info("各科系在臺灣的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = 'TW' "
						+ "group by ld.department_name";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//預設：各科系在中國的專利申請數量
	@Override
	public List<Analysis> countCNEachDepartment(String businessId) {
//		log.info("各科系在中國的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = 'CN' "
						+ "group by ld.department_name";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//預設：各科系在美國的專利申請數量
	@Override
	public List<Analysis> countUSEachDepartment(String businessId) {
//		log.info("各科系在美國的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = 'US' "
						+ "group by ld.department_name";
		Query q = session.createQuery(queryStr);
		
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}

	//各科系在特定區間的專利申請數量
	@Override
	public List<Analysis> countEachDepartmentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在特定區間的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "group by ld.department_name";
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
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//各科系特定區間在臺灣的專利申請數量
	@Override
	public List<Analysis> countTWEachDepartmentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在臺灣的專利申請數量ByYear");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = 'TW' "
						+ "and (date_format(p.patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "group by ld.department_name";
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
//		log.info(q.list().isEmpty());
		return q.list();
	}
	//各科系特定區間在中國的專利申請數量
	@Override
	public List<Analysis> countCNEachDepartmentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在中國的專利申請數量ByYear");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = 'CN' "
						+ "and (date_format(p.patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "group by ld.department_name";
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
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//各科系特定區間在美國的專利申請數量
	@Override
	public List<Analysis> countUSEachDepartmentByYear(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在美國的專利申請數量ByYear");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = 'US' "
						+ "and (date_format(p.patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "group by ld.department_name";
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
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//該科系該國家在該年度的專利-尚未啟用
	public List<Analysis> getDepartmentPatent(String businessId, String departmentId){
		log.info("該科系該國家在該年度的專利");
		Session session = getSession();
		String queryStr = "SELECT distinct ld.department_id, ld.department_name,  p.patent_id, p.patent_appl_country, patent_appl_no "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listDepartment as ld "
						+ "where lb.business_id = :businessId "
						+ "and d.department_name = :departmentId"
						+ "group by p.patent_id";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		if (!StringUtils.isNULL(departmentId)) {
			q.setParameter("departmentId", departmentId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	//預設：專利組合
	@Override
	public int countPorfolio() {
		Session session = getSession();
		String queryStr = "SELECT count(distinct lpf.portfolio_id)"
						+ "FROM Patent as p " 
						+ "JOIN p.listPortfolio as lpf "
						+ "WHERE lpf.create_date IS NOT NULL "; 
		Query q = session.createQuery(queryStr);

		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	//專利組合by year
	@Override
	public int countPorfolioByYear(Long beginDate, Long endDate) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct lpf.portfolio_id)"
						+ "FROM Patent as p " 
						+ "JOIN p.listPortfolio as lpf " 
						+ "WHERE (date_format(lpf.create_date, '%Y') between :beginDate and :endDate) ";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		long count = (long) q.uniqueResult();
		log.info("如果是0，表示未有資料，或create_date皆是NULL: "+count);
		return (int) count;
	}
	
	//預設：專利數
	@Override
	public int countAllPatent() {
//		log.info("專利申請總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_appl_no)" 
						+ "FROM Patent as p " 
						+ "WHERE p.patent_appl_no Not like '%@%' " 
						+ "and patent_appl_date IS NOT NULL ";
		Query q = session.createQuery(queryStr);
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	//專利數By year
	@Override
	public int countAllPatentByYear(Long beginDate, Long endDate) {
//		log.info("專利申請總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_appl_no)" 
						+ "FROM Patent as p " 
						+ "WHERE p.patent_appl_no Not like '%@%' " 
						+ "and patent_appl_date IS NOT NULL "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) ";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	
	//預設：學校數(business_id 去掉智財中心)
	@Override
	public int countSchool() {
		Session session = getSession();
		String queryStr = "SELECT count(distinct b.business_id) "
						+ "FROM Business as b ";
//						+ "WHERE b.create_date IS NOT NULL " ;
		Query q = session.createQuery(queryStr);
		long count = (long) q.uniqueResult();
		if(count==0) {
			log.info(count);
			return (int) count;
		}else {
			count = (long) q.uniqueResult() -1 ;
//			log.info(count);
			return (int) count;
		}
	}
	//學校數By Year(business_id 去掉智財中心)
	@Override
	public int countSchoolByYear(Long beginDate, Long endDate) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct b.business_id) "
						+ "FROM Business as b "
						+ "WHERE (date_format(b.create_date, '%Y') between :beginDate and :endDate)";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);

		long count = (long) q.uniqueResult();
		if(count==0) {
			log.info("如果是0，表示未有資料，或create_date皆是NULL: "+count);
			return (int) count;
		}else {
			count = (long) q.uniqueResult() -1 ;
			log.info(count);
			return (int) count;
		}
	}
	
	@Override
	public List<Analysis> countYearPatent() {
//		log.info("歷年專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id), date_format(p.patent_appl_date, '%Y')"
						+ "FROM Patent as p " 
						+ "WHERE p.patent_appl_no Not like '%@%' "
						+ "and patent_appl_date IS NOT NULL " 
						+ "GROUP BY date_format(patent_appl_date, '%Y') "
						+ "Order by date_format(patent_appl_date, '%Y') asc";

		Query q = session.createQuery(queryStr);
//		log.info(q.list().size());
		return q.list();
	}
	
	@Override
	public List<Analysis> countYearPatentByYear(Long beginDate, Long endDate) {
//		log.info("歷年專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id), date_format(p.patent_appl_date, '%Y')"
						+ "FROM Patent as p " 
						+ "WHERE p.patent_appl_no Not like '%@%' "
						+ "and patent_appl_date IS NOT NULL " 
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "GROUP BY date_format(patent_appl_date, '%Y') "
						+ "Order by date_format(patent_appl_date, '%Y') asc";

		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
//		log.info(q.list().size());
		return q.list();
	}
	
	//Constants.BUSINESS_PLATFORM 平台管理者
	//取得學校清單
	@Override
	public List<String> getSchoolList(){
		Session session = getSession();
//		如果只要有建立過patent的學校
//		String queryStr = "SELECT distinct lb.business_id "
//						+ "FROM Patent as p " 
//						+ "JOIN p.listBusiness as lb "
//						+ "where lb.business_id != :role ";
		String queryStr = "SELECT distinct b.business_id, b.business_name "
						+ "FROM Business as b "
						+ "where b.business_id != :role ";
		Query q = session.createQuery(queryStr);
		q.setParameter("role",Constants.BUSINESS_PLATFORM);
//		log.info(q.list().size());
		return q.list();
	}
	@Override
	public List<Analysis> getDefaultYear(){
		Session session = getSession();
		String queryStr = "SELECT MIN(date_format(patent_appl_date, '%Y')), MAX(date_format(patent_appl_date, '%Y')) "
						+ "FROM Patent as p "
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id != :role ";
		Query q = session.createQuery(queryStr);
		q.setParameter("role",Constants.BUSINESS_PLATFORM);
//		log.info(q.list().size());
		return q.list();
	}
	//取得各校專利數量總和
	@Override
	public List<Analysis> countSchoolSum(){
		Session session = getSession();
		String queryStr = "SELECT distinct lb.business_id, count(p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id != :role "
						+ "GROUP BY lb.business_id";
		Query q = session.createQuery(queryStr);
		q.setParameter("role",Constants.BUSINESS_PLATFORM);
		log.info(q.list().size());
		return q.list();
	}
	//取得各校專利數量總和By Year
	@Override
	public List<Analysis> countSchoolSumByYear(Long beginDate, Long endDate){
		Session session = getSession();
		String queryStr = "SELECT distinct lb.business_id, count(p.patent_id) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id != :role "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "GROUP BY lb.business_id";
		Query q = session.createQuery(queryStr);
		q.setParameter("role",Constants.BUSINESS_PLATFORM);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info("開始年: "+beginDateFormat);
		log.info("結束年: "+endDateFormat);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
//		log.info(q.list().size());
		return q.list();
	}
	//各校各國家總合
	@Override
	public List<Analysis> countSchoolPatentTotal(){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
		
		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "where lb.business_id = :businessId "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}
	//各校各國家申請狀態專利
	@Override
	public List<Analysis> countSchoolPatentApplStatus(){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
		
		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_id = :businessId "
							+ "and lsps.status_desc = :statusDesc "
							+ "and ls.create_date = "
												+ "( select MAX(ls.create_date) "
												+ "FROM p.listPatentStatus as ls "
												+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			String statusDesc = "申請";
			q.setParameter("statusDesc", statusDesc);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}
	//各校各國家公開狀態專利
	@Override
	public List<Analysis> countSchoolPatentNoticeStatus(){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
		
		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_id = :businessId "
							+ "and lsps.status_desc = :statusDesc "
							+ "and ls.create_date = "
												+ "( select MAX(ls.create_date) "
												+ "FROM p.listPatentStatus as ls "
												+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			String statusDesc = "公開";
			q.setParameter("statusDesc", statusDesc);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}
	//各校各國家公告狀態專利
	@Override
	public List<Analysis> countSchoolPatentPublishStatus(){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);

		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_id = :businessId "
							+ "and lsps.status_desc = :statusDesc "
							+ "and ls.create_date = "
												+ "( select MAX(ls.create_date) "
												+ "FROM p.listPatentStatus as ls "
												+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			String statusDesc = "公告";
			q.setParameter("statusDesc", statusDesc);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}
	//各校各國家總和By Year
	@Override
	public List<Analysis> countSchoolPatentTotalByYear(Long beginDate, Long endDate){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);

		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "where lb.business_id = :businessId "
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
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}
	//各校各國家申請狀態專利ByYear
	@Override
	public List<Analysis> countSchoolPatentApplStatusByYear(Long beginDate, Long endDate){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);

		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_id = :businessId "
							+ "and lsps.status_desc = :statusDesc "
							+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
							+ "and ls.create_date = "
												+ "( select MAX(ls.create_date) "
												+ "FROM p.listPatentStatus as ls "
												+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			String statusDesc = "申請";
			q.setParameter("statusDesc", statusDesc);
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
	//各校各國家公開狀態專利ByYear
	@Override
	public List<Analysis> countSchoolPatentNoticeStatusByYear(Long beginDate, Long endDate){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);

		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_id = :businessId "
							+ "and lsps.status_desc = :statusDesc "
							+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
							+ "and ls.create_date = "
												+ "( select MAX(ls.create_date) "
												+ "FROM p.listPatentStatus as ls "
												+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			String statusDesc = "公開";
			q.setParameter("statusDesc", statusDesc);
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
	//各校各國家公告狀態專利ByYear
	@Override
	public List<Analysis> countSchoolPatentPublishStatusByYear(Long beginDate, Long endDate){
		int listInx = 0;
		String businessId = null;
		List<String> schoolList = new ArrayList<String>();
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		schoolList.addAll(getSchoolList());
		Object [][] schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);

		Session session = getSession();
		for (listInx = 0; listInx < schoolListToArray.length; listInx++) {
			businessId = schoolListToArray[listInx][0].toString();
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_id = :businessId "
							+ "and lsps.status_desc = :statusDesc "
							+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
							+ "and ls.create_date = "
												+ "( select MAX(ls.create_date) "
												+ "FROM p.listPatentStatus as ls "
												+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			String statusDesc = "公告";
			q.setParameter("statusDesc", statusDesc);
			if (!StringUtils.isNULL(businessId)) {
				q.setParameter("businessId", businessId);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);

			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
			schoolData.addAll(q.list());
		}
		return schoolData;
	}

	// 預設：各個國家專利總數
	@Override
	public List<Analysis> countSchoolCountry() {
		// log.info("各個國家的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no ) " 
						+ "FROM Patent as p "
						+ "WHERE p.patent_appl_country in ('tw','us','cn') "
						+ "group by p.patent_appl_country";

		Query q = session.createQuery(queryStr);
		return q.list();
	}

	// 預設：各個國家在申請狀態的專利總數
		@Override
		public List<Analysis> countSchoolCountryApplStatus() {
	//		log.info("各個國家在申請狀態的專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), lsps.status_desc "
							+ "FROM Patent as p " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lsps.status_desc = :statusDesc "
							+ "and p.patent_appl_country in ('tw','us','cn') "
							+ "and ls.create_date = "
								+ "( select MAX(ls.create_date) "
								+ "FROM p.listPatentStatus as ls "
								+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "group by p.patent_appl_country";
	
			Query q = session.createQuery(queryStr);
			String statusDesc = "申請";
			q.setParameter("statusDesc", statusDesc);

	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 預設：各個國家在公開狀態的專利總數
		@Override
		public List<Analysis> countSchoolCountryNoticeStatus() {
	//		log.info("各個國家在公開狀態的專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), lsps.status_desc "
					+ "FROM Patent as p " 
					+ "JOIN p.listPatentStatus as ls "
					+ "JOIN ls.primaryKey.status as lsps "
					+ "where lsps.status_desc = :statusDesc "
					+ "and p.patent_appl_country in ('tw','us','cn') "
					+ "and ls.create_date = "
						+ "( select MAX(ls.create_date) "
						+ "FROM p.listPatentStatus as ls "
						+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
					+ "group by p.patent_appl_country";
			
			Query q = session.createQuery(queryStr);
			String statusDesc = "公開";

			q.setParameter("statusDesc", statusDesc);
	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 預設：各個國家在公告狀態的專利總數
		@Override
		public List<Analysis> countSchoolCountryPublishStatus() {
	//		log.info("各個國家在公告狀態的專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), lsps.status_desc "
					+ "FROM Patent as p " 
					+ "JOIN p.listPatentStatus as ls "
					+ "JOIN ls.primaryKey.status as lsps "
					+ "where lsps.status_desc = :statusDesc "
					+ "and p.patent_appl_country in ('tw','us','cn') "
					+ "and ls.create_date = "
						+ "( select MAX(ls.create_date) "
						+ "FROM p.listPatentStatus as ls "
						+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
					+ "group by p.patent_appl_country";
			
			Query q = session.createQuery(queryStr);
			String statusDesc = "公告";
			q.setParameter("statusDesc", statusDesc);

	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 預設：單一國家的各年份總數(國家可選)
		@Override
		public List<Analysis> countSchoolSingleCountry(String countryId) {
	//		log.info("單一國家的各年份總數(國家可選)");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), date_format(patent_appl_date, '%Y') "
							+ "FROM Patent as p " 
							+ "where p.patent_appl_date IS NOT NULL "
							+ "and p.patent_appl_date NOT IN('', 0) "
							+ "and p.patent_appl_country in ('tw','us','cn') "
							+ "and p.patent_appl_country = :countryId "
							+ "group by date_format(patent_appl_date, '%Y')";
			Query q = session.createQuery(queryStr);
			q.setParameter("countryId", countryId);
	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 各個國家在特定日期區間的專利總數
		@Override
		public List<Analysis> countSchoolCountryByYear(Long beginDate, Long endDate) {
	//		log.info("各個國家在特定日期區間的專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id )"
							+ "FROM Patent as p " 
							+ "where (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
							+ "and p.patent_appl_country in ('tw','us','cn') "
							+ "group by p.patent_appl_country";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
	
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
			log.info(beginDateFormat + " - " + endDateFormat);
//			log.info(q.list().size());
			return q.list();
		}

	// 各個國家在申請狀態的特定日期區間的專利總數
		@Override
		public List<Analysis> countSchoolCountryApplStatusByYear(Long beginDate, Long endDate) {
	//		log.info("各個國家在特定日期區間的申請狀態專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
					+ "FROM Patent as p " 
					+ "JOIN p.listPatentStatus as ls "
					+ "JOIN ls.primaryKey.status as s "
					+ "where s.status_desc = :statusDesc "
					+ "and p.patent_appl_country in ('tw','us','cn') "
					+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
					+ "and ls.create_date = ( select MAX(ls.create_date) " 
										  + "FROM p.listPatentStatus as ls " 
										  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
					+ "group by p.patent_appl_country";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			String statusDesc = "申請";
			q.setParameter("statusDesc", statusDesc);
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);

	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 各個國家在公開狀態的特定日期區間的專利總數
		@Override
		public List<Analysis> countSchoolCountryNoticeStatusByYear(Long beginDate, Long endDate) {
	//		log.info("各個國家在特定日期區間的公開狀態專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
					+ "FROM Patent as p " 
					+ "JOIN p.listPatentStatus as ls "
					+ "JOIN ls.primaryKey.status as s "
					+ "where s.status_desc = :statusDesc "
					+ "and p.patent_appl_country in ('tw','us','cn') "
					+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
					+ "and ls.create_date = " 
					  + "( select MAX(ls.create_date) " 
					  + "FROM p.listPatentStatus as ls " 
					  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
					+ "group by p.patent_appl_country";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			String statusDesc = "公開";
			q.setParameter("statusDesc", statusDesc);
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);

	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 各個國家在公告狀態的特定日期區間的專利總數
		@Override
		public List<Analysis> countSchoolCountryPublishStatusByYear(Long beginDate, Long endDate) {
	//		log.info("各個國家在特定日期區間的公各狀態專利總數");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
					+ "FROM Patent as p " 
					+ "JOIN p.listPatentStatus as ls "
					+ "JOIN ls.primaryKey.status as s "
					+ "WHERE s.status_desc = :statusDesc "
					+ "and p.patent_appl_country in ('tw','us','cn') "
					+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
					+ "and ls.create_date = ( select MAX(ls.create_date) " 
										  + "FROM p.listPatentStatus as ls " 
										  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
					+ "group by p.patent_appl_country";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			
			String statusDesc = "公告";
			q.setParameter("statusDesc", statusDesc);
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
	//		log.info(q.list().isEmpty());
			return q.list();
		}

	// 單一國家在特定日期區間的各年份總數(國家可選)
		@Override
		public List<Analysis> countSchoolSingleCountryByYear(Long beginDate, Long endDate, String countryId) {
	//		log.info("單一國家的各年份總數(國家可選)");
			Session session = getSession();
			String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), date_format(patent_appl_date, '%Y') "
							+ "FROM Patent as p " 
							+ "WHERE (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
							+ "and p.patent_appl_country in ('tw','us','cn') "
							+ "and p.patent_appl_country = :countryId "
							+ "group by date_format(patent_appl_date, '%Y')";
			Query q = session.createQuery(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
	
			q.setParameter("beginDate", beginDateFormat);
			q.setParameter("endDate", endDateFormat);
			q.setParameter("countryId", countryId);

	//		log.info(q.list().isEmpty());
			return q.list();
		}
	//For Excel Export 各國家總合By Select School
	@Override
	public List<Analysis> countSchoolPatentTotal(JSONArray schoolArray){
		int listInx = 0;
		String businessName = null;
		List<Analysis> schoolData = new ArrayList<Analysis>();
		
		Session session = getSession();
		for (listInx = 0; listInx < schoolArray.length(); listInx++) {
			businessName = schoolArray.optString(listInx);
			String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "where lb.business_name = :businessName "
							+ "GROUP BY p.patent_appl_country ";
			Query q = session.createQuery(queryStr);
			if (!StringUtils.isNULL(businessName)) {
				q.setParameter("businessName", businessName);
			}
			schoolData.addAll(q.list());
		}
		return schoolData;
	}

	//For Excel Export 各狀態各國家By Select Status and school
		@Override
		public List<Analysis> countSchoolPatentStatus(JSONArray statusDesc,JSONArray schoolArray){
			int schoolInx = 0;
			int statusInx = 0;
			String businessName = null;
			String status =null;
			List<Analysis> schoolData = new ArrayList<Analysis>();
	
			Session session = getSession();
			log.info(statusDesc.length());
			for(statusInx=0;statusInx<statusDesc.length();statusInx++) {
				status = statusDesc.optString(statusInx);
				for (schoolInx = 0; schoolInx < schoolArray.length(); schoolInx++) {
					businessName = schoolArray.optString(schoolInx);
					String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
							+ "FROM Patent as p "
							+ "JOIN p.listBusiness as lb " 
							+ "JOIN p.listPatentStatus as ls "
							+ "JOIN ls.primaryKey.status as lsps "
							+ "where lb.business_name = :businessName "
							+ "and lsps.status_desc = :status "
							+ "and ls.create_date = "
							+ "( select MAX(ls.create_date) "
							+ "FROM p.listPatentStatus as ls "
							+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
							+ "GROUP BY p.patent_appl_country ";
					Query q = session.createQuery(queryStr);
					q.setParameter("status", status);
					if (!StringUtils.isNULL(businessName)) {
						q.setParameter("businessName", businessName);
					}
					schoolData.addAll(q.list());
				}
			}
	//		log.info(schoolData);
			return schoolData;
		}

	//For Excel Export 各國家總合By Select School
		@Override
		public List<Analysis> countSchoolPatentTotalByYear(JSONArray schoolArray,Long beginDate, Long endDate){
			int listInx = 0;
			String businessName = null;
			List<Analysis> schoolData = new ArrayList<Analysis>();
			
			Session session = getSession();
			for (listInx = 0; listInx < schoolArray.length(); listInx++) {
				businessName = schoolArray.optString(listInx);
				String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country " 
								+ "FROM Patent as p "
								+ "JOIN p.listBusiness as lb " 
								+ "where lb.business_name = :businessName "
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
				log.info(q.list().isEmpty());
				schoolData.addAll(q.list());
			}
			log.info(schoolData);
			return schoolData;
		}

		//For Excel Export 各狀態各國家By Select Status and school
			@Override
			public List<Analysis> countSchoolPatentStatusByYear(JSONArray statusDesc,JSONArray schoolArray,Long beginDate, Long endDate){
				int schoolInx = 0;
				int statusInx = 0;
				String businessName = null;
				String status =null;
				List<Analysis> schoolData = new ArrayList<Analysis>();
		
				Session session = getSession();
				log.info(statusDesc.length());
				for(statusInx=0;statusInx<statusDesc.length();statusInx++) {
					status = statusDesc.optString(statusInx);
					for (schoolInx = 0; schoolInx < schoolArray.length(); schoolInx++) {
						businessName = schoolArray.optString(schoolInx);
						String queryStr = "SELECT lb.business_id, lb.business_name, count(p.patent_id), p.patent_appl_country, lsps.status_desc " 
								+ "FROM Patent as p "
								+ "JOIN p.listBusiness as lb " 
								+ "JOIN p.listPatentStatus as ls "
								+ "JOIN ls.primaryKey.status as lsps "
								+ "where lb.business_name = :businessName "
								+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
								+ "and lsps.status_desc = :status "
								+ "and ls.create_date = "
								+ "( select MAX(ls.create_date) "
								+ "FROM p.listPatentStatus as ls "
								+ "where p.patent_id = ls.primaryKey.patent.patent_id ) "
								+ "GROUP BY p.patent_appl_country ";
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
						log.info("開始年: "+beginDateFormat+"，結束年: "+endDateFormat);
						q.setParameter("beginDate", beginDateFormat);
						q.setParameter("endDate", endDateFormat);
						log.info(q.list().isEmpty());
						log.info(schoolArray);
						log.info(statusDesc);
						schoolData.addAll(q.list());
					}
				}
				log.info(schoolData);
				return schoolData;
			}

	@Override
	public List<Analysis> testPatent() {
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_id), s.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as s "
				+ "WHERE s.status_desc = :statusDesc "
				+ "and p.patent_appl_country in ('tw','us','cn') "
				+ "and ls.create_date = ( select MAX(ls.create_date) " 
									  + "FROM p.listPatentStatus as ls " 
									  + "where p.patent_id = ls.primaryKey.patent.patent_id ) "
				+ "group by s.status_desc";
		Query q = session.createQuery(queryStr);
//		String statusDesc = "'申請','公開','公告'"; 
		String statusDesc = "'申請','公開'"; 
		q.setParameter("statusDesc", statusDesc);
		
		log.info(q.list().isEmpty());
		return q.list();
	}

	@Override
	public List<Analysis> testPatent(String businessId) {
		// TODO Auto-generated method stub
		return null;
	}

}
