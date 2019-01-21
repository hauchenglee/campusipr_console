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
	
	@Override
	public void delete(String id) {
		Assignee dbBean = getByKey(id);
		if(dbBean!=null){
			delete(dbBean);
		}
	}
	
	@Override
	public List<Assignee> getByPatentId(String patentId) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		return criteria.list();
	}

}
