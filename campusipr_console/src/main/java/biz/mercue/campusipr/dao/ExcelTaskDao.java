package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.ExcelTask;


public interface ExcelTaskDao {
	
	
	ExcelTask getById(String id);
	
	ExcelTask getByBusinessId(String businessId,String id);

	void create(ExcelTask bean);
	
	void delete(String id);

	

	List<ExcelTask> getByBusiness(String businessId);
	
	List<ExcelTask> getByAdmin(String adminId);
	
	List<ExcelTask> getNotFinishByAdmin(String adminId);
	
	List<ExcelTask> getNotInformByAdmin(String adminId);
	
	//ExcelTask getByFileName(String name);


}
