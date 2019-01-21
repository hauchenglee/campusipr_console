package biz.mercue.campusipr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;

public class ExcelUtils {
	
	private static Logger log = Logger.getLogger(ExcelUtils.class.getName());
	
	private static String[] school_columns = {Constants.EXCEL_COLUMN_BUSSINESS,
			Constants.EXCEL_COLUMN_SCHOOL_NO,
			Constants.EXCEL_COLUMN_PATENT_NAME, Constants.EXCEL_COLUMN_PATENT_NAME_EN, 
			Constants.EXCEL_COLUMN_APPLICANT_COUNTRY, Constants.EXCEL_COLUMN_PATENT_STATUS,
			Constants.EXCEL_COLUMN_APPLICANT, Constants.EXCEL_COLUMN_ASSIGNEE,
			Constants.EXCEL_COLUMN_INVENTOR, Constants.EXCEL_COLUMN_APPLICATION_DATE,
			Constants.EXCEL_COLUMN_APPLICATION_NO, Constants.EXCEL_COLUMN_PUBLIC_DATE,
			Constants.EXCEL_COLUMN_PUBLIC_NO, Constants.EXCEL_COLUMN_NOTICE_DATE,
			Constants.EXCEL_COLUMN_NOTICE_NO, Constants.EXCEL_COLUMN_PATENT_NO,
			Constants.EXCEL_COLUMN_PAY_VAILD_DATE,
			Constants.EXCEL_COLUMN_PAY_EXPIRE_YEAR, Constants.EXCEL_COLUMN_PATENT_GEGIN_DATE,
			Constants.EXCEL_COLUMN_PATENT_END_DATE, Constants.EXCEL_COLUMN_PATENT_CANEL_DATE,
			Constants.EXCEL_COLUMN_PATENT_EXPIRE_DATE, Constants.EXCEL_COLUMN_PATENT_VAILD_DATE,
			Constants.EXCEL_COLUMN_PATENT_ABSTRACT, Constants.EXCEL_COLUMN_PATENT_CLAIM,
			Constants.EXCEL_COLUMN_PATENT_CLASSIFICATION, Constants.EXCEL_COLUMN_PATENT_DESC,
			Constants.EXCEL_COLUMN_PATENT_BUILD_DATE, Constants.EXCEL_COLUMN_PATENT_BUILD_PERSON,
			Constants.EXCEL_COLUMN_SCHOOL_APPL_YEAR, Constants.EXCEL_COLUMN_SCHOOL_NOTE};
	
	private static String[] plateform_columns = {Constants.EXCEL_COLUMN_FILE_NO,
			Constants.EXCEL_COLUMN_BUSSINESS, Constants.EXCEL_COLUMN_SCHOOL_NO, 
			Constants.EXCEL_COLUMN_PATENT_NAME, Constants.EXCEL_COLUMN_PATENT_NAME_EN, 
			Constants.EXCEL_COLUMN_APPLICANT_COUNTRY, Constants.EXCEL_COLUMN_PATENT_STATUS,
			Constants.EXCEL_COLUMN_APPLICANT, Constants.EXCEL_COLUMN_ASSIGNEE,
			Constants.EXCEL_COLUMN_INVENTOR, Constants.EXCEL_COLUMN_APPLICATION_DATE,
			Constants.EXCEL_COLUMN_APPLICATION_NO, Constants.EXCEL_COLUMN_PUBLIC_DATE,
			Constants.EXCEL_COLUMN_PUBLIC_NO, Constants.EXCEL_COLUMN_NOTICE_DATE,
			Constants.EXCEL_COLUMN_NOTICE_NO, Constants.EXCEL_COLUMN_PATENT_NO,
			Constants.EXCEL_COLUMN_PAY_VAILD_DATE,
			Constants.EXCEL_COLUMN_PAY_EXPIRE_YEAR, Constants.EXCEL_COLUMN_PATENT_GEGIN_DATE,
			Constants.EXCEL_COLUMN_PATENT_END_DATE, Constants.EXCEL_COLUMN_PATENT_CANEL_DATE,
			Constants.EXCEL_COLUMN_PATENT_EXPIRE_DATE, Constants.EXCEL_COLUMN_PATENT_VAILD_DATE,
			Constants.EXCEL_COLUMN_PATENT_ABSTRACT, Constants.EXCEL_COLUMN_PATENT_CLAIM,
			Constants.EXCEL_COLUMN_PATENT_CLASSIFICATION, Constants.EXCEL_COLUMN_PATENT_DESC,
			Constants.EXCEL_COLUMN_PATENT_BUILD_DATE, Constants.EXCEL_COLUMN_PATENT_BUILD_PERSON};
	
