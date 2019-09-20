package biz.mercue.campusipr.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisService {
	
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
	ByteArrayInputStream exportPlatformOverviewByYear(Long beginDate, Long endDate);
	ByteArrayInputStream exportSchoolOverviewByYear(String businessId, Long beginDate, Long endDate);
	ByteArrayInputStream exportSchoolDepartmentByYear(String businessId, Long beginDate, Long endDate);
	ByteArrayInputStream exportCountryByYear(String businessId, Long beginDate, Long endDate);
	JSONObject schoolData(JSONArray statusDesc, JSONArray businessId, JSONArray countryId);
	ByteArrayInputStream exportPlatformSchoolByYear(JSONArray statusDesc, JSONArray businessName, JSONArray countryId,
			Long beginDate, Long endDate);
	
	JSONObject statusData(String businessId, Long beginDate, Long endDate);
}
