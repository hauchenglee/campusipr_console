package biz.mercue.campusipr.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yaml.snakeyaml.scanner.Constant;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Message;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;


@Controller
public class MessageController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	AdminTokenService adminTokenService;
	
	
	@RequestMapping(value="/api/getchannelmessage", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getChannelMessage(HttpServletRequest request,
			@RequestParam(value ="page",required=false,defaultValue ="1") int page,
			@RequestParam(value ="lang",required=false,defaultValue ="tw") String lang) {
		log.info("updatePatent ");
	
	    ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			
			
			Calendar calendar1 = Calendar.getInstance();
			calendar1.set(Calendar.MONTH, Calendar.JANUARY);
			calendar1.set(Calendar.DAY_OF_MONTH, 10);
			List<Message> listMessage = new  ArrayList<Message>();
			
			Message message1 = new Message();
			message1.setMessage_id(KeyGeneratorUtils.generateRandomString());
			message1.setMessage_date(calendar1.getTime());
			message1.setMessage_type(Constants.MESSAGE_TYPE_TEXT);
			message1.setMessage_text("這是文字1<a href=\"https://www.google.com\">連結<a>");
			message1.setReceiver_id(tokenBean.getAdmin().getAdmin_id());
			message1.setReceiver_name(tokenBean.getAdmin().getAdmin_name());
			message1.setSender_id(Constants.SYSTEM_ADMIN);
			message1.setSender_name("系統訊息");
			
			calendar1.add(Calendar.MINUTE, 2);
			Message message2 = new Message();
			message2.setMessage_id(KeyGeneratorUtils.generateRandomString());
			message2.setMessage_date(calendar1.getTime());
			message2.setMessage_type(Constants.MESSAGE_TYPE_TEXT);
			message2.setMessage_text("收到！");
			message2.setSender_id(tokenBean.getAdmin().getAdmin_id());
			message2.setSender_name(tokenBean.getAdmin().getAdmin_name());
			message2.setReceiver_id(Constants.SYSTEM_ADMIN);
			message2.setReceiver_name("系統訊息");
			
			listMessage.add(message1);
			listMessage.add(message2);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(listMessage);
			responseBody.setTotal_count(2);
			responseBody.setPage_size(20);
//			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
//			patent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
//			String ip = request.getRemoteAddr();
//			patent.setAdmin(tokenBean.getAdmin());
//			patent.setAdmin_ip(ip);
//			Permission permission = permissionService.getSettingPermissionByModule(Constants.MODEL_CODE_PATENT_CONTENT, Constants.EDIT);
//			if(tokenBean.checkPermission(permission.getPermission_id())) {
//				if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
//					int taskResult =  patentService.authorizedUpdatePatent(null, patent);
//					responseBody.setCode(taskResult);
//				}else {
//					int taskResult =  patentService.authorizedUpdatePatent(tokenBean.getBusiness().getBusiness_id(), patent);
//					responseBody.setCode(taskResult);
//				}
//				
//			}else {
//				responseBody.setCode(Constants.INT_NO_PERMISSION);
//			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Message.class);
	}

}
