package biz.mercue.campusipr.controller;

import java.util.ArrayList;
import java.util.Calendar;
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


import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Message;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.MessageService;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.ListResponseBody;



@Controller
public class MessageController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	MessageService messageService;
	
	
	@RequestMapping(value="/api/getchannelmessage", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getChannelMessage(HttpServletRequest request,
			@RequestParam(value ="page",required=false,defaultValue ="1") int page,
			@RequestParam(value ="lang",required=false,defaultValue ="tw") String lang) {
		log.info("getChannelMessage ");
	
	    ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form = messageService.getMessagesByAdminId(tokenBean.getAdmin().getAdmin_id(), Constants.SYSTEM_ADMIN, page, Constants.SYSTEM_PAGE_SIZE);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Message.class);
	}
	
	@RequestMapping(value="/api/getlastestmessage", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getLastestMessage(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("getLastestMessage ");
		
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			JSONObject jsonObject = new JSONObject(receiveJSONString);
			long lastTimestamp = jsonObject.getLong("lastTimestamp");
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTimeInMillis(lastTimestamp);
			List<Message> list = messageService.getMessagesAfterTime(tokenBean.getAdmin().getAdmin_id(), Constants.SYSTEM_ADMIN,calendar1.getTime() );
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);
			responseBody.setTotal_count(list.size());
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.Patent.class);
	
	}

}
