package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.*;
import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.util.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;


@Service("excelTaskService")
@Transactional
public class ExcelTaskServiceImpl implements ExcelTaskService {
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
        if (StringUtils.isNULL(bean.getExcel_task_id())) {
            bean.setExcel_task_id(KeyGeneratorUtils.generateRandomString());
        }
        dao.create(bean);
        return Constants.INT_SUCCESS;
    }

    @Override
    public int deleteTask(ExcelTask bean) {
        ExcelTask dbBean = dao.getById(bean.getExcel_task_id());
        int result = -1;
        if (dbBean != null) {
            dao.delete(dbBean.getExcel_task_id());
            result = Constants.INT_SUCCESS;
        } else {
            result = Constants.INT_CANNOT_FIND_DATA;
        }
        return result;
    }

    @Override
    public int updateTask(ExcelTask bean) {
        ExcelTask dbBean = dao.getById(bean.getExcel_task_id());
        int result = -1;
        if (dbBean != null) {
            dbBean.setIs_finish(bean.isIs_finish());

            result = Constants.INT_SUCCESS;
        } else {
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
            if (excel != null) {
                log.info("file :" + excel.getName());
                task.setBusiness(admin.getBusiness());
                task.setAdmin(admin);
                task.setIs_finish(false);
                task.setIs_inform(false);
                task.setCreate_date(new Date());
                task.setTask_file_name(excel.getName());
                dao.create(task);
            }

        } catch (Exception e) {
            log.error("Exception :" + e.getMessage());
        } finally {

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
    public int previewTask(ExcelTask bean, Admin admin) {
        ExcelTask dbBean = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), bean.getExcel_task_id());
        boolean is_continue = true;
        FileInputStream fileInputStream = null;
        try {
            if (dbBean != null) {
                List<FieldMap> filedList = bean.getListMap();
                if (filedList.size() > 0) {
                    Map<String, FieldMap> maps = convertFieldList2Map(filedList);
                    if (checkRequiredField(maps)) {
                        File excel = new File(Constants.FILE_UPLOAD_PATH + File.separator + dbBean.getTask_file_name());
                        fileInputStream = new FileInputStream(excel);
                        Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
                        List<Patent> listPatent = readBook2Patent(book, filedList, null, bean.getExcel_task_id());
                        log.info("listPatent:" + listPatent.size());
                        bean.setListPatent(listPatent);
                        return Constants.INT_SUCCESS;
                    } else {
                        return Constants.INT_DATA_ERROR;
                    }
                } else {
                    return Constants.INT_DATA_ERROR;
                }

            } else {
                return Constants.INT_CANNOT_FIND_DATA;
            }
        } catch (Exception e) {
            log.error("Exception :" + e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    log.error("Exception:" + e.getMessage());
                }
            }
        }
        return Constants.INT_SYSTEM_PROBLEM;
    }


    @Override
    public List<Patent> submitTask(ExcelTask bean, Admin admin) throws Exception {
        ExcelTask dbBean = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), bean.getExcel_task_id());
        if (dbBean == null) {
            log.error("dbBean == null: " + Constants.INT_CANNOT_FIND_DATA);
            throw new Exception();
        }

        List<FieldMap> filedList = bean.getListMap();
        if (filedList.isEmpty()) {
            log.error("filedList.isEmpty(): " + Constants.INT_DATA_ERROR);
            throw new Exception();
        }

        List<Integer> other_info_index = bean.getOther_info_index();
        Map<String, FieldMap> maps = convertFieldList2Map(filedList);
        if (!checkRequiredField(maps)) {
            log.error("checkRequiredField == false: " + Constants.INT_DATA_ERROR);
            throw new Exception();
        }

        File excel = new File(Constants.FILE_UPLOAD_PATH + File.separator + dbBean.getTask_file_name());
        FileInputStream fileInputStream = new FileInputStream(excel);
        Workbook book = ExcelUtils.file2Workbook(fileInputStream, excel.getName());
        String extensionName = FilenameUtils.getExtension(excel.getName());
        log.info(extensionName);

        List<Patent> listPatent = readBook2Patent(book, filedList, other_info_index, bean.getExcel_task_id());
        if (listPatent == null || listPatent.isEmpty()) {
            if ("xls".equalsIgnoreCase(extensionName)) {
                book2FileXls(book, admin, dbBean.getExcel_task_id());
            }
            if ("xlsx".equalsIgnoreCase(extensionName)) {
                book2FileXlsx(book, admin, dbBean.getExcel_task_id());
            }
            log.info(dbBean.getExcel_task_id());
            throw new CustomException.DataErrorException();
        }

        fileInputStream.close();
        return listPatent;
    }

    public void book2FileXls(Workbook book, Admin admin, String excelTaskId) {
        ExcelTask task = null;
        task = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), excelTaskId);
        // excelTaskId 沒有抓到副檔名
        File f = null;
        f = new File(Constants.FILE_UPLOAD_PATH + task.getExcel_task_id() + ".xls");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            book.write(fos);
            fos.close();
            log.info("匯出結束");
            log.info(f);
        } catch (Exception e) {
            log.error(e);
        }
