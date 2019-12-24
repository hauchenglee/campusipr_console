package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisDao {

	int countPatentFamilyByYear(String businessId, Long beginDate, Long endDate);
	int countDepartmentByYear(String businessId, Long beginDate, Long endDate);
	int countInventorByYear(String businessId, Long beginDate, Long endDate);
	int countInventorEnByYear(String businessId, Long beginDate, Long endDate);
	
	List<Analysis> overYearPatent(String businessId, Long beginDate, Long endDate);

	List<Analysis> countSingleCountryByYear(String businessId, Long beginDate, Long endDate, String countryId);
	
	int countPorfolio(Long beginDate, Long endDate);
	int countSchool(Long beginDate, Long endDate);
	List<String> getSchoolList();

	List<Analysis> countSchoolPatentTotalByYear(Long beginDate, Long endDate);

	List<Analysis> getEachDepartmentDefaultYear(String businessId);
	List<Analysis> getDefaultYear();
	List<Analysis> countSchoolPatentTotalByYear(JSONArray schoolArray, Long beginDate, Long endDate);
	List<Analysis> countSchoolPatentStatusByYear(JSONArray statusDesc, JSONArray schoolArray, Long beginDate,
			Long endDate);
	
	int countNoticePatentByYear(String businessId, Long beginDate, Long endDate);
	int countPublishPatentByYear(String businessId, Long beginDate, Long endDate);
	int countTech(String businessId);
	int countApplPatentByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> eachCountry(String businessId, Long beginDate, Long endDate, String status);
	List<Analysis> eachDepartment(String businessId, Long beginDate, Long endDate, String countryId);
	List<Analysis> eachSchoolStatus(String status, Long beginDate, Long endDate);
	List<Analysis> getBusinessDefaultYear();
}
