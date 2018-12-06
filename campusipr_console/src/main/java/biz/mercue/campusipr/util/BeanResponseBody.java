package biz.mercue.campusipr.util;


import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.BaseBean;
import biz.mercue.campusipr.model.View;

public class BeanResponseBody {
	
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
	BaseBean data;
	
	public BaseBean getBean() {
		return data;
	}
	public void setBean(BaseBean data) {
		this.data = data;
	}

}
