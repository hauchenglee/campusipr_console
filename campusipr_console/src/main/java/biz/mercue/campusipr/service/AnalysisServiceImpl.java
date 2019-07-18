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
	
	@Override
	public ListQueryForm countCountry(String businessId, Long beginDate, Long endDate) {
		log.info("analysisPatent");
		List<String> countList = new ArrayList<String>();
		countList = analysisDao.countYearPatent(businessId, beginDate, endDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		Timestamp bd = new Timestamp(beginDate);
		Timestamp ed = new Timestamp(endDate);
		String beginDateFormat = sdf.format(bd);
		String endDateFormat = sdf.format(ed);
		int bdint = Integer.parseInt(beginDateFormat);
		int edint = Integer.parseInt(endDateFormat);
		log.info("beginDate: "+beginDateFormat);
		log.info("endDate: "+endDateFormat);
		//已成功得到各年份資料
		//預計以For迴圈在無專利的年份補零
		//並將結果相加回傳(這個項目可能要再開一個方法)
//		((JSONObject) countList).getJSONObject(beginDateFormat);
		log.info("countList size:" +countList.size());
		ListQueryForm form = new ListQueryForm(0, 0, countList);
		return form;
	}
	public ListQueryForm testAnalysis (String businessId) {
		List<String> countList = new ArrayList<String>();
//		countList = analysisDao.testPatent(businessId);
		countList = analysisDao.countInventorTotal(businessId);
//		log.info(countList.isEmpty());
		log.info(countList);
		ListQueryForm form = new ListQueryForm(0, 0, countList);
		return form;
	}
	@Override
	public ListQueryForm testAnalysis(String businessId, Long beginDate, Long endDate) {
		List<String> countList = new ArrayList<String>();
		countList = analysisDao.countInventor(businessId, beginDate, endDate);
		log.info(countList);
		ListQueryForm form = new ListQueryForm(0, 0, countList);
		return form;
	}

}
