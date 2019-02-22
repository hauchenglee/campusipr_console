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
@Table(name="synchronize_business")
public class SynchronizeBusiness extends BaseBean{
	
	@Id
	private String sync_id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date sync_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date sync_next_date;
	
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="business_id")
	private Business business;
	
	private int random_time;


	public String getSync_id() {
		return sync_id;
	}


	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}


	public Date getSync_date() {
		return sync_date;
	}


	public void setSync_date(Date sync_date) {
		this.sync_date = sync_date;
	}


	public Date getSync_next_date() {
		return sync_next_date;
	}


	public void setSync_next_date(Date sync_next_date) {
		this.sync_next_date = sync_next_date;
	}


	public Business getBusiness() {
		return business;
	}


	public void setBusiness(Business business) {
		this.business = business;
	}


	public int getRandom_time() {
		return random_time;
	}


	public void setRandom_time(int random_time) {
		this.random_time = random_time;
	}
	

	
	

}
