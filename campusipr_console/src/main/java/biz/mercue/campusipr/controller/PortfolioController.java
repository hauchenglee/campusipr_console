package biz.mercue.campusipr.controller;


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

import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Portfolio;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.PortfolioService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.JWTUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.ListResponseBody;

@Controller
public class PortfolioController {
	
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	PortfolioService portfolioService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	
	@RequestMapping(value="/api/portfoliolist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPortfolioList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		log.info("getPortfolioList ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			ListQueryForm form = portfolioService.getByBusinessId(tokenBean.getBusiness_id(), page);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(form);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString( View.Portfolio.class);
	}
	
	
	@RequestMapping(value="/api/getportfoliobyid/{portfoliId}", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPortfolioById(HttpServletRequest request,@PathVariable String portfoliId) {
		log.info("getPortfolioById ");
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			log.info("tokenBean.getBusiness_id():"+tokenBean.getBusiness_id());
			Portfolio portfolio = portfolioService.getById(tokenBean.getBusiness_id(), portfoliId);
			if(portfolio !=null) {
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(portfolio);
			}else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
	
		return responseBody.getJacksonString(View.PortfolioDetail.class);
	}
	
	
	@RequestMapping(value="/api/addportfolio", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addPortfolio(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("addPortfolio ");
		
		Portfolio portfolio = (Portfolio) JacksonJSONUtils.readValue(receiveJSONString, Portfolio.class);
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			int taskResult = portfolioService.addPortfolio(portfolio);

			responseBody.setCode(taskResult);
		
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return  responseBody.getJacksonString(View.Portfolio.class);
	}
	
	
	@RequestMapping(value="/api/updateportfolio", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePortfolio(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updatePortfolio ");
		
		Portfolio portfolio = (Portfolio) JacksonJSONUtils.readValue(receiveJSONString, Portfolio.class);
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			int taskResult = portfolioService.updatePortfolio(portfolio);
			responseBody.setCode(taskResult);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
	
		return  responseBody.getJacksonString(View.Portfolio.class);
	}
	
	
	@RequestMapping(value="/api/removeportfolio", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String removePortfolio(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("removePortfolio ");
		Portfolio portfolio = (Portfolio) JacksonJSONUtils.readValue(receiveJSONString, Portfolio.class);
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {

			int taskResult = portfolioService.deletePortfolio(portfolio);
			responseBody.setCode(taskResult);

		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Portfolio.class);
	}
	

}
