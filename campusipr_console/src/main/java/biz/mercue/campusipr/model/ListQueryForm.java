package biz.mercue.campusipr.model;

import java.util.ArrayList;
import java.util.List;


public class ListQueryForm {
	
	private int task_result = -1;
	
	private int page_size;
	
	private int total_count;
	
	private List list;

	private int unApplPatent ;
	private int analYearsTotal;
	private int analFamilyTotal;
	private int analDepartmentTotal;
	private int analInventorToltal;
	private List<Analysis> analAllYearsList;
	
	private List<Analysis> countCountryTotal ;
	private List<Analysis> countCountryApplStatusTotal ;
	private List<Analysis> countCountryNoticeStatusTotal ;
	private List<Analysis> countCountryPublishStatusTotal ;
	private List<Analysis> countCountryByYearTotal ;
	

	private List<Analysis> countEachDepartmentTotal;
	private List<Analysis> countTWEachDepartmentTotal;
	private List<Analysis> countCNEachDepartmentTotal;
	private List<Analysis> countUSEachDepartmentTotal;
	
	public ListQueryForm() {
	}
	
	public ListQueryForm(int taskResult ,int totalCount,int pageSize,List list) {
		
		this.task_result = taskResult;
		
		this.page_size = pageSize;
		
		this.total_count = totalCount;
		
		this.list = list;
	}
	
	public ListQueryForm(int totalCount,int pageSize,List list) {
		this.page_size = pageSize;
		
		this.total_count = totalCount;
		
		this.list = list;
	}

	public ListQueryForm(int unApplPatent, int analYearsTotal, int analFamilyTotal, int analDepartmentTotal,
			int analInventorToltal, List<Analysis> analAllYearsList) {
		this.unApplPatent = unApplPatent;
		this.analYearsTotal = analYearsTotal;
		this.analFamilyTotal =analFamilyTotal;
		this.analDepartmentTotal = analDepartmentTotal;
		this.analInventorToltal= analInventorToltal;
		this.analAllYearsList = analAllYearsList;
	}

	public ListQueryForm(List<Analysis> countCountryTotal, List<Analysis> countCountryApplStatusTotal,
			List<Analysis> countCountryNoticeStatusTotal, List<Analysis> countCountryPublishStatusTotal,
			List<Analysis> countCountryByYearTotal) {
		super();
		this.countCountryTotal = countCountryTotal;
		this.countCountryApplStatusTotal = countCountryApplStatusTotal;
		this.countCountryNoticeStatusTotal = countCountryNoticeStatusTotal;
		this.countCountryPublishStatusTotal = countCountryPublishStatusTotal;
		this.countCountryByYearTotal = countCountryByYearTotal;
	}

	
	
	public ListQueryForm(List<Analysis> countEachDepartmentTotal, List<Analysis> countTWEachDepartmentTotal,
			List<Analysis> countCNEachDepartmentTotal, List<Analysis> countUSEachDepartmentTotal) {
		this.countEachDepartmentTotal = countEachDepartmentTotal;
		this.countTWEachDepartmentTotal = countTWEachDepartmentTotal;
		this.countCNEachDepartmentTotal = countCNEachDepartmentTotal;
		this.countUSEachDepartmentTotal = countUSEachDepartmentTotal;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}



	public int getTask_result() {
		return task_result;
	}



	public void setTask_result(int task_result) {
		this.task_result = task_result;
	}



	public int getUnApplPatent() {
		return unApplPatent;
	}



	public void setUnApplPatent(int unApplPatent) {
		this.unApplPatent = unApplPatent;
	}



	public int getAnalYearsTotal() {
		return analYearsTotal;
	}



	public void setAnalYearsTotal(int analYearsTotal) {
		this.analYearsTotal = analYearsTotal;
	}



	public int getAnalFamilyTotal() {
		return analFamilyTotal;
	}



	public void setAnalFamilyTotal(int analFamilyTotal) {
		this.analFamilyTotal = analFamilyTotal;
	}



	public int getAnalDepartmentTotal() {
		return analDepartmentTotal;
	}



	public void setAnalDepartmentTotal(int analDepartmentTotal) {
		this.analDepartmentTotal = analDepartmentTotal;
	}



	public int getAnalInventorToltal() {
		return analInventorToltal;
	}



	public void setAnalInventorToltal(int analInventorToltal) {
		this.analInventorToltal = analInventorToltal;
	}



	public List<Analysis> getAnalAllYearsList() {
		return analAllYearsList;
	}



	public void setAnalAllYearsList(List<Analysis> analAllYearsList) {
		this.analAllYearsList = analAllYearsList;
	}

	public List<Analysis> getCountCountryTotal() {
		return countCountryTotal;
	}

	public void setCountCountryTotal(List<Analysis> countCountryTotal) {
		this.countCountryTotal = countCountryTotal;
	}

	public List<Analysis> getCountCountryApplStatusTotal() {
		return countCountryApplStatusTotal;
	}

	public void setCountCountryApplStatusTotal(List<Analysis> countCountryApplStatusTotal) {
		this.countCountryApplStatusTotal = countCountryApplStatusTotal;
	}

	public List<Analysis> getCountCountryNoticeStatusTotal() {
		return countCountryNoticeStatusTotal;
	}

	public void setCountCountryNoticeStatusTotal(List<Analysis> countCountryNoticeStatusTotal) {
		this.countCountryNoticeStatusTotal = countCountryNoticeStatusTotal;
	}

	public List<Analysis> getCountCountryPublishStatusTotal() {
		return countCountryPublishStatusTotal;
	}

	public void setCountCountryPublishStatusTotal(List<Analysis> countCountryPublishStatusTotal) {
		this.countCountryPublishStatusTotal = countCountryPublishStatusTotal;
	}

	public List<Analysis> getCountCountryByYearTotal() {
		return countCountryByYearTotal;
	}

	public void setCountCountryByYearTotal(List<Analysis> countCountryByYearTotal) {
		this.countCountryByYearTotal = countCountryByYearTotal;
	}

	public List<Analysis> getCountEachDepartmentTotal() {
		return countEachDepartmentTotal;
	}

	public void setCountEachDepartmentTotal(List<Analysis> countEachDepartmentTotal) {
		this.countEachDepartmentTotal = countEachDepartmentTotal;
	}

	public List<Analysis> getCountTWEachDepartmentTotal() {
		return countTWEachDepartmentTotal;
	}

	public void setCountTWEachDepartmentTotal(List<Analysis> countTWEachDepartmentTotal) {
		this.countTWEachDepartmentTotal = countTWEachDepartmentTotal;
	}

	public List<Analysis> getCountCNEachDepartmentTotal() {
		return countCNEachDepartmentTotal;
	}

	public void setCountCNEachDepartmentTotal(List<Analysis> countCNEachDepartmentTotal) {
		this.countCNEachDepartmentTotal = countCNEachDepartmentTotal;
	}

	public List<Analysis> getCountUSEachDepartmentTotal() {
		return countUSEachDepartmentTotal;
	}

	public void setCountUSEachDepartmentTotal(List<Analysis> countUSEachDepartmentTotal) {
		this.countUSEachDepartmentTotal = countUSEachDepartmentTotal;
	}

}
