package biz.mercue.campusipr.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.service.*;
import biz.mercue.campusipr.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
	
	@Autowired
	ServletContext servletContext;

	@Autowired
	QuartzService quartzService;
	
	@RequestMapping(value="/test", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String test(HttpServletRequest request) {
		log.info("test ");
		BeanResponseBody response = new BeanResponseBody();
		
		
		response.setCode(Constants.INT_SUCCESS);
		response.setMessage("V_2019_08_19_03");
		response.setMessage_en("V_2019_08_19_03");
		String result = JacksonJSONUtils.mapObjectWithView(response, View.Public.class);
		log.info("result :"+result);
		return result;
	}
	

	@RequestMapping(value="/api/demo/{patentId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String demo(HttpServletRequest request, @PathVariable String patentId, @RequestBody String receiveJSONString) {
		log.info("/api/demo");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		String businessId;
		if (tokenBean != null) {
			businessId = tokenBean.getBusiness_id();
		} else {
			businessId = Constants.BUSINESS_PLATFORM;
		}
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String str = jsonObject.optString("str1");
//		quartzService.createJob();
		int result = patentService.demo("", businessId, patentId, str);
		return "{\"aaa\": \"" + result + "\"}";
	}
	
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
	
	
	@RequestMapping(value="/api/demopatentchange", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String demoPatentChange(HttpServletRequest request) {
		log.info("getPatent ");
		BeanResponseBody responseBody  = new BeanResponseBody();
	
		InputStream is = null;
		String fileAsString = null;
		
		try {
			log.info("HTML_FORGET_PASSWORD:"+Constants.HTML_FORGET_PASSWORD);
			log.info("URL_RESET_PASSWORD:"+Constants.URL_RESET_PASSWORD);
			String html = FileUtils.readHtml(Constants.HTML_FORGET_PASSWORD);
			log.info("1");
//			String htmlContent = String.format(html, "黃富榆",Constants.URL_RESET_PASSWORD +"?token=" + KeyGeneratorUtils.generateRandomString(),
//					"黃富榆",Constants.URL_RESET_PASSWORD + "?token=" + KeyGeneratorUtils.generateRandomString());
			 
			//htmlContent =htmlContent.replaceAll("forget_link", Constants.URL_RESET_PASSWORD + "?token=" + KeyGeneratorUtils.generateRandomString());
			log.info("2");
			List<String> list = new ArrayList<String>();
			list.add("kevinhsieh@mercue.biz");
			log.info("3");
			new MailSender().sendSimpleMail(list, "asd", "asd");
			 
			log.info("4");
		}catch (Exception e) {
			log.error("Exception :"+e.getMessage());
		}finally {
			if(is != null) {
				try {
					is.close();
				}catch (Exception e) {
					log.error("Exception :"+e.getMessage());
				}
			}
		}
		//log.info("fileAsString : "+fileAsString);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.PatentDetail.class);
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

	@RequestMapping(value="/api/deleteAllFortest", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisTest(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("analysisTest ");
		JSONObject jsonObject = new JSONObject(receiveJSONString);
//		JSONResponseBody responseBody = new JSONResponseBody();
		BeanResponseBody response = new BeanResponseBody();
		String businessId = jsonObject.optString("business_id");
//		JSONArray businessName = jsonObject.optJSONArray("business_name");
//		JSONArray statusDesc=jsonObject.optJSONArray("statusDesc");
//		JSONArray countryId = jsonObject.optJSONArray("country_id");
//		log.info(jsonObject.optJSONArray("statusDesc").length());
//		log.info(statusDesc);
//		JSONObject analyizeData = analysisService.schoolData(statusDesc, businessName, countryId);
//		JSONObject analyizeData = analysisService.testAnalysis(businessId, beginTime, endTime);
//		log.info(analyizeData);
		String delectData  = patentService.deleteAll(businessId.toString());
		String result = JacksonJSONUtils.mapObjectWithView(response, View.Public.class);
		response.setCode(Constants.INT_SUCCESS);
//		responseBody.setCode(Constants.INT_SUCCESS);
//		responseBody.setData(delectData);
		return result;
	}
	
}
