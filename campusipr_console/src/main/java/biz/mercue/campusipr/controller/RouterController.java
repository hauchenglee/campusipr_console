package biz.mercue.campusipr.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;

@Controller
public class RouterController {
	
	

	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@RequestMapping(value="/", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	public ModelAndView mainRouter(HttpServletRequest request){
		String redirectUrl = null;
		String token = (String) request.getSession().getAttribute(Constants.JSON_TOKEN);
	    if(!StringUtils.isNULL(token)){ 
		    log.info("token is not null");
			redirectUrl = Constants.REDIRECT_MAINPAGE;
	    }else{
			redirectUrl = Constants.REDIRECT_LOGIN;
	    }	
		return new ModelAndView("redirect:" + redirectUrl);
	}

}
