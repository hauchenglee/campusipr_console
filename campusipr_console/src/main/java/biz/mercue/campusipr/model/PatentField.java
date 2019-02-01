package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="patent_field")
public class PatentField extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String field_id;
	
	@JsonView(View.Public.class)
	private String field_name;
	
	@JsonView(View.Public.class)
	private String field_name_en;
	
	@JsonView(View.Public.class)
	private String field_code;
	
	@JsonView(View.Public.class)
	private int field_order;
	
	
	private boolean searchable;
	
	@JsonView(View.Public.class)
	private String filed_search_code;
	
	@JsonView(View.Public.class)
	private String field_format;
	
	private boolean is_input;

	public String getField_id() {
		return field_id;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getField_name_en() {
		return field_name_en;
	}

	public void setField_name_en(String field_name_en) {
		this.field_name_en = field_name_en;
	}

	public String getField_code() {
		return field_code;
	}

	public void setField_code(String field_code) {
		this.field_code = field_code;
	}

	public int getField_order() {
		return field_order;
	}

	public void setField_order(int field_order) {
		this.field_order = field_order;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public String getFiled_search_code() {
		return filed_search_code;
	}

	public void setFiled_search_code(String filed_search_code) {
		this.filed_search_code = filed_search_code;
	}

	public String getField_format() {
		return field_format;
	}

	public void setField_format(String field_format) {
		this.field_format = field_format;
	}

	public boolean isIs_input() {
		return is_input;
	}

	public void setIs_input(boolean is_input) {
		this.is_input = is_input;
	}

}
