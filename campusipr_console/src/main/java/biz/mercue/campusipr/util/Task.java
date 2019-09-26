package biz.mercue.campusipr.util;

import org.springframework.beans.factory.annotation.Autowired;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.PatentService;

public class Task {
	public static final int READY = 0;
	public static final int RUNNING = 1;
	public static final int FINISHED = 2;
	@SuppressWarnings("unused")
	private int status;
	// 宣告一個任務的自有業務含義的變數，用於標識任務
	private int taskId;


//	Patent patent;
	@Autowired
	PatentService patentService;
	
	// 任務的初始化方法
	public Task(int taskId) {
		this.status = READY;
		this.taskId = taskId;
//		this.patent = patent;
//		System.out.println("PatentID: " + patent.getPatent_appl_no());
	}

	/**
	 * 執行任務
	 */
	public void execute() {
		// 設定狀態為執行中
		setStatus(Task.RUNNING);
//		patentService.syncPatentData(patent);
		System.out.println("當前執行緒 ID 是：" + Thread.currentThread().getName()
				+ " | 任務 ID 是：" + this.taskId);
		// 附加一個延時
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 執行完成，改狀態為完成
		setStatus(FINISHED);
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTaskId() {
		return taskId;
	}
}
