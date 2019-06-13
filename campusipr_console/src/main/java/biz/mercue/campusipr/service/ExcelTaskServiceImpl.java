package biz.mercue.campusipr.service;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biz.mercue.campusipr.dao.*;
import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.DocumentOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.ExcelTask;
import biz.mercue.campusipr.model.FieldMap;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
import biz.mercue.campusipr.util.ExcelUtils;
import biz.mercue.campusipr.util.FileUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;




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



					List<PatentField> totalList = fieldDao.getInputFields();
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
	public int previewTask(ExcelTask bean,Admin admin) {
		ExcelTask dbBean = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), bean.getExcel_task_id());
		boolean is_continue = true;
		FileInputStream fileInputStream = null;
		try {
			if(dbBean !=null) {
				List<FieldMap> filedList = bean.getListMap();
				if(filedList.size() > 0) {
					Map<String, FieldMap> maps = convertFieldList2Map(filedList);
					if(checkRequiredField(maps)) {
						File excel = new File(Constants.FILE_UPLOAD_PATH+File.separator+dbBean.getTask_file_name());
						fileInputStream = new FileInputStream(excel);
					    Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
					    List<Patent>listPatent = readBook2Patent(book, filedList, null, 1);
					    log.info("listPatent:"+listPatent.size());
					    bean.setListPatent(listPatent);
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
	public Map<Integer, List<Patent>> submitTask(ExcelTask bean,Admin admin) {
		ExcelTask dbBean = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), bean.getExcel_task_id());
		boolean is_continue = true;
		FileInputStream fileInputStream = null;
		Map<Integer, List<Patent>> mapPatent = new HashMap<>();
		try {
			if(dbBean !=null) {
				List<FieldMap> filedList = bean.getListMap();
				List<Integer> other_info_index = bean.getOther_info_index();				
				if(filedList.size() > 0) {
					Map<String, FieldMap> maps = convertFieldList2Map(filedList);
					if(checkRequiredField(maps)) {
						File excel = new File(Constants.FILE_UPLOAD_PATH+File.separator+dbBean.getTask_file_name());
						fileInputStream = new FileInputStream(excel);
					    Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
						List<Patent> listPatent = this.readBook2Patent(book, filedList, other_info_index, 0);

						if (listPatent == null || listPatent.size() == 0) {
							mapPatent.put(Constants.INT_DATA_ERROR, null);
							return mapPatent;
						}

						mapPatent.put(Constants.INT_SUCCESS, listPatent);
					    return mapPatent;
					}else {
						mapPatent.put(Constants.INT_DATA_ERROR, null);
						return mapPatent;
					}
				}else {
					mapPatent.put(Constants.INT_DATA_ERROR, null);
					return mapPatent;
				}
	
			}else {
				mapPatent.put(Constants.INT_CANNOT_FIND_DATA, null);
				return mapPatent;
			}
		}catch (Exception e) {
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
		
		mapPatent.put(Constants.INT_SYSTEM_PROBLEM, null);
		return mapPatent;
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
	
	
	private List<Patent> readBook2Patent(Workbook book, List<FieldMap> listField, List<Integer> other_info_index, int size) {
		log.info("readBook2Patent");
		List<Patent> listPatent = new ArrayList<Patent>();
		Sheet sheet = book.getSheetAt(0);
		int rowIndex = 0;
		
		List<Country> listCountry = countryDao.getAll();
		whenPatentApplNoIsNull: for (Row row : sheet) {
			log.info("Row");
			if (rowIndex == 0) {
				log.info("Title Row");
			} else {
				if(size != 0 && listPatent.size() >=size) {
					break;
				}
				
				String countryName = null;
				Patent patent = new Patent();
				PatentExtension patentExtension = new PatentExtension();
				boolean isApplNoNull = true;
				for (FieldMap fieldMap : listField) {
					if (fieldMap.getExcel_field_index() != -1) {
						PatentField  field= fieldMap.getField();
						if(field == null) {
							log.error("field is null");
						}
						log.info("index:"+fieldMap.getExcel_field_index());
						switch (fieldMap.getField().getField_id()) {
						case Constants.PATENT_NAME_FIELD:
							log.info(fieldMap.getExcel_field_index());
//							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
//								patent.setPatent_name(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
//							}
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patent.setPatent_name(cellValue);
								}
							}
							log.info("patent name:"+patent.getPatent_name());
							break;
						case Constants.PATENT_NAME_EN_FIELD:
							log.info(fieldMap.getExcel_field_index());
//							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
//								patent.setPatent_name_en(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
//							}
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patent.setPatent_name_en(cellValue);
								}
							}
							//log.info("patent appl no:"+patent.getPatent_appl_no());
							break;
						case Constants.PATENT_COUNTRY_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								countryName = row.getCell(fieldMap.getExcel_field_index()).getStringCellValue();
								
								if(!StringUtils.isNULL(countryName)) {
									for (Country country : listCountry) {
										if (country.getCountry_name().contains(countryName) || country.getCountry_alias_name().contains(countryName)) {
											patent.setPatent_appl_country(country.getCountry_id());
											break;
										}
									}
								}
							}
							break;
						case Constants.PATENT_NO_FIELD:
							log.info(fieldMap.getExcel_field_index());
//							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
//								patent.setPatent_no(row.getCell(fieldMap.getExcel_field_index()).getStringCellValue());
//							}
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patent.setPatent_no(cellValue);
								}
							}
							log.info("patent no:"+patent.getPatent_no());
							break;
						case Constants.PATENT_APPL_NO_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								int cellType = row.getCell(fieldMap.getExcel_field_index()).getCellType(); // cell type
								String patentApplNo = null;

								// type is null --> jump out for loop
								if (cellType == 3) {
									isApplNoNull = true;
									break;
								}
								
								// check country field value to detect add tw, us or cn etc
								String countryAddName = null;
								if(!StringUtils.isNULL(countryName)) {
									for (Country country : listCountry) {
										if (country.getCountry_name().contains(countryName) || country.getCountry_alias_name().contains(countryName)) {
											countryAddName = country.getCountry_id().toUpperCase();
											break;
										}
									}
								}
								
								if (StringUtils.isNULL(countryAddName)) {
									listPatent = null;
									break whenPatentApplNoIsNull;
								}
								
								// type is numeric --> need to add country name
								if (cellType == 0) {
									row.getCell(excelFieldIndex).setCellType(Cell.CELL_TYPE_STRING); // change cell type numeric to string
									patentApplNo = countryAddName + row.getCell(excelFieldIndex).getStringCellValue();
								}
								
								// type is string
								if (cellType == 1) {
									patentApplNo = row.getCell(excelFieldIndex).getStringCellValue();
									// resolve cell value is number but cell type is string
									if (!patentApplNo.substring(0, 2).equalsIgnoreCase(countryAddName)) {
										patentApplNo = countryAddName + row.getCell(excelFieldIndex).getStringCellValue();
									}
								}
								
								// check country name equals patent appl no country name
								String countryStartName = patentApplNo.substring(0, 2); // patent appl no start country name, e.g. tw, us or cn

								// check country field name is equals patent appl no or not
								if (!countryAddName.equalsIgnoreCase(countryStartName)) {
									// data is incorrect, thus set data is null
									listPatent = null;
									break whenPatentApplNoIsNull;
								}
								patent.setPatent_appl_no(patentApplNo);
								isApplNoNull = false;
							}
							break;
						case Constants.PATENT_APPL_DATE_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								Cell cellApplDate = row.getCell(fieldMap.getExcel_field_index());
								Date applDate = parseDateCell(cellApplDate);
								patent.setPatent_appl_date(applDate);
							}
							break;
						case Constants.PATENT_PUBLISH_DATE_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								Cell cellPublishDate = row.getCell(fieldMap.getExcel_field_index());
								Date pubDate = parseDateCell(cellPublishDate);
								patent.setPatent_publish_date(pubDate);
							}
							break;
						case Constants.PATENT_NOTICE_DATE_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								Cell cellNoticeDate = row.getCell(fieldMap.getExcel_field_index());
								Date noticeDate = parseDateCell(cellNoticeDate);
								patent.setPatent_notice_date(noticeDate);
							}
							break;			
						case Constants.APPLIANT_NAME_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								Cell cellAppliant = row.getCell(fieldMap.getExcel_field_index());
								List<String[]> listAppliantName = parseHumanCell(cellAppliant);
								if(listAppliantName !=  null && listAppliantName.size() >0) {
									List<Applicant> listApplicant = new ArrayList<Applicant>();
									for(String[] array : listAppliantName) {
										Applicant applicant = new Applicant();
										applicant.setApplicant_name(array[0]);
										applicant.setApplicant_name_en(array[1]);
										listApplicant.add(applicant);
									}
									patent.setListApplicant(listApplicant);
								}
							}
							break;
						case Constants.ASSIGNEE_NAME_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								Cell cellAssignee = row.getCell(fieldMap.getExcel_field_index());
								List<String[]> listAssigneeName = parseHumanCell(cellAssignee);
								if(listAssigneeName !=  null && listAssigneeName.size() >0) {
									List<Assignee> listAssignee = new ArrayList<Assignee>();
									for(String[] array : listAssigneeName) {
										Assignee assignee = new Assignee();
										assignee.setAssignee_name(array[0]);
										assignee.setAssignee_name_en(array[1]);
										listAssignee.add(assignee);
									}
									patent.setListAssignee(listAssignee);
								}
							}
							break;
							
						case Constants.INVENTOR_NAME_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index())!= null) {
								Cell cellInventor = row.getCell(fieldMap.getExcel_field_index());
								List<String[]> listInventorName = parseHumanCell(cellInventor);
								if(listInventorName !=  null && listInventorName.size() >0) {
									List<Inventor> listInventor = new ArrayList<Inventor>();
									for(String[] array : listInventorName) {
										Inventor inventor = new Inventor();
										inventor.setInventor_name(array[0]);
										inventor.setInventor_name_en(array[1]);
										listInventor.add(inventor);
									}
									patent.setListInventor(listInventor);
								}
							}
							break;
						case Constants.SCHOOL_NO_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setBusiness_num(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setBusiness_num("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_APPL_YEAR_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_appl_year(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_appl_year("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_DEPARTMENT_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_school_department(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_school_department("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_SUBSIDY_UNIT:
							log.info(fieldMap.getExcel_field_index());
							// 補助單位
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_subsidy_unit(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_subsidy_unit("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_SUBSIDY_NO:
							log.info(fieldMap.getExcel_field_index());
							// 補助編號
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_subsidy_num(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_subsidy_num("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_SUBSIDY_PLAN:
							log.info(fieldMap.getExcel_field_index());
							// 補助計劃名稱
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_subsidy_plan(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_subsidy_plan("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_AGENT:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_agent(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_agent("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_AGENT_NO:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									patentExtension.setExtension_agent_num(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_agent_num("");;
								patent.setExtension(patentExtension);
							}
							break;
						case Constants.SCHOOL_MEMO_FIELD:
							log.info(fieldMap.getExcel_field_index());
							if (row.getCell(fieldMap.getExcel_field_index()) != null) {
								int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
								String cellValue = getCellValue_byInputString(row, excelFieldIndex);
								if (!StringUtils.isNULL(cellValue)) {
									log.info(cellValue);
									patentExtension.setExtension_memo(cellValue);
									patent.setExtension(patentExtension);
								}
							} else {
								patentExtension.setExtension_memo("");
								patent.setExtension(patentExtension);
							}
							break;
						default:
							break;
						}

					}
				} // close for (FieldMap fieldMap : listField)
				
				// handle not select title and index, and iterator field not select value
				Map<String, Integer> sorted = handleTitleMap(book, other_info_index);
				String appendOtherInfo = "";

				if (other_info_index.size() > 0 && !isApplNoNull) {
					for (Map.Entry map : sorted.entrySet()) {
						Integer fieldIndex = (Integer) map.getValue();
						String fieldTitle = (String) map.getKey();
						if (row.getCell(fieldIndex) != null) {
							String cellValue = getCellValue_byInputString(row, fieldIndex);
							if (!StringUtils.isNULL(cellValue)) {
								appendOtherInfo += fieldTitle + "：" + cellValue + "\n";
							}
						}
					}
				} // close if (other_info_index.size() > 0 && !isApplNoNull)
				log.info(appendOtherInfo);

				if (!StringUtils.isNULL(appendOtherInfo)) {
					patentExtension.setExtension_other_information(appendOtherInfo);
					patent.setExtension(patentExtension);
				} else {
					patentExtension.setExtension_other_information("");
					patent.setExtension(patentExtension);
				}

				log.info("patent add");
				if (!StringUtils.isNULL(patent.getPatent_appl_no()) && !StringUtils.isNULL(patent.getPatent_appl_country())) {
					patent.setEdit_source(Patent.EDIT_SOURCE_IMPORT);
					listPatent.add(patent);
				} else {
					break whenPatentApplNoIsNull;
				}
			}
			rowIndex++;
		}

		return listPatent;
		

	}
	
	private String getCellValue_byInputString(Row row, int fieldIndex) {
		int cellType = row.getCell(fieldIndex).getCellType(); // cell type
		String cellValue = "";
		
		/**
		 * CELL_TYPE_NUMERIC -> 0
		 * CELL_TYPE_STRING -> 1
		 * CELL_TYPE_FORMULA -> 2
		 * CELL_TYPE_BLANK -> 3
		 * CELL_TYPE_BOOLEAN -> 4
		 * CELL_TYPE_ERROR -> 5
		 */
		switch(cellType) {
		case 0:
			row.getCell(fieldIndex).setCellType(Cell.CELL_TYPE_STRING);
			cellValue = row.getCell(fieldIndex).getStringCellValue();
			break;
		case 1:
			cellValue = row.getCell(fieldIndex).getStringCellValue();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			row.getCell(fieldIndex).setCellType(Cell.CELL_TYPE_STRING);
			cellValue = row.getCell(fieldIndex).getStringCellValue();
			break;
		case 5:
			break;
		}
		
		return cellValue;
	}
	
	private Map<String, Integer> handleTitleMap(Workbook book, List<Integer> other_info_index) {
		Map<String, Integer> otherInfoTitleMap = new TreeMap<>();
		Map<String, Integer> readExcelTitleMap;
		try {
			readExcelTitleMap = ExcelUtils.readExcelTitle(book);
			for (Integer fieldIndex : other_info_index) {
				for (Map.Entry map : readExcelTitleMap.entrySet()) {
		            if (fieldIndex == (Integer) map.getValue()) {
		            	otherInfoTitleMap.put((String) map.getKey(), fieldIndex);
		            }
		        }
			}
			
		    Map<String, Integer> sorted = otherInfoTitleMap
		            .entrySet()
		            .stream()
		            .sorted(comparingByValue())
		            .collect(
		                toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                    LinkedHashMap::new));
		    
		    sorted = otherInfoTitleMap
		            .entrySet()
		            .stream()
		            .sorted(comparingByValue())
		            .collect(
		                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
		                    LinkedHashMap::new));
		    return sorted;
		} catch (IOException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	
	private Date parseDateCell(Cell cell) {
		log.info("parseDateCell");
		Date date= null;
		DecimalFormat df = new DecimalFormat("0");
		try {
			switch (cell.getCellTypeEnum()) {
	        case STRING:
	        	date = DateUtils.parseMultipleFormat(cell.getRichStringCellValue().getString());
	        	
	            break;
	        case NUMERIC:
	            if("General".equals(cell.getCellStyle().getDataFormatString())){
	            	log.info("value 1 :"+cell.getNumericCellValue());
	            	//date = df.format(cell.getNumericCellValue());
	            	cell.setCellType(Cell.CELL_TYPE_STRING);
	            	String cellValue = cell.getStringCellValue();
	            	SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
	            	date = originalFormat.parse(cellValue);
	            	log.info("date 1: " + date);
	            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
	            	date = cell.getDateCellValue();
	            	log.info("value 2:"+ DateUtils.getSimpleFormatDate(date));
	            }else{
	            	log.info("value 3:"+cell.getNumericCellValue());
	            	//date = df.format(cell.getNumericCellValue());
	            	cell.setCellType(Cell.CELL_TYPE_STRING);
	            	String cellValue = cell.getStringCellValue();
	            	SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
	            	date = originalFormat.parse(cellValue);
	            	log.info("date 3: " + date);
	            }
	            break;

	        default:
	           // value = cell.toString();
	            break;
			}
			return date;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
    }
	
	
	private String  parseNumricCell(Cell cell) {
		String data = null;
		DecimalFormat df = new DecimalFormat("0");
		switch (cell.getCellTypeEnum()) {
        case STRING:
        	data = cell.getRichStringCellValue().getString();
        	
            break;
        case NUMERIC:
        	
            if("General".equals(cell.getCellStyle().getDataFormatString())){
            	//log.info("value 1 :"+cell.getNumericCellValue());
            	data = String.valueOf(df.format(cell.getNumericCellValue()));
            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
            	log.info("value 2");
            }else{
            	log.info("value 3:"+cell.getNumericCellValue());
            	data = String.valueOf(df.format(cell.getNumericCellValue()));
            }
            break;

        default:
           // value = cell.toString();
            break;
		}
		return  data;
    }
	
	
	private List<String[]>  parseHumanCell(Cell cell) {
		List<String[]> list = new ArrayList<String[]>();
		String strName = cell.getStringCellValue();
//		if(strName.contains("\\r?\\n")) {
//			log.info("strName :"+strName);
//			strName = strName.replaceAll("\\r?\\n", "、");
//			log.info("replace strName :"+strName);
//		}
		
		String[] newLineName = strName.split("\\r?\\n");
		
		
		//log.info( "newLineName :"+newLineName.length);
		if(newLineName.length > 1) {
			strName = "";
			for(String line : newLineName) {				
				if(!StringUtils.isNULL(line)) {
					strName += line + "、";
				}
				
			}
			//log.info( "strName :"+strName);
			strName = strName.substring(0, strName.length());
			strName = strName.replaceAll(";、", "、");
			strName = strName.replaceAll("、、", "、");
			//log.info( "strName :"+strName);
		}
		
		if(strName.contains(";")) {
			String[] nameArray = strName.split(";");
			for(String name : nameArray ) {
				String[] inventor = new String[2];
				splitChineseAndEnglish(name,inventor);
				list.add(inventor);
			}
		}else if(strName.contains("、")) {
			String[] nameArray = strName.split("、");
			for(String name : nameArray ) {

				String[] inventor = new String[2];
				splitChineseAndEnglish(name,inventor);
				list.add(inventor);
			}
		}else {
			String[] inventor = new String[2];
			splitChineseAndEnglish(strName,inventor);
			list.add(inventor);
		}
//		for(Inventor inventor : list ) {
//			log.info("inventor:"+inventor.getInventor_name() +"/"+inventor.getInventor_name_en()+"/");
//		}
		return list;
	}
	
	private void  splitChineseAndEnglish(String strName,String[] nameArray) {
		
		String chineseName = getChanesesStr(strName);
		String englishName = null;
		if(!StringUtils.isNULL(chineseName)) {
			 englishName = strName.replace(chineseName, "").trim();
			 nameArray[0] = chineseName.trim();
			 
		}
		
		if(!StringUtils.isNULL(englishName)) {
			 nameArray[1] = englishName.trim();
			
		}
	}
	
	public static String getChanesesStr(String str) {
		String resultStr = "";
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			resultStr += m.group(0);
		}
		return resultStr;
	}
}
