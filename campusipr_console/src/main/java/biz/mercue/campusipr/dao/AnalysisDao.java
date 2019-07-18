package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisDao {

	int countUnApplPatent(String businessId);
	List<String> countYearPatent(String businessId, Long beginDate, Long endDate);
	List<Analysis> countAllYearPatent(String businessId);
	List<String> countPatentFamily(String businessId, Long beginDate, Long endDate);
	List<String> testPatent(String businessId);
	int countPatentTotal(String businessId);
	List<String> countYearPatentTotal(String businessId, Long beginDate, Long endDate);
	int countPatentFamilyTotal(String businessId);
	int countDepartmentTotal(String businessId);
	int countInventorTotal(String businessId);
	List<String> countDepartment(String businessId, Long beginDate, Long endDate);
	List<String> countInventor(String businessId, Long beginDate, Long endDate);
	int countInventorEnTotal(String businessId);
	List<String> countInventorEn(String businessId, Long beginDate, Long endDate);

}
