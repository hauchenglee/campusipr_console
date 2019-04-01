package biz.mercue.campusipr.util;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.service.ReminderService;


@Component
public class ReminderJobListener implements JobListener {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	 private static class ReminderJobListenerLoader {
 		private static final ReminderJobListener instance = new ReminderJobListener();
 }
 
	public static ReminderJobListener getInstance() {
		return ReminderJobListenerLoader.instance;
	}
	
	private ReminderService reminderService;
	
	public void setReminderService(ReminderService reminderService) {
		this.reminderService = reminderService;
	}
	
	@Override
	public String getName() {
		return "reminderJobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String taskId = context.getJobDetail().getKey().getName();
		try {
			ReminderTask reminder = reminderService.getById(taskId);
			reminder.setIs_send(true);

			log.info("job done, with id: " + reminder.getTask_id());
		} catch (Exception e) {
			log.info(e);
		}
		
		
	}

	

}
