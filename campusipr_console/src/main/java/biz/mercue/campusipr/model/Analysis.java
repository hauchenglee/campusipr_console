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
