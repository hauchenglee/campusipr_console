package biz.mercue.campusipr.service;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisService {
	JSONObject testAnalysis();
	JSONObject testAnalysis (String businessId);
	JSONObject testAnalysis (String businessId, Long beginDate, Long endDate);
	
	JSONObject schoolOverview(String businessId);
	JSONObject schoolOverviewByYear(String businessId, Long beginDate, Long endDate);
	JSONObject schoolCountry(String businessId, String countryId);
	JSONObject schoolCountryByYear(String businessId, Long beginDate, Long endDate, String countryId);
	JSONObject schoolDepartment(String businessId);
	JSONObject schoolDepartmentByYear(String businessId, Long beginDate, Long endDate);
	
	JSONObject platformOverview();
	JSONObject platformOverviewByYear(Long beginDate, Long endDate);
	JSONObject platformCountry(String countryId);
	JSONObject platformCountryByYear(String countryId, Long beginDate, Long endDate);
	JSONObject platformSchool();
	JSONObject platformSchoolByYear(Long beginDate, Long endDate);

}
