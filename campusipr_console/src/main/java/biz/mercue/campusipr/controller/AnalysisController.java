package biz.mercue.campusipr.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.AnalysisService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.ExcelUtils;
import biz.mercue.campusipr.util.JSONResponseBody;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;

@Controller
public class AnalysisController {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	AnalysisService analysisService;
	
	@Autowired
	PermissionService permissionService;
	
	@RequestMapping(value="/api/analysistest", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisTest(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("analysisTest ");
//		JSONObject jsonObject = new JSONObject(receiveJSONString);
//		JSONResponseBody responseBody = new JSONResponseBody();
//		String businessId = jsonObject.optString("business_id");
//		Long beginTime = jsonObject.getLong("beginTime");
//		Long endTime = jsonObject.getLong("endTime");
//		JSONObject analyizeData = analysisService.testAnalysis(businessId);
//		responseBody.setCode(Constants.INT_SUCCESS);
//		responseBody.setData(analyizeData); //沒顯示??
//		return responseBody.toString();
		return null;
	}

	
	// 學校端 分析總覽預設畫面
	@RequestMapping(value = "/api/analysisschooloverview", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisSchoolOverview(HttpServletRequest request, @RequestBody String receiveJSONString,
			@RequestParam(value = "order_field", required = false, defaultValue = "") String fieldId,
			@RequestParam(value = "asc", required = false, defaultValue = "1") int is_asc) {
		log.info("analysis All patent ");
		log.info("order_field:" + fieldId);
		log.info("asc:" + is_asc);
		JSONResponseBody responseBody = new JSONResponseBody();
		try {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String businessId = jsonObject.optString("business_id");
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT,
						Constants.VIEW);
				if (tokenBean.checkPermission(permission.getPermission_id())) {
					if (tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						responseBody.setCode(Constants.INT_NO_PERMISSION);
					} else {
						JSONObject analyizeData = analysisService.analysisAll(businessId);
						responseBody.setCode(Constants.INT_SUCCESS);
						responseBody.setData(analyizeData);
					}
				} else {
					responseBody.setCode(Constants.INT_NO_PERMISSION);
				}
			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
            log.error("Exception :" + e.getMessage());
            responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        }
		
		return responseBody.toString();
//		log.info("patentList:"+responseBody.getJacksonString(View.Analysis.class));
//		return responseBody.getJacksonString(View.Analysis.class);
	}
	
	//學校端 依年分析總覽
	@RequestMapping(value="/api/analysisschoolbyyear", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisSchoolByYear(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("analysis patent ByYear ");
		log.info("order_field:"+fieldId);
		log.info("asc:"+is_asc);
		JSONResponseBody responseBody  = new JSONResponseBody();
		try {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String businessId = jsonObject.optString("business_id");
			Long beginTime = jsonObject.getLong("beginTime");
			Long endTime = jsonObject.getLong("endTime");
			AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			if(tokenBean!=null) {
				Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
				if(tokenBean.checkPermission(permission.getPermission_id())) {
					if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						responseBody.setCode(Constants.INT_NO_PERMISSION);
					} else {
						JSONObject analyizeData = analysisService.analysisByYear(businessId, beginTime, endTime);
						responseBody.setCode(Constants.INT_SUCCESS);
						responseBody.setData(analyizeData);
					}
				}else {
					responseBody.setCode(Constants.INT_NO_PERMISSION);
				}
			}else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
            log.error("Exception :" + e.getMessage());
            responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        }
		return responseBody.toString();
	}
	
	// 學校端 國家分析預設畫面
	@RequestMapping(value = "/api/analysisschoolcountry", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisSchoolCountry(HttpServletRequest request, @RequestBody String receiveJSONString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "order_field", required = false, defaultValue = "") String fieldId,
			@RequestParam(value = "asc", required = false, defaultValue = "1") int is_asc) {
		log.info("analysis platform country ");
		log.info("order_field:" + fieldId);
		log.info("asc:" + is_asc);
		JSONResponseBody responseBody = new JSONResponseBody();
		try {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String businessId = jsonObject.optString("business_id");
			Long beginTime = jsonObject.getLong("beginTime");
			Long endTime = jsonObject.getLong("endTime");
			String countryId = jsonObject.optString("country_id");
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
				if (tokenBean.checkPermission(permission.getPermission_id())) {
					if (tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						responseBody.setCode(Constants.INT_NO_PERMISSION);
					} else {
						JSONObject analyizeData = analysisService.analysisAllCountry(businessId, beginTime, endTime, countryId);
						responseBody.setCode(Constants.INT_SUCCESS);
						responseBody.setData(analyizeData);
					}
				} else {
					responseBody.setCode(Constants.INT_NO_PERMISSION);
				}
			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (JSONException e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.toString();
	}

	//學校端 國家分析依年度
	@RequestMapping(value="/api/analysisschoolcountrybyyear", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisSchoolCountryByYear(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("analysis platform country ");
		log.info("order_field:"+fieldId);
		log.info("asc:"+is_asc);
		JSONResponseBody responseBody  = new JSONResponseBody();
		try {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String businessId = jsonObject.optString("business_id");
			Long beginTime = jsonObject.getLong("beginTime");
			Long endTime = jsonObject.getLong("endTime");
			String countryId = jsonObject.optString("country_id");
			AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			if(tokenBean!=null) {
				Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
				if(tokenBean.checkPermission(permission.getPermission_id())) {
					if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						responseBody.setCode(Constants.INT_NO_PERMISSION);
					} else {
						JSONObject analyizeData =  analysisService.analysisCountryByYear(businessId, beginTime, endTime, countryId);
						responseBody.setCode(Constants.INT_SUCCESS);
						responseBody.setData(analyizeData);
					}
				}else {
					responseBody.setCode(Constants.INT_NO_PERMISSION);
				}
			}else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (JSONException e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.toString();
	}
	
	//學校端 預設科系分析
	@RequestMapping(value="/api/analysisschooldepartment", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisSchoolDepartment(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("analysis platform country ");
		log.info("order_field:"+fieldId);
		log.info("asc:"+is_asc);
		JSONResponseBody responseBody  = new JSONResponseBody();
		try {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String businessId = jsonObject.optString("business_id");
			Long beginTime = jsonObject.getLong("beginTime");
			Long endTime = jsonObject.getLong("endTime");
			AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			if(tokenBean!=null) {
				Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
				if(tokenBean.checkPermission(permission.getPermission_id())) {
					if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						responseBody.setCode(Constants.INT_NO_PERMISSION);
					} else {
						JSONObject analyizeData =  analysisService.analysisAllDepartment(businessId, beginTime, endTime);
						responseBody.setCode(Constants.INT_SUCCESS);
						responseBody.setData(analyizeData);
					}
				}else {
					responseBody.setCode(Constants.INT_NO_PERMISSION);
				}
			}else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (JSONException e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.toString();
	}
	
	//學校端  科系依年度查詢
	@RequestMapping(value="/api/analysisschooldepartmentbyyear", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String analysisSchoolDepartmentByYear(HttpServletRequest request,
			@RequestBody String receiveJSONString,
			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
		log.info("analysis platform country ");
		log.info("order_field:"+fieldId);
		log.info("asc:"+is_asc);
		JSONResponseBody responseBody  = new JSONResponseBody();
		try {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String businessId = jsonObject.optString("business_id");
			Long beginTime = jsonObject.getLong("beginTime");
			Long endTime = jsonObject.getLong("endTime");
			AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			if(tokenBean!=null) {
				Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
				if(tokenBean.checkPermission(permission.getPermission_id())) {
					if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						responseBody.setCode(Constants.INT_NO_PERMISSION);
					} else {
						JSONObject analyizeData =  analysisService.analysisDepartmentByYears(businessId, beginTime, endTime);
						responseBody.setCode(Constants.INT_SUCCESS);
						responseBody.setData(analyizeData);
					}
				}else {
					responseBody.setCode(Constants.INT_NO_PERMISSION);
				}
			}else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (JSONException e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}
		return responseBody.toString();
	}
	
//	@RequestMapping(value="/api/exportschooltotal", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> exportSchoolTotal(HttpServletRequest request,@RequestBody String receiveJSONString){
//		log.info("exportpatent ");
//		return null;
//	}
	
//	@RequestMapping(value="/api/exportschoolcountry", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> exportSchoolCountry(HttpServletRequest request,@RequestBody String receiveJSONString){
//		log.info("exportpatent ");
//		return null;
//	}
//	
//	@RequestMapping(value="/api/exportplatformdepartment", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> exportSchoolDepartment(HttpServletRequest request,@RequestBody String receiveJSONString){
//		log.info("exportpatent ");
//		return null;
//	}
//	
//	@RequestMapping(value="/api/analysisplatformoverview", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String analysisPlatformOverview(HttpServletRequest request,
//			@RequestBody String receiveJSONString,
//			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
//			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
//			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
//		return null;
//	}
//	
//	@RequestMapping(value="/api/analysisplatformbyyear", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String analysisPlatformByYear(HttpServletRequest request,
//			@RequestBody String receiveJSONString,
//			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
//			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
//			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
//		return null;
//	}
//	
//	@RequestMapping(value="/api/analysisschoolcountry", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String analysisPLCountry(HttpServletRequest request,
//			@RequestBody String receiveJSONString,
//			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
//			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
//			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
//		return null;
//	}
//	
//	@RequestMapping(value="/api/analysisplatformdepartment", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String analysisPlatformDepartment(HttpServletRequest request,
//			@RequestBody String receiveJSONString,
//			@RequestParam(value ="page",required=false,defaultValue = "1") int page,
//			@RequestParam(value ="order_field",required=false,defaultValue = "") String fieldId,
//			@RequestParam(value ="asc",required=false,defaultValue = "1") int is_asc) {
//		return null;
//	}

	
//	@RequestMapping(value="/api/exportplatformtotal", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> exportPlatformTotal(HttpServletRequest request,@RequestBody String receiveJSONString){
//		log.info("exportpatent ");
//		StringResponseBody responseBody  = new StringResponseBody();
//		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
//		if(tokenBean!=null) {
//			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.VIEW);
//			
//			if(tokenBean.checkPermission(permission.getPermission_id())) {
//
//                JSONObject jsonObject = new JSONObject(receiveJSONString);
//				List<String> patent_ids = (List<String>) JacksonJSONUtils.readValue(jsonObject.optJSONArray("patent_ids").toString(), new TypeReference<List<String>>(){});
//				List<String> field_ids = (List<String>) JacksonJSONUtils.readValue(jsonObject.optJSONArray("field_ids").toString(), new TypeReference<List<String>>(){});
//
//				String bussinessId = tokenBean.getBusiness().getBusiness_id();
//				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
//					bussinessId = null;
//				}
//				
//				List<Patent> listPatent = AnalysisService.getExcelByPatentIds(patent_ids, bussinessId);
//				
//				String fileName = tokenBean.getBusiness().getBusiness_name();
//				ByteArrayInputStream fileOut = ExcelUtils.Patent2Excel(field_ids, listPatent, bussinessId);
//				
//				HttpHeaders headers = new HttpHeaders();
//				headers.add( "Content-disposition", "attachment; filename="+fileName+".xls" );
//				
//
//				return ResponseEntity
//		                .ok()
//		                .headers(headers)
//		                .contentType(MediaType.parseMediaType("application/ms-excel"))
//		                .body(new InputStreamResource(fileOut));
//			} else {
//				responseBody.setCode(Constants.INT_NO_PERMISSION);
//				return null;
//			}
//		} else {
//			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
//			return null;
//		}
//		return null;
//	}
	
//	@RequestMapping(value="/api/exportplatformschool", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> exportPlatformSchool(HttpServletRequest request,@RequestBody String receiveJSONString){
//		log.info("exportpatent ");
//		return null;
//	}
//	
//	@RequestMapping(value="/api/exportplatformcountry", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> exportPlatformCountry(HttpServletRequest request,@RequestBody String receiveJSONString){
//		log.info("exportpatent ");
//		return null;
//	}
}
