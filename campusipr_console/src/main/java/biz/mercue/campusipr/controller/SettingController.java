package biz.mercue.campusipr.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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


import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.model.Banner;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Currency;
import biz.mercue.campusipr.model.FieldSync;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.AdminTokenService;
import biz.mercue.campusipr.service.AnnuityReminderService;
import biz.mercue.campusipr.service.BannerService;
import biz.mercue.campusipr.service.BusinessService;
import biz.mercue.campusipr.service.CountryService;
import biz.mercue.campusipr.service.CurrencyService;
import biz.mercue.campusipr.service.PermissionService;
import biz.mercue.campusipr.service.FieldService;
import biz.mercue.campusipr.service.FieldSyncService;
import biz.mercue.campusipr.util.BeanResponseBody;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.ImageUtils;
import biz.mercue.campusipr.util.JWTUtils;
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
	CurrencyService currencyService;
	
	@Autowired
	AnnuityReminderService annuityReminderService;
	
	@Autowired
	BannerService bannerService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	@Autowired
	FieldService fieldService;
	
	
	@Autowired
	FieldSyncService fieldSyncService;
	
	@Autowired
	BusinessService businessService;
	
	
	@RequestMapping(value="/api/countrylist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCountryList(HttpServletRequest request,@RequestParam(value ="lang",required=false,defaultValue ="tw") String lang) {
		log.info("getCountryList ");
		ListResponseBody listResponseBody  = new ListResponseBody();


		List<Country> list = countryService.getListByLanguage(lang);
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setList(list);
		return listResponseBody.getJacksonString(View.Public.class);
	}
	
	@RequestMapping(value="/api/currencylist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCurrencylist(HttpServletRequest request) {
		log.info("getCurrencylist ");
		ListResponseBody listResponseBody  = new ListResponseBody();


		List<Currency> list = currencyService.getAll();
		listResponseBody.setCode(Constants.INT_SUCCESS);
		listResponseBody.setList(list);
		return listResponseBody.getJacksonString(View.Public.class);
	}
	

	
	@RequestMapping(value="/api/getannuityreminder", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAnnuityReminder(HttpServletRequest request,
	  @RequestParam(value ="business",required=false,defaultValue ="") String businessId) {
		log.info("getAnnuityReminder ");
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		log.info("business:"+businessId);
		if(tokenBean!=null) {
			List<AnnuityReminder> reminderList = null;
			if(tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
				log.info("cross business");
				reminderList = annuityReminderService.getByBusinessId(businessId);
			}else {
				reminderList = annuityReminderService.getByBusinessId(tokenBean.getBusiness().getBusiness_id());
			}
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(reminderList);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			
		}
		return responseBody.getJacksonString(View.Reminder.class);
	}
	
	
	@RequestMapping(value="/api/updateannuityreminder", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAnnuityReminder(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("updateAnnuityReminder ");
		
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<AnnuityReminder> reminders = new ArrayList<AnnuityReminder>();
			JSONArray reminderArr = new JSONArray(receiveJSONString);
			for (int index = 0; index < reminderArr.length(); index++) {
				JSONObject reminderObj = reminderArr.optJSONObject(index);
				AnnuityReminder reminder = (AnnuityReminder) JacksonJSONUtils.readValue(reminderObj.toString(), AnnuityReminder.class);
				reminders.add(reminder);
			}
			log.info(reminders.size());
			if(!tokenBean.checkPermission(Constants.PERMISSION_CROSS_BUSINESS)) {
				
			}else {
				for (AnnuityReminder reminder:reminders) {
					reminder.setBusiness(tokenBean.getBusiness());
				}
			}
			int taskResult = annuityReminderService.update(reminders, tokenBean.getBusiness().getBusiness_id());
			responseBody.setCode(taskResult);
			if(taskResult == Constants.INT_SUCCESS) {
				responseBody.setList(reminders);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			
		}
		return responseBody.getJacksonString(View.Reminder.class);
	}
	
	
	
	@RequestMapping(value="/api/getbannerlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBannerList(HttpServletRequest request) {
		log.info("getBannerList ");
		ListResponseBody responseBody  = new ListResponseBody();
		
		List<Banner> list = bannerService.getAll();

		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		return responseBody.getJacksonString(View.Banner.class);
	}
	
	@RequestMapping(value="/api/getavailablebannerlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAvailableBannerList(HttpServletRequest request) {
		log.info("getAvailableBannerList ");
		ListResponseBody responseBody  = new ListResponseBody();

		
		List<Banner> list = bannerService.getAvailable();

		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		return responseBody.getJacksonString(View.Banner.class);
	}
	
	
	@RequestMapping(value = "/api/addbanner", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
					"multipart/form-data" })
	@ResponseBody
	public String addBanner(HttpServletRequest request, @RequestParam("data") String receiveJSONString,
			@RequestPart("file") MultipartFile[] files) {
		log.info("addBanner");
		MapResponseBody responseBody = new MapResponseBody();

		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			try {
				log.info("receiveJSONString:" + receiveJSONString);

				Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);
				String bannerId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
				banner.setBanner_id(bannerId);
				log.info("files.length :" + files.length);
				for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

					MultipartFile file = files[fileIndex];
					if (file != null && !file.getOriginalFilename().isEmpty()) {
						String imageName = file.getOriginalFilename();
						String extendName = FilenameUtils.getExtension(imageName);
						String finalFileName = bannerId + "." + extendName;

						File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

						if (ImageUtils.writeFile(file, finalFile)) {
							banner.setBanner_image_file(finalFileName);
						}
					}
				}

				bannerService.addBanner(banner);
				responseBody.setCode(Constants.INT_SUCCESS);
			} catch (Exception e) {
				log.error("Exception :" + e.getMessage());
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}
		return responseBody.getJacksonString(View.Banner.class);
	}
	
	@RequestMapping(value = "/api/updatebanner", produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
			"multipart/form-data" })
	@ResponseBody
	public String updateBanner(HttpServletRequest request, @RequestParam("data") String receiveJSONString,
			@RequestPart("file") MultipartFile[] files) {
		log.info("updateBanner");
		MapResponseBody responseBody = new MapResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			try {

				Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);
				String bannerId = banner.getBanner_id();
				for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

					MultipartFile file = files[fileIndex];
					if (file != null && !file.getOriginalFilename().isEmpty()) {
						String imageName = file.getOriginalFilename();
						String extendName = FilenameUtils.getExtension(imageName);
						String finalFileName = bannerId + "." + extendName;

						File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

						if (ImageUtils.writeFile(file, finalFile)) {
							banner.setBanner_image_file(finalFileName);
						}
					}
				}

				bannerService.updateBanner(banner);
				responseBody.setCode(Constants.INT_SUCCESS);
			} catch (Exception e) {
				log.error("Exception :" + e.getMessage());
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}

		return responseBody.getJacksonString(View.Banner.class);
	}
	
	
	@RequestMapping(value="/api/deletebanner", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String deleteBanner(HttpServletRequest request,@RequestBody String receiveJSONString) {
		log.info("deleteBanner");

		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			Banner banner =  (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);
			log.info("banner :"+banner.getBanner_id());
		
			//TODO delete banner file
			bannerService.deleteBanner(banner);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);
		
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}
		
		return responseBody.getJacksonString(View.Banner.class);
	}
	
	
	@RequestMapping(value="/api/updatebannerorder", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateBannerOrder(HttpServletRequest request,@RequestBody String receiveJSONString) {
		

		
		BeanResponseBody responseBody  = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			List<Banner> list = (List<Banner>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<Banner>>(){});
			bannerService.updateBannerList(list);
			responseBody.setCode(Constants.INT_SUCCESS);
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}
		
		return responseBody.getJacksonString(View.Banner.class);
	}
	
	
	@RequestMapping(value="/api/getsearchfield", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getSearchField(HttpServletRequest request) {
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<PatentField>  list  = fieldService.getSearableFields();
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString(View.Public.class);
	}
	
	@RequestMapping(value="/api/getallfield", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAllField(HttpServletRequest request) {
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<PatentField>  list  = fieldService.getAllFields();
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		
		
		return responseBody.getJacksonString(View.Public.class);
	}
	
	
	@RequestMapping(value="/api/getallfieldsync", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAllFieldSync(HttpServletRequest request) {
		ListResponseBody responseBody  = new ListResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean!=null) {
			List<FieldSync>  list  = fieldSyncService.getAll();
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setList(list);
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		
		
		return responseBody.getJacksonString(View.Public.class);
	}
}
