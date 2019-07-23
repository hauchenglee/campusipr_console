package biz.mercue.campusipr.service;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisService {
	ListQueryForm testAnalysis (String businessId);
	JSONObject testAnalysis (String businessId, Long beginDate, Long endDate);
	JSONObject analysisAll(String businessId);
	JSONObject analysisByYear(String businessId, Long beginDate, Long endDate);
	JSONObject analysisAllCountry(String businessId, Long beginDate, Long endDate, String countryId);
	JSONObject analysisCountryByYear(String businessId, Long beginDate, Long endDate, String countryId);
	JSONObject analysisAllDepartment(String businessId, Long beginDate, Long endDate);
	JSONObject analysisDepartmentByYears(String businessId, Long beginDate, Long endDate);

}
