package biz.mercue.campusipr.controller;

import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.service.*;
import biz.mercue.campusipr.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	BusinessService businessService;

	@Autowired
	ExcelTaskService excelTaskService;

	@Autowired
	FieldService fieldService;

	@Autowired
	ExcelExportService excelExportService;
	
	@RequestMapping(value="/api/addpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatent(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="businessId",required=false) String businessId) throws Exception {
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
			patentService.patentHistoryFirstAdd(patent, patent.getPatent_id(), businessId);
			responseBody.setCode(taskResult);
			if(taskResult == Constants.INT_SUCCESS) {
				responseBody.setBean(patent);
			}

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.Patent.class);
	}
	
	@RequestMapping(value="/api/syncpatentdata", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String syncPatentData(HttpServletRequest request, @RequestBody String receiveJSONString, @RequestParam(value = "no", required = false) String patentApplNo) {
		log.info("syncPatentData ");
		
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
			
			int taskResult = patentService.syncPatentData(patent);
			
			//TODO charles
//			patentService.syncPatentStatus(patent);
			responseBody.setCode(taskResult);
			responseBody.setBean(patent);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.PatentDetail.class);
	}
	
	@RequestMapping(value="/api/addpatentbyapplno", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatentByApplNo(HttpServletRequest request,@RequestBody String receiveJSONString, @RequestParam(value ="businessId",required=false) String businessId) throws Exception {
		log.info("addPatentByApplNo ");
		
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			log.info(receiveJSONString);
			String ip = request.getRemoteAddr();
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
			if (StringUtils.isNULL(businessId)) {
				businessId = tokenBean.getBusiness_id();
			}
			Business business = businessService.getById(businessId);
			patent.setAdmin(admin);
			patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
			patent.setBusiness(business);
			patent.setAdmin_ip(ip);
			int taskResult = patentService.addPatentByApplNo(patent, tokenBean.getAdmin(), business, patent.getSourceFrom());
			
			//TODO charles
//			patentService.syncPatentStatus(patent);
			responseBody.setCode(taskResult);
			responseBody.setBean(patent);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.PatentDetail.class);
	}
	
	@RequestMapping(value="/api/checknopublicapplno", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String checkNoPublicApplNo(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("checknopublicapplno:");

		JSONResponseBody responseBody = new JSONResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));

		if(tokenBean!=null) {
			String ip = request.getRemoteAddr();
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
			patent.setAdmin(admin);
			patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
			patent.setBusiness(tokenBean.getBusiness());
			patent.setAdmin_ip(ip);
			JSONObject jsonObject = patentService.checkNoPublicApplNo(patent, tokenBean.getBusiness());
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setData(jsonObject);
			return responseBody.toString();
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			return responseBody.getJacksonString( View.PatentDetail.class);
		}
	}
	
	@RequestMapping(value="/api/mergepatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String mergePatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("mergePatent ");
		
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			String ip = request.getRemoteAddr();
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			Admin admin = adminService.getById(Constants.SYSTEM_ADMIN);
			patent.setAdmin(admin);
			patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
			patent.setBusiness(tokenBean.getBusiness());
			patent.setAdmin_ip(ip);
			int taskResult = patentService.addPatentByNoPublicApplNo(patent, tokenBean.getBusiness(), admin);

			responseBody.setCode(taskResult);
			responseBody.setBean(patent);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.PatentDetail.class);
	}
	
	
	@RequestMapping(value="/api/updatepatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePatent(HttpServletRequest request,@RequestBody String receiveJSONString) throws Exception {
		log.info("updatePatent ");
	
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
//		log.info("receiveJSONString :"+receiveJSONString);
		if(tokenBean!=null) {
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			patent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
			String ip = request.getRemoteAddr();
			patent.setAdmin(tokenBean.getAdmin());
			patent.setAdmin_ip(ip);
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.EDIT);
            if (tokenBean.checkPermission(permission.getPermission_id())) {
                int taskResult = patentService.authorizedUpdatePatent(tokenBean.getBusiness().getBusiness_id(), patent);
                responseBody.setCode(taskResult);
            } else {
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
			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("getPatentList ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form =null;
			
			log.info("token :"+tokenBean.getAdmin_token_id());
			
			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
			if (tokenBean.checkPermission(permission.getPermission_id())) {
				form = patentService.getByBusinessId(tokenBean.getBusiness().getBusiness_id(), page, fieldId, is_asc);
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setListQuery(form);
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
	
	@RequestMapping(value="/api/getallpatentbyid/{patentId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAllPatentbyId(HttpServletRequest request,@PathVariable String patentId) {
		log.info("getAllPatentbyId ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		Patent patent = patentService.getById(null, patentId);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setBean(patent);
		return responseBody.getJacksonString(View.PatentDetail.class);
	}
	
	@RequestMapping(value="/api/deletePatentbyId/{patentId}", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String deletePatentbyId(HttpServletRequest request,@PathVariable String patentId) {
		log.info("deletePatentbyId ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		patentService.deleteById(patentId, tokenBean.getBusiness_id());
		responseBody.setCode(Constants.INT_SUCCESS);
		return responseBody.getJacksonString(View.PatentDetail.class);
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
	public String combinePatentFamily(HttpServletRequest request,@RequestBody String receiveJSONString, @RequestParam(value = "patent_id", required = false) String patentId){
		log.info("combinePatentFamily ");
		log.info(receiveJSONString);
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
		
			PatentFamily family = (PatentFamily) JacksonJSONUtils.readValue(receiveJSONString, PatentFamily.class);	
			if(family != null) {
				String businessId = null;
				if(!tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)){
					// school
					businessId = tokenBean.getBusiness().getBusiness_id();
				} else {
					// platform
					businessId = Constants.BUSINESS_PLATFORM;
				}
				int taskResult = patentService.combinePatentFamily(family, businessId, patentId, tokenBean.getAdmin(), request.getRemoteAddr());
				responseBody.setCode(taskResult);
				if(taskResult == Constants.INT_SUCCESS) {
					if (family.getListPatentIds().size() == 1) {
						responseBody.setBean(null);
					} else {
						responseBody.setBean(family);
					}
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
		log.info("getpatentbyfamily");
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


	@RequestMapping(value = "/api/exportpatentexcel", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public ResponseEntity<InputStreamResource> exportPatentExcel(HttpServletRequest request, @RequestBody String receiveJSONString) throws IOException {
		log.info("exportpatent");
		StringResponseBody responseBody = new StringResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean == null) {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			return null;
		}
		Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
		if (!tokenBean.checkPermission(permission.getPermission_id())) {
			responseBody.setCode(Constants.INT_NO_PERMISSION);
			return null;
		}

		String businessId = tokenBean.getBusiness().getBusiness_id();
		String fileName = tokenBean.getBusiness().getBusiness_name();

		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String jsonPid = jsonObject.optJSONArray("patent_ids").toString();
		String jsonFid = jsonObject.optJSONArray("field_ids").toString();

		List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(jsonPid, new TypeReference<List<String>>() {});
		List<String> fieldIds = (List<String>) JacksonJSONUtils.readValue(jsonFid, new TypeReference<List<String>>() {});

		List<Patent> listPatent = patentService.getExcelByPatentIds(patentIds, businessId);
		ByteArrayInputStream fileOut = excelExportService.PatentToExcel(fieldIds, listPatent, businessId);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-disposition", "attachment; filename=" + fileName + ".xls");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.parseMediaType("application/ms-excel"))
				.body(new InputStreamResource(fileOut));

	}

    @RequestMapping(value = "/api/getallexcelfield", method = {RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
	public String getAllExcelField(HttpServletRequest request) {
		log.info("getAllExcelField");
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		//tokenBean = new AdminToken();
		if (tokenBean != null) {
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(fieldService.getAllFields());
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.FieldMap.class);
	}
		
	@RequestMapping(value = "/api/importpatentexcel", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
					"multipart/form-data" })
	@ResponseBody
	public String importPatentExcel(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
		log.info("importPatentExcel");
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		//tokenBean = new AdminToken();
		if (tokenBean != null) {
			if (file != null && !file.getOriginalFilename().isEmpty()) {
				ExcelTask task = excelTaskService.addTaskByFile(file, tokenBean.getAdmin());
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(task);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	@RequestMapping(value = "/api/gettaskfield/{taskId}", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getTaskField(HttpServletRequest request, @PathVariable String taskId) throws IOException {
		log.info("getTaskField ");
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			ExcelTask task = excelTaskService.getTaskField(tokenBean.getAdmin(), taskId);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setBean(task);
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	
	
	@RequestMapping(value = "/api/previewexceltask", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String previewExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("previewExcelTask ");
		BeanResponseBody responseBody = new BeanResponseBody();
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
		return responseBody.getJacksonString(View.ExcelTask.class);
	}
	
	@RequestMapping(value = "/api/submitexceltask", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String submitExcelTask(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
		log.info("submitExcelTask ");
		BeanResponseBody responseBody = new BeanResponseBody();
		Map<Integer, List<Patent>> mapPatent = new HashMap<>();
		String ip = request.getRemoteAddr();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			ExcelTask task = (ExcelTask) JacksonJSONUtils.readValue(receiveJSONString, ExcelTask.class);
			mapPatent = excelTaskService.submitTask(task, tokenBean.getAdmin());
			int responseBodyCode = Constants.INT_SYSTEM_PROBLEM;
			for (Integer mapPatentKey : mapPatent.keySet()) {
				log.info("mapPatentKey: " + mapPatentKey);
				switch (mapPatentKey) {
					case Constants.INT_SUCCESS:
						Map<String, Patent> mergeMap = patentService.addPatentByExcel(mapPatent.get(mapPatentKey), tokenBean.getAdmin(), tokenBean.getBusiness(), ip);
						if (mergeMap != null && !mergeMap.isEmpty()) {
							responseBodyCode = patentService.mergeDiffPatentByExcel(mergeMap, tokenBean.getAdmin(), tokenBean.getBusiness());
						} else {
							responseBodyCode = Constants.INT_SUCCESS;
						}
						break;
					case Constants.INT_DATA_ERROR:
						responseBodyCode = Constants.INT_DATA_ERROR;
						break;
					case Constants.INT_CANNOT_FIND_DATA:
						responseBodyCode = Constants.INT_CANNOT_FIND_DATA;
						break;
					case Constants.INT_SYSTEM_PROBLEM:
						responseBodyCode = Constants.INT_SYSTEM_PROBLEM;
						break;
				}
			}
			responseBody.setCode(responseBodyCode);
			responseBody.setBean(task);
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.ExcelTask.class);
	}

	@RequestMapping(value="/api/searchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchPatent(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
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
			if (tokenBean.checkPermission(permission.getPermission_id())) {
				ListQueryForm form = patentService.fieldSearchPatent(searchText, field.getField_id(), tokenBean.getBusiness().getBusiness_id(), page, fieldId, is_asc);
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setListQuery(form);
			} else {
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
	public String syncApplicantData(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page) throws Exception {
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
	
	// Exportfile download
	@RequestMapping(value = "/api/downloadexport", method = RequestMethod.POST, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request,
			@RequestBody String receiveJSONString) throws IOException {
		log.info("downloadExport");
		int responseBodyCode = Constants.INT_DATA_ERROR;

//		File f = (Constants.FILE_LOAD_URL + .getExcel_task_id() + "fix"+"xls");
//		{ file_name : "12345" }

		try {
			JSONObject dataJSON = new JSONObject(receiveJSONString);
			String fileName = dataJSON.optString("file_name");
			File f = new File(Constants.FILE_UPLOAD_PATH + fileName);

			InputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[4096];

			int bytesRead = 0;
			ByteArrayOutputStream bao = new ByteArrayOutputStream();

			while ((bytesRead = fis.read(buffer)) != -1) {
				bao.write(buffer, 0, bytesRead);
			}

			byte[] data = bao.toByteArray();
			ByteArrayInputStream fileOut = new ByteArrayInputStream(data);

			HttpHeaders headers = new HttpHeaders();
//			String fileName = "錯誤回報";
			headers.add("Content-disposition", "attachment;");
			log.info("回報成功");
			return ResponseEntity
					.ok()
					.headers(headers)
					.contentType(MediaType.parseMediaType("application/ms-excel"))
					.body(new InputStreamResource(fileOut));
			
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/api/advancedsearch", method = RequestMethod.POST, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String advanceSearch(HttpServletRequest request,
								@RequestBody String receiveJSONString,
								@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("advance search controller");
		ListResponseBody responseBody = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String query = jsonObject.optString("searchText");

		AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (adminToken != null) {
			try {
				ListQueryForm form = patentService.advancedSearch(query, adminToken.getBusiness_id(), page, Constants.SYSTEM_PAGE_SIZE);
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setListQuery(form);
			} catch (Exception e) {
				responseBody.setCode(Constants.INT_INCORRECT_SYNTAX);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
}
