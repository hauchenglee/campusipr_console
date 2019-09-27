package biz.mercue.campusipr.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.PatentService;

//import biz.mercue.campusipr.util.Task;

public class SyncThread extends Thread {
	private Logger log = Logger.getLogger(this.getClass().getName());
	// 本執行緒待執行的任務列表，你也可以指為任務索引的起始值
//		private List<Task> taskList = null;
		private List<Patent> patentList;
		private int threadId;
		@Autowired
		Patent patent;
		@Autowired
		PatentService patentService;
		/**
		 * 構造工作執行緒，為其指派任務列表，及命名執行緒 ID
		 * 
		 * @param taskList
		 *            欲執行的任務列表
		 * @param threadId
		 *            執行緒 ID
		 */
//		@SuppressWarnings("unchecked")
		public SyncThread(List<Patent> patentList, int threadId) {
			this.patentList = patentList;
			this.threadId = threadId;
			log.info(Thread.currentThread().getState());
		}

		/**
		 * 執行被指派的所有任務
		 */
		public void run() {
			List<Patent> patentList = new ArrayList<Patent>();
			
			for (Patent patent : patentList) {
				this.patent= patent;
				patentService.test(this.patent);
//				task.execute(this.patent);
			}
		}
	
	
//	int syncCode;
//	int taskId;
//	public MyThread(int syncCode  ,int taskId) {
//		this.syncCode = syncCode;
//		this.taskId = taskId;
//	}
//
//	public void run(){
//		synchronized (this) {
//			System.out.println("Start " + taskId);
//		}
//    }
}