	public static final ByteArrayInputStream Patent2Excel(List<Patent> list, String BussinessId) {
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
	        headerFont.setFontHeightInPoints((short) 14);
	        headerFont.setColor(IndexedColors.RED.getIndex());
	
	        // Create a CellStyle with the font
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);
	
	        // Create a Row
	        Row headerRow = sheet.createRow(0);
	
	        // Create cells
	        if (BussinessId != null) {
		        for(int i = 0; i < school_columns.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(school_columns[i]);
		            cell.setCellStyle(headerCellStyle);
		        }
	        } else {
	        	for(int i = 0; i < plateform_columns.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(plateform_columns[i]);
		            cell.setCellStyle(headerCellStyle);
		        }
	        }
	
	        // Create Cell Style for formatting Date
	        CellStyle dateCellStyle = workbook.createCellStyle();
	        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
	
	        // Create Other rows and cells with employees data
	        int rowNum = 1;
	        for(Patent patent: list) {
	            Row row = sheet.createRow(rowNum++);
	            if (BussinessId != null) {
		
	            	if (patent.getListBusiness() != null) {
		            	for (Business bussiness:patent.getListBusiness()) {
		            		if (BussinessId.equals(bussiness.getBusiness_id())) {
		            			row.createCell(0).setCellValue(bussiness.getBusiness_name());
		            		}
		            	}
		            }
	            	
	            	if (patent.getListExtension() != null) {
	            		for (PatentExtension ext:patent.getListExtension()) {
	            			if (BussinessId.equals(ext.getBusiness_id())) {
	            				row.createCell(1).setCellValue(ext.getBusiness_num());
	            			}
	            		}
	            	}
		            
		            row.createCell(2).setCellValue(patent.getPatent_name());
		            
		            row.createCell(3).setCellValue(patent.getPatent_name_en());
		            
		            row.createCell(4).setCellValue(patent.getPatent_appl_country());
		            
		            if (patent.getListStatus() != null) {
		            	String statusStr = "";
		            	int index = 0;
		            	for (Status status:patent.getListStatus()) {
		            		if (status.getPatentStatus() != null) {
			            		if (status.getPatentStatus().getCreate_date() != null) {
		            				statusStr += DateUtils.getDashFormatDateTime(status.getPatentStatus().getCreate_date());
		            			}
		            		}
		            		if (!StringUtils.isNULL(status.getEvent_code())) {
		            			if (!StringUtils.isNULL(statusStr)) {
		            				statusStr += "_";
		            			}
		            			statusStr += status.getEvent_code();
			            	}
		            		if (!StringUtils.isNULL(status.getEvent_code_desc())) {
		            			if (!StringUtils.isNULL(statusStr)) {
		            				statusStr += "_";
		            			}
		            			statusStr += "_"+status.getEvent_code_desc();
			            	}
			            	if (index < patent.getListStatus().size() - 1) {
				            	statusStr += "\n";
			            	}
		            		
		            		index++;
		            	}
		            	row.createCell(5).setCellValue(statusStr);
		            }
		            
		            if (patent.getListApplicant() != null) {
		            	String applicantStr = "";
			            int lastIndex = patent.getListApplicant().size() - 1;
			            for (Applicant appl:patent.getListApplicant()) {
			            	if (appl.getApplicant_id().equals(
			            			patent.getListApplicant().get(lastIndex).getApplicant_id())) {
			            		if (!StringUtils.isNULL(appl.getApplicant_name())) {
			            			applicantStr += appl.getApplicant_name();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_name_en();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address_en();
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(appl.getApplicant_name())) {
			            			applicantStr += appl.getApplicant_name();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_name_en();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address_en();
			            		}
			            		applicantStr += "\n";
			            	}
			            }
			            row.createCell(6).setCellValue(applicantStr);
		            }
		            
		            if (patent.getListAssignee() != null) {
		            	String assigneeStr = "";
			            int lastIndexAs = patent.getListAssignee().size() - 1;
			            for (Assignee assignee:patent.getListAssignee()) {
			            	if (assignee.getAssignee_id().equals(
			            			patent.getListAssignee().get(lastIndexAs).getAssignee_id())) {
			            		if (!StringUtils.isNULL(assignee.getAssignee_name())) {
			            			assigneeStr += assignee.getAssignee_name();
			            		}
			            		if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
			            			if (!StringUtils.isNULL(assigneeStr)) {
			            				assigneeStr += "_";
			            			}
			            			assigneeStr += assignee.getAssignee_name_en();
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(assignee.getAssignee_name())) {
			            			assigneeStr += assignee.getAssignee_name();
			            		}
			            		if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
			            			if (!StringUtils.isNULL(assigneeStr)) {
			            				assigneeStr += "_";
			            			}
			            			assigneeStr += assignee.getAssignee_name_en();
			            		}
			            		assigneeStr += "\n";
			            	}
			            }

			            row.createCell(7).setCellValue(assigneeStr);
		            }
		           
