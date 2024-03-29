package biz.mercue.campusipr.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;




@Repository("patentEditHistoryDao")
public class PatentEditHistoryDaoImpl extends AbstractDao<String,  PatentEditHistory> implements PatentEditHistoryDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public PatentEditHistory getById(String id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}

	@Override
	public List<PatentEditHistory> getByPatentId(String patentId) {
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}

	@Override
	public List<PatentEditHistory> getByPatentAndField(String patentId, String fieldId, String businessId,int page,int pageSize) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("admin","admin");
			Criterion re1 = Restrictions.eq("admin.business.business_id", businessId);
			Criterion re2 = Restrictions.eq("admin.admin_id", Constants.SYSTEM_ADMIN);
//			Criterion re3 = Restrictions.eq("admin.business.business_id", Constants.SYSTEM_ADMIN);
			criteria.add(Restrictions.or(re1,re2));
		}
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		criteria.add(Restrictions.eq("field_id", fieldId));
		criteria.add(Restrictions.or(Restrictions.eq("business_id", Constants.SYSTEM_ADMIN),Restrictions.eq("business_id", businessId)));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}

//	@Override
	public List<PatentEditHistory> getSysHistory(String patentId, String fieldId, String businessId,int page,int pageSize) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("admin","admin");
			Criterion re1 = Restrictions.eq("admin.business.business_id", businessId);
			Criterion re2 = Restrictions.eq("admin.admin_id", Constants.SYSTEM_ADMIN);
//			Criterion re3 = Restrictions.or(Restrictions.eq("admin.business.business_id", businessId),Restrictions.eq("admin.business.business_id", Constants.SYSTEM_ADMIN));
			criteria.add(Restrictions.or(re1,re2));
		}
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		criteria.add(Restrictions.eq("field_id", fieldId));
		criteria.add(Restrictions.eq("business_id", businessId));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	@Override
	public int countByPatentAndField(String patentId, String fieldId, String businessId) {
		// TODO Auto-generated method stub
		Criteria criteria =  createEntityCriteria();
		if(!StringUtils.isNULL(businessId)) {
			criteria.createAlias("admin","admin");
			Criterion re1 = Restrictions.eq("admin.business.business_id", businessId);
			Criterion re2 = Restrictions.eq("admin.admin_id", Constants.SYSTEM_ADMIN);
			criteria.add(Restrictions.or(re1,re2));
		}
		criteria.createAlias("patent","patent");
		criteria.add(Restrictions.eq("patent.patent_id", patentId));
		criteria.add(Restrictions.eq("field_id", fieldId));
		criteria.add(Restrictions.eq("business_id", businessId));
		criteria.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		return (int)count;
	}
	
}
