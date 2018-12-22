package biz.mercue.campusipr.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.RoleDao;
import biz.mercue.campusipr.model.Role;
import biz.mercue.campusipr.util.Constants;



@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private RoleDao dao;
	
//	@Override
//	public List<Role> getAllBusinessRole(String businessId) {
//		List<Role> list = dao.getAllBusinessRole(businessId);
//		list.forEach(bean->{
//			log.info(bean.getPermissionList());
//			
//		});
//		return list;
//	}
	
	
//	@Override
//	public int addRole(Role role) {
//		Role dbBean = dao.getByName(role.getBusiness().getBusiness_id(),role.getRole_name());
//		if(dbBean == null){
//			dao.addRole(role);
//			return Constants.INT_SUCCESS;
//		}else {
//			return Constants.INT_DATA_DUPLICATE;
//		}
//	}
	
	
	@Override
	public int updateRole(Role role) {
		
		Role dbBean = dao.getById(role.getRole_id());
		if(dbBean != null){
			dbBean.setRole_name(role.getRole_name());
			dbBean.setPermissionList(role.getPermissionList());
		}
		return 0;
	}
	
	
//	@Override
//	public int deleteRole(Role role) {
//		
//		Role dbBean = dao.getById(role.getRole_id(), role.getBusiness().getBusiness_id());
//		if(dbBean != null){
//			dao.deleteRole(role);
//		}
//		return 0;
//	}

}