//		File fileName = new File("https://api.mercue.biz:663/imageservice/campusipr/file/");
//		File f = new File("C:/mercue/errFile.xls");
    }

    public void book2FileXlsx(Workbook book, Admin admin, String excelTaskId) {
        ExcelTask task = null;
        task = dao.getByBusinessId(admin.getBusiness().getBusiness_id(), excelTaskId);
        File f = null;
        f = new File(Constants.FILE_UPLOAD_PATH + task.getExcel_task_id() + ".xlsx");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            book.write(fos);
            fos.close();
            log.info("匯出結束");
            log.info(f);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public List<ExcelTask> getByBusiness(String businessId) {
        return dao.getByBusiness(businessId);
    }

    @Override
    public List<ExcelTask> getByAdmin(String adminId) {
        return dao.getByAdmin(adminId);
    }

    @Override
    public List<ExcelTask> getNotFinishByAdmin(String adminId) {
        return dao.getNotFinishByAdmin(adminId);
    }

    @Override
    public List<ExcelTask> getNotInformByAdmin(String adminId) {
        return dao.getNotInformByAdmin(adminId);
    }


    private Map<String, FieldMap> convertFieldList2Map(List<FieldMap> list) {
        Map<String, FieldMap> map = new HashMap<String, FieldMap>();

        for (FieldMap fieldMap : list) {
            map.put(fieldMap.getField().getField_id(), fieldMap);
        }
        return map;
    }


    private boolean checkRequiredField(Map<String, FieldMap> maps) {
        if (maps.containsKey(Constants.PATENT_APPL_NO_FIELD) && maps.containsKey(Constants.PATENT_COUNTRY_FIELD)) {
            FieldMap map1 = maps.get(Constants.PATENT_APPL_NO_FIELD);
            FieldMap map2 = maps.get(Constants.PATENT_COUNTRY_FIELD);
            if (!StringUtils.isNULL(map1.getExcel_field_name()) && !StringUtils.isNULL(map2.getExcel_field_name())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private boolean checkRow(Row row) {
        boolean rowIsEmpty = true;
//		Sheet sheet = book.getSheetAt(0);
        List<Integer> emptyRowList = new ArrayList<Integer>();
        int emptyCell;
//		for (int y = 0; y < sheet.getLastRowNum(); y++) {
        if (row != null) {
            for (int x = 0; x < row.getLastCellNum(); x++) {
                if (row.getCell(x) == null || row.getCell(x).getCellType() == 3
                        || row.getCell(x).getCellType() == Cell.CELL_TYPE_BLANK
                        || row.getLastCellNum() == -1) {
                } else {
                    rowIsEmpty = false;
                }
                if (row.getCell(x) != null) {
                    int type = row.getCell(x).getCellType();
                    if (type == 1) {
                        String value = row.getCell(x).getStringCellValue().trim().replaceAll("[\\s\\u00A0]+", "");
                        if (value.isEmpty() || value == null || value == "") {
                        } else {
                            rowIsEmpty = false;
                        }
                    }
                }
            }

        } else {
            rowIsEmpty = true;
        }
//		}
        return rowIsEmpty;
    }

    private List<Patent> readBook2Patent(Workbook book, List<FieldMap> listField, List<Integer> other_info_index,
                                         String excelTaskId) {
        log.info("readBook2Patent");
        String pattern = "[0-9]";
        List<Patent> listPatent = new ArrayList<Patent>();
        Sheet sheet = book.getSheetAt(0);
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        log.info(physicalNumberOfRows);
        int rowIndex = 0;
        int emptyCell = 0;
        int x = 0;
        int y = 0;
        List<Integer> errorRowList = new ArrayList<Integer>();
        List<Integer> errorColumnList = new ArrayList<Integer>();
        List<Integer> emptyRowList = new ArrayList<Integer>();
        List<Country> listCountry = countryDao.getAll();
        log.info("getLastRowNum: " + sheet.getLastRowNum());
        Set<Integer> emptySet = new HashSet<Integer>();
        for (y = 0; y < sheet.getLastRowNum(); y++) {
//			log.info(y +"行，有 "+sheet.getRow(y).getLastCellNum()+"格");

            if (sheet.getRow(y) == null) {
                book.getSheetAt(0).createRow(y);
//				log.info(sheet.getRow(y).getLastCellNum());
                if (sheet.getRow(y).getLastCellNum() == -1) {
                    emptyRowList.add(y);
                    emptySet.add(y);
//					log.info(y +": Row is null");
                }
            }

            if (sheet.getRow(y) != null && sheet.getRow(y).isFormatted() && sheet.getRow(y).getLastCellNum() == -1) {
                emptyRowList.add(y);
                emptySet.add(y);
//				log.info(y + "行有格式");
            }
            if (sheet.getRow(y) != null) {
                if (sheet.getRow(y).getLastCellNum() == -1 && !sheet.getRow(y).isFormatted()) {
                    emptyRowList.add(y);
                    emptySet.add(y);
//					log.info(y + "行有格式，ROW == -1");
                }
                for (x = 0; x < sheet.getRow(y).getLastCellNum(); x++) {

                    if (sheet.getRow(y).getCell(x) == null || sheet.getRow(y).getCell(x).getCellType() == 3
                            || sheet.getRow(y).getCell(x).getCellType() == Cell.CELL_TYPE_BLANK
                            || sheet.getRow(y).getLastCellNum() == -1) {
                        emptyCell++;
//						if(sheet.getRow(y).getCell(x) == null) {
//							log.info("a");
//						}else if(sheet.getRow(y).getCell(x).getCellType() == 3) {
//							log.info("b");
//						}else if(sheet.getRow(y).getCell(x).getCellType() == Cell.CELL_TYPE_BLANK) {
//							log.info("c");
//						}else if(sheet.getRow(y).getLastCellNum() == -1) {
//							log.info("d");
//						}else {
//							log.info("e");
//						}
//						log.info(emptyCell);
                    }
                    if (sheet.getRow(y).getCell(x) != null) {
                        int type = sheet.getRow(y).getCell(x).getCellType();
                        if (type == 1) {
                            String value = sheet.getRow(y).getCell(x).getStringCellValue().trim()
                                    .replaceAll("[\\s\\u00A0]+", "");
                            if (value.isEmpty() || value == null || value == "") {
                                emptyCell++;
//								log.info("type 1 empty ");
                            }
//							log.info("type 1 :" + value);
                        }
                    }
                    if (emptyCell == sheet.getRow(y).getLastCellNum()) {
                        emptyRowList.add(y);
                        emptySet.add(y);
//						log.info(y + "行是空行");
                    }

                    if (sheet.getRow(y).getLastCellNum() == 1
                            && sheet.getRow(y).getCell(sheet.getRow(rowIndex).getLastCellNum()) == null) {
                        emptyRowList.add(y);
                        emptySet.add(y);
//						log.info(y + "行是空行，有空格的那種");
                    }
                }
                emptyCell = 0;
            }
        }
        for (int r = 0; r < emptyRowList.size(); r++) {
            int emptyRowIndex = emptyRowList.get(r);
            if (sheet.getRow(emptyRowIndex) != null) {
//				log.info("emptyRowList.size(): "+emptyRowList.size()+", emptyRowIndex: "+emptyRowIndex);
                book.getSheetAt(0).removeRow(sheet.getRow(emptyRowIndex));
//				log.info("成功移除第"+emptyRowIndex+"行");
            }
        }
        List<Integer> sortEmpty = new ArrayList<Integer>(emptySet);
        Collections.sort(sortEmpty);
        log.info("emptyRow: " + sortEmpty);
        for (int r = 0; r < sortEmpty.size(); r++) {
            int emptyRowIndex = sortEmpty.get(r);
            book.getSheetAt(0).shiftRows((emptyRowIndex + 1 - r), sheet.getLastRowNum(), -1);
//			log.info("移除成功第"+(emptyRowIndex)+"行");
        }
        for (Row row : sheet) {
//			log.info("Row");
            if (rowIndex == 0) {
                log.info("Title Row");
            } else {
                if (sheet.getRow(rowIndex) == null || sheet.getRow(rowIndex).getLastCellNum() == -1
                        || (sheet.getRow(rowIndex).getLastCellNum() == 1
                        && row.getCell(sheet.getRow(rowIndex).getLastCellNum()) == null)
                        || (rowIndex >= (sheet.getLastRowNum()) && sheet.getLastRowNum() > 1 && checkRow(row))) {
                    log.info("break");
                    break;
                } else {
                    int lastCell = sheet.getRow(rowIndex).getLastCellNum();
//					log.info(lastCell);

                    String countryName = null;
                    Patent patent = new Patent();
                    PatentExtension patentExtension = new PatentExtension();
                    boolean isApplNoNull = true;
                    for (FieldMap fieldMap : listField) {
                        if (fieldMap.getExcel_field_index() != -1) {
                            PatentField field = fieldMap.getField();
                            if (field == null) {
                                log.error("field is null");
                            }
                            switch (fieldMap.getField().getField_id()) {
                                case Constants.PATENT_NAME_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patent.setPatent_name(cellValue);
                                            patent.setPatent_excel_name(cellValue);
                                        }
                                    }
                                    log.info("patent name:" + patent.getPatent_name());
                                    break;
                                case Constants.PATENT_NAME_EN_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patent.setPatent_name_en(cellValue);
                                            patent.setPatent_excel_name_en(cellValue);
                                        }
                                    }
                                    break;
                                case Constants.PATENT_COUNTRY_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) == null) {
                                        errorRowList.add(rowIndex);
                                        errorColumnList.add(fieldMap.getExcel_field_index());
                                        emptyCell++;
//									log.info("無國家- row:"+rowIndex+"、col:" +fieldMap.getExcel_field_index());
                                    }
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        countryName = row.getCell(fieldMap.getExcel_field_index()).getStringCellValue()
                                                .trim().replaceAll("[\\s\\u00A0]+", "");
//									log.info(countryName);
                                        if (!StringUtils.isNULL(countryName)) {
                                            for (Country country : listCountry) {
                                                if (country.getCountry_name().contains(countryName)
                                                        || country.getCountry_alias_name().contains(countryName)) {
                                                    patent.setPatent_appl_country(country.getCountry_id());
                                                    break;
                                                }
                                            }
                                            if (StringUtils.isNULL(patent.getPatent_appl_country())) {
                                                errorRowList.add(rowIndex);
                                                errorColumnList.add(fieldMap.getExcel_field_index());
                                                log.info("patent.getPatent_appl_country() is null- row:" + rowIndex
                                                        + "、col:" + fieldMap.getExcel_field_index());
                                            }
                                        }
                                        if (StringUtils.isNULL(countryName)) {
                                            if (!StringUtils.isNULL(patent.getPatent_appl_no())) {
                                                errorRowList.add(rowIndex);
                                                errorColumnList.add(fieldMap.getExcel_field_index());
//											log.info("countryName is null and apply no isn't null- row:" +rowIndex+"、col:" +fieldMap.getExcel_field_index());
                                            }
                                            errorRowList.add(rowIndex);
                                            errorColumnList.add(fieldMap.getExcel_field_index());
//										log.info("countryName is null- row:" +rowIndex+"、col:" +fieldMap.getExcel_field_index());
                                        }
                                    }
                                    break;
                                case Constants.PATENT_NO_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patent.setPatent_no(cellValue);
                                        }
                                    }
//								log.info("patent no:"+patent.getPatent_no());
                                    break;
                                case Constants.PATENT_APPL_NO_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) == null) {
                                        row.createCell(fieldMap.getExcel_field_index()).setCellValue("");
                                        errorRowList.add(rowIndex);
                                        errorColumnList.add(fieldMap.getExcel_field_index());
                                        log.info("patentApplNo補空");
                                        log.info("Cell==null(跟Type3不同)- row:" + rowIndex + "、col:"
                                                + fieldMap.getExcel_field_index());
                                        break;
                                    }
                                    String patentApplNo = "";
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
                                        int cellType = row.getCell(fieldMap.getExcel_field_index()).getCellType(); // cell
                                        // type
                                        // type is null --> jump out for loop
                                        if (cellType == 3) {
                                            errorRowList.add(rowIndex);
                                            errorColumnList.add(fieldMap.getExcel_field_index());
                                            log.info("ErrorIndex:申請號為null，cellType == 3- row:" + rowIndex + "、col:"
                                                    + fieldMap.getExcel_field_index());
                                            break;
                                        }

                                        // type is numeric --> need to add country name
                                        if (cellType == 0) {
                                            log.info("type is numeric");
                                            row.getCell(excelFieldIndex).setCellType(Cell.CELL_TYPE_STRING); // change cell
                                            // type
                                            // numeric
                                            // to string
                                            patentApplNo = row.getCell(excelFieldIndex).getStringCellValue();
                                            // 有沒有含中文字or非需求字的判斷
//										if(checkApplyNo(patentApplNo) ==true) {
//											errorRowList.add(rowIndex);
//											errorColumnList.add(fieldMap.getExcel_field_index());
//											log.info("歐北輸入申請號''，row:" + rowIndex + "、col:" + fieldMap.getExcel_field_index());
//											break;
//										}
                                        }
                                        // type is string
                                        if (cellType == 1) {
                                            patentApplNo = row.getCell(excelFieldIndex).getStringCellValue()
                                                    .replaceAll("[\\s\\u00A0]+", "").trim();
                                            if (row.getCell(excelFieldIndex).getStringCellValue() == ""
                                                    || patentApplNo.isEmpty()) {
                                                errorRowList.add(rowIndex);
                                                errorColumnList.add(fieldMap.getExcel_field_index());
                                                log.info("ErrorIndex:申請號為''，row:" + rowIndex + "、col:"
                                                        + fieldMap.getExcel_field_index());
                                                break;
                                            }
                                            // 有沒有含中文字or非需求字的判斷
//										if(checkApplyNo(patentApplNo) ==true) {
//										errorRowList.add(rowIndex);
//										errorColumnList.add(fieldMap.getExcel_field_index());
//										log.info("歐北輸入申請號''，row:" + rowIndex + "、col:" + fieldMap.getExcel_field_index());										
//										break;
//										}
                                            log.info(patentApplNo);
                                        }
                                        patent.setPatent_appl_no(patentApplNo);
                                        isApplNoNull = false;
                                    }
                                    break;
                                case Constants.PATENT_APPL_DATE_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        Date date = parseDateCell(cell);
                                        patent.setPatent_appl_date(date);
                                    }
                                    break;
                                case Constants.PATENT_PUBLISH_NO_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patent.setPatent_publish_no(cellValue);
                                        }
                                    }
                                    break;
                                case Constants.PATENT_PUBLISH_DATE_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        Date date = parseDateCell(cell);
                                        patent.setPatent_publish_date(date);
                                    }
                                    break;
                                case Constants.PATENT_NOTICE_NO_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patent.setPatent_notice_no(cellValue);
                                        }
                                    }
                                    break;
                                case Constants.PATENT_NOTICE_DATE_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        Date date = parseDateCell(cell);
                                        patent.setPatent_notice_date(date);
                                    }
                                    break;
                                case Constants.APPLIANT_NAME_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cellValue = row.getCell(fieldMap.getExcel_field_index());
                                        List<String> list_name = parseCellPattern(cellValue);
                                        List<Applicant> listApplicant = new ArrayList<Applicant>();
                                        if (list_name != null && list_name.size() > 0) {
                                            for (String name : list_name) {
                                                Applicant applicant = new Applicant();
                                                applicant.setApplicant_name(name);
                                                listApplicant.add(applicant);
                                            }
                                        }
                                        patent.setListApplicant(listApplicant);
                                        patent.setPatent_excel_applicant(listApplicant);
                                    }
                                    break;
                                case Constants.ASSIGNEE_NAME_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cellValue = row.getCell(fieldMap.getExcel_field_index());
                                        List<String> list_name = parseCellPattern(cellValue);
                                        List<Assignee> listAssignee = new ArrayList<Assignee>();
                                        if (list_name != null && list_name.size() > 0) {
                                            for (String name : list_name) {
                                                Assignee assignee = new Assignee();
                                                assignee.setAssignee_name(name);
                                                listAssignee.add(assignee);
                                            }
                                        }
                                        patent.setListAssignee(listAssignee);
                                        patent.setPatent_excel_assignee(listAssignee);
                                    }
                                    break;
                                case Constants.INVENTOR_NAME_FIELD:
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cellValue = row.getCell(fieldMap.getExcel_field_index());
                                        List<String> list_name = parseCellPattern(cellValue);
                                        List<Inventor> listInventor = new ArrayList<Inventor>();
                                        if (list_name != null && list_name.size() > 0) {
                                            for (String name : list_name) {
                                                Inventor inv = new Inventor();
                                                inv.setInventor_name(name);
                                                listInventor.add(inv);
                                            }
                                        }
                                        patent.setListInventor(listInventor);
                                        patent.setPatent_excel_inventor(listInventor);
                                    }
                                    break;
                                case Constants.SCHOOL_NO_FIELD:
                                    log.info(fieldMap.getExcel_field_index());
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setBusiness_num(cellValue);
                                            patent.setExtension(patentExtension);
                                            patent.setPatent_excel_school_no(cellValue);
                                        }
                                    } else {
                                        patentExtension.setBusiness_num("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_APPL_YEAR_FIELD:
                                    log.info(fieldMap.getExcel_field_index());
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setExtension_appl_year(cellValue);
                                            patent.setExtension(patentExtension);
                                            patent.setPatent_excel_school_appl_year(cellValue);
                                        }
                                    } else {
                                        patentExtension.setExtension_appl_year("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_DEPARTMENT_FIELD:
                                    log.info(fieldMap.getExcel_field_index());
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        int excelFieldIndex = fieldMap.getExcel_field_index(); // excel field index
                                        Cell cell = row.getCell(excelFieldIndex);
                                        List<String> list_name = parseCellPattern(cell);
                                        List<Department> listDepartment = new ArrayList<>();
                                        if (list_name != null && list_name.size() > 0) {
                                            for (String name : list_name) {
                                                Department department = new Department();
                                                department.setDepartment_name(name);
                                                listDepartment.add(department);
                                            }
                                        }
                                        patent.setListDepartment(listDepartment);
                                    }
                                    break;
                                case Constants.SCHOOL_SUBSIDY_UNIT:
                                    log.info(fieldMap.getExcel_field_index());
                                    // 補助單位
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setExtension_subsidy_unit(cellValue);
                                            patent.setExtension(patentExtension);
                                        }
                                    } else {
                                        patentExtension.setExtension_subsidy_unit("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_SUBSIDY_NO:
                                    log.info(fieldMap.getExcel_field_index());
                                    // 補助編號
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setExtension_subsidy_num(cellValue);
                                            patent.setExtension(patentExtension);
                                        }
                                    } else {
                                        patentExtension.setExtension_subsidy_num("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_SUBSIDY_PLAN:
                                    log.info(fieldMap.getExcel_field_index());
                                    // 補助計劃名稱
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setExtension_subsidy_plan(cellValue);
                                            patent.setExtension(patentExtension);
                                        }
                                    } else {
                                        patentExtension.setExtension_subsidy_plan("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_AGENT:
                                    log.info(fieldMap.getExcel_field_index());
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setExtension_agent(cellValue);
                                            patent.setExtension(patentExtension);
                                        }
                                    } else {
                                        patentExtension.setExtension_agent("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_AGENT_NO:
                                    log.info(fieldMap.getExcel_field_index());
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            patentExtension.setExtension_agent_num(cellValue);
                                            patent.setExtension(patentExtension);
                                        }
                                    } else {
                                        patentExtension.setExtension_agent_num("");
                                        ;
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                case Constants.SCHOOL_MEMO_FIELD:
                                    log.info(fieldMap.getExcel_field_index());
                                    if (row.getCell(fieldMap.getExcel_field_index()) != null) {
                                        Cell cell = row.getCell(fieldMap.getExcel_field_index());
                                        String cellValue = getCellValue(cell);
                                        if (!StringUtils.isNULL(cellValue)) {
                                            log.info(cellValue);
                                            patentExtension.setExtension_memo(cellValue);
                                            patent.setExtension(patentExtension);
                                            patent.setPatent_excel_memo(cellValue);
                                        }
                                    } else {
                                        patentExtension.setExtension_memo("");
                                        patent.setExtension(patentExtension);
                                    }
                                    break;
                                default:
                                    break;
                            }
