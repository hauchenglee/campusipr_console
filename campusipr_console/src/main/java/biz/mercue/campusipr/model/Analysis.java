package biz.mercue.campusipr.model;

import java.util.List;

import javax.persistence.Transient;

public class Analysis extends BaseBean{
	
	@Transient
	public List<Object> plantformSchoolUSPatentData;
	@Transient
	public List<Object> plantformSchoolTWPatentData;
	@Transient
	public List<Object> plantformSchoolCNPatentData;
	
	//test
	@Transient
	public List<Object> patentDataList;
	@Transient
	public List<Object> patentYearList;
	@Transient
	public List<Object> perYearCountList;
	
	
	
	public List<Object> getPatentYearList() {
		return patentYearList;
	}
	public void setPatentYearList(List<Object> patentYearList) {
		this.patentYearList = patentYearList;
	}
	//沒用到
	@Transient
	public int schoolSum;
	@Transient
	public int applSum;
	@Transient
	public int porfolioSum;
	
	public int getSchoolSum() {
		return schoolSum;
	}
	public void setSchoolSum(int schoolSum) {
		this.schoolSum = schoolSum;
	}
	
	public int getApplSum() {
		return applSum;
	}
	public void setApplSum(int applSum) {
		this.applSum = applSum;
	}
	public int getPorfolioSum() {
		return porfolioSum;
	}
	public void setPorfolioSum(int porfolioSum) {
		this.porfolioSum = porfolioSum;
	}
	public List<Object> getPlantformSchoolUSPatentData() {
		return plantformSchoolUSPatentData;
	}
	public void setPlantformSchoolUSPatentTotal(List<Object> plantformSchoolUSPatentData) {
		this.plantformSchoolUSPatentData = plantformSchoolUSPatentData;
	}
	public List<Object> getPlantformSchoolTWPatentData() {
		return plantformSchoolTWPatentData;
	}
	public void setPlantformSchoolTWPatentTotal(List<Object> plantformSchoolTWPatentData) {
		this.plantformSchoolTWPatentData = plantformSchoolTWPatentData;
	}
	public List<Object> getPlantformSchoolCNPatentData() {
		return plantformSchoolCNPatentData;
	}
	public void setPlantformSchoolCNPatentTotal(List<Object> plantformSchoolCNPatentData) {
		this.plantformSchoolCNPatentData = plantformSchoolCNPatentData;
	}
	

	
}
