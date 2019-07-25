package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisDao {

	int countUnApplPatent(String businessId);
	int countPatentFamily(String businessId, Long beginDate, Long endDate);
	int countPatentTotal(String businessId);
	int countPatentFamilyTotal(String businessId);
	int countDepartmentTotal(String businessId);
	int countInventorTotal(String businessId);
	int countDepartment(String businessId, Long beginDate, Long endDate);
	int countInventor(String businessId, Long beginDate, Long endDate);
	int countInventorEnTotal(String businessId);
	int countInventorEn(String businessId, Long beginDate, Long endDate);
	int countPatent(String businessId, Long beginDate, Long endDate);
	
	List<Analysis> countAllYearPatent(String businessId);
	List<Analysis> countPatentByYear(String businessId, Long beginDate, Long endDate);
	List<Analysis> testPatent(String businessId);
	
	
	List<Analysis> countCountryTotal(String businessId);
	List<Analysis> countCountryApplStatusTotal(String businessId);
	List<Analysis> countCountryNoticeStatusTotal(String businessId);
	List<Analysis> countCountryPublishStatusTotal(String businessId);
	List<Analysis> countCountryByYearTotal(String businessId, String countryId);
	

	List<Analysis> countCountry(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryApplStatus(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryNoticeStatus(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryPublishStatus(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCountryByYear(String businessId, Long beginDate, Long endDate, String countryId);
	List<Analysis> countEachDepartmentTotal(String businessId);
	List<Analysis> countEachDepartment(String businessId, Long beginDate, Long endDate);
	List<Analysis> countTWEachDepartmentTotal(String businessId);
	List<Analysis> countCNEachDepartmentTotal(String businessId);
	List<Analysis> countUSEachDepartmentTotal(String businessId);
	List<Analysis> countTWEachDepartment(String businessId, Long beginDate, Long endDate);
	List<Analysis> countCNEachDepartment(String businessId, Long beginDate, Long endDate);
	List<Analysis> countUSEachDepartment(String businessId, Long beginDate, Long endDate);
	int countPorfolio(String businessId);
	int countPorfolioByYear(String businessId, Long beginDate, Long endDate);
	
}
