package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name="applicant")
public class Applicant extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String applicant_id;
	
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent;
	
	
	@JsonView(View.Public.class)
	private String applicant_name;
	
	@JsonView(View.Public.class)
	private String applicant_name_en;
	
	@JsonView(View.Public.class)
	private String applicant_address;
	
	@JsonView(View.Public.class)
	private String applicant_address_en;
	
	@JsonView(View.Public.class)
	private int applicant_order;
	
	@JsonView(View.Public.class)
	private String country_id;
	
	@JsonView(View.Public.class)
	private String country_name;

	public String getApplicant_id() {
		return applicant_id;
	}

	public void setApplicant_id(String applicant_id) {
		this.applicant_id = applicant_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getApplicant_name() {
		return applicant_name;
	}

	public void setApplicant_name(String applicant_name) {
		this.applicant_name = applicant_name;
	}

	public String getApplicant_name_en() {
		return applicant_name_en;
	}

	public void setApplicant_name_en(String applicant_name_en) {
		this.applicant_name_en = applicant_name_en;
	}

	public String getApplicant_address() {
		return applicant_address;
	}

	public void setApplicant_address(String applicant_address) {
		this.applicant_address = applicant_address;
	}

	public String getApplicant_address_en() {
		return applicant_address_en;
	}

	public void setApplicant_address_en(String applicant_address_en) {
		this.applicant_address_en = applicant_address_en;
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

	public int getApplicant_order() {
		return applicant_order;
	}

	public void setApplicant_order(int applicant_order) {
		this.applicant_order = applicant_order;
	}
	

}
