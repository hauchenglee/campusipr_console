package biz.mercue.seed.util;

import java.util.List;



import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.seed.model.View;


public class ListResponseBody {
	
	@JsonView(View.Public.class)
	int code;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	@JsonView(View.Public.class)
	String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@JsonView(View.Public.class)
	List data;
	
	public List getList() {
		return data;
	}
	public void setList(List list) {
		this.data = list;
	}

}
