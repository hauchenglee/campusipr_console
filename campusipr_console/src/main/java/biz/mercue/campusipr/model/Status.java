package biz.mercue.campusipr.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="status")
public class Status extends BaseBean{
	
	
	@Id
	@JsonView({View.Patent.class,View.Portfolio.class})
	private String status_id;
	
	
	private String country_id;
	

	private String event_code;
	
	private String event_code_desc;
	
	private String event_class;
	
	@JsonView({View.Patent.class,View.Portfolio.class})
	private String status_desc;
	
	@JsonView({View.Patent.class,View.Portfolio.class})
	private String status_desc_en;

	@JsonView({View.Patent.class})
	private String status_color;
	
	@OneToMany(mappedBy= "primaryKey.status", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval =true)
	private List<PatentStatus> listPatentStatus;
	
	@Transient
	@JsonView({View.Patent.class})
	private Date  create_date;

	private String status_from;

	public String getStatus_id() {
		return status_id;
	}


	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}


	public String getCountry_id() {
		return country_id;
	}


	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}


	public String getEvent_class() {
		return event_class;
	}


	public void setEvent_class(String event_class) {
		this.event_class = event_class;
	}

	public String getEvent_code() {
		return event_code;
	}


	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}


	public String getStatus_desc() {
		return status_desc;
	}


	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}


	public String getStatus_desc_en() {
		return status_desc_en;
	}


	public void setStatus_desc_en(String status_desc_en) {
		this.status_desc_en = status_desc_en;
	}


	public String getStatus_color() {
		return status_color;
	}


	public void setStatus_color(String status_color) {
		this.status_color = status_color;
	}


	public String getEvent_code_desc() {
		return event_code_desc;
	}


	public void setEvent_code_desc(String event_code_desc) {
		this.event_code_desc = event_code_desc;
	}


	public String getStatus_from() {
		return status_from;
	}


	public void setStatus_from(String status_from) {
		this.status_from = status_from;
	}




	public Date getCreate_date() {
		return create_date;
	}


	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}


	public List<PatentStatus> getListPatentStatus() {
		return listPatentStatus;
	}


	public void setListPatentStatus(List<PatentStatus> listPatentStatus) {
		this.listPatentStatus = listPatentStatus;
	}
	
	public void addListPatentStatus(PatentStatus patentStatus) {
		if(this.listPatentStatus == null) {
			this.listPatentStatus  = new ArrayList<PatentStatus>();
		}
		this.listPatentStatus.add(patentStatus);
	}
	
	
}