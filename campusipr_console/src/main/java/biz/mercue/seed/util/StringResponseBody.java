package biz.mercue.seed.util;


import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.seed.model.View;



public class StringResponseBody {
	
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
	String data;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public String toString(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constants.JSON_CODE, code);
		
		jsonObject.put(Constants.JSON_MESSAGE, message);
		
		jsonObject.put(Constants.JSON_DATA, data);
		return jsonObject.toString();
	}
	

}
