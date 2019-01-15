package biz.mercue.campusipr.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="patent_extension")
public class PatentExtension extends BaseBean {
	
	@Id
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String extension_id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private Patent patent;
	

	@JsonView({View.Patent.class})
	private String business_id;
	
	//檔號
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String extension_file_num;
	
	
	//校內編號
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String business_num;
	
	//申請年度
	@JsonView(View.PatentEnhance.class)
	private String extension_appl_year;
	
	
	//備註
	@JsonView(View.PatentEnhance.class)
	private String extension_memo;
	
	//補助單位
	@JsonView(View.PatentEnhance.class)
	private String extension_subsidy_unit;
	
	//補助編號
	@JsonView(View.PatentEnhance.class)
	private String extension_subsidy_num;
	
	//補助計畫名稱
	@JsonView(View.PatentEnhance.class)
	private String extension_subsidy_plan;
	
	//事務所
	@JsonView(View.PatentEnhance.class)
	private String extension_agent;
	
	
	//事務所編號
	@JsonView(View.PatentEnhance.class)
	private String extension_agent_num;


	public String getExtension_id() {
		return extension_id;
	}


	public void setExtension_id(String extension_id) {
		this.extension_id = extension_id;
	}


	public Patent getPatent() {
		return patent;
	}


	public void setPatent(Patent patent) {
		this.patent = patent;
	}


	public String getBusiness_id() {
		return business_id;
	}


	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}


	public String getBusiness_num() {
		return business_num;
	}


	public void setBusiness_num(String business_num) {
		this.business_num = business_num;
	}


	public String getExtension_appl_year() {
		return extension_appl_year;
	}


	public void setExtension_appl_year(String extension_appl_year) {
		this.extension_appl_year = extension_appl_year;
	}


	public String getExtension_memo() {
		return extension_memo;
	}


	public void setExtension_memo(String extension_memo) {
		this.extension_memo = extension_memo;
	}


	public String getExtension_subsidy_unit() {
		return extension_subsidy_unit;
	}


	public void setExtension_subsidy_unit(String extension_subsidy_unit) {
		this.extension_subsidy_unit = extension_subsidy_unit;
	}


	public String getExtension_subsidy_num() {
		return extension_subsidy_num;
	}


	public void setExtension_subsidy_num(String extension_subsidy_num) {
		this.extension_subsidy_num = extension_subsidy_num;
	}


	public String getExtension_subsidy_plan() {
		return extension_subsidy_plan;
	}


	public void setExtension_subsidy_plan(String extension_subsidy_plan) {
		this.extension_subsidy_plan = extension_subsidy_plan;
	}


	public String getExtension_agent() {
		return extension_agent;
	}


	public void setExtension_agent(String extension_agent) {
		this.extension_agent = extension_agent;
	}


	public String getExtension_agent_num() {
		return extension_agent_num;
	}


	public void setExtension_agent_num(String extension_agent_num) {
		this.extension_agent_num = extension_agent_num;
	}
	

}
