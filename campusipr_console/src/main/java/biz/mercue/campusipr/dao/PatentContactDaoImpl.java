package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.PatentContact;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatentContactDaoImpl extends AbstractDao<String, PatentContact> implements PatentContactDao {
    @Override
    public PatentContact getById(String id) {
        return getByKey(id);
    }

    @Override
    public List<PatentContact> getDefaultContactByBusiness(String businessId) {
        String hql = "select pc from PatentContact as pc join pc.business as pcb" +
                " where pc.contact_order = :defaultOrder and pcb.business_id = :businessId";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("defaultOrder", 0);
        query.setParameter("businessId", businessId);
        return query.list();
    }

    @Override
    public List<String> getDefaultContactIdsByBusiness(String businessId) {
        String hql = "select pc.patent_contact_id from PatentContact as pc join pc.business as pcb" +
                " where pc.contact_order = :defaultOrder and pcb.business_id = :businessId";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("defaultOrder", 0);
        query.setParameter("businessId", businessId);
        return query.list();
    }
}
