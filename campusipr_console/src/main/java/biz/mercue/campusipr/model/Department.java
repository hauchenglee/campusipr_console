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
	
	@ManyToOne
	@JsonView(View.PatentDetail.class)
	@JoinColumn(name="business_id")
	private Business business;
	
	@JsonView(View.Public.class)
	private String department_name;
	
	@JsonView(View.Public.class)
	private String department_name_en;
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent;
}
