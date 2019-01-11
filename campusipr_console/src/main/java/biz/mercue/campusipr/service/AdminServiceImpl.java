package biz.mercue.campusipr.service;


import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.EndDocument;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.AdminTokenDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.AdminToken;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;
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
			return Constants.INT_CANNOT_FIND_DATA;
			
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
	public int checkPassword(String adminId, String password) {

		Admin dbBean = dao.getById(adminId);

		if (dbBean != null) {
			try {
				if (encoder.matches(password, dbBean.getAdmin_password())) {
					return Constants.INT_SUCCESS;
				} else {
					log.info("login fail, password error");
					return Constants.INT_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				return Constants.INT_SYSTEM_PROBLEM;
			}
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}
//	@Override
//	public List<Admin> getListByBusinessId(String businessId) {
//		List<Admin> list = dao.getByBusinessId(businessId);
//		return list;
//	}
	
	
//	@Override
//	public List<Admin> getAllBusiness(){
//		return dao.getAllAdminList();
//	}
	
	@Override
	public ListQueryForm getRoleBusinessAdminList(String roleId,String businessId,int page){
		int cout = dao.getRoleBusinessAdminCount(roleId, businessId);
		List list = dao.getRoleBusinessAdminList(roleId, businessId,page,Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		
		return form;
	}
	@Override
	public ListQueryForm getRoleAdminList(String roleId,int page){
		int cout = dao.getRoleBusinessAdminCount(roleId,null);
		List list = dao.getRoleBusinessAdminList(roleId,null,page,Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		return form;
	}
	
	@Override
	public ListQueryForm searchRoleAdminList(String roleId,String businessId,String text,int page) {
		int cout = dao.searchRoleAdminListCount(roleId,businessId,text);
		List list = dao.searchRoleAdminList(roleId, businessId, text, page, Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		return form;
	}

	@Override
	public int createAdmin(Admin admin) {
		Admin dbBean = dao.getByEmail(admin.getAdmin_email());
		try{
			if(dbBean == null){
	
				admin.setAdmin_id(KeyGeneratorUtils.generateRandomString());
				if(!StringUtils.isNULL(admin.getAdmin_password())) {
					admin.setAdmin_password(encoder.encode(admin.getAdmin_password()));
				}

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
				dbBean.setAdmin_unit_name(admin.getAdmin_unit_name());
				dbBean.setRole(admin.getRole());
				dbBean.setUpdate_date(new Date());
				dbBean.setAvailable(admin.isAvailable());
				
				if(!StringUtils.isNULL(admin.getAdmin_password())){
					dbBean.setAdmin_password(encoder.encode(admin.getAdmin_password()));
				}
	
				return Constants.INT_SUCCESS;
			}else{
				
				return Constants.INT_CANNOT_FIND_DATA;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}
	
	@Override
	public int updatePassword(String adminId,String password) {
		Admin dbBean = dao.getById(adminId);
		try{
			if(dbBean != null){

				dbBean.setAdmin_password(encoder.encode(password));
				
	
				return Constants.INT_SUCCESS;
			}else{
				
				return Constants.INT_CANNOT_FIND_DATA;
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
			
			return Constants.INT_CANNOT_FIND_DATA;
		}

	}
	

	
//	@Override
//	public List<Permission> getPermissionById(String adminId){
//		List<Permission> list = new ArrayList<Permission>();
//		Admin admin = getById(adminId);
//		if(admin != null && admin.getRole() != null){
//			list = admin.getRole().getPermissionList();
//			log.info("permisstion list :"+list.size());
//		}
//		return list;
//		
//	}
	
	@Override
	public Admin getByEmail(String email){
		Admin admin = dao.getByEmail(email);
		if(admin!=null) {
			Business business = admin.getBusiness();
			if(business!=null){
				log.info(business.getBusiness_name());
			}
		}
		return admin;
	}

//	@Override
//	public Map<String, Admin> getMapByBusinessId(String businessId) {
//		List<Admin> list = getListByBusinessId(businessId);
//		
//		Map<String,Admin> map = new HashMap<String,Admin>();
//		if(list.size() > 0){
//			for(Admin admin : list){
//				map.put(admin.getAdmin_id(), admin);
//			}
//		}
//		return map;
//	}
	
	

}
