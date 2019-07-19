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
	public ListQueryForm analysisAllCountry(String businessId, Long beginDate, Long endDate) {
		return null;
	}

	@Override
	public ListQueryForm analysisCountryByYears(String businessId, Long beginDate, Long endDate) {
		return null;
	}

	@Override
	public ListQueryForm analysisAllDepartment(String businessId, Long beginDate, Long endDate) {
		return null;
	}

	@Override
	public ListQueryForm analysisDepartmentByYears(String businessId, Long beginDate, Long endDate) {
		return null;
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
