package biz.mercue.campusipr.util;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.View;



public class JSONResponseBody extends ResponseBody {
	

	
	@JsonView(View.Public.class)
	JSONObject data;
	
	public JSONObject getData() {
		return data;
	}
	
	public void setData(JSONObject obj) {
		if(data == null) {
			data = new JSONObject();
		}
		data = obj;
	}
	
	public String toString(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constants.JSON_CODE, code);
		jsonObject.put(Constants.JSON_MESSAGE, message);	
		jsonObject.put(Constants.JSON_MESSAGE_EN, message_en);
		jsonObject.put(Constants.JSON_DATA, data);
		return jsonObject.toString();
	}
	
}