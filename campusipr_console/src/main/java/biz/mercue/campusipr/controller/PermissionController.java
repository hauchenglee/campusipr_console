package biz.mercue.campusipr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.MapResponseBody;


@Controller
public class PermissionController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	PermissionService permissionService;
	
	@RequestMapping(value="/api/getadminpermissionlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAdminPermissionList(HttpServletRequest request) {
		log.info("getAdminPermissionList ");
	
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<Permission> list = permissionService.getPermissionListByAdmin(tokenBean.getAdmin().getAdmin_id());
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		return responseBody.getJacksonString(View.Permission.class);
	}
	
	
	@RequestMapping(value="/api/getpermissionlistbyadmin", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPermissionListbyAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("getPermissionListbyAdmin ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			log.info("admin :"+tokenBean.getAdmin());
			String adminId = new JSONObject(receiveJSONString).getString("admin_id");
			List<Permission> list = permissionService.getPermissionListByAdmin(adminId);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Permission.class);
	}
	
	
	@RequestMapping(value="/api/getpermissionlistbyrole", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPermissionListbyRole(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("getPermissionListbyAdmin ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			
			String roleId = new JSONObject(receiveJSONString).getString("role_id");
			List<Permission> list = permissionService.getPermissionListByRole(roleId);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Permission.class);
	}
	
	
	@RequestMapping(value="/api/getallpermissionlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPermissionListbyRole(HttpServletRequest request) {
		log.info("getPermissionListbyAdmin ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			
			
			List<Permission> list = permissionService.getAllPermission();
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Permission.class);
	}

}
