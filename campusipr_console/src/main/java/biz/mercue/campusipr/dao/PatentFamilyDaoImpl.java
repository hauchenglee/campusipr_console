package biz.mercue.campusipr.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
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
	public PatentFamily getByPatentIdAndBusinessId(String patentId, String businessId) {
		String hql = "select pf from PatentFamily as pf inner join pf.listPatent pflp where pflp.patent_id = :patent_id and pf.business_id = :business_id";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("patent_id", patentId);
		query.setParameter("business_id", businessId);

		return (PatentFamily) query.uniqueResult();
	}

	@Override
	public List<String> getPatentIds(String familyId) {
		String hql = "select pflp.patent_id from PatentFamily pf inner join pf.listPatent pflp where pf.patent_family_id = :familyId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("familyId", familyId);

		return query.list();
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
