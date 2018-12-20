package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="agent")
public class Agent {
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String agent_id;
	
	@JsonView(View.PatentDetail.class)
	private String agent_name;
	
	@JsonView(View.PatentDetail.class)
	private String agent_name_en;
	
	@JsonView(View.PatentDetail.class)
	private String agent_address;
	
	@JsonView(View.PatentDetail.class)
	private int agent_order;
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	private String country_id;
	
	@JsonView(View.PatentDetail.class)
	private String country_name;

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public String getAgent_name_en() {
		return agent_name_en;
	}

	public void setAgent_name_en(String agent_name_en) {
		this.agent_name_en = agent_name_en;
	}

	public String getAgent_address() {
		return agent_address;
	}

	public void setAgent_address(String agent_address) {
		this.agent_address = agent_address;
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

}
