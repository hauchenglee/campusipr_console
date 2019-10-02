package biz.mercue.campusipr.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PatentServiceImpl;

public class SyncThread extends Thread{
	private Logger log = Logger.getLogger(this.getClass().getName());
	// 本執行緒待執行的任務列表，你也可以指為任務索引的起始值
		private List<Task> taskList = new ArrayList();
		public static List<Patent> patentList = new ArrayList();
		private int threadId;
		public Patent patent;
		public static SyncThread syncThread;
//		@Autowired
		PatentService patentService = new PatentServiceImpl();
				
		/**
		 * 構造工作執行緒，為其指派任務列表，及命名執行緒 ID
		 * 
		 * @param taskList
		 *            欲執行的任務列表
		 * @param threadId
		 *            執行緒 ID
		 */
//		public SyncThread(List<Patent> patentList, int threadId) {
//			this.patentList = patentList;
//			this.threadId = threadId;
//			syncThread = this;
//			syncThread.patentService  = this.patentService;
//			log.info(Thread.currentThread().getState());
//		}
		public SyncThread(List<Task> taskList, int threadId) {
			this.taskList = taskList;
			this.threadId = threadId;
			syncThread = this;
			syncThread.patentService  = this.patentService;
			log.info(Thread.currentThread().getState());
		}
		/**
		 * 執行被指派的所有任務
		 */
		@Override
		public void run() {
			try {
//				for(Patent patent:patentList) {
				for (Task task:taskList) {
					log.info(task.patent.getPatent_appl_no());
//					patentService.test(patent);
					task.execute(task.patent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
