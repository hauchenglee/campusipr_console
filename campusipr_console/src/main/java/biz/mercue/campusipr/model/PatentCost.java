package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="patent_cost")
public class PatentCost extends BaseBean {
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String cost_id;
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent;
	
	@JsonView(View.PatentDetail.class)
	private String cost_name;
	
	@JsonView(View.PatentDetail.class)
	private int cost_price;
	
	@JsonView(View.PatentDetail.class)
	private String cost_unit;
	
	
	@JsonView(View.PatentDetail.class)
	private String cost_currency;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date cost_date;
	
	@JsonView(View.PatentDetail.class)
	private String cost_memo;
	
	private Date create_date;
	
	private Date update_date;

	public String getCost_id() {
		return cost_id;
	}

	public void setCost_id(String cost_id) {
		this.cost_id = cost_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public String getCost_name() {
		return cost_name;
	}

	public void setCost_name(String cost_name) {
		this.cost_name = cost_name;
	}

	public int getCost_price() {
		return cost_price;
	}

	public void setCost_price(int cost_price) {
		this.cost_price = cost_price;
	}

	public String getCost_currency() {
		return cost_currency;
	}

	public void setCost_currency(String cost_currency) {
		this.cost_currency = cost_currency;
	}

	public Date getCost_date() {
		return cost_date;
	}

	public void setCost_date(Date cost_date) {
		this.cost_date = cost_date;
	}

	public String getCost_memo() {
		return cost_memo;
	}

	public void setCost_memo(String cost_memo) {
		this.cost_memo = cost_memo;
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

	public String getCost_unit() {
		return cost_unit;
	}

	public void setCost_unit(String cost_unit) {
		this.cost_unit = cost_unit;
	}
	

}
