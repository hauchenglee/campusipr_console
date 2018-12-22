package biz.mercue.campusipr.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.AdminToken;




@Repository("adminTokenDao")
public class AdminTokenDaoImpl extends AbstractDao<String,  AdminToken> implements AdminTokenDao {

	@Override
	public int addToken(AdminToken token) {
		persist(token);
		return 0;
	}
	
	
	@Override
	public AdminToken getById(String token) {
	
		return getByKey(token);
	}

	@Override
	public List<AdminToken> getByUserId(String userId) {
		Criteria criteria = createEntityCriteria();	
		criteria.createAlias("admin", "admin");  
		criteria.add(Restrictions.eq("admin.admin_id", userId));
		return criteria.list();
	}

	@Override
	public List<AdminToken> getByAvailableUserId(String userId) {
		Criteria criteria = createEntityCriteria();	
		criteria.createAlias("admin", "admin");  
		criteria.add(Restrictions.eq("admin.admin_id", userId));
		criteria.add(Restrictions.eq("available", true));
		return criteria.list();
	}

}
