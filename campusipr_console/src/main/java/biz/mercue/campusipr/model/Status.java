package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="status_id")
@Table(name="status")
public class Status {
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String status_id;
	
	@JsonView(View.PatentDetail.class)
	private String country_id;
	
	@JsonView(View.PatentDetail.class)
	private String event_code;
	
	
	@JsonView(View.PatentDetail.class)
	private String event_code_desc;
	
	
	@JsonView(View.PatentDetail.class)
	private String event_class;
	
	@JsonView(View.PatentDetail.class)
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

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getEvent_code_desc() {
		return event_code_desc;
	}

	public void setEvent_code_desc(String event_code_desc) {
		this.event_code_desc = event_code_desc;
	}

	public String getEvent_class() {
		return event_class;
	}

	public void setEvent_class(String event_class) {
		this.event_class = event_class;
	}

	public Patent getStatus_desc() {
		return status_desc;
	}

	public void setStatus_desc(Patent status_desc) {
		this.status_desc = status_desc;
	}

	public Patent getStatus_desc_en() {
		return status_desc_en;
	}

	public void setStatus_desc_en(Patent status_desc_en) {
		this.status_desc_en = status_desc_en;
	}

	public Patent getStatus_color() {
		return status_color;
	}

	public void setStatus_color(Patent status_color) {
		this.status_color = status_color;
	}

	@JsonView(View.PatentDetail.class)
	private Patent status_desc;
	
	@JsonView(View.PatentDetail.class)
	private Patent status_desc_en;
	
	@JsonView(View.PatentDetail.class)
	private Patent status_color;

	public String getStatus_from() {
		return status_from;
	}

	public void setStatus_from(String status_from) {
		this.status_from = status_from;
	}

}
