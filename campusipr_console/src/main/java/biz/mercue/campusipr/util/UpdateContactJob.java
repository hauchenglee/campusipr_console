package biz.mercue.campusipr.util;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.PatentContact;
import biz.mercue.campusipr.service.BusinessService;
import biz.mercue.campusipr.service.PatentContactService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UpdateContactJob implements Job {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private PatentContactService contactService;

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long start = System.nanoTime();
        log.info("UpdateContactJob start");
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String businessId = dataMap.getString("business_id");

        Business business = businessService.getById(businessId);

        try {
            // 以下被檢測出No row with the given identifier exists
            // 不清楚原因，故先行註解
            // 猜測：舊資料造成干擾（）
//            List<PatentContact> patentContactList = contactService.getDefaultContactByBusiness(businessId);

            // 這是修改上面的bug
            // 解法：不取出完整的bean，而是取出相應的pk，再用這個pk去getById
            List<String> contactIds = contactService.getDefaultContactIdsByBusiness(businessId);
            log.info("contactIds size: " + contactIds.size());
            if (!contactIds.isEmpty()) {
                for (String contactId : contactIds) {
                    PatentContact contact = contactService.getById(contactId);
                    if (contact != null && StringUtils.isNULL(contact.getPatent_contact_id())) {
                        contactService.updateContactByBusiness(contact, contact.getPatent_contact_id(), business);
                    }
                }
            }
            long end = System.nanoTime();
            long total = end - start;
            double second = (double) total / 1_000_000_000.0;
            double minutes = second / 60.0;
            log.info("total update contact spend time (seconds): " + second);
            log.info("total update contact spend time (minutes): " + minutes);
        } catch (Exception e) {
            log.info("update contact schedule exception", e);
        }

    }
}
