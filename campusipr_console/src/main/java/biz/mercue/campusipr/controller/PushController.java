package biz.mercue.campusipr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.PushService;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.ObjectMapperUtil;



@Controller
public class PushController {
	
	@Autowired
	PushService pushService;

	
	

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@RequestMapping(value="/pushlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPushList(HttpServletRequest request){
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		
		List<PushTask> list = pushService.getByAccount("123");
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = ObjectMapperUtil.mapObjectWithView(listResponseBody, View.Public.class);
		log.info("result :"+result);
		return result;
	}
}
