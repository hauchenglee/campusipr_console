package biz.mercue.campusipr.util;

import java.util.List;



import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.Analysis;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.View;


public class ListResponseBody extends ResponseBody {
	

	@JsonView(View.Public.class)
	int total_count;
	
	@JsonView(View.Public.class)
	int page_size;
	
	@JsonView(View.Public.class)
	List data;
	
	@JsonView(View.Public.class)
	int unApplPatent ;

	@JsonView(View.Public.class)
	int analYearsTotal;

	@JsonView(View.Public.class)
	int analFamilyTotal;

	@JsonView(View.Public.class)
	int analDepartmentTotal;

	@JsonView(View.Public.class)
	int analInventorToltal;

	@JsonView(View.Public.class)
	List<Analysis> analAllYearsList;
	
	
	public List getList() {
		return data;
	}
	public void setList(List list) {
		this.data = list;
	}
	
	
	public void setListQuery(ListQueryForm form) {
		this.data = form.getList();
		this.total_count = form.getTotal_count();
		this.page_size = form.getPage_size();
		
		this.analAllYearsList = form.getAnalAllYearsList();
		this.unApplPatent = form.getUnApplPatent();
		this.analYearsTotal = form.getAnalYearsTotal();
		this.analFamilyTotal = form.getAnalFamilyTotal();
		this.analDepartmentTotal = form.getAnalDepartmentTotal();
		this.analInventorToltal = form.getAnalInventorToltal();
	}
	
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
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