//							log.info("errorList: Column" + errorColumnList + "Row" + errorRowList);
                        }
                    } // close for (FieldMap fieldMap : listField)
                    // handle not select title and index, and iterator field not select value
                    Map<String, Integer> sorted = handleTitleMap(book, other_info_index);
                    String appendOtherInfo = "";
                    if (other_info_index.size() > 0) {
                        for (Map.Entry map : sorted.entrySet()) {
                            Integer fieldIndex = (Integer) map.getValue();
                            String fieldTitle = (String) map.getKey();
                            if (row.getCell(fieldIndex) != null) {
                                if (fieldTitle.contains("日") || fieldTitle.contains("期")) {
                                    Cell cell = row.getCell(fieldIndex);
                                    Date date = parseDateCell(cell);
                                    if (date != null) {
                                        String strDate = DateUtils.getSimpleSlashFormatDate(date);
                                        appendOtherInfo += fieldTitle + "：" + strDate + "\n";
                                    } else {
                                        appendOtherInfo += fieldTitle + "：" + "\n";
                                    }
                                } else {
                                    Cell cell = row.getCell(fieldIndex);
                                    String cellValue = getCellValue(cell);
                                    appendOtherInfo += fieldTitle + "：" + cellValue + "\n";
                                }
                            }
                        }
                    }
                    // log.info(appendOtherInfo);

                    if (!StringUtils.isNULL(appendOtherInfo)) {
                        patentExtension.setExtension_other_information(appendOtherInfo);
                        patent.setExtension(patentExtension);
                    }
                    // 註解掉影響：如果欄位未選擇或是無值，不會抹除db other info數據
                    /*
                     * else { patentExtension.setExtension_other_information("");
                     * patent.setExtension(patentExtension); }
                     */

                    log.info("patent add");
                    if (!StringUtils.isNULL(patent.getPatent_appl_no())
                            && !StringUtils.isNULL(patent.getPatent_appl_country())) {
                        patent.setEdit_source(Patent.EDIT_SOURCE_IMPORT);
                        listPatent.add(patent);
                    }
                }
            }
            rowIndex++;
