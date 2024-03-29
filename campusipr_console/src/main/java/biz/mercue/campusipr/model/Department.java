package biz.mercue.campusipr.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name="department")
public class Department extends BaseBean {

	@Id
	@JsonView(View.Public.class)
	private String department_id;

	@JsonView(View.Public.class)
	private String department_name;

	@JsonView(View.Public.class)
	private String department_name_en;

	@ManyToOne
	@JoinColumn(name = "patent_id")
	private Patent patent;

	@JsonView(View.Public.class)
	private String business_id;

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getDepartment_name_en() {
		return department_name_en;
	}

	public void setDepartment_name_en(String department_name_en) {
		this.department_name_en = department_name_en;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
}
