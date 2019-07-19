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
	public ListQueryForm analysisAll(String businessId, Long beginDate, Long endDate) {
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

		log.info("analAllYearsList: " + analAllYearsList.size());
		ListQueryForm form = new ListQueryForm(unApplPatent, analYearsTotal, analFamilyTotal, analDepartmentTotal, analInventorToltal, analAllYearsList);
		log.info(form);
		return form;
	}

	@Override
	public ListQueryForm analysisByYear(String businessId, Long beginDate, Long endDate) {
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
		
		log.info("countPatentByYear: " + countPatentByYear.size());
		ListQueryForm form = new ListQueryForm(unApplPatent, analYearsTotal, analFamilyTotal, analDepartmentTotal, analInventorToltal, countPatentByYear);
		
		return form;
	}

	@Override
	public ListQueryForm analysisAllCountry(String businessId, Long beginDate, Long endDate, Object searchText) {
		log.info("analysisAllCountry");
		List<Analysis> countCountryTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryApplStatusTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryNoticeStatusTotal = new ArrayList<Analysis>();
		List<Analysis> countCountryPublishStatusTotal= new ArrayList<Analysis>();
		List<Analysis> countCountryByYearTotal= new ArrayList<Analysis>();
		
		String countryName = (String) searchText;
		List<Country> countryList = countryDao.getListByFuzzy(countryName);
		List<String> coutryIdList = new ArrayList<>();
		for (Country country:countryList) {
			if (!coutryIdList.contains(country.getCountry_id())) {
				coutryIdList.add(country.getCountry_id());
			}
		}
		
		countCountryTotal = analysisDao.countCountryTotal(businessId);
		countCountryApplStatusTotal = analysisDao.countCountryApplStatusTotal(businessId);
		countCountryNoticeStatusTotal = analysisDao.countCountryNoticeStatusTotal(businessId);
		countCountryPublishStatusTotal = analysisDao.countCountryPublishStatusTotal(businessId);
		countCountryByYearTotal = analysisDao.countCountryByYearTotal(businessId, coutryIdList);
		
		ListQueryForm form = new ListQueryForm(countCountryTotal, countCountryApplStatusTotal, countCountryNoticeStatusTotal, countCountryPublishStatusTotal, countCountryByYearTotal);
		return form;
	}

	@Override
	public ListQueryForm analysisCountryByYear(String businessId, Long beginDate, Long endDate, Object searchText) {
		log.info("analysisCountryByYears");
		List<Analysis> countCountry = new ArrayList<Analysis>();
		List<Analysis> countCountryApplStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryNoticeStatus = new ArrayList<Analysis>();
		List<Analysis> countCountryPublishStatus= new ArrayList<Analysis>();
		List<Analysis> countCountryByYear= new ArrayList<Analysis>();
		
		String countryName = (String) searchText;
		List<Country> countryList = countryDao.getListByFuzzy(countryName);
		List<String> coutryIdList = new ArrayList<>();
		for (Country country:countryList) {
			if (!coutryIdList.contains(country.getCountry_id())) {
				coutryIdList.add(country.getCountry_id());
			}
		}
		
		countCountry = analysisDao.countCountryTotal(businessId);
		countCountryApplStatus = analysisDao.countCountryApplStatusTotal(businessId);
		countCountryNoticeStatus= analysisDao.countCountryNoticeStatusTotal(businessId);
		countCountryPublishStatus = analysisDao.countCountryPublishStatusTotal(businessId);
		countCountryByYear = analysisDao.countCountryByYearTotal(businessId, coutryIdList);
		
		ListQueryForm form = new ListQueryForm(countCountry, countCountryApplStatus, countCountryNoticeStatus, countCountryPublishStatus, countCountryByYear);
		return form;
	}

	@Override
	public ListQueryForm analysisAllDepartment(String businessId, Long beginDate, Long endDate) {
		log.info("analysisCountryByYears");
		List<Analysis> countEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartmentTotal = new ArrayList<Analysis>();
		
		countEachDepartmentTotal = analysisDao.countEachDepartmentTotal(businessId);
		countTWEachDepartmentTotal = analysisDao.countTWEachDepartmentTotal(businessId);
		countCNEachDepartmentTotal = analysisDao.countCNEachDepartmentTotal(businessId);
		countUSEachDepartmentTotal = analysisDao.countUSEachDepartmentTotal(businessId);
		
		ListQueryForm form = new ListQueryForm(countEachDepartmentTotal, countTWEachDepartmentTotal, countCNEachDepartmentTotal, countUSEachDepartmentTotal);
		return form;
	}

	//多寫的 科系依年度查詢
	@Override
	public ListQueryForm analysisDepartmentByYears(String businessId, Long beginDate, Long endDate) {
		log.info("analysisCountryByYears");
		List<Analysis> countEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartment = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartment = new ArrayList<Analysis>();
		
		countEachDepartment = analysisDao.countEachDepartmentTotal(businessId);
		countTWEachDepartment = analysisDao.countTWEachDepartmentTotal(businessId);
		countCNEachDepartment = analysisDao.countCNEachDepartmentTotal(businessId);
		countUSEachDepartment = analysisDao.countUSEachDepartmentTotal(businessId);
		
		ListQueryForm form = new ListQueryForm(countEachDepartment, countTWEachDepartment, countCNEachDepartment, countUSEachDepartment);
		return form;
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
	public ListQueryForm testAnalysis(String businessId, Long beginDate, Long endDate) {
		return null;
//		List<Analysis> countList = new ArrayList<Analysis>();
//		countList = analysisDao.countInventorEn(businessId, beginDate, endDate);
//		log.info(countList);
//		ListQueryForm form = new ListQueryForm(0, 0, countList);
//		return form;
	}

}
