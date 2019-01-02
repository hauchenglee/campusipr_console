package biz.mercue.campusipr.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.AdminTokenDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;





@Service("adminTokenService")
@Transactional
public class AdminTokenServiceImpl implements AdminTokenService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	


	@Autowired
	private AdminTokenDao dao;
	
	@Autowired
	private AdminDao adminDao;
	

	@Override
	public AdminToken getById(String token) {
		if(StringUtils.isNULL(token)) {
			return null;
		}
		AdminToken tokenBean = dao.getById(token);
		if(tokenBean != null && tokenBean.getAdmin() !=null && tokenBean.getAdmin().getRole() !=null ){
			
			List<Permission> permissionList = tokenBean.getAdmin().getRole().getPermissionList();
			
			tokenBean.setPermissionList(tokenBean.getAdmin().getRole().getPermissionList());
			tokenBean.setBusiness_id(tokenBean.getBusiness().getBusiness_id());
		}
		
		
		
		return tokenBean;
	}



	@Override
	public int logout(String adminId) {
		if(StringUtils.isNULL(adminId)) {
			return Constants.INT_SYSTEM_PROBLEM;
		}
		log.info("logout admin :"+adminId);

		
		List<AdminToken> list  = dao.getByUserId(adminId);
		list.forEach(bean->{
			bean.setAvailable(false);
		});
		return Constants.INT_SUCCESS;
	}



	@Override
	public AdminToken generateToken(String adminId) {
		log.info("generateToken");
		List<Permission> permissionList = null;
		Business business = null;
		logout(adminId);
		Admin adminBean = adminDao.getById(adminId);
		if(adminBean!=null){
			log.info("admin is not null");
			if(adminBean.getRole() != null){
				log.info("role is not null");
				permissionList  = adminBean.getRole().getPermissionList();
				log.info("permissionList size "+permissionList.size());
				
			}
			business = adminBean.getBusiness();
			log.info("business "+business.getBusiness_id());
			log.info("business "+business.getBusiness_name());
			
			
			AdminToken tokenBean  = new  AdminToken();
			tokenBean.setAdmin_token_id(KeyGeneratorUtils.generateRandomString());
			tokenBean.setAdmin(adminBean);
			tokenBean.setAvailable(true);
			tokenBean.setLogin_date(new Date());
			tokenBean.setBusiness(business);
			tokenBean.setPermissionList(permissionList);
			Calendar cal  = Calendar.getInstance();
			cal.add(Calendar.DATE, 60);
			tokenBean.setExpire_date(cal.getTime()); //add 60 days to expire
			
			
			tokenBean.setLogin_date(new Date());
			tokenBean.setCreate_date(new Date());
			tokenBean.setUpdate_date(new Date());
			
			dao.addToken(tokenBean);
			return tokenBean;
		}else{
			return null;
		}
	}



	@Override
	public AdminToken checkAvailable(String token) {
		AdminToken bean = dao.getById(token);
		if(bean != null && bean.isAvailable()){
			return bean;
		}else{
			return null;
		}
	}



	@Override
	public void updateToken(AdminToken bean) {
		// TODO Auto-generated method stub
		
	}



	

	
	

}
