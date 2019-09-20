package biz.mercue.campusipr.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.service.PushService;
import biz.mercue.campusipr.service.QuartzService;
import biz.mercue.campusipr.service.ReminderService;
import biz.mercue.campusipr.service.SynchronizeService;


@Component
public class QuartzSchedulerListener implements SchedulerListener{

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	QuartzService quartzService;
	
	@Autowired
	PushService pushService;
	
	@Autowired
	ReminderService reminderService;
	
	@Autowired
	SynchronizeService synchronizeService;
	
	@Override
	public void jobScheduled(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerFinalized(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerPaused(TriggerKey triggerKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggersPaused(String triggerGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerResumed(TriggerKey triggerKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggersResumed(String triggerGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobAdded(JobDetail jobDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobsPaused(String jobGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobsResumed(String jobGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerInStandbyMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerStarted() {
		
		log.info("schedule started");
		log.info("schedule pause,for debug");
		//TODO for debug

		// auto sync patent job
//		try {
//			quartzService.createAutoSyncPatentJob();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		List<ReminderTask> taskList = reminderService.getAvailableReminder();
//		log.info("un send task list size: " + taskList.size());
//		for(ReminderTask task: taskList) {
//			try {
//				quartzService.createJob(task);
//			} catch (Exception e) {
//				log.error(e);
//			}
//		}
//		
//		List<SynchronizeTask> syncList = synchronizeService.getAvailableSynchronize();
//		log.info("un sync task list size: " + syncList.size());
//		for(SynchronizeTask sync: syncList) {
//			try {
//				quartzService.createJob(sync);
//			} catch (Exception e) {
//				log.error(e);
//			}
//		}
	}

	@Override
	public void schedulerStarting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulerShuttingdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedulingDataCleared() {
		// TODO Auto-generated method stub
		
	}

}
