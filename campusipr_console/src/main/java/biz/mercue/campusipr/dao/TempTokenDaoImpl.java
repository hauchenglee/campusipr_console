package biz.mercue.campusipr.dao;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


import biz.mercue.campusipr.model.TempToken;



@Repository("tempTokenDao")
public class TempTokenDaoImpl extends AbstractDao<String,  TempToken> implements TempTokenDao {

	
	private Logger log = Logger.getLogger(this.getClass().getName());
	@Override
	public TempToken getById(String id) {

		return getByKey(id);
	}
	
	
	@Override
	public TempToken getByIdAndAdmin(String tokenId,String adminId) {
		Criteria crit = createEntityCriteria();	
		crit.createAlias("admin", "admin");
		crit.add(Restrictions.eq("admin.admin_id", adminId));

		crit.add(Restrictions.eq("token_id", tokenId));
		return (TempToken) crit.uniqueResult();
	}

	
	@Override
	public TempToken getAvailableByAdmin(String adminId) {
		Criteria crit = createEntityCriteria();	
		crit.createAlias("admin", "admin");
		crit.add(Restrictions.eq("admin.admin_id", adminId));

		crit.add(Restrictions.eq("available", true));
		return (TempToken) crit.uniqueResult();
	}
	
	
	@Override
	public void createToken(TempToken bean) {

		persist(bean);
	}
	
	@Override
	public void deleteToken(TempToken bean) {

		delete(bean);
	}
	
	




}
