package biz.mercue.campusipr.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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
	private PatentDao patentDao;

	@Autowired
	private AnalysisDao analysisDao;

	@Override
	public JSONObject analysisAll(String businessId) {
		log.info("analysis All Patent");
		int unApplPatent;
		int analYearsTotal;
		int analFamilyTotal;
		int analDepartmentTotal;
		int analInventorToltal;
		List<Analysis> analAllYearsList = new ArrayList<Analysis>();
		List<Integer> yearList = new ArrayList<Integer>();
		List<Integer> allCountList = new ArrayList<Integer>();
		List<Integer> allyearList = new ArrayList<Integer>();
		List<Integer[]> analList = new ArrayList<Integer[]>();
		
		
		unApplPatent = analysisDao.countUnApplPatent(businessId);
		analYearsTotal = analysisDao.countPatentTotal(businessId);
		analFamilyTotal = analysisDao.countPatentFamilyTotal(businessId);
		analDepartmentTotal = analysisDao.countDepartmentTotal(businessId);
		analInventorToltal = analysisDao.countInventorTotal(businessId) + analysisDao.countInventorEnTotal(businessId);
		analAllYearsList = analysisDao.countAllYearPatent(businessId);

		log.info("未官方同步專利: "+unApplPatent);
		log.info("專利申請總數: "+analYearsTotal);
		log.info("專利家族總數: "+analFamilyTotal);
		log.info("科系總數: "+analDepartmentTotal);
		log.info("發明人總數: "+analInventorToltal);

		Object yearToArray[][] = analAllYearsList.toArray(new Object[analAllYearsList.size()][0]);
		
		try {
			if(yearToArray.length==0) {
				log.info("無任何專利");
			}else {
				String beginStr = yearToArray[0][1].toString();
				String endStr = yearToArray[analAllYearsList.size()-1][1].toString();
				int beginTime = Integer.parseInt(beginStr);
				int endTime = Integer.parseInt(endStr);
				int b = 0;
				int i = 0;
				int j = 0 ;
				Integer year;
				
				for (j = 0; j < yearToArray.length; j++) {
					year = Integer.parseInt(yearToArray[j][1].toString());
					yearList.add(year);
				}
				for (b = beginTime; b <= endTime; b++) {
					if(b==yearList.get(i)) {
						allyearList.add(b);
						allCountList.add(Integer.parseInt(yearToArray[i][0].toString()));
						i++;
					}else {
						allyearList.add(b);
						allCountList.add(0);
					}
				}
				Integer[] combineArr;
				for(i = 0;i<allyearList.size();i++) {
					if(!allyearList.isEmpty()) {
						combineArr = new Integer[2];
						combineArr[0] = allCountList.get(i);
						combineArr[1] = allyearList.get(i);
						analList.add(combineArr);
					}
				}
				log.info("分析期間年數:  "+analList.size());
			}

		} catch (Exception e) {
//			e.printStackTrace();
			log.error(e);
		}
		
		JSONObject result = new JSONObject();
		result.put("unApplPatent",unApplPatent);
		result.put("analYearsTotal",analYearsTotal);
		result.put("analFamilyTotal",analFamilyTotal);
		result.put("analDepartmentTotal",analDepartmentTotal);
		result.put("analInventorToltal",analInventorToltal);
		result.put("analAllYearsList",analList);
		
		return result;
	}
	
	@Override
	public JSONObject analysisByYear(String businessId, Long beginDate, Long endDate) {
		log.info("analysis Patent ByYear");
		int unApplPatent;
		int analYearsTotal ;
		int analFamilyTotal;
		int analDepartmentTotal;
		int analInventorToltal;
		List<Analysis> countPatentByYear = new ArrayList<Analysis>();
		
		List<Integer> yearList = new ArrayList<Integer>();
		List<Integer> allCountList = new ArrayList<Integer>();
		List<Integer> allyearList = new ArrayList<Integer>();
		List<Integer[]> analList = new ArrayList<Integer[]>();
		
		unApplPatent = analysisDao.countUnApplPatent(businessId);
		analYearsTotal= analysisDao.countPatent(businessId, beginDate, endDate);
		analFamilyTotal = analysisDao.countPatentFamily(businessId, beginDate, endDate);
		analDepartmentTotal = analysisDao.countDepartment(businessId, beginDate, endDate);
		analInventorToltal = analysisDao.countInventor(businessId, beginDate, endDate) + analysisDao.countInventorEn(businessId, beginDate, endDate);
		countPatentByYear = analysisDao.countPatentByYear(businessId, beginDate, endDate);

		log.info("未官方同步專利: "+unApplPatent);
		log.info("專利申請總數: "+analYearsTotal);
		log.info("專利家族總數: "+analFamilyTotal);
		log.info("科系總數: "+analDepartmentTotal);
		log.info("發明人總數: "+analInventorToltal);
		
		Object yearToArray[][] = countPatentByYear.toArray(new Object[countPatentByYear.size()][0]);
		
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
		
		JSONObject result = new JSONObject();
		result.put("unApplPatent",unApplPatent);
		result.put("analYearsTotal",analYearsTotal);
		result.put("analFamilyTotal",analFamilyTotal);
		result.put("analDepartmentTotal",analDepartmentTotal);
		result.put("analInventorToltal",analInventorToltal);
		result.put("analAllYearsList",analList);
		
		return result;
	}

	@Override
	public JSONObject analysisAllCountry(String businessId, String countryId) {
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
	public JSONObject analysisAllDepartment(String businessId) {
		log.info("analysis All Department");
		List<Analysis> countEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countTWEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countCNEachDepartmentTotal = new ArrayList<Analysis>();
		List<Analysis> countUSEachDepartmentTotal = new ArrayList<Analysis>();
		List<Object> twdepCombine = new ArrayList<Object>();
		List<Object> cndepCombine = new ArrayList<Object>();
		List<Object> usdepCombine = new ArrayList<Object>();
		
		countEachDepartmentTotal = analysisDao.countEachDepartmentTotal(businessId);
		countTWEachDepartmentTotal = analysisDao.countTWEachDepartmentTotal(businessId);
		countCNEachDepartmentTotal = analysisDao.countCNEachDepartmentTotal(businessId);
		countUSEachDepartmentTotal = analysisDao.countUSEachDepartmentTotal(businessId);
		
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
		
		return result;
	}

	//科系依年度查詢
	@Override
	public JSONObject analysisDepartmentByYears(String businessId, Long beginDate, Long endDate) {
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
		
		countEachDepartment = analysisDao.countEachDepartment(businessId, beginDate, endDate);
		countTWEachDepartment = analysisDao.countTWEachDepartment(businessId, beginDate, endDate);
		countCNEachDepartment = analysisDao.countCNEachDepartment(businessId, beginDate, endDate);
		countUSEachDepartment = analysisDao.countUSEachDepartment(businessId, beginDate, endDate);
		
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
