package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;




@Repository("inventorDao")
public class InventorDaoImpl extends AbstractDao<String,  Inventor> implements InventorDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Inventor getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public void create(Inventor inventor) {
		persist(inventor);
	}
	
	@Override
	public void delete(String id) {
		Inventor dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<Inventor> getByPatentId(String patentId) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		return criteria.list();
	}

}
