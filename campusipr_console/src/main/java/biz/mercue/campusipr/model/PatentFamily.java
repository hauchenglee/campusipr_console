package biz.mercue.campusipr.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Filter;
import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Entity
@Table(name="patentfamily")
public class PatentFamily extends BaseBean{
	
	@Transient
	Gson gson = new Gson();
	
	@Id
	@JsonView({View.Patent.class,View.PatentDetail.class,View.PatentFamily.class,View.PortfolioDetail.class})
	private String patent_family_id;
	
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String country_list;
	
	@JsonView({View.PatentFamily.class})
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_family",
			joinColumns = { @JoinColumn(name = "family_id") },
			inverseJoinColumns = { @JoinColumn(name = "patent_id") })
//	@Filter(name = "businessFilter",condition=" business_id= :business_id")
	private List<Patent> listPatent;

	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	
	@JsonView({View.Patent.class, View.PatentDetail.class})
	private String business_id;

	@Transient
	private List<String> listCountry;

	@JsonView({View.Patent.class})
	@Transient
	private List<String> listPatentIds;

	@Transient
	private List<String> deletePatentIds;

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

	public String getCountry_list() {
		return country_list;
	}

	public void setCountry_list(String country_list) {
		this.country_list = country_list;
		
	}

	public List<String> getListCountry() {
		if(this.listCountry == null) {
			this.listCountry = new ArrayList<String>();
			this.listCountry = gson.fromJson(country_list, new TypeToken<List<String>>() {}.getType());
		}
		return listCountry;
	}

	public void setListCountry(List<String> listCountry) {
		this.listCountry = listCountry;
		this.country_list = gson.toJson(this.listCountry);
	}
	
	public void addPatent(Patent patent) {
		
//		patent.setFamily(this);
		
		if(this.listPatent == null) {
			this.listPatent = new ArrayList<Patent>();
			listPatent.add(patent);
			this.listCountry = new ArrayList<>();
			this.listCountry.add(patent.getPatent_appl_country());
			this.country_list = gson.toJson(this.listCountry);
		}else {
			boolean isContain = false;
			for(Patent mPatent : this.listPatent) {
				if(mPatent.getPatent_id().equals(patent.getPatent_id())) {
					isContain = true;
				}
			}
			if(!isContain) {
				this.listPatent.add(patent);
			}
			
			if(listCountry == null) {
				this.listCountry = gson.fromJson(country_list, new TypeToken<List<String>>() {}.getType());
			}
			if(!listCountry.contains(patent.getPatent_appl_country())) {
				listCountry.add(patent.getPatent_appl_country());
				this.country_list = gson.toJson(this.listCountry);
			}
		}
	}

	public List<String> getListPatentIds() {
		return listPatentIds;
	}

	public void setListPatentIds(List<String> listPatentIds) {
		this.listPatentIds = listPatentIds;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public List<String> getDeletePatentIds() {
		return deletePatentIds;
	}

	public void setDeletePatentIds(List<String> deletePatentIds) {
		this.deletePatentIds = deletePatentIds;
	}
}
