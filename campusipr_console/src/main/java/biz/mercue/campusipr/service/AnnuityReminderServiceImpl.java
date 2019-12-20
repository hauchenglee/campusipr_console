package biz.mercue.campusipr.service;

import biz.mercue.campusipr.dao.AnnuityReminderDao;
import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.ReminderDao;
import biz.mercue.campusipr.model.*;
import biz.mercue.campusipr.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("annuityReminderService")
@Transactional
public class AnnuityReminderServiceImpl implements AnnuityReminderService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private AnnuityReminderDao annuityReminderDao;

    @Autowired
    private PatentDao patentDao;

    @Autowired
    private ReminderDao reminderDao;

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private CountryDao countryDao;

    @Override
    public void create(AnnuityReminder reminder) {
        reminder.setCreate_date(new Date());
        annuityReminderDao.create(reminder);
    }

    @Override
    public int update(List<AnnuityReminder> reminders, String businessId) {
        List<Patent> patentList = patentDao.getByBusinessId(businessId);
        for (Patent patent : patentList) {
            List<ReminderTask> reminderList = reminderDao.getAvailableReminderByPatentId(patent.getPatent_id());
            log.info("remove reminder patent: " + patent.getPatent_id());
            for (ReminderTask reminder : reminderList) {
                reminderDao.delete(reminder.getTask_id());
                try {
                    quartzService.removeJob(reminder);
                } catch (Exception e) {
                    log.error(e);
                }
            }

            if (patent.getListAnnuity() == null || patent.getListAnnuity().size() <= 0) continue;

            List<Annuity> listAnnuity = patent.getListAnnuity();
            for (Annuity annuity : listAnnuity) {
                List<AnnuityReminder> listARSendRightNow = new ArrayList<>();
                Date now = DateUtils.getDayStart(new Date());
                for (AnnuityReminder annuityReminder : reminders) {
                    if (!annuityReminder.isAvailable()) continue;

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(annuity.getAnnuity_end_date());
                    calendar.add(Calendar.DATE, -annuityReminder.getEmail_day());

                    ReminderTask reminder = new ReminderTask();
                    reminder.setTask_id(KeyGeneratorUtils.generateRandomString());
                    reminder.setPatent_id(patent.getPatent_id());
                    reminder.setBusiness_id(annuityReminder.getBusiness().getBusiness_id());
                    reminder.setTask_type(ReminderTask.reminderTypeAnnuity);
                    reminder.setTask_date(calendar.getTime());
                    reminder.setReminder_day(annuityReminder.getEmail_day());
                    reminder.setIs_send(false);
                    reminder.setIs_remind(annuity.is_reminder());

                    boolean quartzResult1 = reminder.getTask_date().after(now);
                    boolean quartzResult2 = reminder.is_remind();
                    boolean quartzResult3 = reminder.is_send();
                    if (quartzResult1 && (quartzResult2 && !quartzResult3)) {
                        reminderDao.create(reminder);
                        try {
                            quartzService.createJob(reminder);
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }

                    boolean sendNowResult1 = reminder.getTask_date().equals(now); // 提醒日期為現在
                    boolean sendNowResult2 = annuity.getAnnuity_end_date().before(now); // 截止日大於今天（也就是過期了）
                    boolean sendNowResult3 = annuity.is_reminder(); // 是否要提醒
                    if ((sendNowResult1 || sendNowResult2) && sendNowResult3) {
                        listARSendRightNow.add(annuityReminder);
                        log.info("send right now List:" + listARSendRightNow.size());
                    }
                }

                if (listARSendRightNow.size() > 0) {
                    AnnuityReminder sendRemindInfo = listARSendRightNow.get(listARSendRightNow.size() - 1);
                    Calendar calendarNowSend = Calendar.getInstance();
                    calendarNowSend.setTime(annuity.getAnnuity_end_date());
                    calendarNowSend.add(Calendar.DATE, -sendRemindInfo.getEmail_day());

                    ReminderTask reminder = new ReminderTask();
                    reminder.setTask_id(KeyGeneratorUtils.generateRandomString());
                    reminder.setPatent_id(patent.getPatent_id());
                    reminder.setBusiness_id(sendRemindInfo.getBusiness().getBusiness_id());
                    reminder.setTask_type(ReminderTask.reminderTypeAnnuity);
                    reminder.setTask_date(calendarNowSend.getTime());
                    reminder.setReminder_day(sendRemindInfo.getEmail_day());
                    reminder.setIs_send(false);
                    reminder.setIs_remind(annuity.is_reminder());
                    reminder.setIs_send(true);
                    reminderDao.create(reminder);

                    MailSender mail = new MailSender();
                    Country country = countryDao.getByLanguage(patent.getPatent_appl_country(), "tw");
                    patent.setCountry_name(country.getCountry_name());
                    String annuity_date = DateUtils.getSimpleSlashFormatDate(annuity.getAnnuity_end_date());
                    patent.setAnnuity_date(annuity_date);
                    List<PatentContact> listContact = new ArrayList<>();
                    for (PatentContact contact : patent.getListContact()) {
                        log.info("contact: " + contact.getContact_email());
                        if (contact.getBusiness() != null) {
                            if (reminder.getBusiness_id().equals(contact.getBusiness().getBusiness_id())) {
                                listContact.add(contact);
                            }
                        }
                    }
                    if (!listContact.isEmpty()) {
                        mail.sendPatentAnnuityReminder(patent, listContact);
                    } else {
                        log.error("no contact for this patent");
                    }

                } // end  if (listARSendRightNow.size() > 0)
            } // end for listAnnuity loop
        } // end for patentList loop

        int successTime = 0;
        for (AnnuityReminder annuityReminder : reminders) {

            if (!StringUtils.isNULL(annuityReminder.getReminder_id())) {
                AnnuityReminder dbBean = annuityReminderDao.getById(annuityReminder.getReminder_id());

                if (dbBean != null) {
                    dbBean.setEmail_day(annuityReminder.getEmail_day());
                    dbBean.setPhone_day(annuityReminder.getPhone_day());
                    dbBean.setAvailable(annuityReminder.isAvailable());
                    dbBean.setUpdate_date(new Date());
                    successTime += 1;
                }
            } else {
                if (StringUtils.isNULL(annuityReminder.getReminder_id())) {
                    annuityReminder.setReminder_id(KeyGeneratorUtils.generateRandomString());
                }
                annuityReminder.setCreate_date(new Date());
                annuityReminderDao.create(annuityReminder);
                successTime += 1;
            }
        }
        if (successTime == reminders.size()) {
            return Constants.INT_SUCCESS;
        } else {
            return Constants.INT_CANNOT_FIND_DATA;
        }
    }

    @Override
    public List<AnnuityReminder> getByBusinessId(String businessId) {
        return annuityReminderDao.getByBusinessId(businessId);
    }
}
