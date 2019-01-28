package biz.mercue.campusipr.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="annuity_reminder")
public class AnnuityReminder extends BaseBean{
	
	@Id
	@JsonView(View.Reminder.class)
	private String reminder_id;
	
	@JsonView(View.Reminder.class)
	private String reminder_text;
	
	
	@JsonView(View.Reminder.class)
	private int email_day;
	
	@JsonView(View.Reminder.class)
	private int phone_day;
	
	@JsonView(View.Reminder.class)
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="business_id", referencedColumnName="business_id")
	private Business business;
	
	
	private boolean available;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;

	public String getReminder_id() {
		return reminder_id;
	}

	public void setReminder_id(String reminder_id) {
		this.reminder_id = reminder_id;
	}

	public String getReminder_text() {
		return reminder_text;
	}

	public void setReminder_text(String reminder_text) {
		this.reminder_text = reminder_text;
	}

	public int getEmail_day() {
		return email_day;
	}

	public void setEmail_day(int email_day) {
		this.email_day = email_day;
	}

	public int getPhone_day() {
		return phone_day;
	}

	public void setPhone_day(int phone_day) {
		this.phone_day = phone_day;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
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
	
	

}
