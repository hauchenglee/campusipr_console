package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.AnnuityReminderDao;
import biz.mercue.campusipr.dao.BusinessDao;
import biz.mercue.campusipr.dao.SynchronizeBusinessDao;
import biz.mercue.campusipr.dao.SynchronizeTaskDao;
import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("businessService")
@Transactional
public class BusinessServiceImpl implements BusinessService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private SynchronizeBusinessDao syncDao;

    @Autowired
    private AnnuityReminderDao annuityReminderDao;

    @Autowired
    private SynchronizeTaskDao syncTaskDao;

    @Autowired
    private QuartzService quartzService;

    @Override
    public void addBusiness(Business business) {
        if (StringUtils.isNULL(business.getBusiness_id())) {
            business.setBusiness_id(KeyGeneratorUtils.generateRandomString());
        }
        businessDao.create(business);
    }

    @Override
    public int safeAddBusiness(Business business) {
        String businessId = business.getBusiness_id();
        if (!StringUtils.isNULL(businessId)) {
            return Constants.INT_DATA_ERROR;
        }
        business.setBusiness_id(KeyGeneratorUtils.generateRandomString());
        for (Integer defaultReminderDay : Constants.defaultReminderDays) {
            AnnuityReminder anuityReminder = new AnnuityReminder();
            anuityReminder.setReminder_id(KeyGeneratorUtils.generateRandomString());
            anuityReminder.setReminder_text("專利{@patent_number}將於{@charges_edate}到期，請記得聯絡事務所進行繳費");
            anuityReminder.setBusiness(business);
            anuityReminder.setEmail_day(defaultReminderDay);
            anuityReminder.setCreate_date(new Date());
            anuityReminder.setAvailable(true);
            annuityReminderDao.create(anuityReminder);
        }
        business.setCreate_date(new Date());
        addBusiness(business);
        return Constants.INT_SUCCESS;
    }

    @Override
    public int updateBusiness(Business business) {
        Business dbBean = businessDao.getById(business.getBusiness_id());
        int result = -1;
        try {
            if (dbBean != null) {
                dbBean.setBusiness_alias(business.getBusiness_alias());
                dbBean.setBusiness_alias_en(business.getBusiness_alias_en());
                dbBean.setBusiness_name(business.getBusiness_name());
                dbBean.setBusiness_name_en(business.getBusiness_name_en());
                dbBean.setAvailable(business.isAvailable());

                dbBean.setContact_name(business.getContact_name());
                dbBean.setContact_email(business.getContact_email());
                dbBean.setContact_tel(business.getContact_tel());
                dbBean.setContact_tel_extension(business.getContact_tel_extension());
                dbBean.setContact_phone(business.getContact_phone());
                dbBean.setUpdate_date(new Date());

                //add annuity reminder
                if (business.isAvailable()) {
                    List<AnnuityReminder> listReminder = annuityReminderDao.getByBusinessId(business.getBusiness_id());
                    if (listReminder.isEmpty()) {
                        for (Integer defaultReminderDay : Constants.defaultReminderDays) {
                            AnnuityReminder anuityReminder = new AnnuityReminder();
                            anuityReminder.setReminder_id(KeyGeneratorUtils.generateRandomString());
                            anuityReminder.setReminder_text("專利{@patent_number}將於{@charges_edate}到期，請記得聯絡事務所進行繳費");
                            anuityReminder.setBusiness(business);
                            anuityReminder.setEmail_day(defaultReminderDay);
                            anuityReminder.setCreate_date(new Date());
                            anuityReminder.setAvailable(true);
                            anuityReminder.setIs_user_define(false);
                            annuityReminderDao.create(anuityReminder);
                        }
                    } else {
                        for (AnnuityReminder reminder : listReminder) {
                            reminder.setAvailable(true);
                        }
                    }
                } else {
                    List<AnnuityReminder> listReminder = annuityReminderDao.getByBusinessId(business.getBusiness_id());
                    if (!listReminder.isEmpty()) {
                        for (AnnuityReminder reminder : listReminder) {
                            reminder.setAvailable(false);
                        }
                    }
                }
                // 如果更新business，也更新相對應的patent contact
                if (business.isAvailable()) {
                    quartzService.createUpdateContactJob(business.getBusiness_id());
                }

                result = Constants.INT_SUCCESS;
            } else {
                result = Constants.INT_CANNOT_FIND_DATA;
            }
        } catch (Exception e) {
            log.error(e);
        }
        return result;
    }

    @Override
    public void deleteBusiness(Business business) throws Exception {
        Business dbBean = businessDao.getById(business.getBusiness_id());
        if (dbBean != null) {
            businessDao.delete(dbBean.getBusiness_id());
            SynchronizeTask syncTaskDB = syncTaskDao.getAvailableBusinessId(business.getBusiness_id());
            syncTaskDao.delete(syncTaskDB.getTask_id());
            quartzService.removeJob(syncTaskDB);
        }
    }

    @Override
    public ListQueryForm getAllByPage(int page) {
        List<Business> businessesList = businessDao.getAllByPage(page, Constants.SYSTEM_PAGE_SIZE);
        int count = businessDao.getAllCount();
        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, businessesList);
    }

    @Override
    public ListQueryForm getAll() {
        List<Business> businessesList = businessDao.getAll();
        int count = businessDao.getAllCount();
        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, businessesList);
    }

    @Override
    public ListQueryForm search(String text, int page) {
        List<Business> businessesList = businessDao.search(text, page, Constants.SYSTEM_PAGE_SIZE);
        int count = businessDao.searchCount(text);
        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, businessesList);
    }

    @Override
    public List<Business> getAvailable(int page, int pageSize) {
        return businessDao.getAvailable(page, pageSize);
    }

    @Override
    public Business getById(String id) {
        return businessDao.getById(id);
    }

    @Override
    public List<Business> getByName(String name) {
        return businessDao.getByName(name);
    }

    @Override
    public List<Business> getByFuzzyName(String name) {
        return businessDao.getByName(name);
    }
}
