package biz.mercue.campusipr.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import biz.mercue.campusipr.model.Patent;

public class ExcelUtils {
	
	private static Logger log = Logger.getLogger(ExcelUtils.class.getName());
	
	private static String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};
	
	public static final String Patent2Excel(List<Patent> list) {
		
		
		try {
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
	        for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
	            cell.setCellStyle(headerCellStyle);
	        }
	
	        // Create Cell Style for formatting Date
	        CellStyle dateCellStyle = workbook.createCellStyle();
	        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
	
	        // Create Other rows and cells with employees data
	        int rowNum = 1;
	        for(Patent patent: list) {
	            Row row = sheet.createRow(rowNum++);
	
	            row.createCell(0).setCellValue(patent.getPatent_name());
	
	            row.createCell(1).setCellValue(patent.getPatent_name());
	
	            Cell dateOfBirthCell = row.createCell(2);
	            dateOfBirthCell.setCellValue(patent.getPatent_name());
	            dateOfBirthCell.setCellStyle(dateCellStyle);
	
	            row.createCell(3).setCellValue(patent.getPatent_name());
	        }
	
			// Resize all columns to fit the content size
	        for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	
	        // Write the output to a file
	        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
	        workbook.write(fileOut);
	        fileOut.close();
	
	        // Closing the workbook
	        workbook.close();
		}catch (Exception e) {
			log.error(e.getMessage());
		}
    
		
		return "";
	}
	
	public static final Workbook file2Workbook(MultipartFile mpFile) throws IOException {
		CommonsMultipartFile cFile = (CommonsMultipartFile) mpFile;  
		DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
		InputStream inputStream = fileItem.getInputStream();
		String extensionName = FilenameUtils.getExtension(mpFile.getOriginalFilename());
		Workbook workbook = null;
        boolean is_support = false;
		if(!StringUtils.isNULL(extensionName)) {
			if("xlsx".equalsIgnoreCase(extensionName)) {
				workbook = new XSSFWorkbook(inputStream);
				is_support =true;
			}else if("xls".equalsIgnoreCase(extensionName)) {
				workbook = new HSSFWorkbook(inputStream);
				is_support =true;
			}else {
				
			}
		}
		
		return workbook;
	}
	
	
	public static final List<Patent> Excel2Patent(String path,Map<String, String> fieldMap) {
		List<Patent> patentList = new ArrayList<Patent>();
		
		return patentList;
	}

}
