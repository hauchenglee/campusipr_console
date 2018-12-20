package biz.mercue.campusipr.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="assignee")
public class Assignee extends BaseBean {
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String assignee_id;
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent;
	
	
	@JsonView(View.PatentDetail.class)
	private String assignee_name;
	
	
	@JsonView(View.PatentDetail.class)
	private String assignee_name_en;

	
	@JsonView(View.PatentDetail.class)
	private String country_id;
	
	@JsonView(View.PatentDetail.class)
	private String country_name;
	
	@JsonView(View.PatentDetail.class)
	private int assignee_order;

	public String getAssignee_id() {
		return assignee_id;
	}

	public void setAssignee_id(String assignee_id) {
		this.assignee_id = assignee_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getAssignee_name() {
		return assignee_name;
	}

	public void setAssignee_name(String assignee_name) {
		this.assignee_name = assignee_name;
	}

	public String getAssignee_name_en() {
		return assignee_name_en;
	}

	public void setAssignee_name_en(String assignee_name_en) {
		this.assignee_name_en = assignee_name_en;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public int getAssignee_order() {
		return assignee_order;
	}

	public void setAssignee_order(int assignee_order) {
		this.assignee_order = assignee_order;
	}

}
