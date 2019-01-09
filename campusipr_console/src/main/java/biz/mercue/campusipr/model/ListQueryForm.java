package biz.mercue.campusipr.model;

import java.util.List;


public class ListQueryForm {
	
	private int task_result = -1;
	
	private int page_size;
	
	private int total_count;
	
	private List list;
	
	
	public ListQueryForm() {
	}
	
	
	
	public ListQueryForm(int taskResult ,int totalCount,int pageSize,List list) {
		
		this.task_result = taskResult;
		
		this.page_size = pageSize;
		
		this.total_count = totalCount;
		
		this.list = list;
	}
	
	public ListQueryForm(int totalCount,int pageSize,List list) {
		this.page_size = pageSize;
		
		this.total_count = totalCount;
		
		this.list = list;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}



	public int getTask_result() {
		return task_result;
	}



	public void setTask_result(int task_result) {
		this.task_result = task_result;
	}

}
