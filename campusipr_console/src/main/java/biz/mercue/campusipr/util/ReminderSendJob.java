package biz.mercue.campusipr.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContact;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.service.CountryService;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.ReminderService;

@Component
public class ReminderSendJob  implements Job {


	private Logger log = Logger.getLogger(this.getClass().getName());


	@Autowired
	ReminderService reminderService;
	
	@Autowired
	PatentService patentService;
	
	@Autowired
	CountryService countryService;

	public void execute(JobExecutionContext context) {

		log.info("do reminder task");

//		try {
			
			JobKey key = context.getJobDetail().getKey();
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			
			
			String taskId = dataMap.getString("task_id");
			log.info("taskId:"+taskId +" executing");
			ReminderTask task = reminderService.getById(taskId);
			if (task != null) {
				MailSender mail = new MailSender();
				Patent patent = patentService.getById(task.getBusiness_id(), task.getPatent_id());
				log.info("patent:"+patent.getPatent_name());
				List<PatentContact> listContact = new ArrayList<>();
				log.info("listContact:"+listContact.size());
				for(PatentContact contact:patent.getListContact()) {
					log.info("contact:"+contact.getContact_email());
					if (contact.getBusiness() != null) {
						if (task.getBusiness_id().equals(contact.getBusiness().getBusiness_id())) {
							listContact.add(contact);
						}
					} else {
						listContact.add(contact);
					}
				}
				Country country = countryService.getByLanguage(patent.getPatent_appl_country(), "tw");
				patent.setCountry_name(country.getCountry_name());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(task.getTask_date());
				calendar.add(Calendar.DATE, task.getReminder_day());
				String annuity_date = DateUtils.getSimpleSlashFormatDate(calendar.getTime());
				patent.setAnnuity_date(annuity_date);
				if (!listContact.isEmpty()) {
					mail.sendPatentAnnuityReminder(patent, listContact);
				}
			}
			reminderService.changeRemindertatus(taskId, true);
//		} catch (Exception e) {
//		
//			log.error(e.getMessage());
//		}
		
	}


}
