package biz.mercue.campusipr.util;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.service.SynchronizeService;


@Component
public class SyncJobListener implements JobListener {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	 private static class SyncJobListenerLoader {
 		private static final SyncJobListener instance = new SyncJobListener();
 }
 
	public static SyncJobListener getInstance() {
		return SyncJobListenerLoader.instance;
	}
	
	private SynchronizeService synchronizeService;
	
	public void setSynchronizeService(SynchronizeService synchronizeService) {
		this.synchronizeService = synchronizeService;
	}
	
	@Override
	public String getName() {
		return "syncJobListener";
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
			SynchronizeTask sync = synchronizeService.getById(taskId);
			sync.setIs_sync(true);

			log.info("job done, with id: " + sync.getTask_id());
		} catch (Exception e) {
			log.info(e);
		}
		
		
	}

	

}
