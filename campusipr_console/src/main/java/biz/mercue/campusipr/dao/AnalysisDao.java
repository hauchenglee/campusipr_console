package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisDao {

	int countUnApplPatent(String businessId);
	int countPatentFamilyByYear(String businessId, Long beginDate, Long endDate);
	int countAllPatent(String businessId);
	int countPatentFamily(String businessId);
	int countDepartment(String businessId);
	int countInventor(String businessId);
	int countDepartmentByYear(String businessId, Long beginDate, Long endDate);
	int countInventorByYear(String businessId, Long beginDate, Long endDate);
	int countInventorEn(String businessId);
	int countInventorEnByYear(String businessId, Long beginDate, Long endDate);
	int countAllPatentByYear(String businessId, Long beginDate, Long endDate);
	
	List<Analysis> countYearPatent(String businessId);
	List<Analysis> countYearPatentByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> testPatent(String businessId);
	
	
	List<Analysis> countCountry(String businessId);
	List<Analysis> countCountryApplStatus(String businessId);
	List<Analysis> countCountryNoticeStatus(String businessId);
	List<Analysis> countCountryPublishStatus(String businessId);
	List<Analysis> countSingleCountry(String businessId, String countryId);
	

	List<Analysis> countCountryByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryApplStatusByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryNoticeStatusByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryPublishStatusByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countSingleCountryByYear(String businessId, Long beginDate, Long endDate, String countryId);
	List<Analysis> countEachDepartment(String businessId);
	List<Analysis> countEachDepartmentByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countTWEachDepartment(String businessId);
	List<Analysis> countCNEachDepartment(String businessId);
	List<Analysis> countUSEachDepartment(String businessId);
	List<Analysis> countTWEachDepartmentByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCNEachDepartmentByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> countUSEachDepartmentByYear(String businessId, Long beginDate, Long endDate);
	
	int countPorfolio();
	int countPorfolioByYear(Long beginDate, Long endDate);
	int countSchool();
	int countSchoolByYear(Long beginDate, Long endDate);
	List<String> getSchoolList();
	List<Analysis> countSchoolPatentTotal();
	List<Analysis> countSchoolPatentApplStatus();
	List<Analysis> countSchoolPatentPublishStatus();
	List<Analysis> countSchoolPatentNoticeStatus();
	List<Analysis> countSchoolPatentTotalByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolPatentApplStatusByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolPatentNoticeStatusByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolPatentPublishStatusByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolCountry();
	List<Analysis> countSchoolCountryApplStatus();
	List<Analysis> countSchoolCountryNoticeStatus();
	List<Analysis> countSchoolCountryPublishStatus();
	List<Analysis> countSchoolSingleCountry(String countryId);
	List<Analysis> countSchoolCountryByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolCountryApplStatusByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolCountryNoticeStatusByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolCountryPublishStatusByYear(Long beginDate, Long endDate);
	List<Analysis> countSchoolSingleCountryByYear(Long beginDate, Long endDate, String countryId);
	int countAllPatent();
	int countAllPatentByYear(Long beginDate, Long endDate);
	List<Analysis> countYearPatent();
	List<Analysis> countYearPatentByYear(Long beginDate, Long endDate);
	List<Analysis> getEachDepartmentDefaultYear(String businessId);
	List<Analysis> countSchoolSum();
	List<Analysis> countSchoolSumByYear(Long beginDate, Long endDate);
	List<Analysis> getDefaultYear();
}
