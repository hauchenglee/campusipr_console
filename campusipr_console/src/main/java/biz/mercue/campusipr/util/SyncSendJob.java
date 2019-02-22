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

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.SynchronizeBusiness;
import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.QuartzService;
import biz.mercue.campusipr.service.SynchronizeBusinessService;
import biz.mercue.campusipr.service.SynchronizeService;


@Component
public class SyncSendJob  implements Job {


	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	PatentService patentService;

	@Autowired
	SynchronizeService synchronizeService;
	
	@Autowired
	SynchronizeBusinessService synchronizebusinessService;
	
	@Autowired
	private QuartzService quartzService;

	public void execute(JobExecutionContext context) {

		log.info("do sync task");

		try {
			
			JobKey key = context.getJobDetail().getKey();
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			
			
			String taskId = dataMap.getString("task_id");
			log.info("taskId:"+taskId +" executing");
			SynchronizeTask task = synchronizeService.getById(taskId);
			if (task != null) {
				MailSender mail = new MailSender();
				SynchronizeBusiness syncBusiness = task.getSync();
				log.info("send mail");
				mail.sendPatentMutipleChange(syncBusiness.getBusiness());
				log.info("Done");
				List<Patent> list = new ArrayList<>();
				patentService.syncPatentsByApplicant(list, Constants.SYSTEM_ADMIN, syncBusiness.getBusiness().getBusiness_id(), null);
				
				//更新下一次同步時間
				Date sycnNextTime = syncBusiness.getSync_next_date();
				Calendar cNextNext = Calendar.getInstance();
				cNextNext.setTime(sycnNextTime);
				cNextNext.add(Calendar.DATE, 7);
				
				syncBusiness.setSync_date(sycnNextTime);
				syncBusiness.setSync_next_date(cNextNext.getTime());
				
				synchronizebusinessService.updateSyncBusiness(syncBusiness);
				
				//加入排程
				SynchronizeTask syncTask = new SynchronizeTask();
				syncTask.setTask_id(KeyGeneratorUtils.generateRandomString());
				Calendar cTask = Calendar.getInstance();
				cTask.setTime(DateUtils.getDayStart(syncBusiness.getSync_date()));
				cTask.add(Calendar.MINUTE, syncBusiness.getRandom_time());
				syncTask.setTask_date(cTask.getTime());
				syncTask.setSync(syncBusiness);
				syncTask.setBusiness_id(syncBusiness.getBusiness().getBusiness_id());
				syncTask.setIs_sync(false);
				synchronizeService.addSync(syncTask);
				quartzService.createJob(syncTask);
			}
			synchronizeService.changeSyncStatus(taskId, true);
		} catch (Exception e) {
		
			log.error(e.getMessage());
		}
		
	}


}
