package biz.mercue.campusipr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.BusinessService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;


@Controller
public class BusinessController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	BusinessService businessService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@RequestMapping(value="/api/getbusinesslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBusinessList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form  = businessService.getAllByPage(page);
		
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Business.class);
	}

	@RequestMapping(value="/api/getallbusinesslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAllBusinessList(HttpServletRequest request) {

		ListResponseBody responseBody = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form  = businessService.getAll();

			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Business.class);
	}
	
	
	@RequestMapping(value="/api/getavailablebusinesslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAvailableBusinessList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<Business> list = businessService.getAvailable(page, Constants.SYSTEM_PAGE_SIZE);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Business.class);
	}
	
	
	@RequestMapping(value="/api/searchbusiness", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchBusiness(HttpServletRequest request,
			@RequestParam(value ="text",required=true,defaultValue ="") String text,
			@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form = businessService.search(text, page);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Business.class);
	}
	
	
	
	@RequestMapping(value="/api/safeaddbusiness", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String safeAddBusiness(HttpServletRequest request,@RequestBody String receiveJSONString) {
		Business business = (Business) JacksonJSONUtils.readValue(receiveJSONString, Business.class);
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			int taskResult = businessService.safeAddBusiness(business);
			
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Business.class);
	}
	
	
	@RequestMapping(value="/api/getbusinessbyid/{businessId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBusinessById(HttpServletRequest request,@PathVariable String businessId) {
		Business business  = businessService.getById(businessId);
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setBean(business);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.BusinessDetail.class);
	}
	
	
	
	@RequestMapping(value="/api/updatebusiness", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateBusiness(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Business business = (Business) JacksonJSONUtils.readValue(receiveJSONString, Business.class);
			businessService.updateBusiness(business);
			responseBody.setCode(Constants.INT_SUCCESS);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Business.class);
	}

}
