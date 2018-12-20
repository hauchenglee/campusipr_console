package biz.mercue.campusipr.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="patent_family")
public class PatentFamily extends BaseBean{
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String patent_family_id;
	
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy ="family")
	private List<Patent> listPatent;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;

	public String getPatent_family_id() {
		return patent_family_id;
	}

	public void setPatent_family_id(String patent_family_id) {
		this.patent_family_id = patent_family_id;
	}

	public List<Patent> getListPatent() {
		return listPatent;
	}

	public void setListPatent(List<Patent> listPatent) {
		this.listPatent = listPatent;
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
