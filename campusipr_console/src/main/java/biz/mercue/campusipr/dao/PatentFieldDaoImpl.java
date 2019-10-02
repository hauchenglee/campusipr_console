package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.PatentField;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatentFieldDaoImpl extends AbstractDao<String, PatentField> implements PatentFieldDao {
    @Override
    public List<PatentField> getAllExcelExportFields() {
        String hql = "select pf from PatentField as pf where pf.field_order between 2 and 109 order by pf.field_order";
        Session session = getSession();
        Query query = session.createQuery(hql);
        return query.list();
    }
}
