package biz.mercue.campusipr.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AnalysisDao;
import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.util.Constants;

@Service("analysisService")
@Transactional
public class AnalysisServiceImpl implements AnalysisService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private CountryDao countryDao;

	@Autowired
	private FieldDao fieldDao;

	@Autowired
	private PatentDao patentDao;

	@Autowired
	private AnalysisDao analysisDao;

	// 未完成：預計以For迴圈在無專利的年份補零
	@Override
	public JSONObject analysisAll(String businessId) {
		log.info("analysisAllPatent");
		int unApplPatent;
		int analYearsTotal;
		int analFamilyTotal;
		int analDepartmentTotal;
		int analInventorToltal;
		unApplPatent = analysisDao.countUnApplPatent(businessId);
		analYearsTotal = analysisDao.countPatentTotal(businessId);
		analFamilyTotal = analysisDao.countPatentFamilyTotal(businessId);
		analDepartmentTotal = analysisDao.countDepartmentTotal(businessId);
		analInventorToltal = analysisDao.countInventorTotal(businessId) + analysisDao.countInventorEnTotal(businessId);

		List<Analysis> analAllYearsList = new ArrayList<Analysis>();
		analAllYearsList = analysisDao.countAllYearPatent(businessId);
		log.info(unApplPatent);
		log.info(analYearsTotal);
		log.info(analFamilyTotal);
		log.info(analDepartmentTotal);
		log.info(analInventorToltal);

		Object list[][] = analAllYearsList.toArray(new Object[analAllYearsList.size()][0]);
		log.info(list[0].length);
		log.info(list.length);
		String beginStr = list[0][1].toString();
		String endStr = list[analAllYearsList.size()-1][1].toString();
		int beginTime = Integer.parseInt(beginStr);
		int endTime = Integer.parseInt(endStr);
		int b;
		int i = 0;
		int j = 0 ;
		try {
			log.info(list[0][1]);
			log.info(list[analAllYearsList.size()-1][1]);
			for(i = 0; i<list.length; i++) {
				for(j = 0; j<list[i].length; j++) {
					log.info(list[i][j]);
					
				}
			}
			Integer[][] yearsList = new Integer[endTime-beginTime][2];
			for (b = beginTime; beginTime < endTime; beginTime++) {
				yearsList[i][1]= beginTime;
				yearsList[i][0]= 0;
				log.info("yearsList: " + yearsList[i][1] + ", 數量: " + yearsList[i][0]);
			}

//			log.info(list[0][1]);
//			log.info(list[analAllYearsList.size()-1][1]);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject result = new JSONObject();
		result.put("unApplPatent",unApplPatent);
		result.put("analYearsTotal",analYearsTotal);
		result.put("analFamilyTotal",analFamilyTotal);
		result.put("analDepartmentTotal",analDepartmentTotal);
		result.put("analInventorToltal",analInventorToltal);
		result.put("analAllYearsList",analAllYearsList);
		
		return result;
	}

	@Override
	public JSONObject analysisByYear(String businessId, Long beginDate, Long endDate) {
		log.info("analysisPatentByYear");
		int unApplPatent;
		int analYearsTotal ;
		int analFamilyTotal;
		int analDepartmentTotal;
		int analInventorToltal;
		
		unApplPatent = analysisDao.countUnApplPatent(businessId);
		analYearsTotal= analysisDao.countPatent(businessId, beginDate, endDate);
		analFamilyTotal = analysisDao.countPatentFamily(businessId, beginDate, endDate);
		analDepartmentTotal = analysisDao.countDepartment(businessId, beginDate, endDate);
		analInventorToltal = analysisDao.countInventor(businessId, beginDate, endDate) + analysisDao.countInventorEn(businessId, beginDate, endDate);
		
		List<Analysis> countPatentByYear = new ArrayList<Analysis>();
		countPatentByYear = analysisDao.countPatentByYear(businessId, beginDate, endDate);
		
		JSONObject result = new JSONObject();
		result.put("unApplPatent",unApplPatent);
		result.put("analYearsTotal",analYearsTotal);
		result.put("analFamilyTotal",analFamilyTotal);
		result.put("analDepartmentTotal",analDepartmentTotal);
		result.put("analInventorToltal",analInventorToltal);
		result.put("analAllYearsList",countPatentByYear);
		
		return result;
	}

	@Override
	public JSONObject analysisAllCountry(String businessId, Long beginDate, Long endDate, String countryId) {
		log.info("analysisAllCountry");
		List<Analysis> countCountryTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryApplStatusTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryNoticeStatusTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryPublishStatusTotal= new ArrayList<Analysis>();
		List<Analysis> countCountryByYearTotal= new ArrayList<Analysis>();
		
		countCountryTotal = analysisDao.countCountryTotal(businessId);
		countCountryApplStatusTotal = analysisDao.countCountryApplStatusTotal(businessId);
		countCountryNoticeStatusTotal = analysisDao.countCountryNoticeStatusTotal(businessId);
		countCountryPublishStatusTotal = analysisDao.countCountryPublishStatusTotal(businessId);
		countCountryByYearTotal = analysisDao.countCountryByYearTotal(businessId, countryId);
		
		JSONObject result = new JSONObject();
		result.put("countCountryTotal",countCountryTotal);
		result.put("countCountryApplStatusTotal",countCountryApplStatusTotal);
		result.put("countCountryNoticeStatusTotal",countCountryNoticeStatusTotal);
		result.put("countCountryPublishStatusTotal",countCountryPublishStatusTotal);
		result.put("countCountryByYearTotal",countCountryByYearTotal);
		
		return result;
	}

	@Override
	public JSONObject analysisCountryByYear(String businessId, Long beginDate, Long endDate, String countryId) {
		log.info("analysisCountryByYears");
		List<Analysis> countCountry = new ArrayList<Analysis>();
		List<Analysis> countCountryApplStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryNoticeStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryPublishStatus= new ArrayList<Analysis>();
		List<Analysis> countCountryByYear= new ArrayList<Analysis>();
		
		countCountry = analysisDao.countCountry(businessId, beginDate, endDate);
		countCountryApplStatus = analysisDao.countCountryApplStatus(businessId, beginDate, endDate);
		countCountryNoticeStatus= analysisDao.countCountryNoticeStatus(businessId, beginDate, endDate);
		countCountryPublishStatus = analysisDao.countCountryPublishStatus(businessId, beginDate, endDate);
		countCountryByYear = analysisDao.countCountryByYear(businessId, beginDate, endDate, countryId);
		
		
		JSONObject result = new JSONObject();
		result.put("countCountryTotal",countCountry);
		result.put("countCountryApplStatusTotal",countCountryApplStatus);
		result.put("countCountryNoticeStatusTotal",countCountryNoticeStatus);
		result.put("countCountryPublishStatusTotal",countCountryPublishStatus);
		result.put("countCountryByYearTotal",countCountryByYear);
		
		return result;
	}

	@Override
	public JSONObject analysisAllDepartment(String businessId, Long beginDate, Long endDate) {
		log.info("analysisCountryByYears");
		List<Analysis> countEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartmentTotal = new ArrayList<Analysis>();
		
		countEachDepartmentTotal = analysisDao.countEachDepartmentTotal(businessId);
		countTWEachDepartmentTotal = analysisDao.countTWEachDepartmentTotal(businessId);
		countCNEachDepartmentTotal = analysisDao.countCNEachDepartmentTotal(businessId);
		countUSEachDepartmentTotal = analysisDao.countUSEachDepartmentTotal(businessId);
		
		JSONObject result = new JSONObject();
		result.put("countEachDepartmentTotal",countEachDepartmentTotal);
		result.put("countTWEachDepartmentTotal",countTWEachDepartmentTotal);
		result.put("countCNEachDepartmentTotal",countCNEachDepartmentTotal);
		result.put("countUSEachDepartmentTotal",countUSEachDepartmentTotal);
		
		return result;
	}

	//科系依年度查詢
	@Override
	public JSONObject analysisDepartmentByYears(String businessId, Long beginDate, Long endDate) {
		log.info("analysisCountryByYears");
		List<Analysis> countEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartment = new ArrayList<Analysis>();
		
		countEachDepartment = analysisDao.countEachDepartmentTotal(businessId);
		countTWEachDepartment = analysisDao.countTWEachDepartmentTotal(businessId);
		countCNEachDepartment = analysisDao.countCNEachDepartmentTotal(businessId);
		countUSEachDepartment = analysisDao.countUSEachDepartmentTotal(businessId);
		
		JSONObject result = new JSONObject();
		result.put("countEachDepartmentTotal",countEachDepartment);
		result.put("countTWEachDepartmentTotal",countTWEachDepartment);
		result.put("countCNEachDepartmentTotal",countCNEachDepartment);
		result.put("countUSEachDepartmentTotal",countUSEachDepartment);
		
		return result;
	}

	public ListQueryForm testAnalysis(String businessId) {
//		int unApplPatent = 0;
//		unApplPatent = analysisDao.countUnApplPatent(businessId);
//		log.info(unApplPatent);
//		return unApplPatent;
		List<Analysis> countList = new ArrayList<Analysis>();
		countList = analysisDao.countCountryApplStatusTotal(businessId);
		log.info(countList);
		ListQueryForm form = new ListQueryForm(0, 0, countList);
		return form;
	}

	@Override
	public JSONObject testAnalysis(String businessId, Long beginDate, Long endDate) {
		return null;
//		List<Analysis> countList = new ArrayList<Analysis>();
//		countList = analysisDao.countInventorEn(businessId, beginDate, endDate);
//		log.info(countList);
//		ListQueryForm form = new ListQueryForm(0, 0, countList);
//		return form;
	}

}
