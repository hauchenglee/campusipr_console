package biz.mercue.campusipr.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.ExcelTask;
import biz.mercue.campusipr.model.Patent;




public interface ExcelTaskService {

	

	ExcelTask getById(String id);

	int addTask(ExcelTask bean);
	
	int deleteTask(ExcelTask bean);
	
	int updateTask(ExcelTask bean);
	
	
	ExcelTask addTaskByFile(MultipartFile mpFile, Admin admin)throws IOException;
	
	ExcelTask getTaskField(Admin admin, String id)throws IOException;

	Map<Integer, List<Patent>> submitTask(ExcelTask bean,Admin admin);
	
	int previewTask(ExcelTask bean,Admin admin);

	List<ExcelTask> getByBusiness(String businessId);
	
	List<ExcelTask> getByAdmin(String adminId);
	
	List<ExcelTask> getNotFinishByAdmin(String adminId);
	
	List<ExcelTask> getNotInformByAdmin(String adminId);
		


}
