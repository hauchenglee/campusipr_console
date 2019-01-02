package biz.mercue.campusipr.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.MapResponseBody;


@Controller
public class PermissionController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@RequestMapping(value="/api/getadminpermissionlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAdminPermissionList(HttpServletRequest request) {
		log.info("getAdminPermissionList ");
	
		ListResponseBody responseBody  = new ListResponseBody();
		

		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/api/getallpermission", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAllPermission(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("getAllPermission ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		MapResponseBody responseBody  = new MapResponseBody();
		

		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}

}
