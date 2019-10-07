package biz.mercue.campusipr.util;



import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.dao.StatusDaoImpl;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PatentServiceImpl;

public class Task {
	public static final int READY = 0;
	public static final int RUNNING = 1;
	public static final int FINISHED = 2;
	private int status;
	
	private int taskId;
	
	public Patent patent;
	PatentService patentService = new PatentServiceImpl();
	StatusDao statusDao = new StatusDaoImpl();
	
	private static Logger log = Logger.getLogger(Task.class.getName());

	// 任務的初始化方法
	public Task(int taskId, Patent editPatent) {
		this.status = READY;
		this.taskId = taskId;
		this.patent = editPatent;
	}

	/**
	 * 執行任務
	 * @param editPatent 
	 */
	public int execute(Patent editPatent) {
		// 設定狀態為執行中
		setStatus(Task.RUNNING);
		log.info("當前執行緒 ID 是：" + Thread.currentThread().getName()
				+ " | 任務 ID 是：" + this.taskId);
		try {
			int code = patentService.syncPatentData(editPatent);
			//thread.sleep(10000);
			// 執行完成，改狀態為完成
			setStatus(FINISHED);
			log.info("editPatent: "+editPatent.getPatent_appl_no()+", status: "+status+ ", code: "+ code);
			return status;
		} catch (Exception e) {
			log.error(e);
		}
		return status;
	}

	
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	public int getTaskId() {
		return taskId;
	}


}
