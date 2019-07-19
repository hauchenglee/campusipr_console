package biz.mercue.campusipr.service;

import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;

public interface AnalysisService {
	ListQueryForm testAnalysis (String businessId);
	ListQueryForm testAnalysis (String businessId, Long beginDate, Long endDate);
	ListQueryForm analysisAll(String businessId, Long beginDate, Long endDate);
	ListQueryForm analysisByYear(String businessId, Long beginDate, Long endDate);
	ListQueryForm analysisAllCountry(String businessId, Long beginDate, Long endDate, Object searchText);
	ListQueryForm analysisCountryByYear(String businessId, Long beginDate, Long endDate, Object searchText);
	ListQueryForm analysisAllDepartment(String businessId, Long beginDate, Long endDate);
	ListQueryForm analysisDepartmentByYears(String businessId, Long beginDate, Long endDate);

}
