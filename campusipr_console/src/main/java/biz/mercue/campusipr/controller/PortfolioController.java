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

import biz.mercue.campusipr.model.Portfolio;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.model.View.PortfolioDetail;
import biz.mercue.campusipr.service.PortfolioService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;

@Controller
public class PortfolioController {
	
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	PortfolioService portfolioService;
	
	
	@RequestMapping(value="/portfoliolist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPortfolioList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("getPortfolioList ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		List<Portfolio> list = portfolioService.getByBusinessId(businessId, page, Constants.SYSTEM_PAGE_SIZE);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Portfolio.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/getportfoliobyid/{portfoliId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPortfolioById(HttpServletRequest request,@PathVariable String portfoliId) {
		log.info("addPortfolio ");
		
		
		BeanResponseBody responseBody  = new BeanResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		Portfolio portfolio = portfolioService.getById(businessId, portfoliId);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setBean(portfolio);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.PortfolioDetail.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/addportfolio", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPortfolio(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPortfolio ");
		
		Portfolio portfolio = (Portfolio) JacksonJSONUtils.readValue(receiveJSONString, Portfolio.class);
		BeanResponseBody responseBody  = new BeanResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		portfolioService.addPortfolio(portfolio);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Portfolio.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/updateportfolio", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePortfolio(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updatePortfolio ");
		
		Portfolio portfolio = (Portfolio) JacksonJSONUtils.readValue(receiveJSONString, Portfolio.class);
		ListResponseBody listResponseBody  = new ListResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		portfolioService.updatePortfolio(portfolio);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
	
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Portfolio.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/removeportfolio", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String removePortfolio(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("removePortfolio ");
		Portfolio portfolio = (Portfolio) JacksonJSONUtils.readValue(receiveJSONString, Portfolio.class);
		ListResponseBody listResponseBody  = new ListResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";

		portfolioService.deletePortfolio(portfolio);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Portfolio.class);
		log.info("result :"+result);
		return result;
	}
	

}