//			log.info("rowIndex: "+rowIndex);

        }
        log.info("rowIndex: " + rowIndex);
        int errorRowIndex = errorRowList.size();
        log.info(errorRowIndex);
        if (errorColumnList.isEmpty() || errorRowList.isEmpty()) {
            return listPatent;
        } else {
            log.info("Errorlist is not Empty");
            log.info("errorList: Column" + errorColumnList + "Row" + errorRowList);
            setColorOnError(book, errorColumnList, errorRowList);
            log.info("Color Set");
            return null;
        }
    }

    private void setColorOnError(Workbook book, List<Integer> errorColumnList, List<Integer> errorRowList) {
        int x = 0;
        int y = 0;
        log.info("errorRowList size: " + errorRowList.size());

        try {
            log.info("XSSF");
            String sheetName = "錯誤回報";
            book.getSheet(sheetName);
            CellStyle style = book.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFCell cell = null;
            XSSFRow row = null;
            for (x = 0; x < errorRowList.size(); x++) {
                int errorRowIndex = errorRowList.get(x);
                int errorColIndex = errorColumnList.get(y);
                row = (XSSFRow) book.getSheetAt(0).getRow(errorRowIndex);
                if (row == null) {
                    log.info("row=null");
                } else {
                    cell = row.getCell(errorColIndex);
                    if (cell == null) {
                        row.createCell(errorColIndex).setCellValue("");
                        row.createCell(errorColIndex).setCellStyle(style);
                    } else {
                        cell.setCellStyle(style);
                    }
                }
                y++;
            }
            log.info("XSSF上色");

        } catch (Exception e) {
            log.error("HSSF or " + e.getMessage());
            String sheetName = "錯誤回報";
            book.getSheet(sheetName);
            CellStyle style = book.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            HSSFCell cell = null;
            HSSFRow row = null;
            log.info("HSSF型態");
            for (x = 0; x < errorRowList.size(); x++) {
                int errorRowIndex = errorRowList.get(x);
                int errorColIndex = errorColumnList.get(y);
                row = (HSSFRow) book.getSheetAt(0).getRow(errorRowIndex);
//				log.info("Row: "+errorRowIndex);
//				log.info("eCol, eRow: "+errorColIndex+", " +errorRowIndex);
                if (row == null) {
                    log.info("row=null");
                } else {
                    cell = row.getCell(errorColIndex);
                    if (cell == null) {
                        row.createCell(errorColIndex).setCellValue("");
                        row.createCell(errorColIndex).setCellStyle(style);
                    } else {
                        cell.setCellStyle(style);
                    }
                }
                y++;
            }
            log.info("HSSF上色");
        }
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
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
            log.error(e);
            return new HashMap<>();
        }
    }

    private Date parseDateCell(Cell cell) {
        try {
            CellType cellType = cell.getCellTypeEnum();
            String cellValue = null;
            double cellIntValue = -1;
            Date cellDate = null;
            switch (cellType) {
                case NUMERIC:
                    /*
                     * 在Excel中的日期格式，其數值為距離1900年1月1日的天數，
                     * 比如2009-12-24將其轉化為數字格式時變成了40171，在用java處理的時候，讀取的也將是40171
                     * 如果僅僅是判斷它是否為日期型別的話，最終會以NUMERIC型別來處理。
                     * 解決：DateUtil.getJavaDate((double) 41275.0) -> return Java Date
                     */
                    cellIntValue = cell.getNumericCellValue();
                    cellDate = DateUtil.getJavaDate(cellIntValue);

                    // print cell data result
                    log.info(new SimpleDateFormat("MM/dd/yyyy").format(cellDate));
                    return cellDate;
                case STRING:
                    cellValue = cell.getStringCellValue();
                    int cellValueLen = cellValue.length();
                    String cellValueSub = cellValue.substring(0, 2);
                    if ((cellValueLen == 7 || cellValueLen == 8)
                            && (cellValueSub.equals("19") || cellValueSub.equals("20"))) {
                        cellDate = DateUtils.parserSimpleDateFormatDate(cellValue);
                        return cellDate;
                    }
                    double cellValueDouble = Double.parseDouble(cellValue);
                    cellDate = DateUtil.getJavaDate(cellValueDouble);
                    return cellDate;
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
//
//	private Date parseDateCell(Cell cell) {
//		log.info("parseDateCell");
//		Date date= null;
//		DecimalFormat df = new DecimalFormat("0");
//		try {
//			switch (cell.getCellTypeEnum()) {
//	        case STRING:
//	        	date = DateUtils.parseMultipleFormat(cell.getRichStringCellValue().getString());
//
//	            break;
//	        case NUMERIC:
//	            if("General".equals(cell.getCellStyle().getDataFormatString())){
//	            	log.info("value 1 :"+cell.getNumericCellValue());
//	            	//date = df.format(cell.getNumericCellValue());
//	            	cell.setCellType(Cell.CELL_TYPE_STRING);
//	            	String cellValue = cell.getStringCellValue();
//	            	SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
//	            	date = originalFormat.parse(cellValue);
//	            	log.info("date 1: " + date);
//	            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
//	            	date = cell.getDateCellValue();
//	            	log.info("value 2:"+ DateUtils.getSimpleFormatDate(date));
//	            }else{
//	            	log.info("value 3:"+cell.getNumericCellValue());
//	            	//date = df.format(cell.getNumericCellValue());
//	            	cell.setCellType(Cell.CELL_TYPE_STRING);
//	            	String cellValue = cell.getStringCellValue();
//	            	SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
//	            	date = originalFormat.parse(cellValue);
//	            	log.info("date 3: " + date);
//	            }
//	            break;
//
//	        default:
//	           // value = cell.toString();
//	            break;
//			}
//			return date;
//		} catch (Exception e) {
//			log.error(e);
//			return null;
//		}
//    }
//
//    private Date parseStringDateCell(Cell cell){
//		try {
//			CellType cellType = cell.getCellTypeEnum();
//			String cellValue = null;
//			Date cellDate = null;
//			switch (cellType) {
//				case NUMERIC:
//					cell.setCellType(CellType.STRING);
//					cellValue = cell.getStringCellValue();
//					cellDate = DateUtils.parserSimpleDateFormatDate(cellValue);
//					return cellDate;
//				case STRING:
//					cellValue = cell.getStringCellValue();
//					int cellValueLen = cellValue.length();
//					String cellValueSub = cellValue.substring(0, 2);
//					if ((cellValueLen == 7 || cellValueLen == 8)
//							&& (cellValueSub.equals("19") || cellValueSub.equals("20"))) {
//						cellDate = DateUtils.parserSimpleDateFormatDate(cellValue);
//						return cellDate;
//					}
//					double cellValueDouble = Double.parseDouble(cellValue);
//					cellDate = DateUtil.getJavaDate(cellValueDouble);
//					return cellDate;
//				default:
//					return null;
//			}
//		} catch (Exception e) {
//			log.error(e);
//			return null;
//		}
//	}
//
//	private String  parseNumricCell(Cell cell) {
//		String data = null;
//		DecimalFormat df = new DecimalFormat("0");
//		switch (cell.getCellTypeEnum()) {
//        case STRING:
//        	data = cell.getRichStringCellValue().getString();
//
//            break;
//        case NUMERIC:
//
//            if("General".equals(cell.getCellStyle().getDataFormatString())){
//            	//log.info("value 1 :"+cell.getNumericCellValue());
//            	data = String.valueOf(df.format(cell.getNumericCellValue()));
//            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
//            	log.info("value 2");
//            }else{
//            	log.info("value 3:"+cell.getNumericCellValue());
//            	data = String.valueOf(df.format(cell.getNumericCellValue()));
//            }
//            break;
//
//        default:
//           // value = cell.toString();
//            break;
//		}
//		return  data;
//    }

    private List<String> parseCellPattern(Cell cell) {
        List<String> listName = new ArrayList<String>();
        String cellValue = cell.getStringCellValue();

        String s1 = cellValue.replaceAll("、", "，");
        String s2 = s1.replaceAll("；", "，");
        String s3 = s2.replaceAll(";", "，");
        String s4 = s3.replaceAll("\n", "，");
        String[] newLineName = s4.split("，");

        listName.addAll(Arrays.asList(newLineName));
        return listName;
    }

    private List<String[]> parseHumanCell(Cell cell) {
        List<String[]> list = new ArrayList<String[]>();
        String strName = cell.getStringCellValue();
//		if(strName.contains("\\r?\\n")) {
//			log.info("strName :"+strName);
//			strName = strName.replaceAll("\\r?\\n", "、");
//			log.info("replace strName :"+strName);
//		}

        String[] newLineName = strName.split("\\r?\\n");


        //log.info( "newLineName :"+newLineName.length);
        if (newLineName.length > 1) {
            strName = "";
            for (String line : newLineName) {
                if (!StringUtils.isNULL(line)) {
                    strName += line + "、";
                }

            }
            //log.info( "strName :"+strName);
            strName = strName.substring(0, strName.length());
            strName = strName.replaceAll(";、", "、");
            strName = strName.replaceAll("、、", "、");
            //log.info( "strName :"+strName);
        }

        if (strName.contains(";")) {
            String[] nameArray = strName.split(";");
            for (String name : nameArray) {
                String[] inventor = new String[2];
                splitChineseAndEnglish(name, inventor);
                list.add(inventor);
            }
        } else if (strName.contains("、")) {
            String[] nameArray = strName.split("、");
            for (String name : nameArray) {

                String[] inventor = new String[2];
                splitChineseAndEnglish(name, inventor);
                list.add(inventor);
            }
        } else {
            String[] inventor = new String[2];
            splitChineseAndEnglish(strName, inventor);
            list.add(inventor);
        }
//		for(Inventor inventor : list ) {
//			log.info("inventor:"+inventor.getInventor_name() +"/"+inventor.getInventor_name_en()+"/");
//		}
        return list;
    }

    private void splitChineseAndEnglish(String strName, String[] nameArray) {

        String chineseName = getChanesesStr(strName);
        String englishName = null;
        if (!StringUtils.isNULL(chineseName)) {
            englishName = strName.replace(chineseName, "").trim();
            nameArray[0] = chineseName.trim();

        }

        if (!StringUtils.isNULL(englishName)) {
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
