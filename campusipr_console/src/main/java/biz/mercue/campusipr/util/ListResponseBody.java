package biz.mercue.campusipr.util;

import java.util.List;



import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.View;


public class ListResponseBody extends ResponseBody {
	

	@JsonView(View.Public.class)
	int total_count;
	
	@JsonView(View.Public.class)
	int page_size;
	
	@JsonView(View.Public.class)
	List data;
	
	public List getList() {
		return data;
	}
	public void setList(List list) {
		this.data = list;
	}
	
	
	public void setListQuery(ListQueryForm form) {
		this.data = form.getList();
		this.total_count = form.getTotal_count();
		this.page_size = form.getPage_size();
	}
	
	
	
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

}
