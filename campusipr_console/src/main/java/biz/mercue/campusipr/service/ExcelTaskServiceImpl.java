package biz.mercue.campusipr.service;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.mercue.campusipr.dao.*;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.taglibs.standard.tag.common.fmt.ParseDateSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.core.result.Field;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.ExcelTask;
import biz.mercue.campusipr.model.FieldMap;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
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
	
	@Autowired
	private CountryDao countryDao;

	@Autowired
	private PatentDao patentDao;

	@Autowired
	private PatentService patentService;

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

		} catch (Exception e) {
			log.error("Exception :"+e.getMessage());
		}finally{

		}
		return task;
	}
	
	@Override
	public ExcelTask getTaskField(Admin admin, String id) throws IOException {
		ExcelTask task = null;
		FileInputStream fileInputStream = null;
		try {
			task = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), id);
			

			if (task != null) {
				File excel = new File(Constants.FILE_UPLOAD_PATH + File.separator + task.getTask_file_name());
				fileInputStream = new FileInputStream(excel);
				Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
				if (book != null) {
					// handle excel task

					// title map index
					Map<String, Integer> titleMap = ExcelUtils.readExcelTitle(book);
					// compare title and filed name
					List<FieldMap> fieldMaps = task.getListMap();
					if (fieldMaps.size() > 0) {

					} else {
						List<PatentField> totalList = fieldDao.getAllFields();
						List<FieldMap> newMapList = new ArrayList<FieldMap>();
						for (PatentField field : totalList) {
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
		} catch (Exception e) {
			task = null;
			log.error("Exception:" + e.getMessage());
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (Exception e) {
					task = null;
					log.error("Exception:" + e.getMessage());
				}
			}
		}

		return task;
	}
	
	
	@Override
	public int submitTask(ExcelTask bean,Admin admin) {
		ExcelTask dbBean = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), bean.getExcel_task_id());
		System.out.println("debean id : " + dbBean.getBusiness().getBusiness_id());
		System.out.println("debean name : " + dbBean.getBusiness().getBusiness_name());
		boolean is_continue = true;
		FileInputStream fileInputStream = null;
		try {
			if(dbBean !=null) {
				List<FieldMap> filedList = bean.getListMap();
				// filedList：Excel的欄位名稱，不包含value
				if(filedList.size() > 0) {
					Map<String, FieldMap> maps = convertFieldList2Map(filedList);
					if(checkRequiredField(maps)) {
						File excel = new File(Constants.FILE_UPLOAD_PATH+File.separator+dbBean.getTask_file_name());
						fileInputStream = new FileInputStream(excel);
					    Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
					    List<Patent> list = this.readBook2Patent(book, filedList, 0);

						for (Patent p : list) {
							// 抓出list裡面的每個patent
							if (p != null) {
								Patent patent = new Patent();
								patent.setPatent_name(p.getPatent_name());
								patent.setPatent_name_en(p.getPatent_name_en());
								patent.setPatent_appl_country(p.getPatent_appl_country());
								patent.setPatent_appl_date(p.getPatent_appl_date());
								patent.setPatent_appl_no(p.getPatent_appl_no());
								patent.setPatent_notice_no(p.getPatent_notice_no());
								patent.setPatent_notice_date(p.getPatent_notice_date());
								patent.setPatent_publish_no(p.getPatent_publish_no());
								patent.setPatent_publish_date(p.getPatent_publish_date());
								patent.setPatent_no(p.getPatent_no());
								patent.setPatent_bdate(p.getPatent_bdate());
								patent.setPatent_edate(p.getPatent_edate());
								patent.setPatent_cancel_date(p.getPatent_cancel_date());
								patent.setPatent_charge_expire_date(p.getPatent_charge_expire_date());
								patent.setPatent_charge_duration_year(p.getPatent_charge_duration_year());
								patent.setBusiness(dbBean.getBusiness());

								if (patent.getPatent_appl_no() != null) {
									System.out.println(patent.getPatent_appl_no());
									if (patent.getBusiness() != null) {
										patentService.addPatent(patent);
									} else {
										break;
									}
								} else {
									break;
								}
							} else {
								break;
							}
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
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Exception :"+e.getMessage());
		}finally {
			if(fileInputStream!=null) {
				try {
					fileInputStream.close();
				}catch (Exception e) {
					log.error("Exception:"+e.getMessage());
				}
			}
		}
		return Constants.INT_SYSTEM_PROBLEM;
		
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
			map.put(fieldMap.getField().getField_id(), fieldMap);
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
	
	
	private List<Patent> readBook2Patent(Workbook book, List<FieldMap> listField, int size) {
		List<Patent> listPatent = new ArrayList<Patent>();
		Sheet sheet = book.getSheetAt(0);
		int rowIndex = 0;
		int cellIndex = 0;
		List<Country> listCountry = countryDao.getAll();
		try {
			for (Row row : sheet) {
				log.info("Row");
				cellIndex = 0;
				if (rowIndex == 0) {
					log.info("Title Row");
				} else {
					Patent patent = new Patent();
					for (FieldMap fieldMap : listField) {
						if (fieldMap.getExcel_field_index() != -1) {
							switch (fieldMap.getField().getField_id()) {
							case Constants.PATENT_NAME_FIELD:
								patent.setPatent_name(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
								break;
							case Constants.PATENT_NAME_EN_FIELD:
								patent.setPatent_name_en(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
								break;

							case Constants.PATENT_COUNTRY_FIELD:
								String countyName = row.getCell(fieldMap.getExcel_field_index()).getStringCellValue();
								for (Country country : listCountry) {
//									錯誤訊息為：java.lang.NullPointerException
//									可能原因：資料庫country table的美國資料為null
									if (country.getCountry_alias_name() != null) {
										if (country.getCountry_name().contains(countyName) || country.getCountry_alias_name().contains(countyName)) {
											patent.setPatent_appl_country(country.getCountry_id());
										}
									}
								}
								break;
							case Constants.PATENT_APPL_NO_FIELD:
//								錯誤訊息為：java.lang.NullPointerException
//								可能原因：row.getCell(fieldMap.getExcel_field_index())為null
								if (row.getCell(fieldMap.getExcel_field_index()) != null) {
									patent.setPatent_appl_no(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
								}
								break;
							case Constants.PATENT_APPL_DATE_FIELD:

								Cell cell = row.getCell(fieldMap.getExcel_field_index());
								
								// patent.setPatent_name(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
								break;
							default:
								break;
							}
						}

					}
					listPatent.add(patent);
				}
				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPatent;
		

	}
	
	private Date ParseDateCell(Cell cell) {
		Date date= null;
//		DecimalFormat df = new DecimalFormat("0");
//		switch (cell.getCellTypeEnum()) {
//        case STRING:
//        	date = DateUtils.parseMultipleFormat(cell.getRichStringCellValue().getString());
//            break;
//        case NUMERIC:
//            if("General".equals(cell.getCellStyle().getDataFormatString())){
//            	date = df.format(cell.getNumericCellValue());
//            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
//            	date = cell.getDateCellValue();
//            }else{
//            	date = df.format(cell.getNumericCellValue());
//            }
//            break;
//
//        default:
//            value = cell.toString();
//            break;
//		}
		return  new Date ();
    }

	
	


	

	







	
}
