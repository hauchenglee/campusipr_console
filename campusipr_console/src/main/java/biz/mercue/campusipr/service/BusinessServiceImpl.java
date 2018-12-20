package biz.mercue.campusipr.service;




import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.BusinessDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;




@Service("businessService")
@Transactional
public class BusinessServiceImpl implements BusinessService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private BusinessDao businessDao;

	
	@Override
	public void addBusiness(Business business) {
		if(StringUtils.isNULL(business.getBusiness_id())) {
			business.setBusiness_id(KeyGeneratorUtils.generateRandomString());
		}
		businessDao.create(business);
	}
	
	@Override
	public int saveAddBusiness(Business business) {
		//add anuityReminder
		addBusiness(business);
		
		return 0;
	}

	
	@Override
	public int updateBusiness(Business business) {
		
		Business dbBean =businessDao.getById(business.getBusiness_id());

		if(dbBean!=null){
			dbBean.setBusiness_alias(business.getBusiness_alias());
			dbBean.setBusiness_alias_en(business.getBusiness_alias_en());
			dbBean.setBusiness_name(business.getBusiness_name());
			dbBean.setBusiness_name_en(business.getBusiness_name_en());
			dbBean.setAvailable(business.isAvailable());
			
			dbBean.setContact_name(business.getContact_name());
			dbBean.setContact_email(business.getContact_email());
			dbBean.setContact_tel(business.getContact_tel());
			dbBean.setContact_tel_extension(business.getContact_tel_extension());
			dbBean.setContact_phone(business.getContact_phone());
			dbBean.setUpdate_date(new Date());
		}
		return 0;
	}

	
	@Override
	public void deleteBusiness(Business business) {
		Business dbBean =businessDao.getById(business.getBusiness_id());

		if(dbBean!=null){
			businessDao.delete(dbBean.getBusiness_id());
		}
	}
	
	
	@Override
	public List<Business> getAll(){
		return businessDao.getAll();
	}

	
	@Override
	public List<Business> getAvailable(int page,int pageSize){
		return businessDao.getAvailable(page, pageSize);
	}
	
	@Override
	public Business getById(String id) {
		log.info("get by id: " + id);
		Business bean = businessDao.getById(id);

		return bean;
	}
	
	
	@Override
	public List<Business> getByName(String name){
		return businessDao.getByName(name);
	}
	
	@Override
	public List<Business> getByFuzzyName(String name){
		return businessDao.getByName(name);
	}
	




	
}
