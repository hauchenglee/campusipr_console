package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentFamily;




@Repository("familyDao")
public class PatentFamilyDaoImpl extends AbstractDao<String,  PatentFamily> implements PatentFamilyDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	@Override
	public PatentFamily getById(String id){
		return getByKey(id);
	}

	@Override
	public void create(PatentFamily patent) {
		persist(patent);
	}
	
	@Override
	public void delete(String id) {
		PatentFamily dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}

	
	

	

}
