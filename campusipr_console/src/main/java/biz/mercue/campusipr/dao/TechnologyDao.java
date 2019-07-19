package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Technology;

public interface TechnologyDao {
    void create(Technology technology);

    Technology getById(String id);

    void delete(String id);

    void delete(String businessId, String id);
}
