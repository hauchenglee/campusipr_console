package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="country")
public class Country {
	
	@Id
	@JsonView(View.Public.class)
	private String country_id;
	
	@JsonView(View.Public.class)
	private String country_lang;
	
	@JsonView(View.Public.class)
	private String country_name;
	
	@JsonView(View.Public.class)
	private int country_order;
	
	@JsonView(View.Public.class)
	private String country_alias_name;

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getCountry_lang() {
		return country_lang;
	}

	public void setCountry_lang(String country_lang) {
		this.country_lang = country_lang;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public int getCountry_order() {
		return country_order;
	}

	public void setCountry_order(int country_order) {
		this.country_order = country_order;
	}

	public String getCountry_alias_name() {
		return country_alias_name;
	}

	public void setCountry_alias_name(String country_alias_name) {
		this.country_alias_name = country_alias_name;
	}
	
	
	

}
