package biz.mercue.campusipr.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;
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
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.MapResponseBody;
import biz.mercue.campusipr.util.StringUtils;





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
	
	@RequestMapping(value="/api/adminlogin", method = {RequestMethod.POST},consumes = Constants.CONTENT_TYPE_JSON, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String login(HttpServletRequest request,@RequestBody String receiveJSONString) {
		MapResponseBody response = new MapResponseBody();
		log.info("receiveJSONString:"+receiveJSONString);
		JSONObject reqJSON = new JSONObject(receiveJSONString);
		String email = reqJSON.optString("admin_email");
		String password = reqJSON.optString("admin_password");
		String recaptcha = reqJSON.optString("recaptcha");
		
		boolean isHuman = GoogleService.getReCaptchaResult(recaptcha);
		
		//TODO fordebug
		isHuman = true;
		
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
				log.info("admin :" + tokenBean.getAdmin().getAdmin_id());
				log.info("business :" + tokenBean.getAdmin().getBusiness().getBusiness_name());

				response.setCode(Constants.INT_SUCCESS);
				response.setData(Constants.JSON_TOKEN, tokenBean.getAdmin_token_id());
				break;

			case Constants.INT_CANNOT_FIND_DATA:
				response.setCode(Constants.INT_CANNOT_FIND_DATA);
				break;

			case Constants.INT_NO_PERMISSION:
				response.setCode(Constants.INT_NO_PERMISSION);
				break;
			case Constants.INT_PASSWORD_ERROR:
				response.setCode(Constants.INT_PASSWORD_ERROR);
				break;

			case Constants.INT_SYSTEM_PROBLEM:
				response.setCode(Constants.INT_SYSTEM_PROBLEM);
				break;
			default:
				response.setCode(Constants.INT_SYSTEM_PROBLEM);
				break;
			}
		} else {
			response.setCode(Constants.INT_DATA_ERROR);

		}
		return response.getJacksonString(View.Public.class);

	}
	
	
	@RequestMapping(value="/api/adminlogout", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String logout(HttpServletRequest request) {
			MapResponseBody response = new MapResponseBody();
			AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			if(tokenBean != null && tokenBean.getAdmin() != null){
				String adminId = tokenBean.getAdmin().getAdmin_id();
				adminTokenService.logout(adminId);						
				response.setCode(Constants.INT_SUCCESS);
			}else {
				response.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}

		return response.getJacksonString(View.Public.class);
	}
	
	//中心 管理者清單
	@RequestMapping(value="/api/getadminlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAdminList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		
		if(token !=null) {
			    //TODO check permission
			
				List<Role> listRole = new ArrayList<Role>();
				Role platformRole = roleService.getById(Constants.ROLE_PLATFORM_MANAGER);
				//TODO check login user permission
				Permission permission1 = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PLATFORM_MANAGER, Constants.VIEW);
				if(token.checkPermission(permission1.getPermission_id())) {
					List<Permission> listplatformPermission = permissionService.getSettingPermissionByRole(Constants.ROLE_PLATFORM_MANAGER);
					platformRole.setListRolePermission(listplatformPermission);
					ListQueryForm platformForm = adminService.getRoleBusinessAdminList(Constants.ROLE_PLATFORM_MANAGER, token.getBusiness().getBusiness_id(),page);
					platformRole.setTotal_count(platformForm.getTotal_count());
					platformRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
					platformRole.setListAdmin(platformForm.getList());
					listRole.add(platformRole);
				}
				
				Role patentRole = roleService.getById(Constants.ROLE_PLATFORM_PATENT);
				//TODO check login user permission
				Permission permission2 = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PLATFORM_PATENT, Constants.VIEW);
				if(token.checkPermission(permission2.getPermission_id())) {
					List<Permission> listpatentPermission = permissionService.getSettingPermissionByRole(Constants.ROLE_PLATFORM_PATENT);
					patentRole.setListRolePermission(listpatentPermission);
					ListQueryForm patentForm = adminService.getRoleBusinessAdminList(Constants.ROLE_PLATFORM_PATENT, token.getBusiness().getBusiness_id(),page);
					patentRole.setTotal_count(patentForm.getTotal_count());
					patentRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
					patentRole.setListAdmin(patentForm.getList());
					listRole.add(patentRole);
				}
				
				
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setList(listRole);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			
		}
		
		return responseBody.getJacksonString(View.Role.class);
	}
	
	@RequestMapping(value="/api/getrolelist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getRoleList(HttpServletRequest request) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(token !=null) {
			List<Role> list = roleService.getAllRole();
			responseBody.setList(list);
			responseBody.setCode(Constants.INT_SUCCESS);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Role.class);
		
	}
	
	
	@RequestMapping(value="/api/getcustomerbusinessadminlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCustomerBusinessAdminList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));

		
		
		if(token !=null) {
			    //TODO check permission
			
				List<Role> listRole = new ArrayList<Role>();
				
				Role businessManagerRole = roleService.getById(Constants.ROLE_BUSINESS_MANAGER);
				Permission permission1 = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_BUSINESS_MANAGER, Constants.VIEW);
				if(token.checkPermission(permission1.getPermission_id())) {
					List<Permission> listBusinessManagerPermission = permissionService.getSettingPermissionByRole(Constants.ROLE_BUSINESS_MANAGER);
					businessManagerRole.setListRolePermission(listBusinessManagerPermission);
					if(token.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						ListQueryForm businessManagerForm = adminService.getRoleBusinessAdminList(Constants.ROLE_BUSINESS_MANAGER, null,page);
						businessManagerRole.setTotal_count(businessManagerForm.getTotal_count());
						businessManagerRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
						businessManagerRole.setListAdmin(businessManagerForm.getList());
						listRole.add(businessManagerRole);
					} else {
						ListQueryForm businessManagerForm = adminService.getRoleBusinessAdminList(Constants.ROLE_BUSINESS_MANAGER, token.getBusiness().getBusiness_id(),page);
						businessManagerRole.setTotal_count(businessManagerForm.getTotal_count());
						businessManagerRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
						businessManagerRole.setListAdmin(businessManagerForm.getList());
						listRole.add(businessManagerRole);
					}
				}
		
				Role businessPatentRole = roleService.getById(Constants.ROLE_BUSINESS_PATENT);
				Permission permission2 = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_BUSINESS_PATENT, Constants.VIEW);
				if(token.checkPermission(permission2.getPermission_id())) {
					List<Permission> listBusinessPatentPermission = permissionService.getSettingPermissionByRole(Constants.ROLE_BUSINESS_PATENT);
					businessPatentRole.setListRolePermission(listBusinessPatentPermission);
					if(token.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						ListQueryForm businessPatentForm = adminService.getRoleBusinessAdminList(Constants.ROLE_BUSINESS_PATENT, null,page);
						businessPatentRole.setTotal_count(businessPatentForm.getTotal_count());
						businessPatentRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
						businessPatentRole.setListAdmin(businessPatentForm.getList());
						listRole.add(businessPatentRole);
					} else {
						ListQueryForm businessPatentForm = adminService.getRoleBusinessAdminList(Constants.ROLE_BUSINESS_PATENT, token.getBusiness().getBusiness_id(),page);
						businessPatentRole.setTotal_count(businessPatentForm.getTotal_count());
						businessPatentRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
						businessPatentRole.setListAdmin(businessPatentForm.getList());
						listRole.add(businessPatentRole);
					}	
				}

				Role userRole = roleService.getById(Constants.ROLE_COMMON_USER);
				Permission permission3 = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_COMMON_USER, Constants.VIEW);
				if(token.checkPermission(permission3.getPermission_id())) {
					List<Permission> listUserPermission = permissionService.getSettingPermissionByRole(Constants.ROLE_COMMON_USER);
					userRole.setListRolePermission(listUserPermission);
					if(token.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
						ListQueryForm userForm = adminService.getRoleBusinessAdminList(Constants.ROLE_COMMON_USER, null,page);
						userRole.setTotal_count(userForm.getTotal_count());
						userRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
						userRole.setListAdmin(userForm.getList());
						listRole.add(userRole);
					} else {
						ListQueryForm userForm = adminService.getRoleBusinessAdminList(Constants.ROLE_COMMON_USER, token.getBusiness().getBusiness_id(),page);
						userRole.setTotal_count(userForm.getTotal_count());
						userRole.setPage_size(Constants.SYSTEM_PAGE_SIZE);
						userRole.setListAdmin(userForm.getList());
						listRole.add(userRole);
					}
				}
		
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setList(listRole);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Role.class);
	}
	
	
	@RequestMapping(value="/api/checkpassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String checkPassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		BeanResponseBody responseBody = new BeanResponseBody();
		if(token !=null) {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			String password = jsonObject.getString("password");
			int taskResult = adminService.checkPassword(token.getAdmin().getAdmin_id(), password);
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	@RequestMapping(value="/api/searchrolelist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchRoleList(HttpServletRequest request,
			@RequestParam(value ="role",required=true,defaultValue ="") String roleId,
			@RequestParam(value ="text",required=true,defaultValue ="") String text,
			@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));

		
		
		if(token !=null) {

			Permission viewPermission = permissionService.getSettingPermissionByRoleAndModule(roleId, Constants.VIEW);
			
			if(viewPermission != null && token.checkPermission(viewPermission.getPermission_id())) {
			    
				String  businessId =null;
				if(!token.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					businessId = token.getBusiness().getBusiness_id();
				}
				
				Role role = roleService.getById(roleId);
				//TODO check login user permission
				ListQueryForm searchForm  = adminService.searchRoleAdminList(roleId, businessId, text, page);
				role.setTotal_count(searchForm.getTotal_count());
				role.setPage_size(Constants.SYSTEM_PAGE_SIZE);
				role.setListAdmin(searchForm.getList());
			
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(role);
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Role.class);
	}
	
	
	
	@RequestMapping(value="/api/getcustomerbusinessrole", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCustomerBusinessRole(HttpServletRequest request ,@RequestParam(value ="role",required=true,defaultValue ="") String roleId,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		log.info("page:"+page);
		log.info("roleId:"+roleId);
		
		
		if(token !=null) {
			//TODO check permission
			if(token.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
				Role role = roleService.getById(roleId);
				if(role!=null) {
					log.info("role is not null");
					ListQueryForm listForm = adminService.getRoleBusinessAdminList(role.getRole_id(), null,page);
					role.setListAdmin(listForm.getList());
					role.setTotal_count(listForm.getTotal_count());
					role.setPage_size(Constants.SYSTEM_PAGE_SIZE);

				
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(role);
				}
			} else {
				Role role = roleService.getById(roleId);
				if(role!=null) {
					log.info("role is not null");
					ListQueryForm listForm = adminService.getRoleBusinessAdminList(role.getRole_id(), token.getBusiness().getBusiness_id(),page);
					role.setListAdmin(listForm.getList());
					role.setTotal_count(listForm.getTotal_count());
					role.setPage_size(Constants.SYSTEM_PAGE_SIZE);

				
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(role);
				}
			}
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		
		
		return responseBody.getJacksonString(View.Role.class);
	}
	
	@RequestMapping(value="/api/addadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			Role role = admin.getRole();
			
			if(checkRolePermission(role,token,Constants.ADD)) {
				if(!token.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
					admin.setBusiness(token.getBusiness());
				}
				taskResult = adminService.createAdmin(admin);
				if(taskResult == Constants.INT_SUCCESS) {
					responseBody.setCode(Constants.INT_SUCCESS);
					
				}else if(taskResult == Constants.INT_USER_DUPLICATE) {
					responseBody.setCode(Constants.INT_USER_DUPLICATE);
				}else {
					responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
	
		return responseBody.getJacksonString(View.Admin.class);
	}
		
	
	@RequestMapping(value="/api/getadmininfo", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAdminInfo(HttpServletRequest request) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		if(tokenBean !=null) {
			
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setBean(tokenBean.getAdmin());
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString(View.Admin.class);
	}
	
	@RequestMapping(value="/api/updateadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			Role role = admin.getRole();
			
			if(checkRolePermission(role,token,Constants.EDIT)) {
				taskResult = adminService.updateAdmin(admin);
				if(taskResult == Constants.INT_SUCCESS) {
					responseBody.setCode(Constants.INT_SUCCESS);
				}else if(taskResult == Constants.INT_CANNOT_FIND_DATA){
					responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
				}else {
					responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
				}
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Admin.class);
	}
	
	
	@RequestMapping(value="/api/forgetpassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String forgetPassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();

		Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);

		if(admin != null) {
			int taskResult = adminService.forgetPassword(admin);
			responseBody.setCode(taskResult);
			
		}else {
			responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			
		}
	
		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	@RequestMapping(value="/api/updatepassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
	

		if(token !=null) {
			JSONObject reqJSON = new JSONObject(receiveJSONString);
			String password = reqJSON.optString("password");
			String repassword = reqJSON.optString("repassword");
			
			if(!StringUtils.isNULL(password) && password.equals(repassword)) {
		
				int taskResult =  adminService.updatePassword(token.getAdmin().getAdmin_id(), password);
				if(taskResult == Constants.INT_SUCCESS) {
					responseBody.setCode(Constants.INT_SUCCESS);
				
				}else if(taskResult == Constants.INT_CANNOT_FIND_DATA){
					responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
				}else {
					responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
				}
			}else {
				responseBody.setCode(Constants.INT_PASSWORD_ERROR);
			}
			
			
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
	
		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	@RequestMapping(value="/api/modifypassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String modifyPassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(token !=null) {
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			if(admin!=null) {
				if(!StringUtils.isNULL(admin.getAdmin_password()) && admin.getAdmin_password().equals(admin.getRe_admin_password())) {

					int taskResult = adminService.updatePassword(admin.getAdmin_id(), admin.getAdmin_password());
					if(taskResult == Constants.INT_SUCCESS) {
						responseBody.setCode(Constants.INT_SUCCESS);
					}else if(taskResult == Constants.INT_CANNOT_FIND_DATA){
						responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
					}else {
						responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
					}
				}else {
					responseBody.setCode(Constants.INT_PASSWORD_ERROR);
				}
			}else {
				responseBody.setCode(Constants.INT_DATA_ERROR);
			}
			
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	@RequestMapping(value = "/api/resetpassword", method = {RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String resetPassword(HttpServletRequest request, @RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();

		JSONObject reqJSON = new JSONObject(receiveJSONString);
		String token = reqJSON.optString("token");
		String password = reqJSON.optString("password");
		String repassword = reqJSON.optString("repassword");

		if (!StringUtils.isNULL(password) && password.equals(repassword)) {
			Admin admin = new Admin();
			admin.setToken(token);
			admin.setAdmin_password(password);
			int taskResult = adminService.resetPassword(admin);
			if (taskResult == Constants.INT_SUCCESS) {
				responseBody.setCode(Constants.INT_SUCCESS);

			} else if (taskResult == Constants.INT_CANNOT_FIND_DATA) {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			} else {
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_PASSWORD_ERROR);
		}

		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	@RequestMapping(value="/api/saferemoveadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String safeRemoveAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		if(tokenBean !=null) {
			
		}
		
		
		return "";
	}
	
	
	@RequestMapping(value="/api/invalidateadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String invalidateAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		
		if(token !=null) {
			
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			admin.setAvailable(false);
			Role role = admin.getRole();
			
			if(checkRolePermission(role,token,Constants.EDIT)) {
				adminService.updateAdmin(admin);
				responseBody.setCode(Constants.INT_SUCCESS);
			}else {
				responseBody.setCode(Constants.INT_NO_PERMISSION);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString( View.Public.class);
	}
	
	private boolean checkRolePermission(Role role,AdminToken token,String operation) {
		boolean hasPermission = false;
		Permission permission = permissionService.getSettingPermissionByRoleAndModule(role.getRole_id(), operation);
		if(permission!=null) {
			hasPermission = token.checkPermission(permission.getPermission_id());
		}
		return hasPermission;
	}
}
