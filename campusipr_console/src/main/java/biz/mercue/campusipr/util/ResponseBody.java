package biz.mercue.campusipr.util;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.View;

public abstract class ResponseBody {
	
	
	@JsonView(View.Public.class)
	protected int code;
	
	
	@JsonView(View.Public.class)
	protected String message;
	
	
	@JsonView(View.Public.class)
	protected String message_en;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		switch (code) {
			case Constants.INT_SUCCESS:
				this.message = Constants.MSG_SUCCESS;
				this.message_en = Constants.MSG_EN_SUCCESS;
				break;
			case Constants.INT_SYSTEM_PROBLEM:
				this.message = Constants.MSG_SYSTEM_PROBLEM;
				this.message_en = Constants.MSG_EN_SYSTEM_PROBLEM;
				break;
			case Constants.INT_CANNOT_FIND_DATA:
				this.message = Constants.MSG_CANNOT_FIND_DATA;
				this.message_en = Constants.MSG_EN_CANNOT_FIND_DATA;
				break;
			case Constants.INT_PASSWORD_ERROR:
				this.message = Constants.MSG_PASSWORD_ERROR;
				this.message_en = Constants.MSG_EN_PASSWORD_ERROR;
				break;
			case Constants.INT_USER_DUPLICATE:
				this.message = Constants.MSG_USER_DUPLICATE;
				this.message_en = Constants.MSG_EN_USER_DUPLICATE;
				break;
			case Constants.INT_DATA_ERROR:
				this.message = Constants.MSG_DATA_ERROR;
				this.message_en = Constants.MSG_EN_DATA_ERROR;
				break;
			case Constants.INT_USER_LOGOUT:
				this.message = Constants.MSG_USER_LOGOUT;
				this.message_en = Constants.MSG_EN_USER_LOGOUT;
				break;
			case Constants.INT_NO_PERMISSION:
				this.message = Constants.MSG_NO_PERMISSION;
				this.message_en = Constants.MSG_EN_NO_PERMISSION;
				break;
			case Constants.INT_ACCESS_TOKEN_ERROR:
				this.message = Constants.MSG_ACCESS_TOKEN_ERROR;
				this.message_en = Constants.MSG_EN_ACCESS_TOKEN_ERROR;
				break;
			case Constants.INT_DATA_DUPLICATE:
				this.message = Constants.MSG_DATA_DUPLICATE;
				this.message_en = Constants.MSG_EN_DATA_DUPLICATE;
				break;
			case Constants.INT_INCORRECT_SYNTAX:
				this.message = Constants.MSG_INCORRECT_SYNTAX;
				this.message_en = Constants.MSG_EN_INCORRECT_SYNTAX;
				break;
			default:
				this.code = Constants.INT_SYSTEM_PROBLEM;
				this.message = Constants.MSG_SYSTEM_PROBLEM;
				this.message_en = Constants.MSG_EN_SYSTEM_PROBLEM;
				break;
		}
	}
	
	public void setCode(int code,String message,String message_en) {
		this.code = code;
		this.message = message;
		this.message_en = message_en;
		
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage_en() {
		return message_en;
	}
	public void setMessage_en(String message_en) {
		this.message_en = message_en;
	}
	
	public String getJacksonString(Class<?> view) {
		return JacksonJSONUtils.mapObjectWithView(this,  view);
	}

}
