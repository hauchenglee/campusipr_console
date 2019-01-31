package biz.mercue.campusipr.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ExcelTask;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.model.View.PatentDetail;
import biz.mercue.campusipr.service.AdminService;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.ExcelTaskService;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
import biz.mercue.campusipr.util.ExcelUtils;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;

@Controller
public class PatentController {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	PatentService patentService;
	
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	ExcelTaskService excelTaskService;
	
	
	
	@RequestMapping(value="/api/addpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPatent ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			String ip = request.getRemoteAddr();
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
			patent.setBusiness(tokenBean.getBusiness());
			patent.setAdmin(tokenBean.getAdmin());
			patent.setAdmin_ip(ip);
			int taskResult = patentService.addPatent(patent);
			responseBody.setCode(taskResult);
			if(taskResult == Constants.INT_SUCCESS) {
				responseBody.setBean(patent);
			}

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/addpatentbyapplno", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatentByApplNo(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPatent ");
		
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			log.info(receiveJSONString);
			String ip = request.getRemoteAddr();
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
			patent.setAdmin(admin);
			patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
			patent.setBusiness(tokenBean.getBusiness());
			patent.setAdmin_ip(ip);
			int taskResult = patentService.addPatentByApplNo(patent);
			patentService.syncPatentStatus(patent);
			responseBody.setCode(taskResult);
			responseBody.setBean(patent);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.PatentDetail.class);
	}
	
	
	@RequestMapping(value="/api/updatepatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updatePatent ");
	
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		log.info("receiveJSONString :"+receiveJSONString);
		if(tokenBean!=null) {
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			patent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
			String ip = request.getRemoteAddr();
			patent.setAdmin(tokenBean.getAdmin());
			patent.setAdmin_ip(ip);
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.EDIT);
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					int taskResult =  patentService.authorizedUpdatePatent(null, patent);
					responseBody.setCode(taskResult);
				}else {
					int taskResult =  patentService.authorizedUpdatePatent(tokenBean.getBusiness().getBusiness_id(), patent);
					responseBody.setCode(taskResult);
				}
				
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	@RequestMapping(value="/api/patentlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentList(HttpServletRequest request,
			@RequestParam(value ="page",required=false,defaultValue ="1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue ="") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue ="1") int is_asc) {
		log.info("getPatentList ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form =null;
			
			log.info("token :"+tokenBean.getAdmin_token_id());
			
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					form = patentService.getByBusinessId(null, page,fieldId,is_asc);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				}else {
					form = patentService.getByBusinessId(tokenBean.getBusiness().getBusiness_id(), page,fieldId,is_asc);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	
	@RequestMapping(value="/api/getpatentbyid/{patentId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentbyId(HttpServletRequest request,@PathVariable String patentId) {
		log.info("getPatentbyId ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					Patent patent = patentService.getById(null, patentId);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(patent);
				}else {
					Patent patent = patentService.getById(tokenBean.getBusiness().getBusiness_id(), patentId);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(patent);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
			return responseBody.getJacksonString(View.PatentDetail.class);
		}else {
			return responseBody.getJacksonString(View.PatentEnhance.class);
		}
	}
	
	@RequestMapping(value="/api/getpatenthistorybyid", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentHistorybyId(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("getPatenthistorybyId ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String patentId = jsonObject.getString("patent_id");
		String fieldId = jsonObject.getString("field_id");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form =null;
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					form = patentService.getHistoryBypatentId(null, patentId, fieldId, page);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				}else {
					form = patentService.getHistoryBypatentId(tokenBean.getBusiness().getBusiness_id(), patentId, fieldId, page);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString(View.PatentHistory.class);
	}
	
	
	@RequestMapping(value="/api/combinepatentfamily", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String combinePatentFamily(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("combinePatentFamily ");
		log.info(receiveJSONString);
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
		
			PatentFamily family = (PatentFamily) JacksonJSONUtils.readValue(receiveJSONString, PatentFamily.class);	
			if(family != null) {
				String businessId = null;
				if(!tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)){
					businessId  = tokenBean.getBusiness().getBusiness_id();
				}
				int taskResult = patentService.combinePatentFamily(family, businessId);
				responseBody.setCode(taskResult);
				if(taskResult == Constants.INT_SUCCESS) {
					responseBody.setBean(family);
				}
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/getpatentbyfamily/{familyId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String gePatentbyFamily(HttpServletRequest request,@PathVariable String familyId) {
		log.info("getPatentbyId ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				List<Patent> list = patentService.getByFamily(familyId);
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setList(list);
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/exportpatentexcel", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public ResponseEntity<InputStreamResource> exportPatentexcel(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("exportpatent ");
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				List<String> ids = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});	

				String bussinessId = tokenBean.getBusiness().getBusiness_id();
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					bussinessId = null;
				}
				
				List<Patent> listPatent = patentService.getExcelByPatentIds(ids, bussinessId);
				
				String fileName = tokenBean.getBusiness().getBusiness_name();
				ByteArrayInputStream fileOut = ExcelUtils.Patent2Excel(listPatent, bussinessId);
				
				HttpHeaders headers = new HttpHeaders();
				headers.add( "Content-disposition", "attachment; filename="+fileName+".xls" );
				

				return ResponseEntity
		                .ok()
		                .headers(headers)
		                .contentType(MediaType.parseMediaType("application/ms-excel"))
		                .body(new InputStreamResource(fileOut));
			} else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
				return null;
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			return null;
		}
	}
		
	@RequestMapping(value = "/api/importpatentexcel", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
					"multipart/form-data" })
	@ResponseBody
	public String importPatentExcel(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		log.info("importPatentExcel");
		BeanResponseBody responseBody = new BeanResponseBody();
		try {
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				log.info("1");
				if (file != null && !file.getOriginalFilename().isEmpty()) {
					ExcelTask task = excelTaskService.addTaskByFile(file, tokenBean.getAdmin());
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(task);
				}

			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}

		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	
	
	@RequestMapping(value = "/api/gettaskfield/{taskId}", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getTaskField(HttpServletRequest request, @PathVariable String taskId) {
		log.info("getTaskField ");
		BeanResponseBody responseBody = new BeanResponseBody();
		try {
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				ExcelTask task = excelTaskService.getTaskField(tokenBean.getAdmin(), taskId);
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(task);

			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	
	
	@RequestMapping(value = "/api/previewexceltask", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String previewExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("previewExcelTask ");
		BeanResponseBody responseBody = new BeanResponseBody();
		try {
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				ExcelTask task = (ExcelTask) JacksonJSONUtils.readValue(receiveJSONString, ExcelTask.class);
				int result = excelTaskService.previewTask(task, tokenBean.getAdmin());
				responseBody.setCode(result);
				responseBody.setBean(task);
			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
			
			responseBody.setCode(Constants.INT_SUCCESS);
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	@RequestMapping(value = "/api/submitexceltask", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String submitExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("submitExcelTask ");
		BeanResponseBody responseBody = new BeanResponseBody();
		try {
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				ExcelTask task = (ExcelTask) JacksonJSONUtils.readValue(receiveJSONString, ExcelTask.class);
				int result = excelTaskService.submitTask(task,tokenBean.getAdmin());
				responseBody.setCode(result);
				responseBody.setBean(task);
			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
			
			responseBody.setCode(Constants.INT_SUCCESS);
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	
	
	
	@RequestMapping(value="/api/searchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchPatent(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="page",required=false,defaultValue ="1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue ="") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue ="1") int is_asc) {
		log.info("searchpatent ");
		log.info("page:"+page);
		log.info("order_field:"+fieldId);
		log.info("asc:"+is_asc);
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String fieldStr = jsonObject.getJSONObject("field").toString();
		PatentField field = (PatentField) JacksonJSONUtils.readValue(fieldStr, PatentField.class);
		Object searchText = jsonObject.get("searchText");
		log.info("searchText:"+searchText);
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					ListQueryForm form =  patentService.fieldSearchPatent(searchText, field.getField_id(), null, page,fieldId,is_asc);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				} else {
					ListQueryForm form =  patentService.fieldSearchPatent(searchText, field.getField_id(), tokenBean.getBusiness().getBusiness_id(), page,fieldId,is_asc);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		log.info("patentList:"+responseBody.getJacksonString(View.Patent.class));
		return responseBody.getJacksonString(View.Patent.class);
		
	}
	

	
	@RequestMapping(value="/api/geteditpatentstatus", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getEditPatentStatus(HttpServletRequest request){
		ListResponseBody responseBody  = new ListResponseBody();

		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				List<Status> list = patentService.getEditStatus();
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setList(list);
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
		
	}

	@RequestMapping(value="/api/syncapplicant", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String syncApplicantData(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("syncapplicant ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String businessId = jsonObject.getString("business_id");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				String ip = request.getRemoteAddr();

				List<Patent> list = new ArrayList<>();
				int taskResult = patentService.syncPatentsByApplicant(list, Constants.SYSTEM_ADMIN, businessId, ip);
				for (Patent patent:list) {
					patentService.syncPatentStatus(patent);
				}
				responseBody.setCode(taskResult);
				responseBody.setTotal_count(list.size());
				responseBody.setList(list);
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		

		return responseBody.getJacksonString(View.Patent.class);
	}

}