		            if (patent.getListAssignee() != null) {
		            	String inventorStr = "";
				        int lastIndexIv = patent.getListInventor().size() - 1;
			            for (Inventor inv:patent.getListInventor()) {
			            	if (inv.getInventor_id().equals(
			            			patent.getListInventor().get(lastIndexIv).getInventor_id())) {
			            		if (!StringUtils.isNULL(inv.getInventor_name())) {
			            			inventorStr += inv.getInventor_name();
			            		}
			            		if (!StringUtils.isNULL(inv.getInventor_name_en())) {
			            			if (!StringUtils.isNULL(inventorStr)) {
			            				inventorStr += "_";
			            			}
			            			inventorStr += inv.getInventor_name_en();
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(inv.getInventor_name())) {
			            			inventorStr += inv.getInventor_name();
			            		}
			            		if (!StringUtils.isNULL(inv.getInventor_name_en())) {
			            			if (!StringUtils.isNULL(inventorStr)) {
			            				inventorStr += "_";
			            			}
			            			inventorStr += inv.getInventor_name_en();
			            		}
			            		inventorStr += "\n";
			            	}
			            }

			            row.createCell(8).setCellValue(inventorStr);
		            }
		            
		            if (patent.getPatent_appl_date() != null) {
			            Cell dateOfApplCell = row.createCell(9);
			            dateOfApplCell.setCellValue(patent.getPatent_appl_date());
			            dateOfApplCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(10).setCellValue(patent.getPatent_appl_no());
		            
		            if (patent.getPatent_notice_date() != null) {
			            Cell dateOfNoticeCell = row.createCell(11);
			            dateOfNoticeCell.setCellValue(patent.getPatent_notice_date());
			            dateOfNoticeCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(12).setCellValue(patent.getPatent_notice_no());
		
		            if (patent.getPatent_publish_date() != null) {
			            Cell dateOfPublishCell = row.createCell(13);
			            dateOfPublishCell.setCellValue(patent.getPatent_publish_date());
			            dateOfPublishCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(14).setCellValue(patent.getPatent_notice_no());
		            
		            row.createCell(15).setCellValue(patent.getPatent_no());
		            
		            if (patent.getPatent_charge_expire_date() != null) {
			            Cell dateOfChargeDateCell = row.createCell(16);
			            dateOfChargeDateCell.setCellValue(patent.getPatent_charge_expire_date());
			            dateOfChargeDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(17).setCellValue(patent.getPatent_charge_duration_year());
		            
		            if (patent.getPatent_bdate() != null) {
			            Cell dateOfBDateCell = row.createCell(18);
			            dateOfBDateCell.setCellValue(patent.getPatent_bdate());
			            dateOfBDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            if (patent.getPatent_edate() != null) {
			            Cell dateOfEDateCell = row.createCell(19);
			            dateOfEDateCell.setCellValue(patent.getPatent_edate());
			            dateOfEDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            if (patent.getPatent_cancel_date() != null) {
			            Cell dateOfCancelDateCell = row.createCell(20);
			            dateOfCancelDateCell.setCellValue(patent.getPatent_cancel_date());
			            dateOfCancelDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            if (patent.getPatentAbstract() != null) {
			            String abs = patent.getPatentAbstract().getContext_abstract();
			            if (abs.length() > 500) {
			            	abs = abs.substring(0, 500) + "...";
			            }
			            row.createCell(23).setCellValue(abs);
		            }
		            
		            if (patent.getPatentClaim() != null) {
			            String claim = patent.getPatentClaim().getContext_claim();
			            if (claim.length() > 500) {
			            	claim = claim.substring(0, 500) + "...";
			            }
			            row.createCell(24).setCellValue(claim);
		            }
		            
		            if (patent.getListIPC()!= null) {
		            	String ipcStr = "";
				        int lastIndexIpc = patent.getListIPC().size() - 1;
			            for (IPCClass ipc:patent.getListIPC()) {
			            	if (ipc.getIpc_class_id().equals(
			            			patent.getListIPC().get(lastIndexIpc).getIpc_class_id())) {
			            		if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
			            			ipcStr += ipc.getIpc_class_id();
			            		}
			            		if (!StringUtils.isNULL(ipc.getIpc_version())) {
			            			ipcStr += "("+ipc.getIpc_version()+")";
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
			            			ipcStr += ipc.getIpc_class_id();
			            		}
			            		if (!StringUtils.isNULL(ipc.getIpc_version())) {
			            			ipcStr += "("+ipc.getIpc_version()+")";
			            		}
			            		ipcStr += "\n";
			            	}
			            }
			            row.createCell(25).setCellValue(ipcStr);
		            }
		            
		            if (patent.getPatentDesc() != null) {
			            String desc = patent.getPatentDesc().getContext_desc();
			            if (desc.length() > 500) {
			            	desc = desc.substring(0, 500) + "...";
			            }
			            row.createCell(26).setCellValue(desc);
		            }
		            
		            Cell dateOfCreateDateCell = row.createCell(27);
		            dateOfCreateDateCell.setCellValue(new Date());
		            dateOfCreateDateCell.setCellStyle(dateCellStyle);
		            
		            if (patent.getListHistory() != null) {
		            	for (PatentEditHistory peh:patent.getListHistory()) {
		            		if ("create".equals(peh.getHistory_data())) {
		            			if (peh.getAdmin() != null) {
		            				row.createCell(28).setCellValue(peh.getAdmin().getAdmin_name());
		            			}
		            		}
		            	}
		            }
		            
		            if (patent.getListExtension() != null) {
	            		for (PatentExtension ext:patent.getListExtension()) {
	            			if (BussinessId.equals(ext.getBusiness_id())) {
	            				row.createCell(29).setCellValue(ext.getExtension_appl_year());
	            				row.createCell(30).setCellValue(ext.getExtension_memo());
	            			}
	            		}
	            	}
	            } else {
	            	
	            	if (patent.getListExtension() != null) {
	            		String extFilNo = "";
	            		String extBussinessNo = "";
	            		int lastIndexExt = patent.getListExtension().size() - 1;
	            		for (PatentExtension ext:patent.getListExtension()) {
	            			if (ext.getExtension_id().equals(
			            			patent.getListExtension().get(lastIndexExt).getExtension_id())) {
	            				extFilNo += ext.getExtension_file_num();
	            				extBussinessNo += ext.getBusiness_num();
	            			} else {
	            				extFilNo += ext.getExtension_file_num() + "\n";
	            				extBussinessNo += ext.getBusiness_num() + "\n";
	            			}
	            		}
	            		row.createCell(0).setCellValue(extFilNo);
	            		row.createCell(2).setCellValue(extBussinessNo);
	            	}
		            
	            	if (patent.getListBusiness() != null) {
	            		String bussinessStr = "";
	            		int lastIndexBussiness = patent.getListBusiness().size() - 1;
		            	for (Business bussiness:patent.getListBusiness()) {
		            		if (bussiness.getBusiness_id().equals(
			            			patent.getListBusiness().get(lastIndexBussiness).getBusiness_id())) {
		            			bussinessStr += bussiness.getBusiness_name();
		            		} else {
		            			bussinessStr += bussiness.getBusiness_name() + "\n";
		            		}
		            	}
		            	row.createCell(1).setCellValue(bussinessStr);
		            }
	            	
		            row.createCell(3).setCellValue(patent.getPatent_name());
		            
		            row.createCell(4).setCellValue(patent.getPatent_name_en());
		            
		            row.createCell(5).setCellValue(patent.getPatent_appl_country());
		            
		            if (patent.getListStatus() != null) {
		            	String statusStr = "";
		            	int index = 0;
		            	for (Status status:patent.getListStatus()) {
		            		if (status.getPatentStatus() != null) {
			            		if (status.getPatentStatus().getCreate_date() != null) {
		            				statusStr += DateUtils.getDashFormatDateTime(status.getPatentStatus().getCreate_date());
		            			}
		            		}
		            		if (!StringUtils.isNULL(status.getEvent_code())) {
		            			if (!StringUtils.isNULL(statusStr)) {
		            				statusStr += "_";
		            			}
		            			statusStr += status.getEvent_code();
			            	}
		            		if (!StringUtils.isNULL(status.getEvent_code_desc())) {
		            			if (!StringUtils.isNULL(statusStr)) {
		            				statusStr += "_";
		            			}
		            			statusStr += status.getEvent_code_desc();
			            	}
			            	if (index < patent.getListStatus().size() - 1) {
				            	statusStr += "\n";
			            	}
		            		
		            		index++;
		            	}
		            	row.createCell(6).setCellValue(statusStr);
		            }
		            
		            if (patent.getListApplicant() != null) {
		            	String applicantStr = "";
			            int lastIndex = patent.getListApplicant().size() - 1;
			            for (Applicant appl:patent.getListApplicant()) {
			            	if (appl.getApplicant_id().equals(
			            			patent.getListApplicant().get(lastIndex).getApplicant_id())) {
			            		if (!StringUtils.isNULL(appl.getApplicant_name())) {
			            			applicantStr += appl.getApplicant_name();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_name_en();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address_en();
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(appl.getApplicant_name())) {
			            			applicantStr += appl.getApplicant_name();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_name_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_name_en();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address();
			            		}
			            		if (!StringUtils.isNULL(appl.getApplicant_address_en())) {
			            			if (!StringUtils.isNULL(applicantStr)) {
			            				applicantStr += "_";
			            			}
			            			applicantStr += appl.getApplicant_address_en();
			            		}
			            		applicantStr += "\n";
			            	}
			            }

			            row.createCell(7).setCellValue(applicantStr);
		            }
		            
		            if (patent.getListAssignee() != null) {
			            String assigneeStr = "";
			            int lastIndexAs = patent.getListAssignee().size() - 1;
			            for (Assignee assignee:patent.getListAssignee()) {
			            	if (assignee.getAssignee_id().equals(
			            			patent.getListAssignee().get(lastIndexAs).getAssignee_id())) {
			            		if (!StringUtils.isNULL(assignee.getAssignee_name())) {
			            			assigneeStr += assignee.getAssignee_name();
			            		}
			            		if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
			            			if (!StringUtils.isNULL(assigneeStr)) {
			            				assigneeStr += "_";
			            			}
			            			assigneeStr += assignee.getAssignee_name_en();
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(assignee.getAssignee_name())) {
			            			assigneeStr += assignee.getAssignee_name();
			            		}
			            		if (!StringUtils.isNULL(assignee.getAssignee_name_en())) {
			            			if (!StringUtils.isNULL(assigneeStr)) {
			            				assigneeStr += "_";
			            			}
			            			assigneeStr += assignee.getAssignee_name_en();
			            		}
			            		assigneeStr += "\n";
			            	}
			            }

			            row.createCell(8).setCellValue(assigneeStr);
		            }
		            
		            if (patent.getListAssignee() != null) {
		            	String inventorStr = "";
			            int lastIndexIv = patent.getListInventor().size() - 1;
			            for (Inventor inv:patent.getListInventor()) {
			            	if (inv.getInventor_id().equals(
			            			patent.getListInventor().get(lastIndexIv).getInventor_id())) {
			            		if (!StringUtils.isNULL(inv.getInventor_name())) {
			            			inventorStr += inv.getInventor_name();
			            		}
			            		if (!StringUtils.isNULL(inv.getInventor_name_en())) {
			            			if (!StringUtils.isNULL(inventorStr)) {
			            				inventorStr += "_";
			            			}
			            			inventorStr += inv.getInventor_name_en();
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(inv.getInventor_name())) {
			            			inventorStr += inv.getInventor_name();
			            		}
			            		if (!StringUtils.isNULL(inv.getInventor_name_en())) {
			            			if (!StringUtils.isNULL(inventorStr)) {
			            				inventorStr += "_";
			            			}
			            			inventorStr += inv.getInventor_name_en();
			            		}
			            		inventorStr += "\n";
			            	}
			            }

			            row.createCell(9).setCellValue(inventorStr);
		            }
		            
		            if (patent.getPatent_appl_date() != null) {
			            Cell dateOfApplCell = row.createCell(10);
			            dateOfApplCell.setCellValue(patent.getPatent_appl_date());
			            dateOfApplCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(11).setCellValue(patent.getPatent_appl_no());
		            
		            if (patent.getPatent_notice_date() != null) {
			            Cell dateOfNoticeCell = row.createCell(12);
			            dateOfNoticeCell.setCellValue(patent.getPatent_notice_date());
			            dateOfNoticeCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(13).setCellValue(patent.getPatent_notice_no());
		            
		            if (patent.getPatent_publish_date() != null) {
			            Cell dateOfPublishCell = row.createCell(14);
			            dateOfPublishCell.setCellValue(patent.getPatent_publish_date());
			            dateOfPublishCell.setCellStyle(dateCellStyle);
		            }
		            
		            row.createCell(15).setCellValue(patent.getPatent_notice_no());
		            
		            row.createCell(16).setCellValue(patent.getPatent_no());
		            
		            if (patent.getPatent_charge_expire_date() != null) {
			            Cell dateOfChargeDateCell = row.createCell(17);
			            dateOfChargeDateCell.setCellValue(patent.getPatent_charge_expire_date());
			            dateOfChargeDateCell.setCellStyle(dateCellStyle);
		            }
		            row.createCell(18).setCellValue(patent.getPatent_charge_duration_year());
		            
		            if (patent.getPatent_bdate() != null) {
			            Cell dateOfBDateCell = row.createCell(19);
			            dateOfBDateCell.setCellValue(patent.getPatent_bdate());
			            dateOfBDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            if (patent.getPatent_edate() != null) {
			            Cell dateOfEDateCell = row.createCell(20);
			            dateOfEDateCell.setCellValue(patent.getPatent_edate());
			            dateOfEDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            if (patent.getPatent_cancel_date() != null) {
			            Cell dateOfCancelDateCell = row.createCell(21);
			            dateOfCancelDateCell.setCellValue(patent.getPatent_cancel_date());
			            dateOfCancelDateCell.setCellStyle(dateCellStyle);
		            }
		            
		            if (patent.getPatentAbstract() != null) {
			            String abs = patent.getPatentAbstract().getContext_abstract();
			            if (abs.length() > 500) {
			            	abs = abs.substring(0, 500) + "...";
			            }
			            row.createCell(24).setCellValue(abs);
		            }
		            
		            if (patent.getPatentClaim() != null) {
			            String claim = patent.getPatentClaim().getContext_claim();
			            if (claim.length() > 500) {
			            	claim = claim.substring(0, 500) + "...";
			            }
			            row.createCell(25).setCellValue(claim);
		            }
		            
		            if (patent.getListIPC()!= null) {
		            	String ipcStr = "";
				        int lastIndexIpc = patent.getListIPC().size() - 1;
			            for (IPCClass ipc:patent.getListIPC()) {
			            	if (ipc.getIpc_class_id().equals(
			            			patent.getListIPC().get(lastIndexIpc).getIpc_class_id())) {
			            		if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
			            			ipcStr += ipc.getIpc_class_id();
			            		}
			            		if (!StringUtils.isNULL(ipc.getIpc_version())) {
			            			ipcStr += "("+ipc.getIpc_version()+")";
			            		}
			            	} else {
			            		if (!StringUtils.isNULL(ipc.getIpc_class_id())) {
			            			ipcStr += ipc.getIpc_class_id();
			            		}
			            		if (!StringUtils.isNULL(ipc.getIpc_version())) {
			            			ipcStr += "("+ipc.getIpc_version()+")";
			            		}
			            		ipcStr += "\n";
			            	}
			            }
			            row.createCell(26).setCellValue(ipcStr);
		            }
		            
		            if (patent.getPatentDesc() != null) {
			            String desc = patent.getPatentDesc().getContext_desc();
			            if (desc.length() > 500) {
			            	desc = desc.substring(0, 500) + "...";
			            }
			            row.createCell(27).setCellValue(desc);
		            }
		            
		            Cell dateOfCreateDateCell = row.createCell(28);
		            dateOfCreateDateCell.setCellValue(new Date());
		            dateOfCreateDateCell.setCellStyle(dateCellStyle);
		            
		            if (patent.getListHistory() != null) {
		            	for (PatentEditHistory peh:patent.getListHistory()) {
		            		if ("create".equals(peh.getHistory_data())) {
		            			if (peh.getAdmin() != null) {
		            				row.createCell(29).setCellValue(peh.getAdmin().getAdmin_name());
		            			}
		            		}
		            	}
		            }
	            }
	        }
	
	        if (BussinessId != null) {
				// Resize all columns to fit the content size
		        for(int i = 0; i < school_columns.length; i++) {
		            sheet.autoSizeColumn(i);
		        }
	        } else {
	        	// Resize all columns to fit the content size
		        for(int i = 0; i < plateform_columns.length; i++) {
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
		}catch (Exception e) {
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

		int cellIndex = 0;

		for (Cell cell : row) {
			log.info("cell :"+cellIndex+" /"+cell.getStringCellValue());
			titleMap.put(cell.getStringCellValue(), cellIndex);


			cellIndex++;
		}

		return titleMap;
	}

}
