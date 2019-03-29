package biz.mercue.campusipr.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;


@FilterDef(name = "businessFilter",  parameters = @ParamDef(name = "business_id", type ="string"))
@Entity
@Table(name="patent")
public class Patent extends BaseBean{
	
	
	@Id
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_id;
	
	//all
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_name;
	
	//all
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_name_en;
	
	//all
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_appl_country;
	
	//all
	@JsonView(View.Patent.class)
	private Date patent_appl_date;
	
	//all
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_appl_no;
	
	//all
	@JsonView(View.Patent.class)
	private String patent_notice_no;
	
	
	//all
	@JsonView(View.Patent.class)
	private Date patent_notice_date;
	
	//all
	@JsonView(View.Patent.class)
	private String patent_publish_no;
	
	//all
	@JsonView(View.Patent.class)
	private Date patent_publish_date;
	
	//all
	@JsonView(View.Patent.class)
	private String patent_no;
	
	//tw
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_bdate;
	
	//tw
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_edate;
	
	//tw
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_cancel_date;
	
	
	//tw
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_charge_expire_date;
	
	//tw
	@JsonView(View.Patent.class)
	private int patent_charge_duration_year;
	
	
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_business", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "business_id") })
	private List<Business> listBusiness;
	
	//all
	@JsonView(View.PatentDetail.class)
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_ipc", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "ipc_class_id") })
	@OrderColumn(name="ipc_order")
	private List<IPCClass> listIPC;
	
