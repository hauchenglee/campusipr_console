package biz.mercue.campusipr.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.classmate.util.ResolvedTypeCache.Key;

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.service.RoleService;
import biz.mercue.campusipr.service.StatusService;
import biz.mercue.campusipr.service.SysRolePermissionService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.MapResponseBody;
import biz.mercue.campusipr.util.ServiceChinaPatent;
import biz.mercue.campusipr.util.StringResponseBody;
import biz.mercue.campusipr.util.StringUtils;
import biz.mercue.campusipr.util.ServiceTaiwanPatent;
import biz.mercue.campusipr.util.ServiceUSPatent;

@Controller
public class TestController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	PatentService patentService;
	
	@Autowired
	StatusService statusService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	SysRolePermissionService sysRolePermissionService;
	
	
	@RequestMapping(value="/gettestid", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getTestID(HttpServletRequest request) {
		log.info("getTestID ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		

		List<String> list = new ArrayList<String>();
		int i = 0;
		while( i < 10 ){
			String id =  KeyGeneratorUtils.generateRandomString();
			//String hasPwd = generatePasswordHash("abc123456");
			list.add(id);
			//System.out.println("hasPwd :"+hasPwd);
			i++;
		}
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Public.class);
		log.info("result :"+result);
		return result;
	}
	

	
	@RequestMapping(value="/generatepassword", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String generatePassword(HttpServletRequest request) {
		log.info("generatePassword ");

		
		
		log.info(encoder.encode("abc123"));
		
		log.info(encoder.matches("abc123", "$2a$10$iejovjHKL1wDgk8HNGtXC.09I5IqPeNnYgQKwfqHcdhuztJQbP6J."));
		
		log.info(encoder.encode("abc123hj"));
		
		
		log.info(encoder.matches("abc123hj", "$2a$10$iejovjHKL1wDgk8HNGtXC.09I5IqPeNnYgQKwfqHcdhuztJQbP6J."));
		
		log.info(encoder.encode("12345678901234567890"));
		return "";
	}

	@RequestMapping(value="/setupsysrolepermission", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String setupSysRolePermission(HttpServletRequest request) {
		log.info("getPatent ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		List<Permission> listP = permissionService.getAllPermission();
		
		sysRolePermissionService.updatesysRole(Constants.ROLE_PLATFORM_MANAGER, listP);
		
		
		
		
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	@RequestMapping(value = "/api/importpatentstatus", method = {RequestMethod.POST },
			produces = Constants.CONTENT_TYPE_JSON, 
			consumes = { "multipart/mixed","multipart/form-data" })
	@ResponseBody
	public String importPatentStatus(HttpServletRequest request, @RequestParam("data") String receiveJSONString,
			@RequestPart("file") MultipartFile[] files) {
		log.info("importPatentStatus");
		MapResponseBody responseBody = new MapResponseBody();
			try {
				log.info("receiveJSONString:" + receiveJSONString);
				log.info("files.length :" + files.length);
				for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

					MultipartFile file = files[fileIndex];
					if (file != null && !file.getOriginalFilename().isEmpty()) {
						
						CommonsMultipartFile cFile = (CommonsMultipartFile) file;  
						DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
						InputStream inputStream = fileItem.getInputStream();
						String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
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
							
							Sheet sheet = workbook.getSheetAt(0);
							 List<Status> list = readExcelStatus(sheet);
							 statusService.patchAddStatus(list);
						}else {
							
						}

					}
				}

				
				responseBody.setCode(Constants.INT_SUCCESS);
			} catch (Exception e) {
				log.error("Exception :" + e.getMessage());
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}

		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	public List<Status> readExcelStatus(Sheet sheet){
		List<Status> list = new ArrayList<Status>();
		int rowIndex = 0;
		int cellIndex = 0 ;
		for (Row row: sheet) {
			log.info("Row");
			cellIndex = 0;
			if(rowIndex == 0) {
				log.info("Title Row");
			}else {
				Status status = new Status();
				status.setStatus_id(KeyGeneratorUtils.generateRandomString());
	            for(Cell cell: row) {
	               //log.info("cell :"+cell.getStringCellValue());
	            	switch(cellIndex) {
	            	case 0:
	            		status.setCountry_id(cell.getStringCellValue().toLowerCase());
	            		break;
	            	case 1:
	            		status.setEvent_code(cell.getStringCellValue());
	            		break;
	            	case 2:
	            		status.setEvent_code_desc(cell.getStringCellValue());
	            		break;	
	            	case 3:
	            		status.setEvent_class(cell.getStringCellValue());
	            		break;
	            	case 4:
	            		status.setStatus_desc_en(cell.getStringCellValue());
	            		break;
	            		
	            	case 5:
	            		status.setStatus_desc(cell.getStringCellValue());
	            		break;
	            		
	            	case 6:
	            		status.setStatus_color(cell.getStringCellValue());
	            		break;
	            		
	            		
	            	default:
	            		
	            	}
	            		  
	            	cellIndex ++;
	            }
	            list.add(status);
			}
			rowIndex ++;
        }
		return list;
	}


}
