package biz.mercue.campusipr.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;
import biz.mercue.campusipr.util.ServiceTaiwanPatent;

@Controller
public class TestController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	PasswordEncoder encoder;
	
	
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

	@RequestMapping(value="/getpatent", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatent(HttpServletRequest request) {
		log.info("getPatent ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		
		List<Patent> patentList = ServiceTaiwanPatent.getPatentRightByPatentNo("I621340");
		
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(patentList);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}

}
