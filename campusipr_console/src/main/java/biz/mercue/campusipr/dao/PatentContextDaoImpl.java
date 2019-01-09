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
import biz.mercue.campusipr.model.PatentContext;




@Repository("patentContextDao")
public class PatentContextDaoImpl extends AbstractDao<String,  PatentContext> implements PatentContextDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public PatentContext getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public void create(PatentContext pc) {
		persist(pc);
	}

}
