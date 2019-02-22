package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="annuity")
public class Annuity extends BaseBean{
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String annuity_id;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date annuity_date;
	
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date annuity_end_date;
	
	@JsonView(View.PatentDetail.class)
	private int annuity_charge_year;
	
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	private boolean is_paid;

	@Transient
	private boolean is_reminder = true;

	public Date getAnnuity_date() {
		return annuity_date;
	}

	public void setAnnuity_date(Date annuity_date) {
		this.annuity_date = annuity_date;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public Date getAnnuity_end_date() {
		return annuity_end_date;
	}

	public void setAnnuity_end_date(Date annuity_end_date) {
		this.annuity_end_date = annuity_end_date;
	}

	public int getAnnuity_charge_year() {
		return annuity_charge_year;
	}

	public void setAnnuity_charge_year(int annuity_charge_year) {
		this.annuity_charge_year = annuity_charge_year;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public boolean is_paid() {
		return is_paid;
	}

	public void setIs_paid(boolean is_paid) {
		this.is_paid = is_paid;
	}

	public String getAnnuity_id() {
		return annuity_id;
	}

	public void setAnnuity_id(String annuity_id) {
		this.annuity_id = annuity_id;
	}

	public boolean is_reminder() {
		return is_reminder;
	}

	public void setIs_reminder(boolean is_reminder) {
		this.is_reminder = is_reminder;
	}
	

}
