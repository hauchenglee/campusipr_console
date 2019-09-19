package biz.mercue.campusipr.service;

import java.util.Calendar;
import java.util.Date;


import biz.mercue.campusipr.util.*;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PushDao;
import biz.mercue.campusipr.dao.ReminderDao;
import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.model.SynchronizeTask;


@Service
@Transactional
public class QuartzService {


	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	Scheduler scheduler;


	@Autowired
	PushDao pushDao;
	
	@Autowired
	ReminderDao reminderDao;


	public void createJob(PushTask bean) throws Exception {
		JobDetail job = JobBuilder.newJob(PushSendJob.class)
				.withIdentity(bean.getPush_id(), "push")
				.usingJobData("push_id", bean.getPush_id())
				.build();
		
			PushTask push = pushDao.getById(bean.getPush_id());
			scheduler.getContext().put("push_data_" + bean.getPush_id() , push);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bean.getPush_date());
			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("trigger_" + bean.getPush_id(), "push_trigger")
					.startAt(calendar.getTime())
					.build();
			scheduler.scheduleJob(job,trigger);
			log.info("create task success, current time: "+new Date() +", push date: "+ calendar.getTime());
			log.info(JobKey.jobKey(bean.getPush_id()));
	}

	public void removeJob(PushTask bean) throws Exception {
		if (scheduler.checkExists(JobKey.jobKey(bean.getPush_id(), "push"))) {
			scheduler.unscheduleJob(TriggerKey.triggerKey("trigger_"+bean.getPush_id(), "push_trigger"));
			log.info("remove task success");
		}
		
	}
	
	public void updateJob(PushTask bean) throws Exception {
		removeJob(bean);
		createJob(bean);
	}
	
	public void removeJob(ReminderTask bean) throws Exception  {
		if (scheduler.checkExists(JobKey.jobKey(bean.getTask_id()))) {
        		scheduler.unscheduleJob(TriggerKey.triggerKey("reminder_"+bean.getTask_id(), "reminder_trigger"));
        		Logger.getLogger(ScheduleUtils.class.getName()).info("remove task success");
        }
	}
	
	public void createJob(ReminderTask bean) throws Exception  {
		
		JobDetail job = JobBuilder.newJob(ReminderSendJob.class)
				.withIdentity(bean.getTask_id(), "reminder")
				.usingJobData("task_id", bean.getTask_id())
				.build();
		
		
			
		ReminderTask reminder = reminderDao.getById(bean.getTask_id());
		scheduler.getContext().put("reminder_data_" + bean.getTask_id() , reminder);
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bean.getTask_date());

		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("reminder_" + bean.getTask_id(), "reminder_trigger")
					.startAt(calendar.getTime())
					.build();
		scheduler.scheduleJob(job,trigger);
		Logger.getLogger(ScheduleUtils.class.getName()).info("create task success, current time: "+new Date() +", reminder date: "+ calendar.getTime());
	}
	
	public void updateJob(ReminderTask bean) throws Exception {
		removeJob(bean);
		createJob(bean);
	}
	
	
	public void removeJob(SynchronizeTask bean) throws Exception  {
		if (scheduler.checkExists(JobKey.jobKey(bean.getTask_id()))) {
        		scheduler.unscheduleJob(TriggerKey.triggerKey("sync_"+bean.getTask_id(), "sync_trigger"));
        		Logger.getLogger(ScheduleUtils.class.getName()).info("remove task success");
        }
	}
	
	public void createJob(SynchronizeTask bean) throws Exception  {
		
		JobDetail job = JobBuilder.newJob(SyncSendJob.class)
				.withIdentity(bean.getTask_id(), "reminder")
				.usingJobData("task_id", bean.getTask_id())
				.build();
		
		
			
		ReminderTask reminder = reminderDao.getById(bean.getTask_id());
		scheduler.getContext().put("sync_data_" + bean.getTask_id() , reminder);
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bean.getTask_date());

		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("sync_" + bean.getTask_id(), "sync_trigger")
					.startAt(calendar.getTime())
					.build();
		scheduler.scheduleJob(job,trigger);
		Logger.getLogger(ScheduleUtils.class.getName()).info("create task success, current time: "+new Date() +", sync date: "+ calendar.getTime());
	}
	
	public void updateJob(SynchronizeTask bean) throws Exception {
		removeJob(bean);
		createJob(bean);
	}

	public void createJob() throws Exception {
		JobDetail job = JobBuilder.newJob(AutoSyncPatentJob.class)
				.withIdentity("syncPatent_job", "syncPatent")
				.build();
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
				.withIdentity("syncPatent_tri", "syncPatent")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
				.build();
		scheduler.scheduleJob(job, trigger);
	}
}
