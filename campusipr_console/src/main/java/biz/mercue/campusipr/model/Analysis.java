package biz.mercue.campusipr.model;

import java.util.List;

import javax.persistence.Transient;

public class Analysis extends BaseBean{
	
	@Transient
	public int schoolSum;
	
	@Transient
	public int applSum;
	
	@Transient
	public int porfolioSum;
	
	@Transient
	public List<Object> plantformSchoolUSPatentData;
	@Transient
	public List<Object> plantformSchoolTWPatentData;
	@Transient
	public List<Object> plantformSchoolCNPatentData;
	
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