	//all + manual
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	@OneToMany(mappedBy= "primaryKey.patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval=true)
	@OrderBy("create_date DESC")
	private List<PatentStatus> listPatentStatus;
	
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval=true)
	@OrderBy("agent_order")
	private List<Agent> listAgent;
	
	
	//all
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval=true)
	@OrderBy("assignee_order")
	private List<Assignee> listAssignee;
	
	//all
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval=true)
	@OrderBy("applicant_order")
	private List<Applicant> listApplicant;
	
	//all
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval=true)
	@OrderBy("inventor_order")
	private List<Inventor> listInventor;
	
	
	//manual
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("contact_order")
	private List<PatentContact> listContact;
	
	//manual
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("cost_date desc")
	private List<PatentCost> listCost;
	
	//all
	@JsonView(View.PatentDetail.class)
	@OneToOne(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PatentAbstract patentAbstract;
	
	
	//all
	@JsonView(View.PatentDetail.class)
	@OneToOne(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PatentClaim patentClaim;
	
	//all
	@JsonView(View.PatentDetail.class)
	@OneToOne(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PatentDescription patentDesc;
	
	//manual + import
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Filter(name = "businessFilter",condition=" business_id= :business_id")
	private List<PatentExtension> listExtension;
	
	
	@JsonView(View.PatentHistory.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("create_date DESC")
	private List<PatentEditHistory> listHistory;
	

	//tw + maunual
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("annuity_date DESC")
	private List<Annuity> listAnnuity;
	
	
	@JsonView(View.PatentDetail.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_portfolio", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "portfolio_id") })
	private List<Portfolio> listPortfolio;
	
	@ManyToOne
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	@JoinColumn(name="patent_family_id")
	private PatentFamily family;
	
	private boolean is_public = false;
	
	@JsonView({View.PatentDetail.class})
	private boolean is_sync = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date sync_date;
	
	//for update and add patent
	@Transient
	@JsonView(View.PatentDetail.class)
	private Business business;
	
	@Transient
	@JsonView(View.PatentDetail.class)
	private Admin admin;
	
	//same patent_appl_no in database or same patent id in database.
	@Transient
	private Patent comparePatent;
	
	
	@Transient
	private String  country_name;
	
	//sample 2019/01/22
	@Transient
	private String  annuity_date;
	
	
	@Transient
	private PatentExtension extension;
	
	@Transient
	private String admin_ip;
	
	@Transient
	private int edit_source;
	
	@Transient
	public static final int EDIT_SOURCE_HUMAN = 1;
	
	@Transient
	public static final int EDIT_SOURCE_SERVICE =2;
	
	
	@Transient
	public static final int EDIT_SOURCE_IMPORT =3;
	

	
	public String getPatent_id() {
		return patent_id;
	}

	public void setPatent_id(String patent_id) {
		this.patent_id = patent_id;
	}

	public String getPatent_name() {
		return patent_name;
	}

	public void setPatent_name(String patent_name) {
		this.patent_name = patent_name;
	}

	public String getPatent_name_en() {
		return patent_name_en;
	}

	public void setPatent_name_en(String patent_name_en) {
		this.patent_name_en = patent_name_en;
	}

	public String getPatent_appl_country() {
		return patent_appl_country;
	}

	public void setPatent_appl_country(String patent_appl_country) {
		this.patent_appl_country = patent_appl_country.toLowerCase();
	}

	public Date getPatent_appl_date() {
		return patent_appl_date;
	}

	public void setPatent_appl_date(Date patent_appl_date) {
		this.patent_appl_date = patent_appl_date;
	}

	public String getPatent_appl_no() {
		return patent_appl_no;
	}

	public void setPatent_appl_no(String patent_appl_no) {
		if (!StringUtils.isNULL(patent_appl_no) && !StringUtils.isNULL(patent_appl_country)) {
			if (patent_appl_country.toLowerCase().equals(Constants.APPL_COUNTRY_TW)) {
				patent_appl_no = Constants.APPL_COUNTRY_TW.toUpperCase()+(patent_appl_no.replace("TW", "").replace("US", "").replace("CN", ""));
			}
			if (patent_appl_country.toLowerCase().equals(Constants.APPL_COUNTRY_US)) {
				patent_appl_no = Constants.APPL_COUNTRY_US.toUpperCase()+(patent_appl_no.replace("TW", "").replace("US", "").replace("CN", ""));
			}
			if (patent_appl_country.toLowerCase().equals(Constants.APPL_COUNTRY_CN)) {
				patent_appl_no = Constants.APPL_COUNTRY_CN.toUpperCase()+(patent_appl_no.replace("TW", "").replace("US", "").replace("CN", ""));
			}
			if (patent_appl_no.contains(".")) {
				patent_appl_no = patent_appl_no.substring(0, patent_appl_no.indexOf("."));
			}
			patent_appl_no = patent_appl_no.replaceAll("\\s+","").replaceAll("/", "").replaceAll(",", "").replaceAll("[\\pP\\p{Punct}]","");
		}
		this.patent_appl_no = patent_appl_no;
	}

	public String getPatent_notice_no() {
		return patent_notice_no;
	}

	public void setPatent_notice_no(String patent_notice_no) {
		if (!StringUtils.isNULL(patent_notice_no)) {
			if (patent_notice_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_TW)) {
				patent_notice_no = patent_notice_no.replace("TW", "").replace("tw", "");
			}
			if (patent_notice_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_US)) {
				patent_notice_no = patent_notice_no.replace("US", "").replace("us", "");
			}
			if (patent_notice_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_CN)) {
				patent_notice_no = patent_notice_no.replace("CN", "").replace("cn", "");
			}
		}
		this.patent_notice_no = patent_notice_no;
	}

	public Date getPatent_notice_date() {
		return patent_notice_date;
	}

	public void setPatent_notice_date(Date patent_notice_date) {
		this.patent_notice_date = patent_notice_date;
	}

	public String getPatent_publish_no() {
		return patent_publish_no;
	}

	public void setPatent_publish_no(String patent_publish_no) {
		if (!StringUtils.isNULL(patent_publish_no)) {
			if (patent_publish_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_TW)) {
				patent_publish_no = patent_publish_no.replace("TW", "").replace("tw", "");
			}
			if (patent_publish_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_US)) {
				patent_publish_no = patent_publish_no.replace("US", "").replace("us", "");
			}
			if (patent_publish_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_CN)) {
				patent_publish_no = patent_publish_no.replace("CN", "").replace("cn", "");
			}
		}
		this.patent_publish_no = patent_publish_no;
	}

	public Date getPatent_publish_date() {
		return patent_publish_date;
	}

	public void setPatent_publish_date(Date patent_publish_date) {
		this.patent_publish_date = patent_publish_date;
	}

	public String getPatent_no() {
		return patent_no;
	}

	public void setPatent_no(String patent_no) {
		if (!StringUtils.isNULL(patent_no)) {
			if (patent_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_TW)) {
				patent_no = patent_no.replace("TW", "").replace("tw", "");
			}
			if (patent_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_US)) {
				patent_no = patent_no.replace("US", "").replace("us", "");
			}
			if (patent_no.toLowerCase().startsWith(Constants.APPL_COUNTRY_CN)) {
				patent_no = patent_no.replace("CN", "").replace("cn", "");
			}
		}
		this.patent_no = patent_no;
	}

	public Date getPatent_bdate() {
		return patent_bdate;
	}

	public void setPatent_bdate(Date patent_bdate) {
		this.patent_bdate = patent_bdate;
	}

	public Date getPatent_edate() {
		return patent_edate;
	}

	public void setPatent_edate(Date patent_edate) {
		this.patent_edate = patent_edate;
	}

	public Date getPatent_cancel_date() {
		return patent_cancel_date;
	}

	public void setPatent_cancel_date(Date patent_cancel_date) {
		this.patent_cancel_date = patent_cancel_date;
	}

	public Date getPatent_charge_expire_date() {
		return patent_charge_expire_date;
	}

	public void setPatent_charge_expire_date(Date patent_charge_expire_date) {
		this.patent_charge_expire_date = patent_charge_expire_date;
	}

	public int getPatent_charge_duration_year() {
		return patent_charge_duration_year;
	}

	public void setPatent_charge_duration_year(int patent_charge_duration_year) {
		this.patent_charge_duration_year = patent_charge_duration_year;
	}

	public List<IPCClass> getListIPC() {
		return listIPC;
	}

	public void setListIPC(List<IPCClass> listIPC) {
		this.listIPC = listIPC;
	}
	
	public void addIPCClass(IPCClass ipc) {
		if(this.listIPC == null) {
			this.listIPC = new ArrayList<IPCClass>();
		}
		listIPC.add(ipc);
	}

	public List<Applicant> getListApplicant() {
		return listApplicant;
	}

	public void setListApplicant(List<Applicant> listApplicant) {
		this.listApplicant = listApplicant;
	}
	public void addApplicant(Applicant applicant) {
		if(this.listApplicant == null) {
			this.listApplicant = new ArrayList<Applicant>();
		}
		listApplicant.add(applicant);
	}

	public List<Inventor> getListInventor() {
		return listInventor;
	}
	
	public void addInventor(Inventor inventor) {
		if(this.listInventor == null) {
			this.listInventor = new ArrayList<Inventor>();
		}
		listInventor.add(inventor);
	}

	public void setListInventor(List<Inventor> listInventor) {
		this.listInventor = listInventor;
	}

	public List<PatentContact> getListContact() {
		return listContact;
	}
	
	public void addContact(PatentContact contact) {
		if(this.listContact == null) {
			this.listContact = new ArrayList<PatentContact>();
		}
		listContact.add(contact);
	}

	public void setListContact(List<PatentContact> listContact) {
		this.listContact = listContact;
	}
	
	public List<PatentEditHistory> getListHistory() {
		return listHistory;
	}

	public void setListHistory(List<PatentEditHistory> listHistory) {
		this.listHistory = listHistory;
	}
	
	public void addHistory(PatentEditHistory history) {
		if(this.listHistory == null) {
			this.listHistory = new ArrayList<PatentEditHistory>();
		}
		listHistory.add(history);
	}
	
	public void addHistory(List<PatentEditHistory> list) {
		if(this.listHistory == null) {
			this.listHistory = new ArrayList<PatentEditHistory>();
		}
		listHistory.addAll(list);
	}
	

	public PatentFamily getFamily() {
		return family;
	}

	public void setFamily(PatentFamily family) {
		this.family = family;
	}

	public List<Portfolio> getListPortfolio() {
		return listPortfolio;
	}

	public void setListPortfolio(List<Portfolio> listPortfolio) {
		this.listPortfolio = listPortfolio;
	}

	public List<PatentCost> getListCost() {
		return listCost;
	}

	public void setListCost(List<PatentCost> listCost) {
		this.listCost = listCost;
	}
	
	public void addCost(PatentCost cost) {
		if(this.listCost == null) {
			this.listCost = new ArrayList<PatentCost>();
		}
		listCost.add(cost);
	}

	public List<Assignee> getListAssignee() {
		return listAssignee;
	}

	public void setListAssignee(List<Assignee> listAssignee) {
		this.listAssignee = listAssignee;
	}

	
	public void addAssignee(Assignee assignee) {
		if(this.listAssignee == null) {
			this.listAssignee = new ArrayList<Assignee>();
		}
		listAssignee.add(assignee);
	}
	public List<Agent> getListAgent() {
		return listAgent;
	}

	public void setListAgent(List<Agent> listAgent) {
		this.listAgent = listAgent;
	}

	
	public void addAgent(Agent agent) {
		if(this.listAgent == null) {
			this.listAgent = new ArrayList<Agent>();
		}
		listAgent.add(agent);
	}
	public List<Business> getListBusiness() {
		return listBusiness;
	}

	public void setListBusiness(List<Business> listBusiness) {
		this.listBusiness = listBusiness;
	}
	
	public void addBusiness(Business business) {
		if(this.listBusiness == null) {
			this.listBusiness = new ArrayList<Business>();
		}
		listBusiness.add(business);
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}
	




	public Admin getAdmin() {
		return admin;
	}


	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<PatentExtension> getListExtension() {
		return listExtension;
	}

	public void setListExtension(List<PatentExtension> listExtension) {
		this.listExtension = listExtension;
	}
	
	public String getAdmin_ip() {
		return admin_ip;
	}

	public void setAdmin_ip(String admin_ip) {
		this.admin_ip = admin_ip;
	}

	public PatentAbstract getPatentAbstract() {
		return patentAbstract;
	}

	public void setPatentAbstract(PatentAbstract patentAbstract) {
		this.patentAbstract = patentAbstract;
	}

	public PatentClaim getPatentClaim() {
		return patentClaim;
	}

	public void setPatentClaim(PatentClaim patentClaim) {
		this.patentClaim = patentClaim;
	}

	public PatentDescription getPatentDesc() {
		return patentDesc;
	}

	public void setPatentDesc(PatentDescription patentDesc) {
		this.patentDesc = patentDesc;
	}

	public int getEdit_source() {
		return edit_source;
	}

	public void setEdit_source(int edit_source) {
		this.edit_source = edit_source;
	}

	public List<Annuity> getListAnnuity() {
		return listAnnuity;
	}

	public void setListAnnuity(List<Annuity> listAnnuity) {
		this.listAnnuity = listAnnuity;
	}
	
	public void addAnnuity(Annuity annuity) {
		if(this.listAnnuity == null) {
			this.listAnnuity = new ArrayList<Annuity>();
		}
		listAnnuity.add(annuity);
	}

	public PatentExtension getExtension() {
		return extension;
	}

	public void setExtension(PatentExtension extension) {
		this.extension = extension;
	}

	public Patent getComparePatent() {
		return comparePatent;
	}

	public void setComparePatent(Patent comparePatent) {
		if(StringUtils.isNULL(this.patent_id)) {
			this.patent_id = comparePatent.getPatent_id();
		}
		this.comparePatent = comparePatent;
	}

	public boolean isIs_public() {
		return is_public;
	}

	public void setIs_public(boolean is_public) {
		this.is_public = is_public;
	}

	public boolean isIs_sync() {
		return is_sync;
	}

	public void setIs_sync(boolean is_sync) {
		this.is_sync = is_sync;
	}

	public List<PatentStatus> getListPatentStatus() {
		return listPatentStatus;
	}

	public void setListPatentStatus(List<PatentStatus> listPatentStatus) {
		this.listPatentStatus = listPatentStatus;
	}
	
	public void addPatentStatus(PatentStatus patentStatus) {
		if(this.listPatentStatus == null) {
			this.listPatentStatus  = new ArrayList<PatentStatus>();
		}
		this.listPatentStatus.add(patentStatus);
	}
	
	public void addStatus(Status status) {
		this.addStatus(status,new Date());
	}
	
	public void addStatus(Status status,Date date) {
		if(this.listPatentStatus == null) {
			this.listPatentStatus  = new ArrayList<PatentStatus>();
		}
		PatentStatus patentStatus = new PatentStatus();
		patentStatus.setPatent(this);
		patentStatus.setStatus(status);
		patentStatus.setCreate_date(date);
		this.listPatentStatus.add(patentStatus);
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getAnnuity_date() {
		return annuity_date;
	}

	public void setAnnuity_date(String annuity_date) {
		this.annuity_date = annuity_date;
	}

	public Date getSync_date() {
		return sync_date;
	}

	public void setSync_date(Date sync_date) {
		this.sync_date = sync_date;
	}


}
