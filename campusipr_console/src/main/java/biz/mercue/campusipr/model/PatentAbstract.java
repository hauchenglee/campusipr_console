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
@Table(name="patent_abstract")
public class PatentAbstract {
	
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String patent_abstract_id;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	@Lob
	private String context_abstract;
	

	public String getPatent_abstract_id() {
		return patent_abstract_id;
	}

	public void setPatent_abstract_id(String patent_abstract_id) {
		this.patent_abstract_id = patent_abstract_id;
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

}
