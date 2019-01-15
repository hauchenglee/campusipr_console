package biz.mercue.campusipr.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="excel_task")
public class ExcelTask extends BaseBean {
	
	@Id
	@JsonView(View.ExcelTask.class)
	private  String excel_task_id;
	
	
	@JsonView(View.ExcelTask.class)
	private  String task_file_name;
	
	@OneToOne
	@JoinColumn(name="business_id")
	private Business business;
	
	@OneToOne
	@JoinColumn(name="admin_id")
	private Admin admin;
	
	@JsonView(View.ExcelTask.class)
	private Date create_date;
	
	@JsonView(View.ExcelTask.class)
	private boolean is_finish;
	
	private boolean is_inform;
	
	@OneToMany(mappedBy = "task",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FieldMap> listMap;

	public String getExcel_task_id() {
		return excel_task_id;
	}

	public void setExcel_task_id(String excel_task_id) {
		this.excel_task_id = excel_task_id;
	}

	public String getTask_file_name() {
		return task_file_name;
	}

	public void setTask_file_name(String task_file_name) {
		this.task_file_name = task_file_name;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public boolean isIs_finish() {
		return is_finish;
	}

	public void setIs_finish(boolean is_finish) {
		this.is_finish = is_finish;
	}

	public boolean isIs_inform() {
		return is_inform;
	}

	public void setIs_inform(boolean is_inform) {
		this.is_inform = is_inform;
	}

}
