package biz.mercue.campusipr.dao;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.PatentContact;

import java.util.List;

public interface PatentContactDao {
    PatentContact getById(String id);

    List<PatentContact> getDefaultContactByBusiness(String businessId);

    List<String> getDefaultContactIdsByBusiness(String businessId);
}
