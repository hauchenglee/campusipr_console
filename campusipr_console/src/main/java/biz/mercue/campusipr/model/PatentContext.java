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
@Table(name="patent_context")
public class PatentContext {
	
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String patent_context_id;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	@Lob
	private String context_abstract;
	
	@JsonView(View.PatentDetail.class)
	@Lob
	private String context_desc;
	
	@JsonView(View.PatentDetail.class)
	@Lob
	private String context_claim;

	public String getPatent_context_id() {
		return patent_context_id;
	}

	public void setPatent_context_id(String patent_context_id) {
		this.patent_context_id = patent_context_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getContext_abstract() {
		return context_abstract;
	}

	public void setContext_abstract(String context_abstract) {
		this.context_abstract = context_abstract;
	}

	public String getContext_desc() {
		return context_desc;
	}

	public void setContext_desc(String context_desc) {
		this.context_desc = context_desc;
	}

	public String getContext_claim() {
		return context_claim;
	}

	public void setContext_claim(String context_claim) {
		this.context_claim = context_claim;
	}

}
