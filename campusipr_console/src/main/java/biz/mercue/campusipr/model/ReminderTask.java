package biz.mercue.campusipr.model;


import java.util.Arrays;
import java.util.Collections;
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
@Table(name="reminder_task")
public class ReminderTask extends BaseBean{
	
	public static final String reminderTypeAnnuity = "annuity";
	
	@Id
	private String task_id;
	
	
	private String patent_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date task_date;
	
	
	private String business_id;
	

	private String task_type;
	
	
	private boolean is_send;
	
	private boolean is_remind;
	
	@JsonView(View.Public.class)
	private int reminder_day;

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	
	public Date getTask_date() {
		return task_date;
	}

	public void setTask_date(Date task_date) {
		this.task_date = task_date;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getTask_type() {
		return task_type;
	}

	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}

	public boolean is_send() {
		return is_send;
	}

	public void setIs_send(boolean is_send) {
		this.is_send = is_send;
	}

	public boolean is_remind() {
		return is_remind;
	}

	public void setIs_remind(boolean is_remind) {
		this.is_remind = is_remind;
	}

	public int getReminder_day() {
		return reminder_day;
	}

	public void setReminder_day(int reminder_day) {
		this.reminder_day = reminder_day;
	}

	public String getPatent_id() {
		return patent_id;
	}

	public void setPatent_id(String patent_id) {
		this.patent_id = patent_id;
	}

	

}
