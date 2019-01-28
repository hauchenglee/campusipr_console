package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.FieldSync;




@Repository("fieldSyncDao")
public class FieldSyncDaoImpl extends AbstractDao<String,  FieldSync> implements FieldSyncDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
	

	@Override
	public List<FieldSync> getAll(){
		Criteria criteria =  createEntityCriteria();
		return criteria.list();
	}
	
}
