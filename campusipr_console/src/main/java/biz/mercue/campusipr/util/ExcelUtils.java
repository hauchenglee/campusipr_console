package biz.mercue.campusipr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import biz.mercue.campusipr.model.*;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ExcelUtils {
	
	private static Logger log = Logger.getLogger(ExcelUtils.class.getName());

	private static String[] school_columns = {
			Constants.EXCEL_COLUMN_PATENT_NAME, // 專利名稱
			Constants.EXCEL_COLUMN_PATENT_NAME_EN, // 專利名稱（英文）
			Constants.EXCEL_COLUMN_APPLICANT_COUNTRY, // 申請國家
			Constants.EXCEL_COLUMN_PATENT_STATUS, // 專利狀態
			Constants.EXCEL_COLUMN_APPLICANT, // 申請人
			Constants.EXCEL_COLUMN_ASSIGNEE, // 專利權人
			Constants.EXCEL_COLUMN_INVENTOR, // 發明人
			Constants.EXCEL_COLUMN_APPLICATION_DATE, // 申請日
			Constants.EXCEL_COLUMN_APPLICATION_NO, // 申請號
			Constants.EXCEL_COLUMN_PUBLIC_DATE, // 公開日
			Constants.EXCEL_COLUMN_PUBLIC_NO, // 公開號
			Constants.EXCEL_COLUMN_NOTICE_DATE, // 公告日
			Constants.EXCEL_COLUMN_NOTICE_NO, // 公告號
			Constants.EXCEL_COLUMN_PATENT_NO, // 證書號
			Constants.EXCEL_COLUMN_PAY_VAILD_DATE, // 年費有效日期
			Constants.EXCEL_COLUMN_PAY_EXPIRE_YEAR, // 年費有效年次
			Constants.EXCEL_COLUMN_PATENT_GEGIN_DATE, // 專利權始日
			Constants.EXCEL_COLUMN_PATENT_END_DATE, // 估算專利截止日
			Constants.EXCEL_COLUMN_PATENT_CANEL_DATE, // 消滅日期
			Constants.EXCEL_COLUMN_PATENT_ABSTRACT, // 專利摘要
			Constants.EXCEL_COLUMN_PATENT_CLAIM, // 專利權利範圍
			Constants.EXCEL_COLUMN_PATENT_DESC, // 專利描述
			Constants.EXCEL_COLUMN_PATENT_IPC, // 國際專利分類
			Constants.EXCEL_COLUMN_SCHOOL_NUM_FIELD, // 學校編號
			Constants.EXCEL_COLUMN_SCHOOL_APPL_YEAR, // 申請年度
			Constants.EXCEL_COLUMN_SCHOOL_SUBSIDY_UNIT, // 補助單位
			Constants.EXCEL_COLUMN_SCHOOL_SUBSIDY_NO, // 補助編號
			Constants.EXCEL_COLUMN_SCHOOL_SUBSIDY_PLAN, // 補助計劃名稱
			Constants.EXCEL_COLUMN_SCHOOL_AGENT, // 事務所
			Constants.EXCEL_COLUMN_SCHOOL_AGENT_NO, // 事務所編號
			Constants.EXCEL_COLUMN_SCHOOL_MEMO_FIELD, // 備註
			Constants.EXCEL_COLUMN_SCHOOL_OTHER_INFO // 其他資訊
	};
	
	private static String[] plateform_columns = {
			Constants.EXCEL_COLUMN_BUSINESS,
			Constants.EXCEL_COLUMN_PATENT_NAME,
			Constants.EXCEL_COLUMN_PATENT_NAME_EN,
			Constants.EXCEL_COLUMN_APPLICANT_COUNTRY,
			Constants.EXCEL_COLUMN_PATENT_STATUS,
			Constants.EXCEL_COLUMN_APPLICANT,
			Constants.EXCEL_COLUMN_ASSIGNEE,
			Constants.EXCEL_COLUMN_INVENTOR,
			Constants.EXCEL_COLUMN_APPLICATION_DATE,
			Constants.EXCEL_COLUMN_APPLICATION_NO,
			Constants.EXCEL_COLUMN_PUBLIC_DATE,
			Constants.EXCEL_COLUMN_PUBLIC_NO,
			Constants.EXCEL_COLUMN_NOTICE_DATE,
			Constants.EXCEL_COLUMN_NOTICE_NO,
			Constants.EXCEL_COLUMN_PATENT_NO,
			Constants.EXCEL_COLUMN_PAY_VAILD_DATE,
			Constants.EXCEL_COLUMN_PAY_EXPIRE_YEAR,
			Constants.EXCEL_COLUMN_PATENT_GEGIN_DATE,
			Constants.EXCEL_COLUMN_PATENT_END_DATE,
			Constants.EXCEL_COLUMN_PATENT_CANEL_DATE,
			Constants.EXCEL_COLUMN_PATENT_ABSTRACT,
			Constants.EXCEL_COLUMN_PATENT_CLAIM,
			Constants.EXCEL_COLUMN_PATENT_DESC,
			Constants.EXCEL_COLUMN_PATENT_IPC
	};

	public static final ByteArrayInputStream Patent2Excel(List<String> field_ids, List<Patent> list, String businessId) {
		ByteArrayOutputStream fileOut = null;

//		try {
		// Create a Workbook
		Workbook workbook = new HSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
	
	        /* CreationHelper helps us create instances of various things like DataFormat, 
	           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Patent");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setColor(IndexedColors.GREEN.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		Map<String, String> excelColumnMap = new HashMap<>();
		excelColumnMap.put("0cd33801e82d0912ba1b0297573ec361", Constants.EXCEL_COLUMN_BUSINESS);
		excelColumnMap.put("edad617ccdc004f37cac8f8710c6e965", Constants.EXCEL_COLUMN_PATENT_NAME);
		excelColumnMap.put("4e765c02d8a5afc000eaa77ba419ff53", Constants.EXCEL_COLUMN_PATENT_NAME_EN);
		excelColumnMap.put("11507a5a70670d1329e3a7effc24ca60", Constants.EXCEL_COLUMN_APPLICANT_COUNTRY);
		excelColumnMap.put("bca0a0b8d0cc16e64758ad3a0fec31ee", Constants.EXCEL_COLUMN_PATENT_STATUS);
		excelColumnMap.put("6c761184252dd9f0148a361ed9c4c8c2", Constants.EXCEL_COLUMN_APPLICANT);
		excelColumnMap.put("614191c8ec65d0e6429801429794ebcd", Constants.EXCEL_COLUMN_ASSIGNEE);
		excelColumnMap.put("f08b87006f899e74c0b570f76349f49d", Constants.EXCEL_COLUMN_INVENTOR);
		excelColumnMap.put("42437fd3b6fd38418a6d0e6068a32f2a", Constants.EXCEL_COLUMN_APPLICATION_DATE);
		excelColumnMap.put("9de48893637f0c68664accdfe515f2ea", Constants.EXCEL_COLUMN_APPLICATION_NO);
		excelColumnMap.put("8cec5a2fbf128bca2cbf39059e0908d4", Constants.EXCEL_COLUMN_PUBLIC_DATE);
		excelColumnMap.put("d3fc8c21aa05c0c8e7c1c52a704d2f72", Constants.EXCEL_COLUMN_PUBLIC_NO);
		excelColumnMap.put("eb2f853e0fefd4af566cbd1fa8f5f744", Constants.EXCEL_COLUMN_NOTICE_DATE);
		excelColumnMap.put("76ab6a77047bd64250795aa9e1bc9e49", Constants.EXCEL_COLUMN_NOTICE_NO);
		excelColumnMap.put("0094b3bb46157502c406b50e899b0c19", Constants.EXCEL_COLUMN_PATENT_NO);
		excelColumnMap.put("5907b02544155369f79c37c4ff0bc777", Constants.EXCEL_COLUMN_PAY_VAILD_DATE);
		excelColumnMap.put("9c48e4dec6c90736de097b4237da33b1", Constants.EXCEL_COLUMN_PAY_EXPIRE_YEAR);
		excelColumnMap.put("e1309a9e14bec907470006a901803a24", Constants.EXCEL_COLUMN_PATENT_GEGIN_DATE);
		excelColumnMap.put("39083412af3b51bf9a33a9a2b5f8b916", Constants.EXCEL_COLUMN_PATENT_END_DATE);
		excelColumnMap.put("e14e7d4ca475479f11e47f52a0063f7f", Constants.EXCEL_COLUMN_PATENT_CANEL_DATE);
		excelColumnMap.put("86cd0dd6691fe69c97af8ebf6cff199c", Constants.EXCEL_COLUMN_PATENT_ABSTRACT);
		excelColumnMap.put("382c9ca7af04c5362e1f24f1bf38249b", Constants.EXCEL_COLUMN_PATENT_CLAIM);
		excelColumnMap.put("006b70bc72595d46bed73899fb557bb1", Constants.EXCEL_COLUMN_PATENT_DESC);
		excelColumnMap.put("785c0e398eca15f96476fceafeb11598", Constants.EXCEL_COLUMN_PATENT_IPC);
		excelColumnMap.put("ab022983f82441ca165f667059761258", Constants.EXCEL_COLUMN_SCHOOL_DEPARTMENT);
		excelColumnMap.put("71edfc0d2783857fefb360e574a90355", Constants.EXCEL_COLUMN_SCHOOL_NUM_FIELD);
		excelColumnMap.put("78f94e6e1b246d9419faa044a1119cc7", Constants.EXCEL_COLUMN_SCHOOL_APPL_YEAR);
		excelColumnMap.put("fcb331d04d95960a7a8fe469015343f8", Constants.EXCEL_COLUMN_SCHOOL_SUBSIDY_UNIT);
		excelColumnMap.put("895c8843a89248f98193dcb7ee81644b", Constants.EXCEL_COLUMN_SCHOOL_SUBSIDY_NO);
		excelColumnMap.put("c5baefafea5a98a6cbcb45820a9c78d6", Constants.EXCEL_COLUMN_SCHOOL_SUBSIDY_PLAN);
		excelColumnMap.put("853e5ab43383cc325c0b8fe18cb46014", Constants.EXCEL_COLUMN_SCHOOL_AGENT);
		excelColumnMap.put("f8b56e61ee5e6e36abd538f71ae32c8f", Constants.EXCEL_COLUMN_SCHOOL_AGENT_NO);
		excelColumnMap.put("8e04eb78ced23ee1d7f7b0ac2e86ee45", Constants.EXCEL_COLUMN_SCHOOL_MEMO_FIELD);
		excelColumnMap.put("a14fca6ef3c18045116175bb23532feb", Constants.EXCEL_COLUMN_SCHOOL_OTHER_INFO);

		int[] rowTitleCount = new int[field_ids.size()];
		List<String> titleList = new ArrayList<>();

		for (String fieldId : field_ids) {
			for (Entry<String, String> entry : excelColumnMap.entrySet()) {
				if (entry.getKey().equals(fieldId)) {
					titleList.add(entry.getValue());
				}
			}
		}

		int columnTitleCount = 0;
		for (String title : titleList) {
			Cell cell = headerRow.createCell(columnTitleCount);
			cell.setCellValue(title);
			cell.setCellStyle(headerCellStyle);
			columnTitleCount++;
		}

		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
		dateCellStyle.setAlignment(HorizontalAlignment.LEFT);

		// Create Other rows and cells with employees data
		int rowNum = 1;
		for (Patent patent : list) {
			int columnCount = 0;
			Row row = sheet.createRow(rowNum++);
			for (String fieldId : field_ids) {
				switch (fieldId) {
					case Constants.CONSERVANCY: // 管理單位
						log.info("CONSERVANCY: " + Constants.CONSERVANCY);
						log.info(columnCount);
						if (patent.getListBusiness() != null) {
							for (Business business : patent.getListBusiness()) {
								if (businessId.equals(business.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(business.getBusiness_name());
								}
							}
						}
						columnCount++;
						break;
					case Constants.PATENT_NAME_FIELD:
						log.info("PATENT_NAME_FIELD: " + Constants.PATENT_NAME_FIELD);
						log.info(columnCount);
						row.createCell(columnCount).setCellValue(patent.getPatent_name());
						columnCount++;
						break;
					case Constants.PATENT_NAME_EN_FIELD:
						log.info("PATENT_NAME_EN_FIELD: " + Constants.PATENT_NAME_EN_FIELD);
						log.info(columnCount);
						row.createCell(columnCount).setCellValue(patent.getPatent_name_en());
						columnCount++;
						break;
					case Constants.PATENT_COUNTRY_FIELD:
						log.info("PATENT_COUNTRY_FIELD: " + Constants.PATENT_COUNTRY_FIELD);
						log.info(columnCount);
						String countryEN = patent.getPatent_appl_country();
						String countryCN = "";
						if (countryEN.equals("tw")) {
							countryCN = "中華民國";
						}
						if (countryEN.equals("us")) {
							countryCN = "美國";
						}
						if (countryEN.equals("cn")) {
							countryCN = "中國大陸";
						}
						row.createCell(columnCount).setCellValue(countryCN);
						columnCount++;
						break;
					case Constants.PATENT_STATUS_FIELD:
						log.info("PATENT_STATUS_FIELD: " + Constants.PATENT_STATUS_FIELD);
						log.info(columnCount);
						if (patent.getListPatentStatus() != null) {
							String statusStr = "";
							int index = 0;
							for (PatentStatus patentStatus : patent.getListPatentStatus()) {
								Status status = patentStatus.getStatus();
								if (!StringUtils.isNULL(status.getStatus_desc())) {
									statusStr += status.getStatus_desc();
								}
								if (patentStatus.getCreate_date() != null) {
									statusStr += "_" + DateUtils.getDashFormatDate(patentStatus.getCreate_date());
								}
								if (index < patent.getListPatentStatus().size() - 1) {
									statusStr += "、";
								}
								index++;
							}
							row.createCell(columnCount).setCellValue(statusStr);
						}
						columnCount++;
						break;
					case Constants.APPLIANT_NAME_FIELD:
						log.info("APPLIANT_NAME_FIELD: " + Constants.APPLIANT_NAME_FIELD);
						log.info(columnCount);
						if (patent.getListApplicant() != null) {
							String applicantStr = "";
							int lastIndex = patent.getListApplicant().size() - 1;
							for (Applicant appl : patent.getListApplicant()) {
								if (appl.getApplicant_id().equals(patent.getListApplicant().get(lastIndex).getApplicant_id())) {
									if (!StringUtils.isNULL(appl.getApplicant_name())) {
										applicantStr += appl.getApplicant_name();
									}
									if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
										if (!StringUtils.isNULL(applicantStr) && !StringUtils.isNULL(appl.getApplicant_name())) {
											applicantStr += "_";
										}
										applicantStr += appl.getApplicant_name_en();
									}
								} else {
									if (!StringUtils.isNULL(appl.getApplicant_name())) {
										applicantStr += appl.getApplicant_name();
									}
									if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
										if (!StringUtils.isNULL(applicantStr) && !StringUtils.isNULL(appl.getApplicant_name())) {
											applicantStr += "_";
										}
										applicantStr += appl.getApplicant_name_en();
									}
									applicantStr += "、";
								}
							}
							row.createCell(columnCount).setCellValue(applicantStr);
						}
						columnCount++;
						break;
					case Constants.ASSIGNEE_NAME_FIELD:
						log.info("ASSIGNEE_NAME_FIELD: " + Constants.ASSIGNEE_NAME_FIELD);
						log.info(columnCount);
						if (patent.getListAssignee() != null) {
							String assigneeStr = "";
							int lastIndexAs = patent.getListAssignee().size() - 1;
							for (Assignee assignee : patent.getListAssignee()) {
								if (assignee.getAssignee_id().equals(patent.getListAssignee().get(lastIndexAs).getAssignee_id())) {
									if (!StringUtils.isNULL(assignee.getAssignee_name())) {
										assigneeStr += assignee.getAssignee_name();
									}
									if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
										if (!StringUtils.isNULL(assigneeStr) && !StringUtils.isNULL(assignee.getAssignee_name())) {
											assigneeStr += "_";
										}
										assigneeStr += assignee.getAssignee_name_en();
									}
								} else {
									if (!StringUtils.isNULL(assignee.getAssignee_name())) {
										assigneeStr += assignee.getAssignee_name();
									}
									if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
										if (!StringUtils.isNULL(assigneeStr) && !StringUtils.isNULL(assignee.getAssignee_name())) {
											assigneeStr += "_";
										}
										assigneeStr += assignee.getAssignee_name_en();
									}
									assigneeStr += "、";
								}
							}
							row.createCell(columnCount).setCellValue(assigneeStr);
						}
						columnCount++;
						break;
					case Constants.INVENTOR_NAME_FIELD:
						log.info("INVENTOR_NAME_FIELD: " + Constants.INVENTOR_NAME_FIELD);
						log.info(columnCount);
						if (patent.getListInventor() != null) {
							String inventorStr = "";
							int lastIndexIv = patent.getListInventor().size() - 1;
							for (Inventor inv : patent.getListInventor()) {
								if (inv.getInventor_id().equals(patent.getListInventor().get(lastIndexIv).getInventor_id())) {
									if (!StringUtils.isNULL(inv.getInventor_name())) {
										inventorStr += inv.getInventor_name();
									}
									if (!StringUtils.isNULL(inv.getInventor_name_en())) {
										if (!StringUtils.isNULL(inventorStr) && !StringUtils.isNULL(inv.getInventor_name())) {
											inventorStr += "_";
										}
										inventorStr += inv.getInventor_name_en();
									}
								} else {
									if (!StringUtils.isNULL(inv.getInventor_name())) {
										inventorStr += inv.getInventor_name();
									}
									if (!StringUtils.isNULL(inv.getInventor_name_en())) {
										if (!StringUtils.isNULL(inventorStr) && !StringUtils.isNULL(inv.getInventor_name())) {
											inventorStr += "_";
										}
										inventorStr += inv.getInventor_name_en();
									}
									inventorStr += "、";
								}
							}

							row.createCell(columnCount).setCellValue(inventorStr);
						}
						columnCount++;
						break;
					case Constants.PATENT_APPL_DATE_FIELD:
						log.info("PATENT_APPL_DATE_FIELD: " + Constants.PATENT_APPL_DATE_FIELD);
						log.info(columnCount);
						if (patent.getPatent_appl_date() != null) {
							Cell dateOfApplCell = row.createCell(columnCount);
							dateOfApplCell.setCellValue(patent.getPatent_appl_date());
							dateOfApplCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.PATENT_APPL_NO_FIELD: //
						log.info("PATENT_APPL_NO_FIELD: " + Constants.PATENT_APPL_NO_FIELD);
						log.info(columnCount);
						String applWithoutAt;
						if (StringUtils.isNULL(patent.getPatent_appl_no())) {
							applWithoutAt = "";
						} else {
							applWithoutAt = StringUtils.getApplNoWithoutAt(patent.getPatent_appl_no());
						}
						row.createCell(columnCount).setCellValue(applWithoutAt);
						columnCount++;
						break;
					case Constants.PATENT_PUBLISH_DATE_FIELD:
						log.info("PATENT_PUBLISH_DATE_FIELD: " + Constants.PATENT_PUBLISH_DATE_FIELD);
						log.info(columnCount);
						if (patent.getPatent_publish_date() != null) {
							Cell dateOfPublishCell = row.createCell(columnCount);
							dateOfPublishCell.setCellValue(patent.getPatent_publish_date());
							dateOfPublishCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.PATENT_PUBLISH_NO_FIELD:
						log.info("PATENT_PUBLISH_NO_FIELD: " + Constants.PATENT_PUBLISH_NO_FIELD);
						log.info(columnCount);
						row.createCell(columnCount).setCellValue(patent.getPatent_publish_no());
						columnCount++;
						break;
					case Constants.PATENT_NOTICE_DATE_FIELD:
						log.info("PATENT_NOTICE_DATE_FIELD: " + Constants.PATENT_NOTICE_DATE_FIELD);
						log.info(columnCount);
						if (patent.getPatent_notice_date() != null) {
							Cell dateOfNoticeCell = row.createCell(columnCount);
							dateOfNoticeCell.setCellValue(patent.getPatent_notice_date());
							dateOfNoticeCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.PATENT_NOTICE_NO_FIELD:
						log.info("PATENT_NOTICE_NO_FIELD: " + Constants.PATENT_NOTICE_NO_FIELD);
						log.info(columnCount);
						row.createCell(columnCount).setCellValue(patent.getPatent_notice_no());
						columnCount++;
						break;
					case Constants.PATENT_NO_FIELD:
						log.info("PATENT_NO_FIELD: " + Constants.PATENT_NO_FIELD);
						log.info(columnCount);
						row.createCell(columnCount).setCellValue(patent.getPatent_no());
						columnCount++;
						break;
					case Constants.ANNUITY_DATE: // 年費有效日期
						log.info("ANNUITY_DATE: " + Constants.ANNUITY_DATE);
						log.info(columnCount);
						if (patent.getPatent_charge_expire_date() != null) {
							Cell dateOfChargeDateCell = row.createCell(columnCount);
							dateOfChargeDateCell.setCellValue(patent.getPatent_charge_expire_date());
							dateOfChargeDateCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.ANNUITY_CHARGE_YEAR: // 年費有效年次
						log.info("ANNUITY_CHARGE_YEAR: " + Constants.ANNUITY_CHARGE_YEAR);
						log.info(columnCount);
						row.createCell(columnCount).setCellValue(patent.getPatent_charge_duration_year());
						columnCount++;
						break;
					case Constants.PATENT_BDATE: // 專利起始日
						log.info("PATENT_BDATE: " + Constants.PATENT_BDATE);
						log.info(columnCount);
						if (patent.getPatent_bdate() != null) {
							Cell dateOfBDateCell = row.createCell(columnCount);
							dateOfBDateCell.setCellValue(patent.getPatent_bdate());
							dateOfBDateCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.PATENT_EDATE: // 專利截止日
						log.info("PATENT_EDATE: " + Constants.PATENT_EDATE);
						log.info(columnCount);
						if (patent.getPatent_edate() != null) {
							Cell dateOfEDateCell = row.createCell(columnCount);
							dateOfEDateCell.setCellValue(patent.getPatent_edate());
							dateOfEDateCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.PATENT_CANCEL_DATE: // 專利取消日期
						log.info("PATENT_CANCEL_DATE: " + Constants.PATENT_CANCEL_DATE);
						log.info(columnCount);
						if (patent.getPatent_cancel_date() != null) {
							Cell dateOfCancelDateCell = row.createCell(columnCount);
							dateOfCancelDateCell.setCellValue(patent.getPatent_cancel_date());
							dateOfCancelDateCell.setCellStyle(dateCellStyle);
						}
						columnCount++;
						break;
					case Constants.PATENT_ABSTRACT: // 專利摘要
						log.info("PATENT_ABSTRACT: " + Constants.PATENT_ABSTRACT);
						log.info(columnCount);
						if (patent.getPatentAbstract() != null) {
							String abs = patent.getPatentAbstract().getContext_abstract();
							if (abs.length() > 5000) {
								abs = abs.substring(0, 5000) + "...(完整內容請由官方專利局取得)";
							}
							row.createCell(columnCount).setCellValue(abs.trim());
						}
						columnCount++;
						break;
					case Constants.PATENT_CLAIM: // 專利範圍
						log.info("PATENT_CLAIM: " + Constants.PATENT_CLAIM);
						log.info(columnCount);
						if (patent.getPatentClaim() != null) {
							String claim = patent.getPatentClaim().getContext_claim();
							if (claim.length() > 5000) {
								claim = claim.substring(0, 5000) + "...(完整內容請由官方專利局取得)";
							}
							row.createCell(columnCount).setCellValue(claim.trim());
						}
						columnCount++;
						break;
					case Constants.PATENT_DESC: // 專利描述
						log.info("PATENT_DESC: " + Constants.PATENT_DESC);
						log.info(columnCount);
						if (patent.getPatentDesc() != null) {
							String desc = patent.getPatentDesc().getContext_desc();
							if (desc.length() > 5000) {
								desc = desc.substring(0, 5000) + "...(完整內容請由官方專利局取得)";
							}
							row.createCell(columnCount).setCellValue(desc.trim());
						}
						columnCount++;
						break;
					case Constants.PATENT_IPC: // 國際專利分類
						log.info("PATENT_IPC: " + Constants.PATENT_IPC);
						log.info(columnCount);
						if (patent.getListIPC() != null) {
							String ipcStr = "";
							int lastIndexIpc = patent.getListIPC().size() - 1;
							for (IPCClass ipc : patent.getListIPC()) {
								if (ipc != null) {
									if (ipc.getIpc_class_id().equals(
											patent.getListIPC().get(lastIndexIpc).getIpc_class_id())) {
										if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
											ipcStr += ipc.getIpc_class_id();
										}
										if (!StringUtils.isNULL(ipc.getIpc_version())) {
											ipcStr += "(" + ipc.getIpc_version() + ")";
										}
									} else {
										if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
											ipcStr += ipc.getIpc_class_id();
										}
										if (!StringUtils.isNULL(ipc.getIpc_version())) {
											ipcStr += "(" + ipc.getIpc_version() + ")";
										}
										ipcStr += "\n";
									}
								}
							}
							row.createCell(columnCount).setCellValue(ipcStr);
						}
						columnCount++;
						break;
					case Constants.SCHOOL_DEPARTMENT_FIELD: // 學校科系
						log.info("SCHOOL_DEPARTMENT_FIELD: " + Constants.SCHOOL_DEPARTMENT_FIELD);
						log.info(columnCount);
						if (patent.getListDepartment() != null) {
							String departmentStr = "";
							int lastIndexDe = patent.getListDepartment().size() - 1;
							for (Department dep : patent.getListDepartment()) {
								if (dep.getDepartment_id().equals(patent.getListDepartment().get(lastIndexDe).getDepartment_id())) {
									if (!StringUtils.isNULL(dep.getDepartment_name())) {
										departmentStr += dep.getDepartment_name();
									}
									if (!StringUtils.isNULL(dep.getDepartment_name_en())) {
										if (!StringUtils.isNULL(departmentStr) && !StringUtils.isNULL(dep.getDepartment_name())) {
											departmentStr += "_";
										}
										departmentStr += dep.getDepartment_name_en();
									}
								} else {
									if (!StringUtils.isNULL(dep.getDepartment_name())) {
										departmentStr += dep.getDepartment_name();
									}
									if (!StringUtils.isNULL(dep.getDepartment_name_en())) {
										if (!StringUtils.isNULL(departmentStr) && !StringUtils.isNULL(dep.getDepartment_name())) {
											departmentStr += "_";
										}
										departmentStr += dep.getDepartment_name_en();
									}
									departmentStr += "、";
								}
							}
							row.createCell(columnCount).setCellValue(departmentStr);
						}
						columnCount++;
						break;
					case Constants.SCHOOL_NO_FIELD: // 學校編號
						log.info("SCHOOL_NO_FIELD: " + Constants.SCHOOL_NO_FIELD);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getBusiness_num());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_APPL_YEAR_FIELD: // 申請年度
						log.info("SCHOOL_APPL_YEAR_FIELD: " + Constants.SCHOOL_APPL_YEAR_FIELD);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_appl_year());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_SUBSIDY_UNIT: // 補助單位
						log.info("SCHOOL_SUBSIDY_UNIT: " + Constants.SCHOOL_SUBSIDY_UNIT);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_subsidy_unit());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_SUBSIDY_NO: // 補助編號
						log.info("SCHOOL_SUBSIDY_NO: " + Constants.SCHOOL_SUBSIDY_NO);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_subsidy_num());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_SUBSIDY_PLAN: // 補助計畫名稱
						log.info("SCHOOL_SUBSIDY_PLAN: " + Constants.SCHOOL_SUBSIDY_PLAN);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_subsidy_plan());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_AGENT: // 事務所
						log.info("SCHOOL_AGENT: " + Constants.SCHOOL_AGENT);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_agent());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_AGENT_NO: // 事務所編號
						log.info("SCHOOL_AGENT_NO: " + Constants.SCHOOL_AGENT_NO);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_agent_num());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_MEMO_FIELD: // 學校備註
						log.info("SCHOOL_MEMO_FIELD: " + Constants.SCHOOL_MEMO_FIELD);
                        log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_memo());
								}
							}
						}
						columnCount++;
						break;
					case Constants.SCHOOL_OTHER_INFO: // 其他資訊
						log.info("SCHOOL_OTHER_INFO: " + Constants.SCHOOL_OTHER_INFO);
						log.info(columnCount);
						if (patent.getListExtension() != null) {
							for (PatentExtension extension : patent.getListExtension()) {
								if (businessId.equals(extension.getBusiness_id())) {
									row.createCell(columnCount).setCellValue(extension.getExtension_other_information());
								}
							}
						}
						columnCount++;
						break;
					default:
						break;
				}
			}
		}

		if (businessId != null) {
			// Resize all columns to fit the content size
			for (int i = 0; i < school_columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
		} else {
			// Resize all columns to fit the content size
			for (int i = 0; i < plateform_columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
		}
		try {
			// Write the output to a file
			fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);
			fileOut.close();

			// Closing the workbook
			workbook.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}


		return new ByteArrayInputStream(fileOut.toByteArray());
	}
	
	public static final Workbook file2Workbook(FileInputStream fileInStream,String fileName) throws IOException {

		
		String extensionName = FilenameUtils.getExtension(fileName);
		Workbook workbook = null;
        boolean is_support = false;
		if(!StringUtils.isNULL(extensionName)) {
			if("xlsx".equalsIgnoreCase(extensionName)) {
				workbook = new XSSFWorkbook(fileInStream);
				is_support =true;
			}else if("xls".equalsIgnoreCase(extensionName)) {
				workbook = new HSSFWorkbook(fileInStream);
				is_support =true;
			}else {
				
			}
		}
		return workbook;
	}
	
	
	public static final Map<String, Integer> readExcelTitle(Workbook book) throws IOException {
		Map<String, Integer> titleMap = new HashMap<String, Integer>();

		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);


		for (Cell cell : row) {
			log.info("cell :"+cell.getColumnIndex()+" /"+cell.getStringCellValue());
			titleMap.put(cell.getStringCellValue(), cell.getColumnIndex());
			
		}
		
		LinkedHashMap<String, Integer> sortMap = sortHashMapByValues(titleMap);

		return sortMap;
	}
	
	public static LinkedHashMap<String, Integer> sortHashMapByValues(
	        Map<String, Integer> titleMap) {
	    List<String> mapKeys = new ArrayList<>(titleMap.keySet());
	    List<Integer> mapValues = new ArrayList<>(titleMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    LinkedHashMap<String, Integer> sortedMap =
	        new LinkedHashMap<>();

	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	    	Integer val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	        	String key = keyIt.next();
	        	Integer comp1 = titleMap.get(key);
	        	Integer comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}

}
