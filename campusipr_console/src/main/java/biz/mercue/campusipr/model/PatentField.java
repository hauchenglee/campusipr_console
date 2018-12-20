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
//	
//	@JsonView(View.Public.class)
//	private String field_name;
//	
//	@JsonView(View.Public.class)
//	private String field_name_en;
//	
//	@JsonView(View.Public.class)
//	private String field_code;
//
//	public String getField_id() {
//		return field_id;
//	}
//
//	public void setField_id(String field_id) {
//		this.field_id = field_id;
//	}
//
//	public String getField_name() {
//		return field_name;
//	}
//
//	public void setField_name(String field_name) {
//		this.field_name = field_name;
//	}
//
//	public String getField_name_en() {
//		return field_name_en;
//	}
//
//	public void setField_name_en(String field_name_en) {
//		this.field_name_en = field_name_en;
//	}
//
//	public String getField_code() {
//		return field_code;
//	}
//
//	public void setField_code(String field_code) {
//		this.field_code = field_code;
//	}

}
