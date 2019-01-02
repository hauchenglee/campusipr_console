package biz.mercue.campusipr.util;


import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.model.BaseBean;
import biz.mercue.campusipr.model.View;

public class BeanResponseBody extends ResponseBody{
	

	
	@JsonView(View.Public.class)
	BaseBean data;
	

	public BaseBean getBean() {
		return data;
	}
	public void setBean(BaseBean data) {
		this.data = data;
	}

}
