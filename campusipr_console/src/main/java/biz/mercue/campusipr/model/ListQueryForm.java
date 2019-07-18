package biz.mercue.campusipr.model;

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
	
	
	public ListQueryForm() {
	}
	
	
	
	public ListQueryForm(int task_result, int page_size, int total_count, List list, int unApplPatent,
			int analYearsTotal, int analFamilyTotal, int analDepartmentTotal, int analInventorToltal,
			List<Analysis> analAllYearsList) {
		super();
		this.task_result = task_result;
		this.page_size = page_size;
		this.total_count = total_count;
		this.list = list;
		this.unApplPatent = unApplPatent;
		this.analYearsTotal = analYearsTotal;
		this.analFamilyTotal = analFamilyTotal;
		this.analDepartmentTotal = analDepartmentTotal;
		this.analInventorToltal = analInventorToltal;
		this.analAllYearsList = analAllYearsList;
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

}
