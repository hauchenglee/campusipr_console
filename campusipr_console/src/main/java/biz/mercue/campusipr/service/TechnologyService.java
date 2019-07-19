package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Technology;

public interface TechnologyService {
    Technology getById(String id);

    int addTechnology(Technology technology);
}
