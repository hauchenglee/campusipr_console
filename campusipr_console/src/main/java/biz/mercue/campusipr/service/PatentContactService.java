package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.PatentContact;

import java.util.List;

public interface PatentContactService {
    List<PatentContact> getDefaultContactByBusiness(String businessId);

    void updateContactByBusiness(PatentContact editContact, String contactId, Business business);
}
