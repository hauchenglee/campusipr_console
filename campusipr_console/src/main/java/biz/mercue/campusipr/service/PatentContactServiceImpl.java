package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.PatentContactDao;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.PatentContact;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatentContactServiceImpl implements PatentContactService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private PatentContactDao patentContactDao;

    @Override
    public List<PatentContact> getDefaultContactByBusiness(String businessId) {
        return patentContactDao.getDefaultContactByBusiness(businessId);
    }

    @Override
    public void updateContactByBusiness(PatentContact editContact, String contactId, Business business) {
        PatentContact dbContact = patentContactDao.getById(contactId);
        dbContact.setPatent(editContact.getPatent());
        dbContact.setAdmin(editContact.getAdmin());
        dbContact.setBusiness(business);
        dbContact.setContact_name(business.getContact_name());
        dbContact.setContact_email(business.getContact_email());
        dbContact.setContact_phone(business.getContact_phone());
        dbContact.setContact_character(editContact.getContact_character());
        dbContact.setCreate_date(editContact.getCreate_date());
        dbContact.setContact_order(0);
    }
}
