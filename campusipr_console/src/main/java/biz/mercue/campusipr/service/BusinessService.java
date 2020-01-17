package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;

import java.util.List;

public interface BusinessService {

    void addBusiness(Business business);

    int safeAddBusiness(Business business);

    int updateBusiness(Business business);

    void deleteBusiness(Business business) throws Exception;

    ListQueryForm getAllByPage(int page);

    ListQueryForm getAll();

    ListQueryForm search(String text, int page);

    List<Business> getAvailable(int page, int pageSize);

    Business getById(String id);

    List<Business> getByName(String name);

    List<Business> getByFuzzyName(String name);
}
