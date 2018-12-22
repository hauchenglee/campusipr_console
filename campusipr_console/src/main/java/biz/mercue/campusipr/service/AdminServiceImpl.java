package biz.mercue.campusipr.service;


import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.AdminTokenDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Permission;
import biz.mercue.campusipr.model.Role;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;




@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private AdminDao dao;
	
	@Autowired
	private AdminTokenDao tokenDao;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	@Override
	public Admin getById(String id) {
		Admin admin = dao.getById(id);
		admin.getBusiness();
		return admin;
	}

	@Override
	public int login(String email, String password) {
		
		Admin dbBean = dao.getByEmail(email);
		
		if(dbBean != null){
			try {
				if (encoder.matches(password, dbBean.getAdmin_password())) {
					
					Role roleBean = dbBean.getRole();
					if(roleBean!= null) {

						List<Permission> list = roleBean.getPermissionList();
						boolean hasPermission = false;
						
						if(!list.isEmpty()){
							hasPermission = true;
						}
						
						if(hasPermission){
							return Constants.INT_SUCCESS;
						}else{
							return Constants.INT_NO_PERMISSION;
						}
					}else {
						return Constants.INT_NO_PERMISSION;
					}
				} else {
					log.info("login fail, password error");
					return Constants.INT_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				log.error("Exception : " + e.getMessage());
			} 
			
		}else{
			return Constants.INT_CANNOT_FIND_USER;
			
		}
		
		return Constants.INT_SYSTEM_PROBLEM;
	}

	@Override
	public int logout(String adminId) {
		List<AdminToken> list  = tokenDao.getByAvailableUserId(adminId);
		list.forEach(bean->{
			bean.setAvailable(false);
		});
		return Constants.INT_SUCCESS;
	}

	@Override
	public List<Admin> getListByBusinessId(String businessId) {
		List<Admin> newlist = new ArrayList<Admin>();
		List<Admin> list = dao.getByBusinessId(businessId);
		for(Admin bean : list) {

			if(bean != null){
				Admin newadmin = new Admin();
				newadmin.setAdmin_id(bean.getAdmin_id());
				newadmin.setAdmin_name(bean.getAdmin_name());
				newadmin.setAdmin_email(bean.getAdmin_email());
				newadmin.setAvailable(bean.isAvailable());
				if(bean.getRole()!=null) {
					newadmin.setRole_name(bean.getRole().getRole_name());
				}else {
					newadmin.setRole_name("");
				}
				newadmin.setBusiness(bean.getBusiness());
				newlist.add(newadmin);
			}
		}
		
		return newlist;
	}

	@Override
	public int createAdmin(Admin admin) {
		Admin dbBean = dao.getByEmail(admin.getAdmin_email());
		try{
			if(dbBean == null){
				admin.setAdmin_id(KeyGeneratorUtils.generateRandomString());
				admin.setAdmin_password(encoder.encode(admin.getAdmin_password()));
				admin.setCreate_date(new Date());
				admin.setUpdate_date(new Date());
				
				
				dao.createAdmin(admin);
				
				return Constants.INT_SUCCESS;
			}else{
				log.info("e-mail:"+dbBean.getAdmin_email()+",business_id:"+dbBean.getBusiness().getBusiness_id());
				return Constants.INT_USER_DUPLICATE;
			}
		}catch(Exception e){
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}
	
	@Override
	public int updateAdmin(Admin admin) {
		Admin dbBean = dao.getById(admin.getAdmin_id());
		try{
			if(dbBean != null){
			
				dbBean.setAdmin_email(admin.getAdmin_email());
				dbBean.setAdmin_name(admin.getAdmin_name());
				dbBean.setRole(admin.getRole());
				dbBean.setUpdate_date(new Date());
				
				if(!StringUtils.isNULL(admin.getAdmin_password())){
					dbBean.setAdmin_password(StringUtils.generatePasswordHash(admin.getAdmin_password()));
				}
	
				return Constants.INT_SUCCESS;
			}else{
				
				return Constants.INT_CANNOT_FIND_USER;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}
	

	@Override
	public int deleteAdmin(Admin admin) {
		Admin dbBean = dao.getById(admin.getAdmin_id());
		if(dbBean != null){
		
			dao.deleteAdmin(dbBean);
			return Constants.INT_SUCCESS;
		}else{
			
			return Constants.INT_CANNOT_FIND_USER;
		}

	}
	

	
	@Override
	public List<Permission> getPermissionById(String adminId){
		List<Permission> list = new ArrayList<Permission>();
		Admin admin = getById(adminId);
		if(admin != null && admin.getRole() != null){
			list = admin.getRole().getPermissionList();
			log.info("permisstion list :"+list.size());
		}
		return list;
		
	}
	
	@Override
	public Admin getByEmail(String email){
		Admin admin = dao.getByEmail(email);
		Business business = admin.getBusiness();
		if(business!=null){
			log.info(business.getBusiness_name());
		}
		return admin;
	}

	@Override
	public Map<String, Admin> getMapByBusinessId(String businessId) {
		List<Admin> list = getListByBusinessId(businessId);
		
		Map<String,Admin> map = new HashMap<String,Admin>();
		if(list.size() > 0){
			for(Admin admin : list){
				map.put(admin.getAdmin_id(), admin);
			}
		}
		return map;
	}
	
	

}
