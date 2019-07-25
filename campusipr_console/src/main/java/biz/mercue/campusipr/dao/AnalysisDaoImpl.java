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

	// 預設：歷年專利申請數量
	@Override
	public List<Analysis> countAllYearPatent(String businessId) {
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

	// 預設：專利申請總數
	@Override
	public int countPatentTotal(String businessId) {
//		log.info("專利申請總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id)" 
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

	// 預設：專利家族總數
	@Override
	public int countPatentFamilyTotal(String businessId) {
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
	public int countDepartmentTotal(String businessId) {
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
	public int countInventorTotal(String businessId) {
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
	public int countInventorEnTotal(String businessId) {
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
	public int countPatent(String businessId, Long beginDate, Long endDate) {
//		log.info("特定年度區間專利申請總數");
		Session session = getSession();
		String queryStr = "SELECT count(distinct p.patent_id)" 
						+ "FROM Patent as p " + "JOIN p.listBusiness as lb "
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
	public int countPatentFamily(String businessId, Long beginDate, Long endDate) {
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
	public int countDepartment(String businessId, Long beginDate, Long endDate) {
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
	public int countInventor(String businessId, Long beginDate, Long endDate) {
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
	public int countInventorEn(String businessId, Long beginDate, Long endDate) {
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
	public List<Analysis> countPatentByYear(String businessId, Long beginDate, Long endDate) {
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
	@Override
	public List<Analysis> countCountryTotal(String businessId) {
		log.info("各個國家的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no ) "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}
	// 預設：各個國家在申請狀態的專利總數
	@Override
	public List<Analysis> countCountryApplStatusTotal(String businessId) {
		log.info("各個國家在申請狀態的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), s.status_desc "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls "
						+ "JOIN ls.primaryKey.status as s "
						+ "where lb.business_id = :businessId "
						+ "and s.status_desc = :statusDesc "
						+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		String statusDesc = "申請";
		q.setParameter("statusDesc", statusDesc);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 預設：各個國家在公開狀態的專利總數
	@Override
	public List<Analysis> countCountryNoticeStatusTotal(String businessId) {
		log.info("各個國家在公開狀態的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), s.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as s "
				+ "where lb.business_id = :businessId "
				+ "and s.status_desc = :statusDesc "
				+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		String statusDesc = "公開";
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("statusDesc", statusDesc);
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 預設：各個國家在公告狀態的專利總數
	@Override
	public List<Analysis> countCountryPublishStatusTotal(String businessId) {
		log.info("各個國家在公告狀態的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), s.status_desc "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "JOIN p.listPatentStatus as ls "
						+ "JOIN ls.primaryKey.status as s "
						+ "where lb.business_id = :businessId "
						+ "and s.status_desc = :statusDesc "
						+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		String statusDesc = "公告";
		q.setParameter("statusDesc", statusDesc);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 預設：單一國家的各年份總數(國家可選)
	@Override
	public List<Analysis> countCountryByYearTotal(String businessId, String countryId) {
		log.info("單一國家的各年份總數(國家可選)");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), date_format(patent_appl_date, '%Y') "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "and p.patent_appl_country = :countryId "
						+ "group by date_format(patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		q.setParameter("countryId", countryId);
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在特定日期區間的專利總數
	@Override
	public List<Analysis> countCountry(String businessId, Long beginDate, Long endDate) {
		log.info("各個國家在特定日期區間的專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no )"
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在申請狀態的特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryApplStatus(String businessId, Long beginDate, Long endDate) {
		log.info("各個國家在特定日期區間的申請狀態專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), s.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as s "
				+ "where lb.business_id = :businessId "
				+ "and s.status_desc = :statusDesc "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		String statusDesc = "申請";
		q.setParameter("statusDesc", statusDesc);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在公開狀態的特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryNoticeStatus(String businessId, Long beginDate, Long endDate) {
		log.info("各個國家在特定日期區間的公開狀態專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), s.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as s "
				+ "where lb.business_id = :businessId "
				+ "and s.status_desc = :statusDesc "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		String statusDesc = "公開";
		q.setParameter("statusDesc", statusDesc);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 各個國家在公告狀態的特定日期區間的專利總數
	@Override
	public List<Analysis> countCountryPublishStatus(String businessId, Long beginDate, Long endDate) {
		log.info("各個國家在特定日期區間的公各狀態專利總數");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), s.status_desc "
				+ "FROM Patent as p " 
				+ "JOIN p.listBusiness as lb "
				+ "JOIN p.listPatentStatus as ls "
				+ "JOIN ls.primaryKey.status as s "
				+ "where lb.business_id = :businessId "
				+ "and s.status_desc = :statusDesc "
				+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
				+ "group by p.patent_appl_country";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);
		
		String statusDesc = "公告";
		q.setParameter("statusDesc", statusDesc);
		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	// 單一國家在特定日期區間的各年份總數(國家可選)
	@Override
	public List<Analysis> countCountryByYear(String businessId, Long beginDate, Long endDate, String countryId) {
		log.info("單一國家的各年份總數(國家可選)");
		Session session = getSession();
		String queryStr = "SELECT p.patent_appl_country, count(distinct p.patent_appl_no), date_format(patent_appl_date, '%Y') "
						+ "FROM Patent as p " 
						+ "JOIN p.listBusiness as lb "
						+ "where lb.business_id = :businessId "
						+ "and (date_format(patent_appl_date, '%Y') between :beginDate and :endDate) "
						+ "and p.patent_appl_country = :countryId "
						+ "group by date_format(patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info(beginDateFormat);
		log.info(endDateFormat);

		q.setParameter("beginDate", beginDateFormat);
		q.setParameter("endDate", endDateFormat);
		q.setParameter("countryId", countryId);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

	//預設：各科系專利申請數量
	@Override
	public List<Analysis> countEachDepartmentTotal(String businessId) {
//		log.info("各科系專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT d.department_name, count(distinct d.department_id) "
						+ "FROM Department as d " 
						+ "where d.business_id = :businessId "
						+ "group by d.department_name";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
//		log.info(q.list().isEmpty());
		return q.list();
	}
	
	//預設：各科系在臺灣的專利申請數量
	@Override
	public List<Analysis> countTWEachDepartmentTotal(String businessId) {
//		log.info("各科系在臺灣的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	public List<Analysis> countCNEachDepartmentTotal(String businessId) {
//		log.info("各科系在中國的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	public List<Analysis> countUSEachDepartmentTotal(String businessId) {
//		log.info("各科系在美國的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	public List<Analysis> countEachDepartment(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在特定區間的專利申請數量");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	public List<Analysis> countTWEachDepartment(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在臺灣的專利申請數量ByYear");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	public List<Analysis> countCNEachDepartment(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在中國的專利申請數量ByYear");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	public List<Analysis> countUSEachDepartment(String businessId, Long beginDate, Long endDate) {
//		log.info("各科系在美國的專利申請數量ByYear");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, count(distinct ld.department_id) "
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
	
	//該科系該國家在該年度的專利-尚未啟用
	public List<Analysis> getDepartmentPatent(String businessId, String departmentId){
		log.info("該科系該國家在該年度的專利");
		Session session = getSession();
		String queryStr = "SELECT ld.department_name, distinct ld.department_id, p.patent_id, p.patent_appl_country, patent_appl_no "
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
	//專利組合
	@Override
	public int countPorfolio(String businessId) {
		Session session = getSession();
		String queryStr = "SELECT count(distinct lpf.portfolio_id)"
						+ "FROM Patent as p " 
						+ "JOIN p.listPortfolio as lpf " 
						+ "WHERE lb.business_id = :businessId ";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	//專利組合by year
	@Override
	public int countPorfolioByYear(String businessId, Long beginDate, Long endDate) {
		Session session = getSession();
		String queryStr = "SELECT date_format(p.patent_appl_date, '%Y'),count(distinct p.patent_id)"
				+ "FROM Patent as p " + "JOIN p.listBusiness as lb " + "WHERE lb.business_id = :businessId "
				+ "and p.patent_appl_no Not like '%@%' " + "GROUP BY date_format(p.patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		long count = (long) q.uniqueResult();
//		log.info(count);
		return (int) count;
	}
	
	
	//學校數(business_id 去掉智財中心)
	
	
	
	public List<Analysis> testPatent(String businessId) {
		Session session = getSession();
		String queryStr = "SELECT date_format(p.patent_appl_date, '%Y'),count(distinct p.patent_id)"
				+ "FROM Patent as p " + "JOIN p.listBusiness as lb " + "WHERE lb.business_id = :businessId "
				+ "and p.patent_appl_no Not like '%@%' " + "GROUP BY date_format(p.patent_appl_date, '%Y')";
		Query q = session.createQuery(queryStr);
		if (!StringUtils.isNULL(businessId)) {
			q.setParameter("businessId", businessId);
		}
		log.info(q.list().isEmpty());
		return q.list();
	}

}
