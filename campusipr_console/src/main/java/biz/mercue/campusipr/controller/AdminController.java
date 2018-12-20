package biz.mercue.campusipr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.util.Constants;

@Controller
public class AdminController {
	
	
	@RequestMapping(value="/login", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String login(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
	}
	
	
	@RequestMapping(value="/logout", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String logout(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		return "";
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
