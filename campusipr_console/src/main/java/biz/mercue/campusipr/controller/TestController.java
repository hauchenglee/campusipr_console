package biz.mercue.campusipr.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;

@Controller
public class TestController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@RequestMapping(value="/gettestid", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCountryList(HttpServletRequest request) {
		log.info("getPatentList ");
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

}
