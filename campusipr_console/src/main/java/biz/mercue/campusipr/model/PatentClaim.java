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
@Table(name="patent_claim")
public class PatentClaim {
	
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String patent_claim_id;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	@Lob
	private String context_claim;
	

	public String getPatent_claim_id() {
		return patent_claim_id;
	}

	public void setPatent_claim_id(String patent_claim_id) {
		this.patent_claim_id = patent_claim_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getContext_claim() {
		return context_claim;
	}

	public void setContext_claim(String context_claim) {
		this.context_claim = context_claim;
	}

}
