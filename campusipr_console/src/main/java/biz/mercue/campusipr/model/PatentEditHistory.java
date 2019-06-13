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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name="patent_edit_history")
public class PatentEditHistory {
	
	@Id
	@JsonView(View.PatentHistory.class)
	private String history_id;
	
	@JsonView(View.PatentDetail.class)
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	private String field_id;
	
	
	@JsonView(View.PatentHistory.class)
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="admin_id", referencedColumnName="admin_id")
	private Admin admin;
	
	
	@JsonView(View.PatentHistory.class)
	private String history_data;
	
	@JsonView(View.PatentHistory.class)
	private String history_status;
	
	@JsonView(View.PatentHistory.class)
	private String admin_ip;
	
	@JsonView(View.PatentHistory.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Transient
	@JsonView(View.PatentHistory.class)
	private String display_data;
	
	@Transient
	@JsonView(View.PatentHistory.class)
	private String display_data_en;
	
	@Transient
	@JsonView(View.PatentHistory.class)
	private String display_status;
	
	@Transient
	@JsonView(View.PatentHistory.class)
	private String display_status_en;
	
	@JsonView(View.PatentHistory.class)
	private String editor;
	
	@JsonView(View.PatentHistory.class)
	private String business_id;

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

	public String getAdmin_ip() {
		return admin_ip;
	}

	public void setAdmin_ip(String admin_ip) {
		this.admin_ip = admin_ip;
	}

	public String getHistory_status() {
		return history_status;
	}

	public void setHistory_status(String history_status) {
		this.history_status = history_status;
	}

	public String getDisplay_data() {
		return display_data;
	}

	public void setDisplay_data(String display_data) {
		this.display_data = display_data;
	}

	public String getDisplay_data_en() {
		return display_data_en;
	}

	public void setDisplay_data_en(String display_data_en) {
		this.display_data_en = display_data_en;
	}

	public String getDisplay_status() {
		return display_status;
	}

	public void setDisplay_status(String display_status) {
		this.display_status = display_status;
	}

	public String getDisplay_status_en() {
		return display_status_en;
	}

	public void setDisplay_status_en(String display_status_en) {
		this.display_status_en = display_status_en;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	
	
	
	

}
