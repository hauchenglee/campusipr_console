package biz.mercue.campusipr.util;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PatentServiceImpl;

//@Component
public class Task {
	public static final int READY = 0;
	public static final int RUNNING = 1;
	public static final int FINISHED = 2;
	private int status;
	// 宣告一個任務的自有業務含義的變數，用於標識任務
	private int taskId;
	
	public static  Task task;
	private Logger log = Logger.getLogger(this.getClass().getName());
	public Patent patent;
	
	
//	@Autowired
	PatentService patentService = new PatentServiceImpl();
	
	// 任務的初始化方法
	public Task(int taskId, Patent editPatent) {
		task = this;
		task.patentService = this.patentService;
		this.status = READY;
		this.taskId = taskId;
		this.patent = editPatent;
	}

	/**
	 * 執行任務
	 * @param editPatent 
	 */
	public void execute(Patent editPatent) {
		// 設定狀態為執行中
		setStatus(Task.RUNNING);
		log.info("當前執行緒 ID 是：" + Thread.currentThread().getName()
				+ " | 任務 ID 是：" + this.taskId);
		// 附加一個延時
		try {
//			patentService.test(editPatent);
			patentService.syncPatentData(editPatent);
			Thread.sleep(1000);
		} catch (Exception e) {
			log.error(e);
		}
		// 執行完成，改狀態為完成
		setStatus(FINISHED);
//		log.info("isAlive: "+Thread.currentThread().isAlive());
		log.info(Thread.currentThread().getState());
		log.info("status: "+status);
		
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public void getStatus(int status) {
		this.status = status;
	}
	public int getTaskId() {
		return taskId;
	}
	
}
