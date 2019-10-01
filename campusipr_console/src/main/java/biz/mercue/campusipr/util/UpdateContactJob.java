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
            List<PatentContact> patentContactList = contactService.getDefaultContactByBusiness(businessId);
            log.info("patentContactList size: " + patentContactList.size());
            if (!patentContactList.isEmpty()) {
                for (PatentContact contact : patentContactList) {
                    contactService.updateContactByBusiness(contact, contact.getPatent_contact_id(), business);
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
