package biz.mercue.campusipr.service;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.core.result.Field;

import biz.mercue.campusipr.dao.ExcelTaskDao;
import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.dao.FieldMapDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ExcelTask;
import biz.mercue.campusipr.model.FieldMap;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.ExcelUtils;
import biz.mercue.campusipr.util.FileUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;





@Service("excelTaskService")
@Transactional
public class ExcelTaskServiceImpl implements ExcelTaskService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private FieldDao fieldDao;
	
	@Autowired
	private ExcelTaskDao dao;
	
	@Autowired
	private FieldMapDao mapDao;

	@Override
	public ExcelTask getById(String id) {
		return dao.getById(id);
	}

	@Override
	public int addTask(ExcelTask bean) {
		if(StringUtils.isNULL(bean.getExcel_task_id())) {
			bean.setExcel_task_id(KeyGeneratorUtils.generateRandomString());
		}
		dao.create(bean);
		return Constants.INT_SUCCESS;
	}
	
	@Override
	public int deleteTask(ExcelTask bean) {
		ExcelTask dbBean =dao.getById(bean.getExcel_task_id());
		int result = -1;
		if(dbBean!=null){
			dao.delete(dbBean.getExcel_task_id());
			result = Constants.INT_SUCCESS;
		}else {
			result = Constants.INT_CANNOT_FIND_DATA;
		}
		return result;
	}
	
	@Override
	public int updateTask(ExcelTask bean) {
		ExcelTask dbBean =dao.getById(bean.getExcel_task_id());
		int result = -1;
		if(dbBean!=null){
			dbBean.setIs_finish(bean.isIs_finish());

			result = Constants.INT_SUCCESS;
		}else {
			result = Constants.INT_CANNOT_FIND_DATA;
		}
		return result;
	}
	
	
	@Override
	public ExcelTask addTaskByFile(MultipartFile mpFile, Admin admin) throws IOException {
		log.info("addTaskByFile");
		ExcelTask task = new ExcelTask();
		task.setExcel_task_id(KeyGeneratorUtils.generateRandomString());
		//FileInputStream fileInputStream = null;
		try {
			File excel = FileUtils.MultipartFile2File(mpFile, task.getExcel_task_id());
			if(excel != null) {
				log.info("file :"+excel.getName());
				task.setBusiness(admin.getBusiness());
				task.setAdmin(admin);
				task.setIs_finish(false);
				task.setIs_inform(false);
				task.setCreate_date(new Date ());
				task.setTask_file_name(excel.getName());
				dao.create(task);
			}
			//fileInputStream = new FileInputStream(excel);
			//Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
			//if (book != null) {
				//handle excel task
			//	Map<String, Integer> titleMap = ExcelUtils.readExcelTitle(book);
				//log.info("1");
				//List<FieldMap> list = mapDao.getByAdmin(adminId);
			//	log.info("2");
				
				//handle task field map list
			//} else {
				//task = null;
			//}
		} catch (Exception e) {
			log.error("Exception :"+e.getMessage());
		}finally{
//			if(fileInputStream!=null) {
//				fileInputStream.close();
//			}
		}
		return task;
	}
	
	@Override
	public ExcelTask getTaskField(Admin admin, String id)throws IOException{
		ExcelTask task = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), id);
		FileInputStream fileInputStream = null;
		if(task!=null) {
			File excel = new File(Constants.FILE_UPLOAD_PATH+File.separator+task.getTask_file_name());
			fileInputStream = new FileInputStream(excel);
		    Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
			if (book != null) {
				//handle excel task
				
				//title map index
				Map<String, Integer> titleMap = ExcelUtils.readExcelTitle(book);
				//compare  title and filed name
				List<FieldMap> fieldMaps = task.getListMap();
				if(fieldMaps.size() > 0) {
					
				}else {
					List<PatentField> totalList = fieldDao.getAllFields();
					List<FieldMap> newMapList = new ArrayList<FieldMap>();
					for(PatentField field : totalList) {
						FieldMap map = new FieldMap();
						map.setField_map_id(KeyGeneratorUtils.generateRandomString());
						map.setField(field);
						map.setTask(task);
						map.setCreate_date(new Date());
						map.setUpdate_date(new Date());
						newMapList.add(map);
					}
					task.setListMap(newMapList);
				}
				task.setTitleMap(titleMap);

				book.close();
			}
		}
		
		
		return task;
	}
	
	
	@Override
	public int submitTask(ExcelTask bean,Admin admin) {
		ExcelTask dbBean = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), bean.getExcel_task_id());
		boolean is_continue = true;
		if(dbBean !=null) {
			List<FieldMap> filedList = bean.getListMap();
			if(filedList.size() > 0) {
				Map<String, FieldMap> maps = convertFieldList2Map(filedList);
				if(checkRequiredField(maps)) {
					for(FieldMap map : filedList) {
						
					}
					return Constants.INT_SUCCESS;
				}else {
					return Constants.INT_DATA_ERROR;
				}
			}else {
				return Constants.INT_DATA_ERROR;
			}
			
			
			
			
			
			
			
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
		
	}


	@Override
	public List<ExcelTask> getByBusiness(String businessId){
		return dao.getByBusiness(businessId);
	}
	
	@Override
	public List<ExcelTask> getByAdmin(String adminId){
		return dao.getByAdmin(adminId);
	}
	
	@Override
	public List<ExcelTask> getNotFinishByAdmin(String adminId){
		return dao.getNotFinishByAdmin(adminId);
	}
	
	@Override
	public List<ExcelTask> getNotInformByAdmin(String adminId){
		return dao.getNotInformByAdmin(adminId);
	}
	
	
	private Map<String , FieldMap> convertFieldList2Map(List<FieldMap> list){
		Map<String , FieldMap> map = new HashMap<String , FieldMap>();
		
		for(FieldMap fieldMap : list) {
			map.put(fieldMap.getField_map_id(), fieldMap);
			
		}
		return map;
	}
	
	
	
	private boolean checkRequiredField(Map<String , FieldMap> maps) {
		if(maps.containsKey(Constants.PATENT_APPL_NO_FIELD) && maps.containsKey(Constants.PATENT_COUNTRY_FIELD)) {
			FieldMap map1 = maps.get(Constants.PATENT_APPL_NO_FIELD);
			FieldMap map2 = maps.get(Constants.PATENT_COUNTRY_FIELD);
			if(!StringUtils.isNULL(map1.getExcel_field_name()) && !StringUtils.isNULL(map2.getExcel_field_name()) ) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}

	







	
}
