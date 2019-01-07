package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;




@Repository("assigneeDao")
public class AssigneeDaoImpl extends AbstractDao<String,  Assignee> implements AssigneeDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Assignee getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public void create(Assignee assignee) {
		persist(assignee);
	}

}
