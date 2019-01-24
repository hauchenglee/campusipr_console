package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="currency")
public class Currency {
	
	@Id
	@JsonView(View.Public.class)
	private String currency_id;
	
	@JsonView(View.Public.class)
	private String currency_name;
	
	@JsonView(View.Public.class)
	private String currency_name_en;

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public String getCurrency_name() {
		return currency_name;
	}

	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}

	public String getCurrency_name_en() {
		return currency_name_en;
	}

	public void setCurrency_name_en(String currency_name_en) {
		this.currency_name_en = currency_name_en;
	}

}
