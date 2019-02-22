package biz.mercue.campusipr.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.service.PushService;
import biz.mercue.campusipr.service.ReminderService;
import biz.mercue.campusipr.service.SynchronizeService;


public class ScheduleUtils {
	
	public static void removePushTask(ServletContext context,PushTask bean){
		StdSchedulerFactory factory = (StdSchedulerFactory) context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
        try{
        	Scheduler scheduler = factory.getScheduler();
        if (scheduler.checkExists(JobKey.jobKey(bean.getPush_id()))) {
        		scheduler.unscheduleJob(TriggerKey.triggerKey("trigger_"+bean.getPush_id(), "push_trigger"));
        		Logger.getLogger(ScheduleUtils.class.getName()).info("remove task success");
        }
        }catch(Exception e){
        	Logger.getLogger(ScheduleUtils.class.getName()).error(e.getMessage());
        }
	}
	
	public static void createPushTask(ServletContext context, PushService pushService, PushTask bean) {
		
		JobDetail job = JobBuilder.newJob(PushSendJob.class)
				.withIdentity(bean.getPush_id(), "push")
				.usingJobData("push_id", bean.getPush_id())
				.build();
		
		StdSchedulerFactory factory = (StdSchedulerFactory) context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);       
		try {
			
			Scheduler scheduler = factory.getScheduler();
			
			PushTask push = pushService.getById(bean.getPush_id());
			scheduler.getContext().put("push_data_" + bean.getPush_id() , push);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bean.getPush_date());

			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("trigger_" + bean.getPush_id(), "push_trigger")
					.startAt(calendar.getTime())
					.build();
			scheduler.scheduleJob(job,trigger);
			 Logger.getLogger(ScheduleUtils.class.getName()).info("create task success, current time: "+new Date() +", push date: "+ calendar.getTime());
		} catch (SchedulerException e) {
			Logger.getLogger(ScheduleUtils.class.getName()).error(e.getMessage());
		}
	}

	public static void removeReminderTask(ServletContext context,ReminderTask bean){
		StdSchedulerFactory factory = (StdSchedulerFactory) context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
        try{
        	Scheduler scheduler = factory.getScheduler();
        if (scheduler.checkExists(JobKey.jobKey(bean.getTask_id()))) {
        		scheduler.unscheduleJob(TriggerKey.triggerKey("reminder_"+bean.getTask_id(), "reminder_trigger"));
        		Logger.getLogger(ScheduleUtils.class.getName()).info("remove task success");
        }
        }catch(Exception e){
        	Logger.getLogger(ScheduleUtils.class.getName()).error(e.getMessage());
        }
	}
	
	public static void createReminderTask(ServletContext context, ReminderService reminderService, ReminderTask bean) {
		
		JobDetail job = JobBuilder.newJob(ReminderSendJob.class)
				.withIdentity(bean.getTask_id(), "reminder")
				.usingJobData("task_id", bean.getTask_id())
				.build();
		
		StdSchedulerFactory factory = (StdSchedulerFactory) context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);       
		try {
			
			Scheduler scheduler = factory.getScheduler();
			
			ReminderTask reminder = reminderService.getById(bean.getTask_id());
			scheduler.getContext().put("reminder_data_" + bean.getTask_id() , reminder);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bean.getTask_date());

			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("reminder_" + bean.getTask_id(), "reminder_trigger")
					.startAt(calendar.getTime())
					.build();
			scheduler.scheduleJob(job,trigger);
			 Logger.getLogger(ScheduleUtils.class.getName()).info("create task success, current time: "+new Date() +", reminder date: "+ calendar.getTime());
		} catch (SchedulerException e) {
			Logger.getLogger(ScheduleUtils.class.getName()).error(e.getMessage());
		}
	}
	
	public static void removeSynchronizeTask(ServletContext context,ReminderTask bean){
		StdSchedulerFactory factory = (StdSchedulerFactory) context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
        try{
        	Scheduler scheduler = factory.getScheduler();
        if (scheduler.checkExists(JobKey.jobKey(bean.getTask_id()))) {
        		scheduler.unscheduleJob(TriggerKey.triggerKey("sync_"+bean.getTask_id(), "sync_trigger"));
        		Logger.getLogger(ScheduleUtils.class.getName()).info("remove task success");
        }
        }catch(Exception e){
        	Logger.getLogger(ScheduleUtils.class.getName()).error(e.getMessage());
        }
	}
	
	public static void createSynchronizeTask(ServletContext context, SynchronizeService synchronizeService, SynchronizeTask bean) {
		
		JobDetail job = JobBuilder.newJob(SyncSendJob.class)
				.withIdentity(bean.getTask_id(), "reminder")
				.usingJobData("task_id", bean.getTask_id())
				.build();
		
		StdSchedulerFactory factory = (StdSchedulerFactory) context.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);       
		try {
			
			Scheduler scheduler = factory.getScheduler();
			
			SynchronizeTask sync = synchronizeService.getById(bean.getTask_id());
			scheduler.getContext().put("sync_data_" + bean.getTask_id() , sync);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bean.getTask_date());

			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("sync_" + bean.getTask_id(), "sync_trigger")
					.startAt(calendar.getTime())
					.build();
			scheduler.scheduleJob(job,trigger);
			 Logger.getLogger(ScheduleUtils.class.getName()).info("create task success, current time: "+new Date() +", sync date: "+ calendar.getTime());
		} catch (SchedulerException e) {
			Logger.getLogger(ScheduleUtils.class.getName()).error(e.getMessage());
		}
	}

}
