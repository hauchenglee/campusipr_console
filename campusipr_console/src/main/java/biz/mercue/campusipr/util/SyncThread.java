package biz.mercue.campusipr.util;

import java.util.List;

public class SyncThread extends Thread {
	// 本執行緒待執行的任務列表，你也可以指為任務索引的起始值
		private List<Task> taskList = null;
		@SuppressWarnings("unused")
		private int threadId;

		/**
		 * 構造工作執行緒，為其指派任務列表，及命名執行緒 ID
		 * 
		 * @param taskList
		 *            欲執行的任務列表
		 * @param threadId
		 *            執行緒 ID
		 */
		@SuppressWarnings("unchecked")
		public SyncThread(List taskList, int threadId) {
			this.taskList = taskList;
			this.threadId = threadId;
		}

		/**
		 * 執行被指派的所有任務
		 */
		public void run() {
//			for (Task task : taskList) {
//				System.out.println(threadId);
//				task.execute();
//			}
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
