package biz.mercue.campusipr.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.ServiceChinaPatent;
import biz.mercue.campusipr.util.ServiceTaiwanPatent;
import biz.mercue.campusipr.util.ServiceUSPatent;
import biz.mercue.campusipr.util.StringResponseBody;

@Controller
public class PatentController {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	PatentService patentService;
	
	
	@Autowired
	AdminTokenService adminTokenService;
	
	
	
	@RequestMapping(value="/api/addpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPatent ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
			patent.setBusiness(tokenBean.getBusiness());
			int taskResult = patentService.addPatent(patent);
			responseBody.setCode(taskResult);

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/addpatentbyapplno", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPatentByApplNo(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPatent ");
		
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
			
		if(tokenBean!=null) {
			Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
			List<Patent> patentList = new ArrayList<>();
			//查詢台灣專利
			patentList = ServiceTaiwanPatent.getPatentRightByApplNo(patent.getPatent_appl_no());
			if (patentList.isEmpty()) {
				//查詢不到改USTPO
				patentList = ServiceUSPatent.getPatentRightByapplNo(patent.getPatent_appl_no());
				if (patentList.isEmpty()) {
					//查詢不到改EPO
					patentList = ServiceChinaPatent.getPatentRightByApplicantNo(patent.getPatent_appl_no());
				}
			}
			if (!patentList.isEmpty()) {
				List<Patent> newPatentList = new ArrayList<>();
				for (Patent updatepatent:patentList) {
					int taskResult = patentService.addPatentByApplNo(updatepatent);
					if (updatepatent != null) {
						newPatentList.add(updatepatent);
					}
				}
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setList(newPatentList);
			} else {
				//查無資料
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/updatepatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePatent(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updatePatent ");
		Patent patent = (Patent) JacksonJSONUtils.readValue(receiveJSONString, Patent.class);
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			int taskResult =  patentService.updatePatent(patent);
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	@RequestMapping(value="/api/patentlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("getPatentList ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form = patentService.getByBusinessId(tokenBean.getBusiness().getBusiness_id(), page);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	
	@RequestMapping(value="/api/getpatentbyid/{patentId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPatentbyId(HttpServletRequest request,@PathVariable String patentId) {
		log.info("getPatentbyId ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			Patent patent = patentService.getById(tokenBean.getBusiness().getBusiness_id(), patentId);
		
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setBean(patent);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.PatentDetail.class);
	}
	
	
	@RequestMapping(value="/api/combinepatentfamily", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String combinePatentFamily(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("combinePatentFamily ");
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<String> ids = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});	
			int taskResult = patentService.combinePatentFamily(ids, tokenBean.getBusiness().getBusiness_id());
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/exportpatentexcel", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String exportPatentexcel(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("exportpatent ");
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			//TODO 
			
			responseBody.setCode(Constants.INT_SUCCESS);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	@RequestMapping(value="/api/importpatentexcel", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String importPatentexcel(HttpServletRequest request,@RequestBody String receiveJSONString){
		log.info("importPatentexcel ");
		StringResponseBody responseBody  = new StringResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<String> patentIds = (List<String>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<String>>(){});	
			
			
			responseBody.setCode(Constants.INT_SUCCESS);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	
	
	
	@RequestMapping(value="/api/searchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String searchPatent(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page){
		log.info("exportpatent ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String searchText = jsonObject.getString("searchText");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {

			ListQueryForm form =  patentService.searchPatent(searchText, tokenBean.getBusiness().getBusiness_id(), page);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Patent.class);
	}
	
	@RequestMapping(value="/api/advancedsearchpatent", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String advancedSearchPatent(HttpServletRequest request,@RequestBody String receiveJSONString,@RequestParam(value ="page",required=false,defaultValue ="1") int page){
		log.info("advancedSearchPatent ");
		ListResponseBody responseBody  = new ListResponseBody();
		JSONObject jsonObject = new JSONObject(receiveJSONString);
		String searchText = jsonObject.getString("searchText");
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
		
	
			ListQueryForm form = patentService.fieldSearchPatent(searchText, tokenBean.getBusiness().getBusiness_id(), page);
		
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Patent.class);
		log.info("result :"+result);
		return result;
	}
	
	private void addEditHistory(Patent patent, Admin admin, String addField) {
		Date now = new Date();
		PatentEditHistory peh = new PatentEditHistory();
		if ("patent_name".equals(addField) && patent.getPatent_name() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id("edad617ccdc004f37cac8f8710c6e965");
			peh.setPatent(patent);
			peh.setAdmin(admin);
			peh.setHistory_data(patent.getPatent_name());
			peh.setCreate_date(now);
		}
		if ("patent_name_en".equals(addField) && patent.getPatent_name_en() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id("4e765c02d8a5afc000eaa77ba419ff53");
			peh.setPatent(patent);
			peh.setAdmin(admin);
			peh.setHistory_data(patent.getPatent_name_en());
			peh.setCreate_date(now);
		}
		if ("assignee".equals(addField) && patent.getListAssignee() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id("614191c8ec65d0e6429801429794ebcd");
			peh.setPatent(patent);
			peh.setAdmin(admin);
			String assigneeStr = "";
			for (Assignee assignee:patent.getListAssignee()) {
				assigneeStr += assignee.getAssignee_name() + ",";
			}
			peh.setHistory_data(assigneeStr);
			peh.setCreate_date(now);
		}
		if ("applicant".equals(addField) && patent.getListApplicant() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id("6c761184252dd9f0148a361ed9c4c8c2");
			peh.setPatent(patent);
			peh.setAdmin(admin);
			String applicantStr = "";
			for (Applicant applicant:patent.getListApplicant()) {
				applicantStr += applicant.getApplicant_name() + ",";
			}
			peh.setHistory_data(applicantStr);
			peh.setCreate_date(now);
		}
		if ("Inventor".equals(addField) && patent.getListInventor() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id("f08b87006f899e74c0b570f76349f49d");
			peh.setPatent(patent);
			peh.setAdmin(admin);
			String inventorStr = "";
			for (Inventor inventor:patent.getListInventor()) {
				inventorStr += inventor.getInventor_name() + ",";
			}
			peh.setCreate_date(now);
		}
		if (peh != null) {
			
			if (patent.getListHistory() != null) {
				patent.getListHistory().add(peh);
			} else {
				List<PatentEditHistory> pehList = new ArrayList<PatentEditHistory>();
				pehList.add(peh);
				patent.setListHistory(pehList);
			}
		}
	}


}
