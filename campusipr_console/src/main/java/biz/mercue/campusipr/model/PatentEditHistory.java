package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name="patent_edit_history")
public class PatentEditHistory {
	
	@Id
	@JsonView(View.PatentHistory.class)
	private String history_id;
	
	@JsonView(View.PatentDetail.class)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	private String field_id;
	
	
	@JsonView(View.PatentHistory.class)
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="admin_id", referencedColumnName="admin_id")
	private Admin admin;
	
	
	@JsonView(View.PatentDetail.class)
	private String history_data;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;

	public String getHistory_id() {
		return history_id;
	}

	public void setHistory_id(String history_id) {
		this.history_id = history_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getField_id() {
		return field_id;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public String getHistory_data() {
		return history_data;
	}

	public void setHistory_data(String history_data) {
		this.history_data = history_data;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	
	
	

}
