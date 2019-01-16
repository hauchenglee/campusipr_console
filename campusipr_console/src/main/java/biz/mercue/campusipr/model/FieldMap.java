package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="field_map")
public class FieldMap {
	
	@Id
	@JsonView({View.FieldMap.class,View.ExcelTask.class})
	private String field_map_id;
	
	@JsonView(View.FieldMap.class)
	@ManyToOne
	@JoinColumn(name="excel_task_id")
	private ExcelTask task;
	
	@JsonView({View.FieldMap.class,View.ExcelTask.class})
	@OneToOne
	@JoinColumn(name="field_id")
	private PatentField field;
	
	
	@JsonView({View.FieldMap.class,View.ExcelTask.class})
	private String excel_field_name;
	
	@JsonView({View.FieldMap.class,View.ExcelTask.class})
	private int excel_field_index;
	

	@JsonView(View.FieldMap.class)
	private Date create_date;
	
	
	private Date update_date;


	public String getField_map_id() {
		return field_map_id;
	}


	public void setField_map_id(String field_map_id) {
		this.field_map_id = field_map_id;
	}


	public ExcelTask getTask() {
		return task;
	}


	public void setTask(ExcelTask task) {
		this.task = task;
	}


	public PatentField getField() {
		return field;
	}


	public void setField(PatentField field) {
		this.field = field;
	}


	public String getExcel_field_name() {
		return excel_field_name;
	}


	public void setExcel_field_name(String excel_field_name) {
		this.excel_field_name = excel_field_name;
	}


	public int getExcel_field_index() {
		return excel_field_index;
	}


	public void setExcel_field_index(int excel_field_index) {
		this.excel_field_index = excel_field_index;
	}


	public Date getCreate_date() {
		return create_date;
	}


	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}


	public Date getUpdate_date() {
		return update_date;
	}


	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
}
