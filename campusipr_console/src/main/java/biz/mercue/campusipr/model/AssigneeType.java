package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="assignee_type")
public class AssigneeType {
	
	@Id
	@JsonView(View.Public.class)
	private String assignee_type_id;
	
	
	@JsonView(View.Public.class)
	private String country_id;
	
	
	@JsonView(View.Public.class)
	private String assignee_type_desc;


	public String getAssignee_type_id() {
		return assignee_type_id;
	}


	public void setAssignee_type_id(String assignee_type_id) {
		this.assignee_type_id = assignee_type_id;
	}


	public String getCountry_id() {
		return country_id;
	}


	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}


	public String getAssignee_type_desc() {
		return assignee_type_desc;
	}


	public void setAssignee_type_desc(String assignee_type_desc) {
		this.assignee_type_desc = assignee_type_desc;
	}
	
	
	
	

}
