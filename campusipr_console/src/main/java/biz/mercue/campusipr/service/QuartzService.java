package biz.mercue.campusipr.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PushDao;
import biz.mercue.campusipr.model.PushTask;
import biz.mercue.campusipr.util.PushSendJob;



@Service
@Transactional
public class QuartzService {


	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	Scheduler scheduler;


	@Autowired
	PushDao pushDao;


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
}
