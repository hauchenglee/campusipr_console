package biz.mercue.campusipr.util;


import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.View;



public class StringResponseBody extends ResponseBody{
	

	
	
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
