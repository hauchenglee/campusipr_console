package biz.mercue.campusipr.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="portfolio")
public class Portfolio extends  BaseBean{
	
	@Id
	@JsonView({View.Portfolio.class,View.Patent.class})
	private String portfolio_id;
	
	@JsonView({View.Portfolio.class,View.Patent.class})
	private String portfolio_name;
	
	@JsonView({View.Portfolio.class,View.Patent.class})
	private String portfolio_memo;
	
	
	@JsonView(View.PortfolioDetail.class)
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_portfolio", 
		joinColumns = { @JoinColumn(name = "portfolio_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "patent_id") })
	private List<Patent> listPatent;
	
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="business_id")
	private Business business;
	
	
	@JsonView({View.Portfolio.class,View.Patent.class})
	private int portfolio_patent_num;
	
	@JsonView({View.Portfolio.class,View.Patent.class})
	private int portfolio_family_num;
	
	@JsonView(View.PortfolioDetail.class)
	private String portfolio_tech;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;

	public String getPortfolio_id() {
		return portfolio_id;
	}

	public void setPortfolio_id(String portfolio_id) {
		this.portfolio_id = portfolio_id;
	}

	public String getPortfolio_name() {
		return portfolio_name;
	}

	public void setPortfolio_name(String portfolio_name) {
		this.portfolio_name = portfolio_name;
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

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getPortfolio_memo() {
		return portfolio_memo;
	}

	public void setPortfolio_memo(String portfolio_memo) {
		this.portfolio_memo = portfolio_memo;
	}

	public int getPortfolio_patent_num() {
		return portfolio_patent_num;
	}

	public void setPortfolio_patent_num(int portfolio_patent_num) {
		this.portfolio_patent_num = portfolio_patent_num;
	}

	public int getPortfolio_family_num() {
		return portfolio_family_num;
	}

	public void setPortfolio_family_num(int portfolio_family_num) {
		this.portfolio_family_num = portfolio_family_num;
	}

	public String getPortfolio_tech() {
		return portfolio_tech;
	}

	public void setPortfolio_tech(String portfolio_tech) {
		this.portfolio_tech = portfolio_tech;
	}
	
	public void addPatent(Patent patent) {
		if(this.listPatent ==null || this.listPatent.size() == 0) {
			listPatent = new ArrayList<Patent>();
		}
		this.portfolio_patent_num += 1;
		//TODO check sample family
		this.portfolio_family_num += 1;
	}
	
	public void removePatent(Patent patent) { 

		if(this.listPatent !=null && this.listPatent.size() > 0) {
			   Iterator<Patent> iterator = listPatent.iterator();
		       while (iterator.hasNext()) {
		    	   Patent indexPatent = iterator.next();
		    	   if(indexPatent.getPatent_id().equals(patent.getPatent_id())) {
		    		   iterator.remove();
		    		   
		    			this.portfolio_patent_num -= 1;
		    			//TODO check sample family
		    			this.portfolio_family_num -= 1;
		    	   }
		       }
		}
	}


}
