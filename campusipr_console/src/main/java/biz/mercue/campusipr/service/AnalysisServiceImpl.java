package biz.mercue.campusipr.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AnalysisDao;
import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.util.Constants;
import javassist.expr.NewArray;

@Service("analysisService")
@Transactional
public class AnalysisServiceImpl implements AnalysisService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PatentDao patentDao;

	@Autowired
	private AnalysisDao analysisDao;

	@Autowired
	private StatusDao statusDao;
	
	@Override
	public JSONObject schoolOverview(String businessId) {
		log.info("analysis All Patent");
		//技術名稱analyticBean.analTechTotal
		int unApplPatent;
		int analYearsTotal;
		int analFamilyTotal;
		int analDepartmentTotal;
		int analInventorToltal;
		List<Analysis> analAllYearsList = new ArrayList<Analysis>();
		List<Object> combineOverview = new ArrayList<Object>();
		
		unApplPatent = analysisDao.countUnApplPatent(businessId);
		analYearsTotal = analysisDao.countAllPatent(businessId);
		analFamilyTotal = analysisDao.countPatentFamily(businessId);
		analDepartmentTotal = analysisDao.countDepartment(businessId);
		analInventorToltal = analysisDao.countInventor(businessId) + analysisDao.countInventorEn(businessId);
		analAllYearsList = analysisDao.countYearPatent(businessId);

		log.info("未官方同步專利: "+unApplPatent);
		log.info("專利申請總數: "+analYearsTotal);
		log.info("專利家族總數: "+analFamilyTotal);
		log.info("科系總數: "+analDepartmentTotal);
		log.info("發明人總數: "+analInventorToltal);

		Object yearToArray[][] = analAllYearsList.toArray(new Object[analAllYearsList.size()][0]);
		combineOverview.addAll(combineOverview(yearToArray));

		JSONObject result = new JSONObject();
		result.put("unApplPatent",unApplPatent);
		result.put("analYearsTotal",analYearsTotal);
		result.put("analFamilyTotal",analFamilyTotal);
		result.put("analDepartmentTotal",analDepartmentTotal);
		result.put("analInventorToltal",analInventorToltal);
		result.put("analAllYearsList",combineOverview);
		
		return result;
	}
	
	@Override
	public JSONObject schoolOverviewByYear(String businessId, Long beginDate, Long endDate) {
		log.info("analysis Patent By Year");
		int unApplPatent;
		int analYearsTotal ;
		int analFamilyTotal;
		int analDepartmentTotal;
		int analInventorToltal;
		List<Analysis> countPatentByYear = new ArrayList<Analysis>();
		
		List<Object> combineOverview = new ArrayList<Object>();
		
		unApplPatent = analysisDao.countUnApplPatent(businessId);
		analYearsTotal= analysisDao.countAllPatentByYear(businessId, beginDate, endDate);
		analFamilyTotal = analysisDao.countPatentFamilyByYear(businessId, beginDate, endDate);
		analDepartmentTotal = analysisDao.countDepartmentByYear(businessId, beginDate, endDate);
		analInventorToltal = analysisDao.countInventorByYear(businessId, beginDate, endDate) + analysisDao.countInventorEnByYear(businessId, beginDate, endDate);
		countPatentByYear = analysisDao.countYearPatentByYear(businessId, beginDate, endDate);

		log.info("未官方同步專利: "+unApplPatent);
		log.info("專利申請總數: "+analYearsTotal);
		log.info("專利家族總數: "+analFamilyTotal);
		log.info("科系總數: "+analDepartmentTotal);
		log.info("發明人總數: "+analInventorToltal);
		
		Object yearToArray[][] = countPatentByYear.toArray(new Object[countPatentByYear.size()][0]);
		combineOverview.addAll(combineOverviewByYear(yearToArray, beginDate, endDate));
		
		JSONObject result = new JSONObject();
		result.put("unApplPatent",unApplPatent);
		result.put("analYearsTotal",analYearsTotal);
		result.put("analFamilyTotal",analFamilyTotal);
		result.put("analDepartmentTotal",analDepartmentTotal);
		result.put("analInventorToltal",analInventorToltal);
		result.put("analAllYearsList",combineOverview);
		
		return result;
	}

	@Override
	public JSONObject schoolCountry(String businessId, String countryId) {
		log.info("analysis All Country");
		List<Analysis> countCountryTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryApplStatusTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryNoticeStatusTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryPublishStatusTotal= new ArrayList<Analysis>();
		List<Analysis> countCountryByYearTotal= new ArrayList<Analysis>();
		List<Analysis> analAllYearsList = new ArrayList<Analysis>();
		List<Object> combineYear = new ArrayList<Object>();
		List<Object> combineCountry = new ArrayList<Object>();
		List<Object> combineApplStatus = new ArrayList<Object>();
		List<Object> combineNoticeStatus = new ArrayList<Object>();
		List<Object> combinePublishStatus = new ArrayList<Object>();
		
		countCountryTotal = analysisDao.countCountry(businessId);
		countCountryApplStatusTotal = analysisDao.countCountryApplStatus(businessId);
		countCountryNoticeStatusTotal = analysisDao.countCountryNoticeStatus(businessId);
		countCountryPublishStatusTotal = analysisDao.countCountryPublishStatus(businessId);
		countCountryByYearTotal = analysisDao.countSingleCountry(businessId, countryId);
		analAllYearsList = analysisDao.countYearPatent(businessId);
		
		Object yearToArray[][] = analAllYearsList.toArray(new Object[analAllYearsList.size()][0]);
		Object dataToArray[][] = countCountryByYearTotal.toArray(new Object[countCountryByYearTotal.size()][0]);
		
		combineYear.addAll(combineCountryYear(yearToArray,dataToArray,countryId));
		
		Object countryToArray [][]=countCountryTotal.toArray(new Object[countCountryTotal.size()][0]);
		Object ApplStatusToArray [][]=countCountryApplStatusTotal.toArray(new Object[countCountryApplStatusTotal.size()][0]);
		Object noticeStatusToArray [][]=countCountryNoticeStatusTotal.toArray(new Object[countCountryNoticeStatusTotal.size()][0]);
		Object publishStatusToArray [][]=countCountryPublishStatusTotal.toArray(new Object[countCountryPublishStatusTotal.size()][0]);
		
		if(countCountryTotal.size()==3) {
			combineCountry.addAll(countCountryTotal);
		}else {
			combineCountry.addAll(combineStatus(countryToArray));
		}
		
		if(countCountryApplStatusTotal.size()==3) {
			combineApplStatus.addAll(countCountryApplStatusTotal);
		}else {
			combineApplStatus.addAll(combineStatus(ApplStatusToArray));
		}
		
		if(countCountryNoticeStatusTotal.size()==3) {
			combineNoticeStatus.addAll(countCountryNoticeStatusTotal);
		}else {
			combineNoticeStatus.addAll(combineStatus(noticeStatusToArray));
		}
		
		if(countCountryPublishStatusTotal.size()==3) {
			combinePublishStatus.addAll(countCountryPublishStatusTotal);
		}else {
			combinePublishStatus.addAll(combineStatus(publishStatusToArray));
		}
		
		JSONObject result = new JSONObject();
		result.put("countCountryTotal",combineCountry);
		result.put("countCountryApplStatusTotal",combineApplStatus);
		result.put("countCountryNoticeStatusTotal",combineNoticeStatus);
		result.put("countCountryPublishStatusTotal",combinePublishStatus);
		result.put("analAllYearsList",analAllYearsList);
		result.put("countCountryByYearTotal",combineYear);
		
		return result;
	}
	@Override
	public JSONObject schoolCountryByYear(String businessId, Long beginDate, Long endDate, String countryId) {
		log.info("analysis Country By Years");
		List<Analysis> countCountry = new ArrayList<Analysis>();
		List<Analysis> countCountryApplStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryNoticeStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryPublishStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryByYear = new ArrayList<Analysis>();

		List<Object> combineCountry = new ArrayList<Object>();
		List<Object> combineApplStatus = new ArrayList<Object>();
		List<Object> combineNoticeStatus = new ArrayList<Object>();
		List<Object> combinePublishStatus = new ArrayList<Object>();

		List<Object> combineYear = new ArrayList<Object>();

		countCountry = analysisDao.countCountryByYear(businessId, beginDate, endDate);
		countCountryApplStatus = analysisDao.countCountryApplStatusByYear(businessId, beginDate, endDate);
		countCountryNoticeStatus = analysisDao.countCountryNoticeStatusByYear(businessId, beginDate, endDate);
		countCountryPublishStatus = analysisDao.countCountryPublishStatusByYear(businessId, beginDate, endDate);
		countCountryByYear = analysisDao.countSingleCountryByYear(businessId, beginDate, endDate, countryId);

		Object yearToArray[][] = countCountryByYear.toArray(new Object[countCountryByYear.size()][0]);
		Object countryToArray[][] = countCountry.toArray(new Object[countCountry.size()][0]);
		Object applStatusToArray[][] = countCountryApplStatus.toArray(new Object[countCountryApplStatus.size()][0]);
		Object noticeStatusToArray[][] = countCountryNoticeStatus
				.toArray(new Object[countCountryNoticeStatus.size()][0]);
		Object publishStatusToArray[][] = countCountryPublishStatus
				.toArray(new Object[countCountryPublishStatus.size()][0]);

		combineYear.addAll(combineCountryByYear(yearToArray, beginDate, endDate, countryId));

		if (countCountry.size() == 3) {
			combineCountry.addAll(countCountry);
		} else {
			combineCountry.addAll(combineStatus(countryToArray));
		}

		if (countCountryApplStatus.size() == 3) {
			combineApplStatus.addAll(countCountryApplStatus);
		} else {
			combineApplStatus.addAll(combineStatus(applStatusToArray));
		}

		if (countCountryNoticeStatus.size() == 3) {
			combineNoticeStatus.addAll(countCountryNoticeStatus);
		} else {
			combineNoticeStatus.addAll(combineStatus(noticeStatusToArray));
		}

		if (countCountryPublishStatus.size() == 3) {
			combinePublishStatus.addAll(countCountryPublishStatus);
		} else {
			combinePublishStatus.addAll(combineStatus(publishStatusToArray));
		}

		JSONObject result = new JSONObject();
		result.put("countCountryTotal", combineCountry);
		result.put("countCountryApplStatusTotal", combineApplStatus);
		result.put("countCountryNoticeStatusTotal", combineNoticeStatus);
		result.put("countCountryPublishStatusTotal", combinePublishStatus);
		result.put("countCountryByYearTotal", combineYear);

		return result;
	}

	@Override
	public JSONObject schoolDepartment(String businessId) {
		log.info("analysis All Department");
		List<Analysis> countEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> getEachDepartmentDefaultYear = new ArrayList<Analysis>();
		
		List<Object> twdepCombine = new ArrayList<Object>();
		List<Object> cndepCombine = new ArrayList<Object>();
		List<Object> usdepCombine = new ArrayList<Object>();
		
		getEachDepartmentDefaultYear = analysisDao.getEachDepartmentDefaultYear(businessId);
		
		countEachDepartmentTotal = analysisDao.countEachDepartment(businessId);
		countTWEachDepartmentTotal = analysisDao.countTWEachDepartment(businessId);
		countCNEachDepartmentTotal = analysisDao.countCNEachDepartment(businessId);
		countUSEachDepartmentTotal = analysisDao.countUSEachDepartment(businessId);
		
		Object depToArray [][]  = countEachDepartmentTotal.toArray(new Object[countEachDepartmentTotal.size()][0]);
		Object twToArray [][]  = countTWEachDepartmentTotal.toArray(new Object[countTWEachDepartmentTotal.size()][0]);
		Object cnToArray [][]  = countCNEachDepartmentTotal.toArray(new Object[countCNEachDepartmentTotal.size()][0]);
		Object usToArray [][]  = countUSEachDepartmentTotal.toArray(new Object[countUSEachDepartmentTotal.size()][0]);
		
		try {
			twdepCombine.addAll(combineDepartment(depToArray, twToArray));
			cndepCombine.addAll(combineDepartment(depToArray, cnToArray));
			usdepCombine.addAll(combineDepartment(depToArray, usToArray));
		} catch (Exception e) {
			log.error(e);
		}
		
		log.info("各科系專利申請數量: " + countEachDepartmentTotal.size());
		
		JSONObject result = new JSONObject();
		result.put("countEachDepartmentTotal",countEachDepartmentTotal);
		result.put("countTWEachDepartmentTotal",twdepCombine);
		result.put("countCNEachDepartmentTotal",cndepCombine);
		result.put("countUSEachDepartmentTotal",usdepCombine);
		result.put("getEachDepartmentDefaultYear",getEachDepartmentDefaultYear);
		
		return result;
	}

	//科系依年度查詢
	@Override
	public JSONObject schoolDepartmentByYear(String businessId, Long beginDate, Long endDate) {
		log.info("analysis Department By Years");
		List<Analysis> countEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartment = new ArrayList<Analysis>();
		List<Object> twdepCombine = new ArrayList<Object>();
		List<Object> cndepCombine = new ArrayList<Object>();
		List<Object> usdepCombine = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		log.info("開始年: "+beginDateFormat);
		log.info("結束年: "+endDateFormat);
		
		countEachDepartment = analysisDao.countEachDepartmentByYear(businessId, beginDate, endDate);
		countTWEachDepartment = analysisDao.countTWEachDepartmentByYear(businessId, beginDate, endDate);
		countCNEachDepartment = analysisDao.countCNEachDepartmentByYear(businessId, beginDate, endDate);
		countUSEachDepartment = analysisDao.countUSEachDepartmentByYear(businessId, beginDate, endDate);
		
		try {
			if(countEachDepartment.isEmpty()) {
				twdepCombine.addAll(countTWEachDepartment);
				cndepCombine.addAll(countCNEachDepartment);
				usdepCombine.addAll(countUSEachDepartment);
			}else {
				Object depToArray [][]  = countEachDepartment.toArray(new Object[countEachDepartment.size()][0]);
				Object twToArray [][]  = countTWEachDepartment.toArray(new Object[countTWEachDepartment.size()][0]);
				Object cnToArray [][]  = countCNEachDepartment.toArray(new Object[countCNEachDepartment.size()][0]);
				Object usToArray [][]  = countUSEachDepartment.toArray(new Object[countUSEachDepartment.size()][0]);
				
				twdepCombine.addAll(combineDepartment(depToArray, twToArray));
				cndepCombine.addAll(combineDepartment(depToArray, cnToArray));
				usdepCombine.addAll(combineDepartment(depToArray, usToArray));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject result = new JSONObject();
		result.put("countEachDepartmentTotal",countEachDepartment);
		result.put("countTWEachDepartmentTotal",twdepCombine);
		result.put("countCNEachDepartmentTotal",cndepCombine);
		result.put("countUSEachDepartmentTotal",usdepCombine);
		
		return result;
	}

	@Override
	public JSONObject platformOverview() {
		int schoolSum = 0;
		int applSum = 0;
		int porfolioSum = 0;
		List<Analysis> countYearPatent= new ArrayList<Analysis>();
		List<Object> combineOverview = new ArrayList<Object>();
		
		schoolSum = analysisDao.countSchool();
		applSum = analysisDao.countAllPatent();
		porfolioSum = analysisDao.countPorfolio();
		countYearPatent = analysisDao.countYearPatent();
		
		Object yearToArray[][] = countYearPatent.toArray(new Object[countYearPatent.size()][0]);
		combineOverview.addAll(combineOverview(yearToArray));
		
		JSONObject result = new JSONObject();
		result.put("schoolSum",schoolSum);
		result.put("applSum",applSum);
		result.put("porfolioSum",porfolioSum);
		result.put("analAllYearsList",combineOverview);
		log.info(result);
		return result;
	}

	@Override
	public JSONObject platformOverviewByYear(Long beginDate, Long endDate) {
		
		int schoolSum = 0;
		int applSum = 0;
		int porfolioSum = 0;
		List<Analysis> countYearPatent= new ArrayList<Analysis>();
		List<Object> combineOverview = new ArrayList<Object>();
		
		schoolSum = analysisDao.countSchoolByYear(beginDate, endDate);
		applSum = analysisDao.countAllPatentByYear(beginDate, endDate);
		porfolioSum = analysisDao.countPorfolioByYear(beginDate, endDate);
		countYearPatent = analysisDao.countYearPatentByYear(beginDate, endDate);
		
		Object yearToArray[][] = countYearPatent.toArray(new Object[countYearPatent.size()][0]);
		combineOverview.addAll(combineOverviewByYear(yearToArray, beginDate, endDate));
		
		JSONObject result = new JSONObject();
		result.put("schoolSum",schoolSum);
		result.put("applSum",applSum);
		result.put("porfolioSum",porfolioSum);
		result.put("analAllYearsList",combineOverview);
		log.info(result);
		return result;
	}
	
	@Override
	public JSONObject platformCountry(String countryId) {
		
		List<Analysis> countSchoolCountry= new ArrayList<Analysis>();
		List<Analysis> countSchoolCountryApplStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolCountryNoticeStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolCountryPublishStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolSingleCountry= new ArrayList<Analysis>();
		List<Analysis> countYearPatent = new ArrayList<Analysis>();
		
		List<Object> combineYear = new ArrayList<Object>();
		List<Object> combineCountry = new ArrayList<Object>();
		List<Object> combineApplStatus = new ArrayList<Object>();
		List<Object> combineNoticeStatus = new ArrayList<Object>();
		List<Object> combinePublishStatus = new ArrayList<Object>();
		
		countSchoolCountry = analysisDao.countSchoolCountry();
		countSchoolCountryApplStatus = analysisDao.countSchoolCountryApplStatus();
		countSchoolCountryNoticeStatus = analysisDao.countSchoolCountryNoticeStatus();
		countSchoolCountryPublishStatus = analysisDao.countSchoolCountryPublishStatus();
		countSchoolSingleCountry = analysisDao.countSchoolSingleCountry(countryId);
		countYearPatent = analysisDao.countYearPatent();
		
		Object yearToArray[][] = countYearPatent.toArray(new Object[countYearPatent.size()][0]);
		Object dataToArray[][] = countSchoolSingleCountry.toArray(new Object[countSchoolSingleCountry.size()][0]);
		
		combineYear.addAll(combineCountryYear(yearToArray,dataToArray,countryId));
		
		Object countryToArray [][]=countSchoolCountry.toArray(new Object[countSchoolCountry.size()][0]);
		Object ApplStatusToArray [][]=countSchoolCountryApplStatus.toArray(new Object[countSchoolCountryApplStatus.size()][0]);
		Object noticeStatusToArray [][]=countSchoolCountryNoticeStatus.toArray(new Object[countSchoolCountryNoticeStatus.size()][0]);
		Object publishStatusToArray [][]=countSchoolCountryPublishStatus.toArray(new Object[countSchoolCountryPublishStatus.size()][0]);
		
		if(countSchoolCountry.size()==3) {
			combineCountry.addAll(countSchoolCountry);
		}else {
			combineCountry.addAll(combineStatus(countryToArray));
		}
		
		if(countSchoolCountryApplStatus.size()==3) {
			combineApplStatus.addAll(countSchoolCountryApplStatus);
		}else {
			combineApplStatus.addAll(combineStatus(ApplStatusToArray));
		}
		
		if(countSchoolCountryNoticeStatus.size()==3) {
			combineNoticeStatus.addAll(countSchoolCountryNoticeStatus);
		}else {
			combineNoticeStatus.addAll(combineStatus(noticeStatusToArray));
		}
		
		if(countSchoolCountryPublishStatus.size()==3) {
			combinePublishStatus.addAll(countSchoolCountryPublishStatus);
		}else {
			combinePublishStatus.addAll(combineStatus(publishStatusToArray));
		}
		
		JSONObject result = new JSONObject();
		result.put("countCountryTotal",combineCountry);
		result.put("countCountryApplStatusTotal",combineApplStatus);
		result.put("countCountryNoticeStatusTotal",combineNoticeStatus);
		result.put("countCountryPublishStatusTotal",combinePublishStatus);
		result.put("countCountryByYearTotal",combineYear);
		result.put("analAllYearsList",countYearPatent);
		log.info(result);
		return result;
	}
	
	@Override
	public JSONObject platformCountryByYear(String countryId, Long beginDate, Long endDate) {
		
		List<Analysis> countSchoolCountry= new ArrayList<Analysis>();
		List<Analysis> countSchoolCountryApplStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolCountryNoticeStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolCountryPublishStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolSingleCountry= new ArrayList<Analysis>();
		
		List<Object> combineCountry = new ArrayList<Object>();
		List<Object> combineApplStatus = new ArrayList<Object>();
		List<Object> combineNoticeStatus = new ArrayList<Object>();
		List<Object> combinePublishStatus = new ArrayList<Object>();
		
		List<Object> combineYear = new ArrayList<Object>();
		
		countSchoolCountry = analysisDao.countSchoolCountryByYear(beginDate, endDate);
		countSchoolCountryApplStatus = analysisDao.countSchoolCountryApplStatusByYear(beginDate, endDate);
		countSchoolCountryNoticeStatus = analysisDao.countSchoolCountryNoticeStatusByYear(beginDate, endDate);
		countSchoolCountryPublishStatus = analysisDao.countSchoolCountryPublishStatusByYear(beginDate, endDate);
		countSchoolSingleCountry = analysisDao.countSchoolSingleCountryByYear(beginDate, endDate, countryId);
		
		
		Object yearToArray[][] = countSchoolSingleCountry.toArray(new Object[countSchoolSingleCountry.size()][0]);
		Object countryToArray [][]=countSchoolCountry.toArray(new Object[countSchoolCountry.size()][0]);
		Object applStatusToArray [][]=countSchoolCountryApplStatus.toArray(new Object[countSchoolCountryApplStatus.size()][0]);
		Object noticeStatusToArray [][]=countSchoolCountryNoticeStatus.toArray(new Object[countSchoolCountryNoticeStatus.size()][0]);
		Object publishStatusToArray [][]=countSchoolCountryPublishStatus.toArray(new Object[countSchoolCountryPublishStatus.size()][0]);
		
		combineYear.addAll(combineCountryByYear(yearToArray, beginDate, endDate, countryId));
		
		if(countSchoolCountry.size()==3) {
			combineCountry.addAll(countSchoolCountry);
		}else {
			combineCountry.addAll(combineStatus(countryToArray));
		}
		
		if(countSchoolCountryApplStatus.size()==3) {
			combineApplStatus.addAll(countSchoolCountryApplStatus);
		}else {
			combineApplStatus.addAll(combineStatus(applStatusToArray));
		}
		
		if(countSchoolCountryNoticeStatus.size()==3) {
			combineNoticeStatus.addAll(countSchoolCountryNoticeStatus);
		}else {
			combineNoticeStatus.addAll(combineStatus(noticeStatusToArray));
		}
		
		if(countSchoolCountryPublishStatus.size()==3) {
			combinePublishStatus.addAll(countSchoolCountryPublishStatus);
		}else {
			combinePublishStatus.addAll(combineStatus(publishStatusToArray));
		}
		
		JSONObject result = new JSONObject();
		result.put("countCountryTotal",combineCountry);
		result.put("countCountryApplStatusTotal",combineApplStatus);
		result.put("countCountryNoticeStatusTotal",combineNoticeStatus);
		result.put("countCountryPublishStatusTotal",combinePublishStatus);
		result.put("countCountryByYearTotal",combineYear);
		log.info(result);
		return result;
	}
	
	@Override
	public JSONObject platformSchool() {
		List<Analysis> countSchoolPatentTotal= new ArrayList<Analysis>();
		List<Analysis> countSchoolPatentApplStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolPatentNoticeStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolPatentPublishStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolSum= new ArrayList<Analysis>();
		List<Analysis> getDefaultYear = new ArrayList<Analysis>();
		List<Object> combineSchoolSum = new ArrayList<Object>();
		List<Status> statusList = new ArrayList<Status>();
		
		statusList=statusDao.getEditable();
		countSchoolPatentTotal = analysisDao.countSchoolPatentTotal();
		countSchoolPatentApplStatus = analysisDao.countSchoolPatentApplStatus();
		countSchoolPatentNoticeStatus = analysisDao.countSchoolPatentNoticeStatus();
		countSchoolPatentPublishStatus = analysisDao.countSchoolPatentPublishStatus();
		getDefaultYear=analysisDao.getDefaultYear();
		countSchoolSum = analysisDao.countSchoolSum();
				
		Object countryToArray [][]=countSchoolPatentTotal.toArray(new Object[countSchoolPatentTotal.size()][0]);
		Object applStatusToArray [][]=countSchoolPatentApplStatus.toArray(new Object[countSchoolPatentApplStatus.size()][0]);
		Object noticeStatusToArray [][]=countSchoolPatentNoticeStatus.toArray(new Object[countSchoolPatentNoticeStatus.size()][0]);
		Object publishStatusToArray [][]=countSchoolPatentPublishStatus.toArray(new Object[countSchoolPatentPublishStatus.size()][0]);
		Object countSchoolSumToArray[][] = countSchoolSum.toArray(new Object[countSchoolSum.size()][0]);
		
		combineSchoolSum.addAll(combineSchoolSum(countSchoolSumToArray));
		
		List <Object> patentCNTotal = new ArrayList<Object>(); 
		List <Object> patentTWTotal = new ArrayList<Object>(); 
		List <Object> patentUSTotal = new ArrayList<Object>();
		patentCNTotal.addAll(combineSchoolData(countryToArray).getPlantformSchoolCNPatentData());
		patentTWTotal.addAll(combineSchoolData(countryToArray).getPlantformSchoolTWPatentData());
		patentUSTotal.addAll(combineSchoolData(countryToArray).getPlantformSchoolUSPatentData());
		
		List <Object> patentCNAppl = new ArrayList<Object>(); 
		List <Object> patentTWAppl = new ArrayList<Object>(); 
		List <Object> patentUSAppl = new ArrayList<Object>(); 
		patentCNAppl.addAll(combineSchoolData(applStatusToArray).getPlantformSchoolCNPatentData());
		patentTWAppl.addAll(combineSchoolData(applStatusToArray).getPlantformSchoolTWPatentData());
		patentUSAppl.addAll(combineSchoolData(applStatusToArray).getPlantformSchoolUSPatentData());
		
		List <Object> patentCNNotice = new ArrayList<Object>(); 
		List <Object> patentTWNotice = new ArrayList<Object>(); 
		List <Object> patentUSNotice = new ArrayList<Object>();
		patentCNNotice.addAll(combineSchoolData(noticeStatusToArray).getPlantformSchoolCNPatentData());
		patentTWNotice.addAll(combineSchoolData(noticeStatusToArray).getPlantformSchoolTWPatentData());
		patentUSNotice.addAll(combineSchoolData(noticeStatusToArray).getPlantformSchoolUSPatentData());
		
		
		List <Object> patentCNPublish = new ArrayList<Object>(); 
		List <Object> patentTWPublish = new ArrayList<Object>(); 
		List <Object> patentUSPublish = new ArrayList<Object>();
		patentCNPublish.addAll(combineSchoolData(publishStatusToArray).getPlantformSchoolCNPatentData());
		patentTWPublish.addAll(combineSchoolData(publishStatusToArray).getPlantformSchoolTWPatentData());
		patentUSPublish.addAll(combineSchoolData(publishStatusToArray).getPlantformSchoolUSPatentData());
		
		
		JSONObject result = new JSONObject();
		result.put("patentCNTotal", patentCNTotal);
		result.put("patentTWTotal", patentTWTotal);
		result.put("patentUSTotal", patentUSTotal);
		
		result.put("patentCNAppl", patentCNAppl);
		result.put("patentTWAppl", patentTWAppl);
		result.put("patentUSAppl", patentUSAppl);
		
		result.put("patentCNNotice", patentCNNotice);
		result.put("patentTWNotice", patentTWNotice);
		result.put("patentUSNotice", patentUSNotice);
		
		result.put("patentCNPublish", patentCNPublish);
		result.put("patentTWPublish", patentTWPublish);
		result.put("patentUSPublish", patentUSPublish);
		
		result.put("getDefaultYear", getDefaultYear);
		result.put("combineSchoolSum", combineSchoolSum);
		result.put("statusList", statusList);
		log.info(result);
		return result;
	}
	
	@Override
	public JSONObject platformSchoolByYear(Long beginDate, Long endDate) {
		List<Analysis> countSchoolPatentTotal= new ArrayList<Analysis>();
		List<Analysis> countSchoolPatentApplStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolPatentNoticeStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolPatentPublishStatus= new ArrayList<Analysis>();
		List<Analysis> countSchoolSum= new ArrayList<Analysis>();
		List<Object> combineSchoolSum = new ArrayList<Object>();
		List <Object> patentCNTotal = new ArrayList<Object>(); 
		List <Object> patentTWTotal = new ArrayList<Object>(); 
		List <Object> patentUSTotal = new ArrayList<Object>();
		List <Object> patentCNAppl = new ArrayList<Object>(); 
		List <Object> patentTWAppl = new ArrayList<Object>(); 
		List <Object> patentUSAppl = new ArrayList<Object>(); 
		List <Object> patentCNNotice = new ArrayList<Object>(); 
		List <Object> patentTWNotice = new ArrayList<Object>(); 
		List <Object> patentUSNotice = new ArrayList<Object>();
		List <Object> patentCNPublish = new ArrayList<Object>(); 
		List <Object> patentTWPublish = new ArrayList<Object>(); 
		List <Object> patentUSPublish = new ArrayList<Object>();
		List<Status> statusList = new ArrayList<Status>();
		statusList=statusDao.getEditable();
		try {
			countSchoolPatentTotal = analysisDao.countSchoolPatentTotalByYear(beginDate, endDate);
			countSchoolPatentApplStatus = analysisDao.countSchoolPatentApplStatusByYear(beginDate, endDate);
			countSchoolPatentNoticeStatus = analysisDao.countSchoolPatentNoticeStatusByYear(beginDate, endDate);
			countSchoolPatentPublishStatus = analysisDao.countSchoolPatentPublishStatusByYear(beginDate, endDate);
			countSchoolSum = analysisDao.countSchoolSumByYear(beginDate, endDate);
			
			Object countryToArray [][]=countSchoolPatentTotal.toArray(new Object[countSchoolPatentTotal.size()][0]);
			Object applStatusToArray [][]=countSchoolPatentApplStatus.toArray(new Object[countSchoolPatentApplStatus.size()][0]);
			Object noticeStatusToArray [][]=countSchoolPatentNoticeStatus.toArray(new Object[countSchoolPatentNoticeStatus.size()][0]);
			Object publishStatusToArray [][]=countSchoolPatentPublishStatus.toArray(new Object[countSchoolPatentPublishStatus.size()][0]);
			Object countSchoolSumToArray[][] = countSchoolSum.toArray(new Object[countSchoolSum.size()][0]);
			
			combineSchoolSum.addAll(combineSchoolSum(countSchoolSumToArray));
			patentCNTotal.addAll(combineSchoolData(countryToArray).getPlantformSchoolCNPatentData());
			patentTWTotal.addAll(combineSchoolData(countryToArray).getPlantformSchoolTWPatentData());
			patentUSTotal.addAll(combineSchoolData(countryToArray).getPlantformSchoolUSPatentData());
			patentCNAppl.addAll(combineSchoolData(applStatusToArray).getPlantformSchoolCNPatentData());
			patentTWAppl.addAll(combineSchoolData(applStatusToArray).getPlantformSchoolTWPatentData());
			patentUSAppl.addAll(combineSchoolData(applStatusToArray).getPlantformSchoolUSPatentData());
			patentCNNotice.addAll(combineSchoolData(noticeStatusToArray).getPlantformSchoolCNPatentData());
			patentTWNotice.addAll(combineSchoolData(noticeStatusToArray).getPlantformSchoolTWPatentData());
			patentUSNotice.addAll(combineSchoolData(noticeStatusToArray).getPlantformSchoolUSPatentData());
			patentCNPublish.addAll(combineSchoolData(publishStatusToArray).getPlantformSchoolCNPatentData());
			patentTWPublish.addAll(combineSchoolData(publishStatusToArray).getPlantformSchoolTWPatentData());
			patentUSPublish.addAll(combineSchoolData(publishStatusToArray).getPlantformSchoolUSPatentData());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		JSONObject result = new JSONObject();

		result.put("patentCNTotal", patentCNTotal);
		result.put("patentTWTotal", patentTWTotal);
		result.put("patentUSTotal", patentUSTotal);
		
		result.put("patentCNAppl", patentCNAppl);
		result.put("patentTWAppl", patentTWAppl);
		result.put("patentUSAppl", patentUSAppl);
		
		result.put("patentCNNotice", patentCNNotice);
		result.put("patentTWNotice", patentTWNotice);
		result.put("patentUSNotice", patentUSNotice);
		
		result.put("patentCNPublish", patentCNPublish);
		result.put("patentTWPublish", patentTWPublish);
		result.put("patentUSPublish", patentUSPublish);
		result.put("combineSchoolSum", combineSchoolSum);
		result.put("statusList", statusList);
		log.info(result);
		return result;
	}
	
	private List<Object> combineSchoolSum(Object[][] dataToArray){
		List<Object> combineSchoolSum = new ArrayList<Object>();
		List<String> schoolList = analysisDao.getSchoolList();
		Object[][]schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
		// Id == combineArr[0] == dataToArray[dataInx][0]
		//Sum == combineArr[1] == dataToArray[dataInx][1]
		try {
			Object[] combineArr;
			int schoolInx = 0;
			int dataInx = 0;
			if(schoolListToArray.length!=0 && dataToArray.length!=0) {
				for(schoolInx = 0;schoolInx<schoolListToArray.length;schoolInx++) {
					combineArr = new Object[2];
					if(dataInx < schoolListToArray.length) {
						if(schoolListToArray[schoolInx][0].equals(dataToArray[dataInx][0])) {
							combineArr[0] = dataToArray[dataInx][0];
							combineArr[1] = dataToArray[dataInx][1];
							combineSchoolSum.add(combineArr);
//							log.info("Id: "+dataToArray[dataInx][0]);
							dataInx++;
						}else {
							combineArr[0] = schoolListToArray[schoolInx][0];
							combineArr[1] = 0;
							combineSchoolSum.add(combineArr);
//							log.info("Id: "+schoolListToArray[schoolInx][0]);
						}
					}else {
						combineArr[0] = schoolListToArray[schoolInx][0];
						combineArr[1] = 0;
						combineSchoolSum.add(combineArr);
//						log.info("Id: "+schoolListToArray[schoolInx][0]);
					}
				}
			}else {
				log.info(dataToArray.length);
				log.info(schoolListToArray.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return combineSchoolSum;
	}
	
	private Analysis combineSchoolData(Object[][] dataToArray){
			List<Object> combineTWPatentTotal = new ArrayList<Object>();
			List<Object> combineCNPatentTotal = new ArrayList<Object>();
			List<Object> combineUSPatentTotal = new ArrayList<Object>();
					
			List <Object> cnDateList = new ArrayList<Object>(); 
			List <Object> twDateList = new ArrayList<Object>(); 
			List <Object> usDateList = new ArrayList<Object>();
			
			List<String> schoolList = analysisDao.getSchoolList();
			Object[][]schoolListToArray = schoolList.toArray(new Object[schoolList.size()][0]);
			
			Analysis analysis = new Analysis();
			try {
				int dataInx = 0;
				int schoolInx = 0;
				int dataIter = 0 ;
				Object busineseId;
				Object busineseName;
				Object sum;
				Object countryId ;
	//			Object status;
				Object[] combineArr;
				if (dataToArray.length != 0 && !schoolList.isEmpty()) {
					for (dataInx = 0; dataInx < dataToArray.length; dataInx++) {
						if (dataToArray[dataInx][3].equals("cn")) {
							busineseId = dataToArray[dataInx][0];
							busineseName = dataToArray[dataInx][1];
							sum = dataToArray[dataInx][2];
							countryId = dataToArray[dataInx][3];
							combineArr = new Object[4];
							combineArr[0] = busineseId;
							combineArr[1] = busineseName;
							combineArr[2] = sum;
							combineArr[3] = countryId;
							cnDateList.add(combineArr);
						}
						if (dataToArray[dataInx][3].equals("tw")) {
							busineseId = dataToArray[dataInx][0];
							busineseName = dataToArray[dataInx][1];
							sum = dataToArray[dataInx][2];
							countryId = dataToArray[dataInx][3];
							combineArr = new Object[4];
							combineArr[0] = busineseId;
							combineArr[1] = busineseName;
							combineArr[2] = sum;
							combineArr[3] = countryId;
							twDateList.add(combineArr);
						}
						if (dataToArray[dataInx][3].equals("us")) {
							busineseId = dataToArray[dataInx][0];
							busineseName = dataToArray[dataInx][1];
							sum = dataToArray[dataInx][2];
							countryId = dataToArray[dataInx][3];
							combineArr = new Object[4];
							combineArr[0] = busineseId;
							combineArr[1] = busineseName;
							combineArr[2] = sum;
							combineArr[3] = countryId;
							usDateList.add(combineArr);
						}
					}
					
//					combineArr[0] == busineseId == schoolListToArray[schoolInx][0]
//					combineArr[1] == busineseName == CNDateListToArray[dataIter][1]
//					combineArr[2] == sum == CNDateListToArray[dataIter][2]
//					combineArr[3] ==countryId == CNDateListToArray[dataIter][3]
					
					Object CNDateListToArray [][]=cnDateList.toArray(new Object[cnDateList.size()][0]);
					Object TWDateListToArray [][]=twDateList.toArray(new Object[twDateList.size()][0]);
					Object USDateListToArray [][]=usDateList.toArray(new Object[usDateList.size()][0]);
					//各學校CN Total
					if(!cnDateList.isEmpty()) {
						dataIter = 0;
						countryId =  CNDateListToArray[0][3];
						for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
							combineArr = new Object[4];
							if(dataIter<CNDateListToArray.length) {
								if(schoolListToArray[schoolInx][0].equals(CNDateListToArray[dataIter][0])) {
									combineArr[0] = schoolListToArray[schoolInx][0];
									combineArr[1] = CNDateListToArray[dataIter][1];
									combineArr[2] = CNDateListToArray[dataIter][2];
									combineArr[3] = CNDateListToArray[dataIter][3];
									combineCNPatentTotal.add(combineArr);
									dataIter++;
								}else {
									combineArr[0] = schoolListToArray[schoolInx][0];
									combineArr[1] = schoolListToArray[schoolInx][1];
									combineArr[2] = 0;
									combineArr[3] = countryId;
									combineCNPatentTotal.add(combineArr);
								}
							}else {
								combineArr[0] = schoolListToArray[schoolInx][0];
								combineArr[1] = schoolListToArray[schoolInx][1];
								combineArr[2] = 0;
								combineArr[3] = countryId;
								combineCNPatentTotal.add(combineArr);
							}
						}
					}else {
						for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
							combineArr = new Object[4];
							combineArr[0] = schoolListToArray[schoolInx][0];
							combineArr[1] = schoolListToArray[schoolInx][1];
							combineArr[2] = 0;
							combineArr[3] = "countryId";
							combineCNPatentTotal.add(combineArr);
						}
//						log.info("cnDateList is Empty");
					}
					//各學校TW Total
					if(!twDateList.isEmpty()) {
						dataIter = 0;
						countryId =  TWDateListToArray[0][3];
						for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
							combineArr = new Object[4];
							if(dataIter<TWDateListToArray.length) {
								if(schoolListToArray[schoolInx][0].equals(TWDateListToArray[dataIter][0])) {
									combineArr[0] = schoolListToArray[schoolInx][0];
									combineArr[1] = TWDateListToArray[dataIter][1];
									combineArr[2] = TWDateListToArray[dataIter][2];
									combineArr[3] = TWDateListToArray[dataIter][3];
									combineTWPatentTotal.add(combineArr);
									dataIter++;
								}else {
									combineArr[0] = schoolListToArray[schoolInx][0];
									combineArr[1] = schoolListToArray[schoolInx][1];
									combineArr[2] = 0;
									combineArr[3] = countryId;
									combineTWPatentTotal.add(combineArr);
								}
							}else {
								combineArr[0] = schoolListToArray[schoolInx][0];
								combineArr[1] = schoolListToArray[schoolInx][1];
								combineArr[2] = 0;
								combineArr[3] = countryId;
								combineTWPatentTotal.add(combineArr);
							}
						}
					}else {
						for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
							combineArr = new Object[4];
							combineArr[0] = schoolListToArray[schoolInx][0];
							combineArr[1] = schoolListToArray[schoolInx][1];
							combineArr[2] = 0;
							combineArr[3] = "countryId";
							combineTWPatentTotal.add(combineArr);
						}
//						log.info("twDateList is Empty");
					}
					//各學校US Total
					if(!usDateList.isEmpty()) {
						dataIter = 0;
						countryId =  USDateListToArray[0][3];
						for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
							combineArr = new Object[4];
							if(dataIter<USDateListToArray.length) {
								if(schoolListToArray[schoolInx][0].equals(USDateListToArray[dataIter][0])) {
									combineArr[0] = schoolListToArray[schoolInx][0];
									combineArr[1] = USDateListToArray[dataIter][1];
									combineArr[2] = USDateListToArray[dataIter][2];
									combineArr[3] = USDateListToArray[dataIter][3];
									combineUSPatentTotal.add(combineArr);
									dataIter++;
								}else {
									combineArr[0] = schoolListToArray[schoolInx][0];
									combineArr[1] = schoolListToArray[schoolInx][1];
									combineArr[2] = 0;
									combineArr[3] = countryId;
									combineUSPatentTotal.add(combineArr);
								}
							}else {
								combineArr[0] = schoolListToArray[schoolInx][0];
								combineArr[1] = schoolListToArray[schoolInx][1];
								combineArr[2] = 0;
								combineArr[3] = countryId;
								combineUSPatentTotal.add(combineArr);
							}
						}
					}else {
						for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
							combineArr = new Object[4];
							combineArr[0] = schoolListToArray[schoolInx][0];
							combineArr[1] = schoolListToArray[schoolInx][1];
							combineArr[2] = 0;
							combineArr[3] = "countryId";
							combineUSPatentTotal.add(combineArr);
						}
//						log.info("usDateList is Empty");
					}
				}else if(dataToArray.length==0) {
					log.info("dataToArray is null");
					for(schoolInx = 0;schoolInx<schoolList.size();schoolInx++) {
						combineArr = new Object[4];
						combineArr[0] = schoolListToArray[schoolInx][0];
						combineArr[1] = schoolListToArray[schoolInx][1];
						combineArr[2] = 0;
						combineArr[3] = "countryId";
						combineCNPatentTotal.add(combineArr);
						combineTWPatentTotal.add(combineArr);
						combineUSPatentTotal.add(combineArr);
					}
				}else {
					log.info("shoolList is null");
				}
				analysis.setPlantformSchoolCNPatentTotal(combineCNPatentTotal);
				analysis.setPlantformSchoolTWPatentTotal(combineTWPatentTotal);
				analysis.setPlantformSchoolUSPatentTotal(combineUSPatentTotal);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return analysis;
		}

	private List<Object> combineCountryByYear(Object yearToArray[][], Long beginDate, Long endDate, String countryId){
		Object[] combineArr;
		List<Object> analList = new ArrayList<Object>();
		List<Object> yearList = new ArrayList<Object>();
		List<Object> allyearList = new ArrayList<Object>();
		List<Object> allCountList = new ArrayList<Object>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			log.info("開始年: "+beginDateFormat);
			log.info("結束年: "+endDateFormat);
			
			int beginTime = Integer.parseInt(beginDateFormat);
			int endTime = Integer.parseInt(endDateFormat);
			int range = endTime - beginTime;
	
			int yearIter = 0;
			int combineInx = 0;
			int yearInx = 0;
			Integer year;
//			combineArr[0] == countryName 
//			combineArr[1] == Sum
//			combineArr[2] == year
			if (yearToArray.length == 0) {
				log.info("無任何專利");
				Object countryN = countryId;
				for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
					yearList.add(yearIter);
				}
				for (combineInx = 0; combineInx <= range; combineInx++) {
					combineArr = new Object[3];
					combineArr[0] = countryN;
					combineArr[1] = 0;
					combineArr[2] = yearList.get(combineInx);
					analList.add(combineArr);
				}
				
			} else {
				Object countryN = yearToArray[0][0];
				for (yearInx = 0; yearInx < yearToArray.length; yearInx++) {
					year = Integer.parseInt(yearToArray[yearInx][2].toString());
					yearList.add(year);
				}
	
				for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
					if (combineInx < yearList.size()) {
						if (yearIter == Integer.parseInt(yearList.get(combineInx).toString())) {
							allyearList.add(yearIter);
							allCountList.add(yearToArray[combineInx][1]);
							combineInx++;
						}else {
							allyearList.add(yearIter);
							allCountList.add(0);
						}
					} else {
						allyearList.add(yearIter);
						allCountList.add(0);
					}
				}
	
				for (combineInx = 0; combineInx < allyearList.size(); combineInx++) {
					if (!allyearList.isEmpty()) {
						combineArr = new Object[3];
						combineArr[0] = countryN;
						combineArr[1] = allCountList.get(combineInx);
						combineArr[2] = allyearList.get(combineInx);
						analList.add(combineArr);
					}
				}
				log.info("分析期間年數:  " + analList.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(yearToArray.length==0) {
				log.info("CountryId 未傳入");
				boolean errCondition = yearToArray.length==0;
				log.info("yearToArray.length==0 : "+errCondition);
			}
			log.error(e);
		}
		return analList;
	}
	private List<Object> combineOverview(Object yearToArray[][]) {
	
			List<Integer> yearList= new ArrayList<Integer>();
			List<Integer> allCountList = new ArrayList<Integer>();
			List<Integer> allyearList = new ArrayList<Integer>();
			List<Object> analList = new ArrayList<Object>();
			
			try {
				if(yearToArray.length==0) {
					log.info("無任何專利");
				}else {
					String beginStr = yearToArray[0][1].toString();
					String endStr = yearToArray[yearToArray.length-1][1].toString();
					int beginTime = Integer.parseInt(beginStr);
					int endTime = Integer.parseInt(endStr);
					int yearIter = 0;
					int combineInx = 0;
					int yearInx = 0 ;
					Integer year;
//					combineArr[0] == sum
//					combineArr[1] == year
					for (yearInx = 0; yearInx < yearToArray.length; yearInx++) {
						year = Integer.parseInt(yearToArray[yearInx][1].toString());
						yearList.add(year);
					}
					for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
						if(yearIter==yearList.get(combineInx)) {
							allyearList.add(yearIter);
							allCountList.add(Integer.parseInt(yearToArray[combineInx][0].toString()));
							combineInx++;
						}else {
							allyearList.add(yearIter);
							allCountList.add(0);
						}
					}
					Integer[] combineArr;
					for(combineInx = 0;combineInx<allyearList.size();combineInx++) {
						if(!allyearList.isEmpty()) {
							combineArr = new Integer[2];
							combineArr[0] = allCountList.get(combineInx);
							combineArr[1] = allyearList.get(combineInx);
							analList.add(combineArr);
						}
					}
					log.info("分析期間年數:  "+analList.size());
				}
	
			} catch (Exception e) {
	//			e.printStackTrace();
				log.error(e);
			}
			return analList;
		}

	private List<Object> combineOverviewByYear(Object yearToArray[][], Long beginDate, Long endDate){
			List<Integer> yearList = new ArrayList<Integer>();
			List<Integer> allCountList = new ArrayList<Integer>();
			List<Integer> allyearList = new ArrayList<Integer>();
			List<Object> analList = new ArrayList<Object>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			log.info("開始年: "+beginDateFormat);
			log.info("結束年: "+endDateFormat);
			
			int beginTime = Integer.parseInt(beginDateFormat);
			int endTime = Integer.parseInt(endDateFormat);
			int yearIter = 0;
			int combineInx = 0;
			int yearInx = 0 ;
			Integer year;
			int range = endTime-beginTime;
			Integer[] combineArr;
			
//			combineArr[0] == sum
//			combineArr[1] == year
			
			try {
				if(yearToArray.length == 0) {
	//				log.info("都是零");
					for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
						yearList.add(yearIter);
					}
					for(combineInx = 0;combineInx<=range; combineInx++) {
						combineArr = new Integer[2];
						combineArr[0] = 0;
						combineArr[1] = yearList.get(combineInx);
						analList.add(combineArr);
					}
				}else {
					for (yearInx = 0; yearInx < yearToArray.length; yearInx++) {
						year = Integer.parseInt(yearToArray[yearInx][1].toString());
						yearList.add(year);
					}
					for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
						if(combineInx<yearList.size()) {
							if(yearIter==yearList.get(combineInx)) {
								allyearList.add(yearIter);
								allCountList.add(Integer.parseInt(yearToArray[combineInx][0].toString()));
								combineInx++;
							}else {
								allyearList.add(yearIter);
								allCountList.add(0);
							}
						}else {
							allyearList.add(yearIter);
							allCountList.add(0);
						}
					}
					for(combineInx = 0;combineInx<allyearList.size();combineInx++) {
						if(!allyearList.isEmpty()) {
							combineArr = new Integer[2];
							combineArr[0] = allCountList.get(combineInx);
							combineArr[1] = allyearList.get(combineInx);
							analList.add(combineArr);
						}
					}
				}
				log.info("分析期間年數:  "+analList.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return analList;
		}

	private List<Object> combineStatus(Object statusToArray[][]) {
	
			List<Object> allCountryList = new ArrayList<Object>();
			allCountryList.add("cn");
			allCountryList.add("tw");
			allCountryList.add("us");
	
			List<Object> countryData = new ArrayList<Object>();
			List<Object> countryList = new ArrayList<Object>();
			List<Object> combineStatus = new ArrayList<Object>();
	
			int countryInx = 0;
			int combineInx = 0;
			int dataInx = 0;
			int dataIter = 0;
			Object[] combineArr;
//			combineArr[0] == Country
//			combineArr[1] == sum
			try {
				if (statusToArray.length == 0) {
					log.info("無狀態");
					for (countryInx = 0; countryInx < allCountryList.size(); countryInx++) {
						countryData.add(0);
					}
					for (combineInx = 0; combineInx < allCountryList.size(); combineInx++) {
						if (!allCountryList.isEmpty()) {
							combineArr = new Object[2];
							combineArr[0] = allCountryList.get(combineInx);
							combineArr[1] = countryData.get(combineInx);
							combineStatus.add(combineArr);
						}
					}
				} else {
					for (dataIter = 0; dataIter < statusToArray.length; dataIter++) {
						countryList.add(statusToArray[dataIter][0]);
					}
					for (countryInx = 0; countryInx < allCountryList.size(); countryInx++) {
						if (dataInx < statusToArray.length) {
							if (allCountryList.get(countryInx).equals(countryList.get(dataInx))) {
								countryData.add(statusToArray[dataInx][1]);
								dataInx++;
							} else {
								countryData.add(0);
							}
						} else {
							countryData.add(0);
						}
					}
	
					for (combineInx = 0; combineInx < allCountryList.size(); combineInx++) {
						if (!allCountryList.isEmpty()) {
							combineArr = new Object[2];
							combineArr[0] = allCountryList.get(combineInx);
							combineArr[1] = countryData.get(combineInx);
							combineStatus.add(combineArr);
						}
					}
	//				log.info("國家數:  " + combineStatus.size());
				}
	
			} catch (Exception e) {
				log.error(e);
	//			e.printStackTrace();
			}
			return combineStatus;
	
		}

	private List<Object> combineCountryYear(Object yearToArray[][], Object dataToArray[][], String countryId) {
			
				Object[] combineArr;
				List<Object> analList = new ArrayList<Object>();
				List<Object> dataYearList = new ArrayList<Object>();
				List<Object> allyearList = new ArrayList<Object>();
				List<Object> allCountList = new ArrayList<Object>();
				
//				combineArr[0] == countryName
//				combineArr[1] == sum
//				combineArr[2] == year
				
				try {
					String beginStr = yearToArray[0][1].toString();
					log.info("開始年: "+beginStr);
					String endStr = yearToArray[yearToArray.length - 1][1].toString();
					log.info("結束年: "+endStr);
					int beginTime = Integer.parseInt(beginStr);
					int endTime = Integer.parseInt(endStr);
					int range = endTime - beginTime;
		
					int yearIter = 0;
					int combineInx = 0;
					int yearInx = 0;
					Integer year;
		
					if (dataToArray.length == 0) {
						log.info("無任何專利");
						Object countryN = countryId;
						for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
							allyearList.add(yearIter);
						}
						for (combineInx = 0; combineInx <= range; combineInx++) {
							combineArr = new Object[3];
							combineArr[0] = countryN;
							combineArr[1] = 0;
							combineArr[2] = allyearList.get(combineInx);
							analList.add(combineArr);
						}
					} else {
					
						Object countryN = dataToArray[0][0];
						for (yearInx = 0; yearInx < dataToArray.length; yearInx++) {
							year = Integer.parseInt(dataToArray[yearInx][2].toString());
							dataYearList.add(year);
						}
						for (yearIter = beginTime; yearIter <= endTime; yearIter++) {
							if (combineInx < dataYearList.size()) {
								if (yearIter == Integer.parseInt(dataYearList.get(combineInx).toString())) {
									allyearList.add(yearIter);
									allCountList.add(dataToArray[combineInx][1]);
									combineInx++;
								}else {
									allyearList.add(yearIter);
									allCountList.add(0);
								}
							} else {
								allyearList.add(yearIter);
								allCountList.add(0);
							}
						}
		
						for (combineInx = 0; combineInx < allyearList.size(); combineInx++) {
							if (!allyearList.isEmpty()) {
								combineArr = new Object[3];
								combineArr[0] = countryN;
								combineArr[1] = allCountList.get(combineInx);
								combineArr[2] = allyearList.get(combineInx);
								analList.add(combineArr);
							}
						}
						log.info("分析期間年數:  " + analList.size());
					}
		
				} catch (Exception e) {
	//				e.printStackTrace();
					log.error("無任何資料");
					log.error(e);
				}
				return analList;
			}

	private List<Object> combineDepartment(Object depToArray[][], Object countryToArray[][]) {
			int depInx;
			int countryDepInx = 0;
			int combineInx;
			int countryDep;
			Object depNameObj;
			Object countryDepNameObj;
			Object countryDepCountObj;
			Object[] combineArr;
	//		log.info("combine Department");
			List<Object> depName = new ArrayList<Object>();
			List<Object> alltwDepName = new ArrayList<Object>();
			List<Object> alltwDepCount = new ArrayList<Object>();
			List<Object> depCombine = new ArrayList<Object>();
			List<Object> countryDepName = new ArrayList<Object>();
			
//			combineArr[0] == DepName
//			combineArr[1] == sum
			
			try {
				if (depToArray.length == 0) {
					log.info("無科系資料");
				} else {
					for (depInx = 0; depInx < depToArray.length; depInx++) {
						depNameObj = depToArray[depInx][0];
						depName.add(depNameObj);
					}
					if (countryToArray.length == 0) {
						log.info("該國家無科系資料");
						for (depInx = 0; depInx < depToArray.length; depInx++) {
							alltwDepName.add(depName.get(depInx));
							alltwDepCount.add(0);
						}
					} else {
						log.info("該國家原科系數量:  " + countryToArray.length);
						for (countryDep = 0; countryDep < countryToArray.length; countryDep++) {
							countryDepNameObj = countryToArray[countryDep][0];
							countryDepName.add(countryDepNameObj);
						}
						for (depInx = 0; depInx < depToArray.length; depInx++) {
							if (countryDepInx < countryToArray.length) {
								if (depName.get(depInx).equals(countryDepName.get(countryDepInx))) {
									countryDepCountObj = countryToArray[countryDepInx][1];
									alltwDepName.add(countryDepName.get(countryDepInx));
									alltwDepCount.add(countryDepCountObj);
									countryDepInx++;
								} else {
									alltwDepName.add(depName.get(depInx));
									alltwDepCount.add(0);
								}
							} else {
								alltwDepName.add(depName.get(depInx));
								alltwDepCount.add(0);
							}
						}
					}
					for (combineInx = 0; combineInx < depToArray.length; combineInx++) {
						if (depToArray.length != 0) {
							combineArr = new Object[2];
							combineArr[0] = alltwDepName.get(combineInx);
							combineArr[1] = alltwDepCount.get(combineInx);
							depCombine.add(combineArr);
						}
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
			return depCombine;
		}



	@Override
	public ByteArrayInputStream exportPlatformOverviewByYear(Long beginDate, Long endDate){
		ByteArrayOutputStream fileOut = null;
		JSONObject platformOverview = platformOverviewByYear(beginDate, endDate);
		String schoolSum = platformOverview.optString("schoolSum");
		String applSum = platformOverview.optString("applSum");
		String porfolioSum= platformOverview.optString("porfolioSum");
		JSONArray analAllYearsList= platformOverview.optJSONArray("analAllYearsList");
//		log.info(platformOverview);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet(); 
			XSSFRow row = sheet.createRow((short)0);
			XSSFCell cell = row.createCell((short) 0); 
			cell.setCellValue(Constants.ANALYSIS_SCHOOLAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(schoolSum); 
			
			row = sheet.createRow((short) 1);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_PATENTAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(applSum); 
			
			row = sheet.createRow((short) 2);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_PORTFOLIOAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(porfolioSum);
			
			row = sheet.createRow((short) 4);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_PATENTAMOUNTBYYEAR);
			for(int dataInx = 0;dataInx<analAllYearsList.length() ;dataInx++) {
				Object yearData =platformOverview.optJSONArray("analAllYearsList").getJSONArray(dataInx).get(1);
				Object countData =platformOverview.optJSONArray("analAllYearsList").getJSONArray(dataInx).get(0);
				row = sheet.createRow((short) 5+dataInx);
				cell = row.createCell((short) 0);
				cell.setCellValue(yearData.toString());
				cell = row.createCell((short) 1);
				cell.setCellValue(countData.toString());
			}
			fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(fileOut.toByteArray());
	}
	@Override
	public ByteArrayInputStream exportSchoolOverviewByYear(String businessId, Long beginDate, Long endDate){
		ByteArrayOutputStream fileOut = null;
		JSONObject schoolOverview = schoolOverviewByYear(businessId, beginDate, endDate);
		String analYearsTotal = schoolOverview.optString("analYearsTotal");
		String analFamilyTotal = schoolOverview.optString("analFamilyTotal");
		String analDepartmentTotal= schoolOverview.optString("analDepartmentTotal");
		String analInventorToltal= schoolOverview.optString("analInventorToltal");
		JSONArray analAllYearsList= schoolOverview.optJSONArray("analAllYearsList");
//		log.info(schoolOverview);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet(); 
			XSSFRow row = sheet.createRow((short)0);
			XSSFCell cell = row.createCell((short) 0); 
			cell.setCellValue(Constants.ANALYSIS_PATENTAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(analYearsTotal); 
			
			row = sheet.createRow((short) 1);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_FAMILYAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(analFamilyTotal); 
			
			row = sheet.createRow((short) 2);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_DEPARTMENTAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(analDepartmentTotal);
			
			row = sheet.createRow((short) 3);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_INVENTORAMOUNT); 
			cell = row.createCell((short) 1);
			cell.setCellValue(analInventorToltal);
			
			row = sheet.createRow((short) 5);
			cell = row.createCell((short) 0);
			cell.setCellValue(Constants.ANALYSIS_PATENTAMOUNTBYYEAR);
			for(int dataInx = 0;dataInx<analAllYearsList.length() ;dataInx++) {
				Object yearData =schoolOverview.optJSONArray("analAllYearsList").getJSONArray(dataInx).get(1);
				Object countData =schoolOverview.optJSONArray("analAllYearsList").getJSONArray(dataInx).get(0);
				row = sheet.createRow((short) 6+dataInx);
				cell = row.createCell((short) 0);
				cell.setCellValue(yearData.toString());
				cell = row.createCell((short) 1);
				cell.setCellValue(countData.toString());
			}
			fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(fileOut.toByteArray());
	}

	@Override
	public ByteArrayInputStream exportCountryByYear(String businessId, Long beginDate, Long endDate) {
		ByteArrayOutputStream fileOut = null;
		// default Country = "tw"
		JSONObject countryData = null;
		JSONArray countCountryTotal = null;
		JSONArray countCountryApplStatusTotal = null;
		JSONArray countCountryNoticeStatusTotal = null;
		JSONArray countCountryPublishStatusTotal = null;
		JSONArray countCountryByYearTotal = null;
		JSONObject countryUSData = null;
		JSONObject countryCNData = null;
		if(businessId==null) {
			countryData = platformCountryByYear("tw", beginDate, endDate);
			countCountryTotal= countryData.optJSONArray("countCountryTotal");
			countCountryApplStatusTotal= countryData.optJSONArray("countCountryApplStatusTotal");
			countCountryNoticeStatusTotal= countryData.optJSONArray("countCountryNoticeStatusTotal");
			countCountryPublishStatusTotal= countryData.optJSONArray("countCountryPublishStatusTotal");
			countCountryByYearTotal= countryData.optJSONArray("countCountryByYearTotal");
			countryUSData = platformCountryByYear("us", beginDate, endDate);
			countryCNData = platformCountryByYear("cn", beginDate, endDate);
		}else {
			countryData = schoolCountryByYear(businessId, beginDate, endDate, "tw");
			countCountryTotal= countryData.optJSONArray("countCountryTotal");
			countCountryApplStatusTotal= countryData.optJSONArray("countCountryApplStatusTotal");
			countCountryNoticeStatusTotal= countryData.optJSONArray("countCountryNoticeStatusTotal");
			countCountryPublishStatusTotal= countryData.optJSONArray("countCountryPublishStatusTotal");
			countCountryByYearTotal= countryData.optJSONArray("countCountryByYearTotal");
			countryUSData = schoolCountryByYear(businessId, beginDate, endDate, "us");
			countryCNData = schoolCountryByYear(businessId, beginDate, endDate, "cn");
		}
//		log.info(countryData);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet(); 
			XSSFRow row ;
			XSSFCell cell; 
			int dataInx = 0;
			row = sheet.createRow((short)0);
			String title [] = {"國家","申請","公開","公告","總數"};
			for(dataInx = 0;dataInx<title.length;dataInx++) {
				cell = row.createCell((short) dataInx);
				Object titleData =title [dataInx];
				cell.setCellValue(titleData.toString());
			}
			for(dataInx = 0;dataInx<countCountryTotal.length();dataInx++) {
				row = sheet.createRow((short) 1+dataInx);
				cell = row.createCell((short) 0);
				Object countryName = countCountryTotal.optJSONArray(dataInx).get(0);
				cell.setCellValue(countryName.toString().toUpperCase()); 
				cell = row.createCell((short) 1);
				Object applCount = countCountryApplStatusTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(applCount.toString()); 
				cell = row.createCell((short) 2);
				Object noticeCount = countCountryNoticeStatusTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(noticeCount.toString()); 
				cell = row.createCell((short) 3);
				Object publishCount = countCountryPublishStatusTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(publishCount.toString()); 
				cell = row.createCell((short) 4);
				Object sum = countCountryTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(sum.toString()); 
			}		
			
			row = sheet.createRow((short) 6);
			cell = row.createCell((short) 0);
			cell.setCellValue("臺灣歷年專利");
			cell = row.createCell((short) 3);
			cell.setCellValue("美國歷年專利");
			cell = row.createCell((short) 6);
			cell.setCellValue("中國歷年專利");
	
			for(dataInx = 0;dataInx<countCountryByYearTotal.length() ;dataInx++) {
				Object yearData =countryData.optJSONArray("countCountryByYearTotal").getJSONArray(dataInx).get(2);
				Object countData =countryData.optJSONArray("countCountryByYearTotal").getJSONArray(dataInx).get(1);
				row = sheet.createRow((short) 7+dataInx);
				cell = row.createCell((short) 0);
				cell.setCellValue(yearData.toString());
				cell = row.createCell((short) 1);
				cell.setCellValue(countData.toString());
				
				yearData =countryUSData.optJSONArray("countCountryByYearTotal").getJSONArray(dataInx).get(2);
				countData =countryUSData.optJSONArray("countCountryByYearTotal").getJSONArray(dataInx).get(1);
				cell = row.createCell((short) 3);
				cell.setCellValue(yearData.toString());
				cell = row.createCell((short) 4);
				cell.setCellValue(countData.toString());
				
				yearData =countryCNData.optJSONArray("countCountryByYearTotal").getJSONArray(dataInx).get(2);
				countData =countryCNData.optJSONArray("countCountryByYearTotal").getJSONArray(dataInx).get(1);
				cell = row.createCell((short) 6);
				cell.setCellValue(yearData.toString());
				cell = row.createCell((short) 7);
				cell.setCellValue(countData.toString());
			}
			
			fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(fileOut.toByteArray());
		
	}

	@Override
	public ByteArrayInputStream exportSchoolDepartmentByYear(String businessId, Long beginDate, Long endDate){
		ByteArrayOutputStream fileOut = null;
		JSONObject schoolDepartment = schoolDepartmentByYear(businessId, beginDate, endDate);
		JSONArray countEachDepartmentTotal= schoolDepartment.optJSONArray("countEachDepartmentTotal");
		JSONArray countTWEachDepartmentTotal= schoolDepartment.optJSONArray("countTWEachDepartmentTotal");
		JSONArray countCNEachDepartmentTotal= schoolDepartment.optJSONArray("countCNEachDepartmentTotal");
		JSONArray countUSEachDepartmentTotal= schoolDepartment.optJSONArray("countUSEachDepartmentTotal");
		log.info(schoolDepartment);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet(); 
			XSSFRow row = sheet.createRow((short)0);
			XSSFCell cell = row.createCell((short) 0); 
			cell.setCellValue("科系"); 
			int dataInx = 0;
			String countryId [] = {"美國","臺灣","中國大陸","總數"};
			for(dataInx = 0;dataInx<countryId.length;dataInx++) {
				cell = row.createCell((short) 1+dataInx);
				Object countryData =countryId [dataInx];
				cell.setCellValue(countryData.toString());
			}
			for(dataInx = 0;dataInx<countEachDepartmentTotal.length();dataInx++) {
				row = sheet.createRow((short) 1+dataInx);
				cell = row.createCell((short) 0);
				Object departmentName = countEachDepartmentTotal.optJSONArray(dataInx).get(0);
				cell.setCellValue(departmentName.toString()); 
				cell = row.createCell((short) 1);
				Object departmentUSData = countUSEachDepartmentTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(departmentUSData.toString()); 
				cell = row.createCell((short) 2);
				Object departmentTWData = countTWEachDepartmentTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(departmentTWData.toString()); 
				cell = row.createCell((short) 3);
				Object departmentCNData = countCNEachDepartmentTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(departmentCNData.toString()); 
				cell = row.createCell((short) 4);
				Object departmentSumData = countEachDepartmentTotal.optJSONArray(dataInx).get(1);
				cell.setCellValue(departmentSumData.toString()); 
			}		
			fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(fileOut.toByteArray());
	}
	
	@Override
	public ByteArrayInputStream exportPlatformSchoolByYear(JSONArray statusDesc, JSONArray businessName, JSONArray countryId,Long beginDate, Long endDate){
		ByteArrayOutputStream fileOut = null;
		List<Analysis> countList = new ArrayList<Analysis>();
		List<Analysis> sumList = new ArrayList<Analysis>();
		countList = analysisDao.countSchoolPatentStatusByYear(statusDesc, businessName, beginDate, endDate);
		sumList= analysisDao.countSchoolPatentTotalByYear(businessName, beginDate, endDate);
		try {
			//只是用log檢查時間
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Timestamp bd = new Timestamp(beginDate);
			Timestamp ed = new Timestamp(endDate);
			String beginDateFormat = sdf.format(bd);
			String endDateFormat = sdf.format(ed);
			log.info("開始年: "+beginDateFormat+"，結束年: "+endDateFormat);
			
			int schoolInx = 0;
			int dataInx = 0;
			int titleInx = 0;
			int countryInx = 0;
			String data;
			String sum;
			String schoolName ;
			String title = null;
			Object [][]countListToArray = countList.toArray(new Object[countList.size()][0]);
			Object [][]sumListToArray = sumList.toArray(new Object[sumList.size()][0]);
			
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet(); 
			XSSFRow row = null ;
			XSSFCell cell;
			CellStyle redStyle = workbook.createCellStyle();
			XSSFFont redFont = workbook.createFont();
			redFont.setColor(IndexedColors.LIGHT_BLUE.index);
			redStyle.setFont(redFont);
//			校名跟欄位名
			for(schoolInx = 0;schoolInx<businessName.length();schoolInx++) {
				schoolName = businessName.get(schoolInx).toString();
				//schoolInx*6: 控制學校名的間距
				row = sheet.createRow((short) 0 + (schoolInx*6));
				cell = row.createCell((short) 0);
				cell.setCellStyle(redStyle);
				cell.setCellValue(schoolName);
				
				row = sheet.createRow((short)1 + (schoolInx*6));
				//總數在所有狀態後面
				for(titleInx = 0;titleInx<statusDesc.length()+1;titleInx++) {
					cell = row.createCell((short) 1+titleInx);
					if(statusDesc.isNull(titleInx)) {
						cell.setCellValue("總數");
					}else {
						title = statusDesc.get(titleInx).toString();
						cell.setCellValue(title);
					}
				}
			}
			//寫入值
			//countListToArray[dataInx][0] == BusinessId
			//countListToArray[dataInx][1] == 學校名
			//countListToArray[dataInx][2] == Sum
			//countListToArray[dataInx][3] == 國家
			//countListToArray[dataInx][4] == 狀態
			for (countryInx=0;countryInx < countryId.length();countryInx++) {
//				log.info(countryId.get(countryInx).toString());
				for(schoolInx = 0;schoolInx<businessName.length();schoolInx++) {
					schoolName = businessName.get(schoolInx).toString();
					switch (countryId.get(countryInx).toString()) {
					case "中華民國":
						row = sheet.createRow((short) 2 + (schoolInx * 6));
						cell = row.createCell((short) 0);
						cell.setCellValue("臺灣");
						for (titleInx = 0; titleInx < statusDesc.length(); titleInx++) {
							title = statusDesc.opt(titleInx).toString();
							for (dataInx = 0; dataInx < countList.size(); dataInx++) {
								if (countListToArray[dataInx][1].equals(schoolName)&& countListToArray[dataInx][3].equals("tw")) {
									if (countListToArray[dataInx][4].equals(title)) {
										cell = row.createCell((short) 1 + titleInx);
										data = countListToArray[dataInx][2].toString();
										cell.setCellValue(data);
									}
								}
							}
							if(row.getCell(1 + titleInx) == null) {
								cell = row.createCell((short) 1 + titleInx);
								data = "0";
								cell.setCellValue(data);
							}
//							log.info(schoolName + ": " + schoolInx);
//							log.info(title + ": " + titleInx);
						}
						cell = row.createCell((short) 1 + titleInx);
						for (dataInx = 0; dataInx < sumList.size(); dataInx++) {
							if (sumListToArray[dataInx][1].equals(schoolName)&& sumListToArray[dataInx][3].equals("tw")) {
								sum = sumListToArray[dataInx][2].toString();
								cell.setCellValue(sum);
							}
						}
						if(cell.getStringCellValue()=="") {
							sum = "0";
							cell.setCellValue(sum);
						}
						break;
					case "美國":
						row = sheet.createRow((short) (2 + countryInx) + (schoolInx * 6));
						cell = row.createCell((short) 0);
						cell.setCellValue("美國");
						for (titleInx = 0; titleInx < statusDesc.length(); titleInx++) {
							title = statusDesc.get(titleInx).toString();
							for (dataInx = 0; dataInx < countList.size(); dataInx++) {
								if (countListToArray[dataInx][1].equals(schoolName)&& countListToArray[dataInx][3].equals("us")) {
									if (countListToArray[dataInx][4].equals(title)) {
										cell = row.createCell((short) 1 + titleInx);
										data = countListToArray[dataInx][2].toString();
										cell.setCellValue(data);
//										log.info(countListToArray[dataInx][2].toString());
									}
								}
							}
							if(row.getCell(1 + titleInx) == null) {
								cell = row.createCell((short) 1 + titleInx);
								data = "0";
								cell.setCellValue(data);
							}
						}
						cell = row.createCell((short) 1 + titleInx);
						for (dataInx = 0; dataInx < sumList.size(); dataInx++) {
							if (sumListToArray[dataInx][1].equals(schoolName)&& sumListToArray[dataInx][3].equals("us")) {
								sum = sumListToArray[dataInx][2].toString();
								cell.setCellValue(sum);
							}
						}
						if(cell.getStringCellValue()=="") {
							sum = "0";
							cell.setCellValue(sum);
						}
						break;
					case "中國大陸":
						row = sheet.createRow((short) (2 + countryInx) + (schoolInx * 6));
						cell = row.createCell((short) 0);
						cell.setCellValue("中國大陸");
						for (titleInx = 0; titleInx < statusDesc.length(); titleInx++) {
							title = statusDesc.get(titleInx).toString();
							for (dataInx = 0; dataInx < countList.size(); dataInx++) {
								if (countListToArray[dataInx][1].equals(schoolName)&& countListToArray[dataInx][3].equals("cn")) {
									if (countListToArray[dataInx][4].equals(title)) {
										cell = row.createCell((short) 1 + titleInx);
										data = countListToArray[dataInx][2].toString();
										cell.setCellValue(data);
//										log.info("value: " + countListToArray[dataInx][2].toString());
									}
								}
							}
							if(row.getCell(1 + titleInx) == null) {
								cell = row.createCell((short) 1 + titleInx);
								data = "0";
								cell.setCellValue(data);
							}
						}
						cell = row.createCell((short) 1 + titleInx);
						for (dataInx = 0; dataInx < sumList.size(); dataInx++) {
							if (sumListToArray[dataInx][1].equals(schoolName)&& sumListToArray[dataInx][3].equals("cn")) {
								sum = sumListToArray[dataInx][2].toString();
								cell.setCellValue(sum);
							}
						}
						if(cell.getStringCellValue()=="") {
							sum = "0";
							cell.setCellValue(sum);
						}
						break;
					default:
						break;
					}
				}
			}
			log.info("schoolInx: " + schoolInx);
			
			fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(fileOut.toByteArray());
	}
	
	
	@Override
	public JSONObject schoolData (JSONArray statusDesc, JSONArray businessName, JSONArray countryId) {
		List<Analysis> countList = new ArrayList<Analysis>();
		List<Analysis> sumList = new ArrayList<Analysis>();
		List<Object> combineList = new ArrayList<Object>();
		
		JSONObject result = new JSONObject();
		result.put("countList",countList);
		result.put("sumList",sumList);
		result.put("combineList", combineList);
		return result;
	}
	
	public JSONObject testAnalysis(JSONArray statusDesc, JSONArray businessId) {
//		int counttt = 0;
//		counttt = analysisDao.countPorfolio(businessId);
//		log.info(unApplPatent);
//		return unApplPatent;
		List<Analysis> countList = new ArrayList<Analysis>();
//		countList = analysisDao.countSchoolPatentStatus(statusDesc, businessId);
		log.info(countList.size());
//		log.info(countList);
		JSONObject result = new JSONObject();
		result.put("getSchoolPatentTest",countList);
		return result;
	}

	@Override
	public JSONObject testAnalysis(String businessId, Long beginDate, Long endDate) {
		int counttt = 0;
//		List<Analysis> countList = new ArrayList<Analysis>();
//		countList = analysisDao.countInventorEn(businessId, beginDate, endDate);
//		log.info(countList);
		counttt = analysisDao.countPorfolioByYear(beginDate, endDate);
		log.info(counttt);
		JSONObject result = new JSONObject();
		result.put("countSchoolByYear",counttt);
		return result;
	}


}
