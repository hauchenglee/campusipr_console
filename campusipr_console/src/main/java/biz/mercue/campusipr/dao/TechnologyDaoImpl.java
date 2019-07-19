package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Technology;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository("technologyDao")
public class TechnologyDaoImpl extends AbstractDao<String, Technology> implements TechnologyDao {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void create(Technology technology) {
        persist(technology);
    }

    @Override
    public Technology getById(String id) {
        return getByKey(id);
    }

    @Override
    public void delete(String id) {
        Technology dbBean = getByKey(id);
        if (dbBean != null) {
            delete(dbBean);
        }
    }

    @Override
    public void delete(String businessId, String id) {

    }
}
