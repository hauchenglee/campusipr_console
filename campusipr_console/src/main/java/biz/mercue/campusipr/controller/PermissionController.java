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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.util.BeanResponseBody;
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
	public String getPermissionListbyRole(HttpServletRequest request,@RequestParam(value ="role",required=true,defaultValue ="") String roleId) {
		log.info("getPermissionListbyRole");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			

			List<Permission> list = permissionService.getPermissionListByRole(roleId);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Permission.class);
	}
	
	
	@RequestMapping(value="/api/getpermissionbyrole", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPermissionbyRole(HttpServletRequest request,@RequestParam(value ="role",required=true,defaultValue ="") String roleId) {
		log.info("getPermissionbyRole ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			log.info("role :"+roleId);
			Role role = permissionService.getByRole(roleId);
			
			if(role!=null) {
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(role);
			}else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
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
	
	
	@RequestMapping(value="/api/updaterolepermission", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateRolePermission(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("getPermissionListbyAdmin ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Role role = (Role) JacksonJSONUtils.readValue(receiveJSONString, Role.class);
			int taskResult = permissionService.updateRolePermission(role);
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Permission.class);
	}

}
