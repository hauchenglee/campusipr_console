package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Patent;

public interface AnalysisDao {

	int countUnApplPatent(String businessId);
	List<String> countYearPatent(String businessId, Long beginDate, Long endDate);
	List<String> countAllYearPatent(String businessId);
	List<String> countPatentFamily(String businessId, Long beginDate, Long endDate);
	List<String> testPatent(String businessId);
	List<String> countPatentTotal(String businessId);
	List<String> countYearPatentTotal(String businessId, Long beginDate, Long endDate);
	List<String> countPatentFamilyTotal(String businessId);
	List<String> countDepartmentTotal(String businessId);
	List<String> countInventorTotal(String businessId);
	List<String> countDepartment(String businessId, Long beginDate, Long endDate);
	List<String> countInventor(String businessId, Long beginDate, Long endDate);

}
