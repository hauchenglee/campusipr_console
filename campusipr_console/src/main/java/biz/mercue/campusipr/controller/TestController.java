package biz.mercue.campusipr.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.service.RoleService;
import biz.mercue.campusipr.service.SysRolePermissionService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.ServiceChinaPatent;
import biz.mercue.campusipr.util.StringResponseBody;
import biz.mercue.campusipr.util.ServiceTaiwanPatent;
import biz.mercue.campusipr.util.ServiceUSPatent;

@Controller
public class TestController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	PatentService patentService;
	
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	SysRolePermissionService sysRolePermissionService;
	
	
	@RequestMapping(value="/gettestid", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getTestID(HttpServletRequest request) {
		log.info("getTestID ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		

		List<String> list = new ArrayList<String>();
		int i = 0;
		while( i < 10 ){
			String id =  KeyGeneratorUtils.generateRandomString();
			//String hasPwd = generatePasswordHash("abc123456");
			list.add(id);
			//System.out.println("hasPwd :"+hasPwd);
			i++;
		}
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Public.class);
		log.info("result :"+result);
		return result;
	}
	

	
	@RequestMapping(value="/generatepassword", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String generatePassword(HttpServletRequest request) {
		log.info("generatePassword ");

		
		
		log.info(encoder.encode("abc123"));
		
		log.info(encoder.matches("abc123", "$2a$10$iejovjHKL1wDgk8HNGtXC.09I5IqPeNnYgQKwfqHcdhuztJQbP6J."));
		
		log.info(encoder.encode("abc123hj"));
		
		
		log.info(encoder.matches("abc123hj", "$2a$10$iejovjHKL1wDgk8HNGtXC.09I5IqPeNnYgQKwfqHcdhuztJQbP6J."));
		
		log.info(encoder.encode("12345678901234567890"));
		return "";
	}

	@RequestMapping(value="/syncpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String syncPatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("sync Patent ");
		JSONObject receiveJSONObj = new JSONObject(receiveJSONString);
		String assignee = receiveJSONObj.optString("assignee");
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		List<Patent> patentList = ServiceTaiwanPatent.getPatentRightByAssigneeNameCh(assignee, 10);
		
		for (Patent patent:patentList) {
			Patent patentCheck = patentService.getByPatentNo(patent.getPatent_no());
			if (patentCheck == null) {
				patentService.addPatent(patent);
			}
		}
		
		List<Patent> patentEnList = ServiceTaiwanPatent.getPatentRightByAssigneeNameEn(assignee, 10);
		
		for (Patent patent:patentEnList) {
			Patent patentCheck = patentService.getByPatentNo(patent.getPatent_no());
			if (patentCheck == null) {
				patentService.addPatent(patent);
			}
		}
		
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(patentList);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	@RequestMapping(value="/syncpatentus", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String syncPatentUS(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("sync Patent ");
		JSONObject receiveJSONObj = new JSONObject(receiveJSONString);
		String assignee = receiveJSONObj.optString("assignee");
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		List<Patent> patentList = ServiceUSPatent.getPatentRightByAssigneeName(assignee, 10);
		
		for (Patent patent:patentList) {
			Patent patentCheck = patentService.getByPatentNo(patent.getPatent_no());
			if (patentCheck == null) {
				patentService.addPatent(patent);
			}
		}
		
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(patentList);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	@RequestMapping(value="/syncpatentcn", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String syncPatentCN(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("sync Patent ");
		JSONObject receiveJSONObj = new JSONObject(receiveJSONString);
		String assignee = receiveJSONObj.optString("assignee");
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		List<Patent> patentList = ServiceChinaPatent.getPatentRightByAssigneeName("上海建工集团股份有限公司");
		
		for (Patent patent:patentList) {
			Patent patentCheck = patentService.getByPatentNo(patent.getPatent_no());
			if (patentCheck == null) {
				patentService.addPatent(patent);
			}
		}
		
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(patentList);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/setupsysrolepermission", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String setupSysRolePermission(HttpServletRequest request) {
		log.info("getPatent ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		List<Permission> listP = permissionService.getAllPermission();
		
		sysRolePermissionService.updatesysRole(Constants.ROLE_PLATFORM_MANAGER, listP);
		
		
		
		
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}


}
