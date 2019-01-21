package biz.mercue.campusipr.controller;

import java.io.ByteArrayInputStream;
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

import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ExcelTask;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.ExcelTaskService;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.ExcelUtils;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.MapResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;

@Controller
public class PatentController {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	PatentService patentService;
	
	
	@Autowired
	PermissionService permissionService;
	
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	ExcelTaskService excelTaskService;
	
	
	
	@RequestMapping(value="/api/addpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPatent ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			String ip = request.getRemoteAddr();
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
			patent.setBusiness(tokenBean.getBusiness());
			patent.setAdmin(tokenBean.getAdmin());
			patent.setAdmin_ip(ip);
			int taskResult = patentService.addPatent(patent);
			responseBody.setCode(taskResult);

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
		if(tokenBean!=null) {
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			patent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
			String ip = request.getRemoteAddr();

			patent.setAdmin(tokenBean.getAdmin());
			patent.setAdmin_ip(ip);
			int taskResult =  patentService.updatePatent(patent);
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	@RequestMapping(value="/api/patentlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("getPatentList ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form =null;
			
			log.info("token :"+tokenBean.getAdmin_token_id());
			
			
			//TODO
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					log.info("1");
					form = patentService.getByBusinessId(null, page);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setListQuery(form);
				}else {
					log.info("2");
					form = patentService.getByBusinessId(tokenBean.getBusiness().getBusiness_id(), page);
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
//			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
//			
//			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					Patent patent = patentService.getById(null, patentId);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(patent);
				}else {
					Patent patent = patentService.getById(tokenBean.getBusiness().getBusiness_id(), patentId);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(patent);
				}
//			}else {
//				responseBody.setCode(Constants.INT_NO_PERMISSION);
//			}
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
	public String getPatentHistorybyId(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("getPatenthistorybyId ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String patentId = jsonObject.getString("patent_id");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					List<PatentEditHistory> listHis = patentService.getHistoryBypatentId(null, patentId);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setList(listHis);
				}else {
					List<PatentEditHistory> listHis = patentService.getHistoryBypatentId(tokenBean.getBusiness().getBusiness_id(), patentId);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setList(listHis);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/combinepatentfamily", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String combinePatentFamily(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("combinePatentFamily ");
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<String> ids = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});	
			int taskResult = patentService.combinePatentFamily(ids, tokenBean.getBusiness().getBusiness_id());
			responseBody.setCode(taskResult);
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
//			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			
//			if(tokenBean.checkPermission(permission.getPermission_id())) {
				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					List<Patent> listPatent = patentService.getAllByBussinessId(null);
					
					String fileName = "test";
					ByteArrayInputStream fileOut = ExcelUtils.Patent2Excel(listPatent, null);
					
					
					HttpHeaders headers = new HttpHeaders();
					headers.add( "Content-disposition", "attachment; filename="+fileName+".xls" );
					

					return ResponseEntity
			                .ok()
			                .headers(headers)
			                .contentType(MediaType.parseMediaType("application/ms-excel"))
			                .body(new InputStreamResource(fileOut));
				} else {
					List<Patent> listPatent = patentService.getAllByBussinessId(tokenBean.getBusiness().getBusiness_id());
					
					String fileName = "test";
					ByteArrayInputStream fileOut = ExcelUtils.Patent2Excel(listPatent, tokenBean.getBusiness().getBusiness_id());
					
					
					HttpHeaders headers = new HttpHeaders();
					headers.add( "Content-disposition", "attachment; filename="+fileName+".xls" );
					

					return ResponseEntity
			                .ok()
			                .headers(headers)
			                .contentType(MediaType.parseMediaType("application/ms-excel"))
			                .body(new InputStreamResource(fileOut));
				}
//			} else {
//				responseBody.setCode(Constants.INT_NO_PERMISSION);
//				return null;
//			}
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

		return responseBody.getJacksonString(View.Public.class);
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
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	@RequestMapping(value = "/api/submitexceltask", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String submitExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("getTaskField ");
		BeanResponseBody responseBody = new BeanResponseBody();
		try {
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				ExcelTask task = (ExcelTask) JacksonJSONUtils.readValue(receiveJSONString, ExcelTask.class);

			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	
	
	@RequestMapping(value="/api/searchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchPatent(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page){
		log.info("exportpatent ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String searchText = jsonObject.getString("searchText");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {

			ListQueryForm form =  patentService.searchPatent(searchText, tokenBean.getBusiness().getBusiness_id(), page);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	@RequestMapping(value="/api/advancedsearchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String advancedSearchPatent(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page){
		log.info("advancedSearchPatent ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String searchText = jsonObject.getString("searchText");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
		
	
			ListQueryForm form = patentService.fieldSearchPatent(searchText, tokenBean.getBusiness().getBusiness_id(), page);
		
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}


}
