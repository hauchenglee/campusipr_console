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

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="annuity")
public class Annuity extends BaseBean{
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String annuity;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date annuity_date;
	
	@JsonView(View.PatentDetail.class)
	private int annuity_beg;
	
	@JsonView(View.PatentDetail.class)
	private int  annuity_end;
	
	@JsonView(View.PatentDetail.class)	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;

	public String getAnnuity() {
		return annuity;
	}

	public void setAnnuity(String annuity) {
		this.annuity = annuity;
	}

	public Date getAnnuity_date() {
		return annuity_date;
	}

	public void setAnnuity_date(Date annuity_date) {
		this.annuity_date = annuity_date;
	}

	public int getAnnuity_beg() {
		return annuity_beg;
	}

	public void setAnnuity_beg(int annuity_beg) {
		this.annuity_beg = annuity_beg;
	}

	public int getAnnuity_end() {
		return annuity_end;
	}

	public void setAnnuity_end(int annuity_end) {
		this.annuity_end = annuity_end;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}
	

}
