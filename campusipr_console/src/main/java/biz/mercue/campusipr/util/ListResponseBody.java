package biz.mercue.campusipr.util;

import java.util.ArrayList;
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
	
	@JsonView(View.Public.class)
	List<Analysis> countCountryTotal;
	@JsonView(View.Public.class)
	List<Analysis> countCountryApplStatusTotal;
	@JsonView(View.Public.class)
	List<Analysis> countCountryNoticeStatusTotal;
	@JsonView(View.Public.class)
	List<Analysis> countCountryPublishStatusTotal;
	@JsonView(View.Public.class)
	List<Analysis> countCountryByYearTotal;
	
	@JsonView(View.Public.class)
	List<Analysis> countEachDepartmentTotal;
	@JsonView(View.Public.class)
	List<Analysis> countTWEachDepartmentTotal;
	@JsonView(View.Public.class)
	List<Analysis> countCNEachDepartmentTotal;
	@JsonView(View.Public.class)
	List<Analysis> countUSEachDepartmentTotal;
	
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
	
	public void setListCountryQuery(ListQueryForm form) {
		
		this.countCountryTotal = form.getCountCountryTotal();
		this.countCountryApplStatusTotal = form.getCountCountryApplStatusTotal();
		this.countCountryNoticeStatusTotal = form.getCountCountryNoticeStatusTotal();
		this.countCountryPublishStatusTotal = form.getCountCountryPublishStatusTotal();
		this.countCountryByYearTotal = form.getCountCountryByYearTotal();
	}
	
	public void setListDepartmentQuery(ListQueryForm form) {
		this.countEachDepartmentTotal = form.getCountEachDepartmentTotal();
		this.countTWEachDepartmentTotal = form.getCountTWEachDepartmentTotal();
		this.countCNEachDepartmentTotal = form.getCountCNEachDepartmentTotal();
		this.countUSEachDepartmentTotal = form.getCountUSEachDepartmentTotal();
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
	
}
