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
import biz.mercue.campusipr.model.Inventor;




@Repository("applicantDao")
public class ApplicantDaoImpl extends AbstractDao<String,  Applicant> implements ApplicantDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Applicant getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}
	
	@Override
	public void create(Applicant appl) {
		persist(appl);
	}

}
