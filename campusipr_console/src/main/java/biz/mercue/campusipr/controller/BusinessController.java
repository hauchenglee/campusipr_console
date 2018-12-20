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

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.BusinessService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;


@Controller
public class BusinessController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	BusinessService businessService;
	
	@RequestMapping(value="/getbusinesslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBusinessList(HttpServletRequest requests) {
		
		ListResponseBody responseBody  = new ListResponseBody();
		List<Business> list = businessService.getAll();
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Business.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/getavailablebusinesslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAvailableBusinessList(HttpServletRequest requests,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		
		ListResponseBody responseBody  = new ListResponseBody();
		List<Business> list = businessService.getAvailable(page, Constants.SYSTEM_PAGE_SIZE);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Business.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	@RequestMapping(value="/safeaddbusiness", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String safeAddBusiness(HttpServletRequest request,@RequestBody String receiveJSONString) {
		Business business = (Business) JacksonJSONUtils.readValue(receiveJSONString, Business.class);
		StringResponseBody responseBody  = new StringResponseBody();
		
		businessService.saveAddBusiness(business);
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Business.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/getbusinessbyid/{businessId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBusinessById(HttpServletRequest request,@PathVariable String businessId) {
		Business business  = businessService.getById(businessId);
		BeanResponseBody responseBody  = new BeanResponseBody();
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setBean(business);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.BusinessDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	@RequestMapping(value="/updatebusiness", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateBusiness(HttpServletRequest request,@RequestBody String receiveJSONString) {
		Business business = (Business) JacksonJSONUtils.readValue(receiveJSONString, Business.class);
		BeanResponseBody responseBody  = new BeanResponseBody();
		businessService.updateBusiness(business);
		
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Business.class);
		log.info("result :"+result);
		return result;
	}

}
