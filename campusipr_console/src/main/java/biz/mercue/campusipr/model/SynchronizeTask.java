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
@Table(name="synchronize_task")
public class SynchronizeTask extends BaseBean{
	
	@Id
	private String task_id;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="sync_id", referencedColumnName="sync_id")
	private SynchronizeBusiness sync;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date task_date;
	
	
	private String business_id;
	
	
	private boolean is_sync;


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


	public boolean is_sync() {
		return is_sync;
	}


	public void setIs_sync(boolean is_sync) {
		this.is_sync = is_sync;
	}


	public SynchronizeBusiness getSync() {
		return sync;
	}


	public void setSync(SynchronizeBusiness sync) {
		this.sync = sync;
	}
	
	

}
