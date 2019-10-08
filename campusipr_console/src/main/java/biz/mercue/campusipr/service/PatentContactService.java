package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.PatentContact;

import java.util.List;

public interface PatentContactService {
    PatentContact getById(String contactId);

    List<PatentContact> getDefaultContactByBusiness(String businessId);

    List<String> getDefaultContactIdsByBusiness(String businessId);

    void updateContactByBusiness(PatentContact editContact, String contactId, Business business);
}
