package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="inventor")
public class Inventor extends BaseBean {
	
	@Id
	@JsonView(View.Public.class)
	private String inventor_id;
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent; 
	
	@JsonView(View.Public.class)
	private String inventor_name; 
	
	@JsonView(View.Public.class)
	private String inventor_name_en; 
	
	@JsonView(View.Public.class)
	private String country_id;
	
	@JsonView(View.Public.class)
	private String country_name;
	
	@JsonView(View.Public.class)
	private int inventor_order;

	public String getInventor_id() {
		return inventor_id;
	}

	public void setInventor_id(String inventor_id) {
		this.inventor_id = inventor_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getInventor_name() {
		return inventor_name;
	}

	public void setInventor_name(String inventor_name) {
		this.inventor_name = inventor_name;
	}

	public String getInventor_name_en() {
		return inventor_name_en;
	}

	public void setInventor_name_en(String inventor_name_en) {
		this.inventor_name_en = inventor_name_en;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id.toLowerCase();;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name.toLowerCase();
	}

	public int getInventor_order() {
		return inventor_order;
	}

	public void setInventor_order(int inventor_order) {
		this.inventor_order = inventor_order;
	}

}
