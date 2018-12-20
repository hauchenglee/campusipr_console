package biz.mercue.campusipr.model;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@SuppressWarnings("serial")
@Entity
@Table(name="push_task")
public class PushTask extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String push_id;
	

	@JsonView(View.Public.class)
	private boolean is_send;
	
	
	@JsonView(View.Public.class)
	private boolean is_instant;
	
	@JsonView(View.Public.class)
	private boolean available;
	

	
	private String business_id;
	
	
	@JsonView(View.Public.class)
	private String push_text;
	

	




	

	
	@JsonView(View.Public.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date push_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	
	

	public String getPush_id() {
		return push_id;
	}
	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}
	





	
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	
	public boolean getIs_send() {
		return is_send;
	}

	public void setIs_send(boolean is_send) {
		this.is_send = is_send;
	}
	
	public boolean getAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public Date getPush_date() {
		return push_date;
	}
	public void setPush_date(Date push_date) {
		this.push_date = push_date;
	}
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	public Date getUpdate_date() {
		return update_date;
	}
	
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}


	public boolean is_instant() {
		return is_instant;
	}
	public void setIs_instant(boolean is_instant) {
		this.is_instant = is_instant;
	}
	public String getPush_text() {
		return push_text;
	}
	public void setPush_text(String push_text) {
		this.push_text = push_text;
	}

}
