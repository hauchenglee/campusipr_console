package biz.mercue.campusipr.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminService;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.service.RoleService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.GoogleService;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.MapResponseBody;





@Controller
public class AdminController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	PermissionService permissionService;
	
	@RequestMapping(value="/adminlogin", method = {RequestMethod.POST},consumes = Constants.CONTENT_TYPE_JSON, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String login(HttpServletRequest request,@RequestBody String receiveJSONString) {
		MapResponseBody response = new MapResponseBody();
		log.info("receiveJSONString:"+receiveJSONString);
		JSONObject reqJSON = new JSONObject(receiveJSONString);
		String email = reqJSON.optString("admin_email");
		String password = reqJSON.optString("admin_password");
		String recaptcha = reqJSON.optString("recaptcha");
		
		boolean isHuman = GoogleService.getReCaptchaResult(recaptcha);
		
		log.info("email:"+email);
		log.info("password:"+password);
		
		if (isHuman) {
			int handleResult = adminService.login(email, password);

			switch (handleResult) {
			case Constants.INT_SUCCESS:

				Admin adminBean = adminService.getByEmail(email);

				AdminToken tokenBean = adminTokenService.generateToken(adminBean.getAdmin_id());

				log.info("tokenBean:" + JacksonJSONUtils.mapObjectWithView(tokenBean, View.Token.class));

				log.info("token :" + tokenBean.getAdmin_token_id());
				log.info("admin :" + tokenBean.getAdminBean().getAdmin_id());
				log.info("business :" + tokenBean.getAdminBean().getBusiness().getBusiness_name());

				response.setCode(Constants.INT_SUCCESS);
				response.setMessage(Constants.MSG_SUCCESS);
				response.setData(Constants.JSON_TOKEN, tokenBean.getAdmin_token_id());
				break;

			case Constants.INT_CANNOT_FIND_USER:
				response.setCode(Constants.INT_CANNOT_FIND_USER);
				response.setMessage(Constants.MSG_CANNOT_FIND_USER);
				break;

			case Constants.INT_NO_PERMISSION:
				response.setCode(Constants.INT_NO_PERMISSION);
				response.setMessage(Constants.MSG_NO_PERMISSION);
				break;
			case Constants.INT_PASSWORD_ERROR:
				response.setCode(Constants.INT_PASSWORD_ERROR);
				response.setMessage(Constants.MSG_PASSWORD_ERROR);
				break;

			case Constants.INT_SYSTEM_PROBLEM:
				response.setCode(Constants.INT_SYSTEM_PROBLEM);
				response.setMessage(Constants.MSG_SYSTEM_PROBLEM);
				break;
			default:
				response.setCode(Constants.INT_SYSTEM_PROBLEM);
				response.setMessage(Constants.MSG_SYSTEM_PROBLEM);
				break;
			}
		} else {

			response.setCode(Constants.INT_DATA_ERROR);
			response.setMessage(Constants.MSG_DATA_ERROR);

		}

		String result = JacksonJSONUtils.mapObjectWithView(response, View.Public.class);

		log.info("result :" + result);
		return result;

	}
	
	
	@RequestMapping(value="/adminlogout", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String logout(HttpServletRequest request,@RequestBody String receiveJSONString) {
		MapResponseBody response = new MapResponseBody();

			
			AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
			
			if(tokenBean != null && tokenBean.getAdminBean() != null){
				String adminId = tokenBean.getAdminBean().getAdmin_id();
				adminTokenService.logout(adminId);
				
				//close the logout agent's connection 
				BeanResponseBody responseBody = new BeanResponseBody();
		
				
				
				
				response.setCode(Constants.INT_SUCCESS);
				response.setMessage(Constants.MSG_SUCCESS);
			}

			
		

		String  result = JacksonJSONUtils.mapObjectWithView(response,  View.Public.class);
		return result;
	}
	
	@RequestMapping(value="/addadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
	}
	
	
	@RequestMapping(value="/getuserinfo", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getUserInfo(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
	}
	
	@RequestMapping(value="/updateadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
	}
	
	
	@RequestMapping(value="/saveremoveadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String saveRemoveAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
	}
	
	
	@RequestMapping(value="/invalidateadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String invalidateAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
	}

}
