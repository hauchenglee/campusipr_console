package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="field_sync")
public class FieldSync extends BaseBean{
	
	
	@Id
	@JsonView(View.Public.class)
	private String field_id;
	
	@Id
	@JsonView(View.Public.class)
	private String country_id;


	public String getField_id() {
		return field_id;
	}


	public void setField_id(String field_id) {
		this.field_id = field_id;
	}


	public String getCountry_id() {
		return country_id;
	}


	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

}
