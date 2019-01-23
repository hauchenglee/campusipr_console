package biz.mercue.campusipr.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.ExcelTask;




public interface ExcelTaskService {

	

	ExcelTask getById(String id);

	int addTask(ExcelTask bean);
	
	int deleteTask(ExcelTask bean);
	
	int updateTask(ExcelTask bean);
	
	
	ExcelTask addTaskByFile(MultipartFile mpFile, Admin admin)throws IOException;
	
	ExcelTask getTaskField(Admin admin, String id)throws IOException;

	
	int submitTask(ExcelTask bean,Admin admin);

	List<ExcelTask> getByBusiness(String businessId);
	
	List<ExcelTask> getByAdmin(String adminId);
	
	List<ExcelTask> getNotFinishByAdmin(String adminId);
	
	List<ExcelTask> getNotInformByAdmin(String adminId);
		


}
