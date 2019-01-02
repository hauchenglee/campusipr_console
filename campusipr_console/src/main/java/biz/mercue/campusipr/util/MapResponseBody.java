package biz.mercue.campusipr.util;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.View;



public class MapResponseBody extends ResponseBody {
	

	
	@JsonView(View.Public.class)
	Map<String, Object> data;
	
	public Map<String, Object> getData() {
		return data;
	}
	
	public void setMap(Map<String, Object> data) {
		this.data = data;
	}
	
	public void setData(String key,Object obj) {
		if(data == null) {
			data = new HashMap<String, Object>();
		}
		data.put(key, obj);
	}
	
	public String toString(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constants.JSON_CODE, code);
		jsonObject.put(Constants.JSON_MESSAGE, message);	
		jsonObject.put(Constants.JSON_DATA, data);
		return jsonObject.toString();
	}
	
}
