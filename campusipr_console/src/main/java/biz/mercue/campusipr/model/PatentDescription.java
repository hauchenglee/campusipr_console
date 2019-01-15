package biz.mercue.campusipr.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="patent_desc")
public class PatentDescription {
	
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String patent_desc_id;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	@Lob
	private String context_desc;
	

	public String getPatent_desc_id() {
		return patent_desc_id;
	}

	public void setPatent_desc_id(String patent_desc_id) {
		this.patent_desc_id = patent_desc_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getContext_desc() {
		return context_desc;
	}

	public void setContext_desc(String context_desc) {
		this.context_desc = context_desc;
	}

}
