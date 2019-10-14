package biz.mercue.campusipr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.dao.StatusDaoImpl;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.service.PatentService;
import biz.mercue.campusipr.service.PatentServiceImpl;

public class SyncThread extends Thread implements Runnable{
	private Logger log = Logger.getLogger(this.getClass().getName());
	// 本執行緒待執行的任務列表，你也可以指為任務索引的起始值
		private List<Task> taskList = new ArrayList();
		public static List<Patent> patentList = new ArrayList();
		private int threadId;
		public Patent patent;
		public static SyncThread syncThread;
		CountDownLatch mCountDownLatch;
		
		PatentService patentService = new PatentServiceImpl();
		StatusDao statusDao = new StatusDaoImpl();
		/**
		 * 構造工作執行緒，為其指派任務列表，及命名執行緒 ID
		 * 
		 * @param taskList
		 *            欲執行的任務列表
		 * @param threadId
		 *            執行緒 ID
		 */

		public SyncThread(List<Task> taskList, int threadId, CountDownLatch countDownLatch) {
			this.taskList = taskList;
			this.threadId = threadId;
			mCountDownLatch = countDownLatch;
		}
		/**
		 * 執行被指派的所有任務
		 */
		@Override
		public void run() {
			try {
				for (Task task:taskList) {
					int status = task.execute(task.patent);
//					log.info("status: "+status);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}finally {
				this.mCountDownLatch.countDown();
			}
		}

}
