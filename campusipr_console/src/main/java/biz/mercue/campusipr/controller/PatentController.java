package biz.mercue.campusipr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;

@Controller
public class PatentController {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	PatentService patentService;
	
	
	
	@RequestMapping(value="/addpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPatent ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		StringResponseBody responseBody  = new StringResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";
		patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
		
		patentService.addPatent(patent);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	@RequestMapping(value="/updatepatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updatePatent ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		StringResponseBody responseBody  = new StringResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		patentService.updatePatent(patent);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	@RequestMapping(value="/patentlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("getPatentList ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		List<Patent> list = patentService.getByBusinessId(businessId, 1, 20);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	@RequestMapping(value="/getpatentbyid/{patentId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentbyId(HttpServletRequest request,@PathVariable String patentId) {
		log.info("getPatentbyId ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		Patent patent = patentService.getById(businessId, patentId);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setBean(patent);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.PatentDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/combinepatentfamily", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String combinePatentFamily(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("combinePatentFamily ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		String businessId ="123";
		
		List<Patent> list = patentService.getByBusinessId(businessId, 1, 20);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/exportpatentexcel", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String exportPatentexcel(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("exportpatent ");
		StringResponseBody responseBody  = new StringResponseBody();
		String businessId ="123";
		
		List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});
		List<Patent> PatentList =  patentService.getByPatentIds(patentIds);
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		//responseBody.setData(data);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/importpatentexcel", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String importPatentexcel(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("exportpatent ");
		StringResponseBody responseBody  = new StringResponseBody();
		String businessId ="123";
		
		List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});
		List<Patent> PatentList =  patentService.getByPatentIds(patentIds);
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		//responseBody.setData(data);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	
	@RequestMapping(value="/searchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchPatent(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("exportpatent ");
		StringResponseBody responseBody  = new StringResponseBody();
		String businessId ="123";
		
		List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});
		List<Patent> PatentList =  patentService.getByPatentIds(patentIds);
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		//responseBody.setData(data);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	@RequestMapping(value="/advancedsearchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String advancedSearchPatent(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("exportpatent ");
		StringResponseBody responseBody  = new StringResponseBody();
		String businessId ="123";
		
		List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});
		List<Patent> PatentList =  patentService.getByPatentIds(patentIds);
		
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		//responseBody.setData(data);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	


}
