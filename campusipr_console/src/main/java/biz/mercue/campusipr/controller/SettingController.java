package biz.mercue.campusipr.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.model.Banner;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AnnuityReminderService;
import biz.mercue.campusipr.service.BannerService;
import biz.mercue.campusipr.service.CountryService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.ImageUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ListResponseBody;
import biz.mercue.campusipr.util.MapResponseBody;
import biz.mercue.campusipr.util.StringResponseBody;

@Controller
public class SettingController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	AnnuityReminderService annuityReminderService;
	
	@Autowired
	BannerService bannerService;
	
	
	@RequestMapping(value="/countrylist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCountryList(HttpServletRequest request,@RequestParam(value ="lang",required=false,defaultValue ="tw") String lang) {
		log.info("getPatentList ");
		ListResponseBody listResponseBody  = new ListResponseBody();
		String businessId ="123";

		List<Country> list = countryService.getListByLanguage(lang);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setMessage(Constants.MSG_SUCCESS);
		listResponseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(listResponseBody, View.Public.class);
		log.info("result :"+result);
		return result;
	}
	

	
	@RequestMapping(value="/getannuityreminder", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAnnuityReminder(HttpServletRequest request) {
		log.info("Annuity ");
		BeanResponseBody reesponseBody  = new BeanResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";
		
		AnnuityReminder reminder = annuityReminderService.getByBusinessId(businessId);
		reesponseBody.setCode(Constants.INT_SUCCESS);
		reesponseBody.setMessage(Constants.MSG_SUCCESS);
		reesponseBody.setBean(reminder);
		String result = JacksonJSONUtils.mapObjectWithView(reesponseBody, View.Reminder.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/updateannuityreminder", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAnnuityReminder(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updateAnnuityReminder ");
		AnnuityReminder reminder = (AnnuityReminder) JacksonJSONUtils.readValue(receiveJSONString, AnnuityReminder.class);
		BeanResponseBody reesponseBody  = new BeanResponseBody();
		String businessId ="04ea692278889b6621409d68c88aab17";
		
		annuityReminderService.update(reminder);
		reesponseBody.setCode(Constants.INT_SUCCESS);
		reesponseBody.setMessage(Constants.MSG_SUCCESS);
		reesponseBody.setBean(reminder);
		String result = JacksonJSONUtils.mapObjectWithView(reesponseBody, View.Reminder.class);
		log.info("result :"+result);
		return result;
	}
	
	
	
	@RequestMapping(value="/getbannerlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBannerList(HttpServletRequest request) {
		log.info("getBannerList ");
		ListResponseBody responseBody  = new ListResponseBody();
		
		List<Banner> list = bannerService.getAll();

		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Banner.class);
		log.info("result :"+result);
		return result;
	}
	
	@RequestMapping(value="/getavailablebannerlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAvailableBannerList(HttpServletRequest request) {
		log.info("getAvailableBannerList ");
		ListResponseBody responseBody  = new ListResponseBody();

		
		List<Banner> list = bannerService.getAvailable();

		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Banner.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/addbanner", method = {RequestMethod.POST} , produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed", "multipart/form-data" })
	@ResponseBody
	public String addBanner(HttpServletRequest request,@RequestParam("data") String  receiveJSONString,@RequestPart("file") MultipartFile[] files) {
		log.info("addBanner");
		MapResponseBody responseBody  = new MapResponseBody();
		try {
			log.info("receiveJSONString:"+receiveJSONString);
			
			Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);
			String bannerId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
			banner.setBanner_id(bannerId);
			log.info("files.length :"+files.length);
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
				
				
				MultipartFile file = files[fileIndex];
				if(file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String finalFileName = bannerId + "." + extendName;
		
					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
				
					if (ImageUtils.writeFile(file, finalFile)){
						banner.setBanner_image_file(finalFileName);
					}
				} 
			}
			
			bannerService.addBanner(banner);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);
		}catch (Exception e) {
			log.error("Exception :"+e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			responseBody.setMessage(Constants.MSG_SYSTEM_PROBLEM);
		}

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Banner.class);
		log.info("result :"+result);
		return result;
	}
	
	@RequestMapping(value="/updatebanner", produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed", "multipart/form-data" })
	@ResponseBody
	public String updateBanner(HttpServletRequest request,@RequestParam("data") String  receiveJSONString,@RequestPart("file") MultipartFile[] files) {
		log.info("updateBanner");
		MapResponseBody responseBody  = new MapResponseBody();
		try {
			
			Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);
			String  bannerId = banner.getBanner_id();
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
				
				
				MultipartFile file = files[fileIndex];
				if(file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String finalFileName = bannerId + "." + extendName;
		
					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
				
					if (ImageUtils.writeFile(file, finalFile)){
						banner.setBanner_image_file(finalFileName);
					}
				} 
			}
			
			bannerService.updateBanner(banner);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);
		}catch (Exception e) {
			log.error("Exception :"+e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			responseBody.setMessage(Constants.MSG_SUCCESS);
		}

		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Banner.class);
		log.info("result :"+result);
		return result;
	}
	
	
	@RequestMapping(value="/updatebannerorder", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateBannerOrder(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		List<Banner> list = (List<Banner>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<Banner>>(){});
		
		BeanResponseBody responseBody  = new BeanResponseBody();
	
		
		bannerService.updateBannerList(list);
		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		//responseBody.setBean(reminder);
		
		String result = JacksonJSONUtils.mapObjectWithView(responseBody, View.Banner.class);
		log.info("result :"+result);
		return result;
	}
}
