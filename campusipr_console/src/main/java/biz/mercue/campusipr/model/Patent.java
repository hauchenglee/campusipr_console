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

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="patent")
public class Patent extends BaseBean{
	
	
	@Id
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_id;
	
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_name;
	
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	private String patent_name_en;
	
	@JsonView(View.Patent.class)
	private String patent_appl_country;
	
	
	@JsonView(View.Patent.class)
	private Date patent_appl_date;
	
	@JsonView(View.Patent.class)
	private String patent_appl_no;
	
	@JsonView(View.Patent.class)
	private String patent_notice_no;
	
	@JsonView(View.Patent.class)
	private Date patent_notice_date;
	
	@JsonView(View.Patent.class)
	private String patent_publish_no;
	
	@JsonView(View.Patent.class)
	private Date patent_publish_date;
	
	@JsonView(View.Patent.class)
	private String patent_no;
	
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_bdate;
	
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_edate;
	
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_cancel_date;
	
	@JsonView(View.Patent.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date patent_charge_expire_date;
	
	@JsonView(View.Patent.class)
	private int patent_charge_duration_year;
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_business", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "business_id") })
	private List<Business> listBusiness;
	
	@JsonView(View.PatentDetail.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_ipc", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "ipc_class_id") })
	@OrderColumn(name="ipc_order")
	private List<IPCClass> listIPC;
	
	
	@JsonView(View.Patent.class)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_status", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "status_id") })
	private List<Status> listStatus;
	
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("agent_order")
	private List<Agent> listAgent;
	
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("assignee_order")
	private List<Assignee> listAssignee;
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("applicant_order")
	private List<Applicant> listApplicant;
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("inventor_order")
	private List<Inventor> listInventor;
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("contact_order")
	private List<PatentContact> listContact;
	
	
	@JsonView(View.PatentDetail.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("cost_date desc")
	private List<PatentCost> listCost;
	
	
	@JsonView(View.PatentDetail.class)
	@OneToOne(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PatentContext patentContext;
	
	
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
	@OneToOne(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PatentExtension patent_extension;
	
	
	@JsonView(View.PatentHistory.class)
	@OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("create_date DESC")
	private List<PatentEditHistory> listHistory;
	
	@ManyToOne
	@JsonView(View.Patent.class)
	@JoinColumn(name="patent_family_id")
	private PatentFamily family;
	
	//for update and add patent
	@Transient
	@JsonView(View.PatentDetail.class)
	private Business business;
	
	@Transient
	@JsonView(View.PatentDetail.class)
	private Admin admin;
	
	@Transient
	private String admin_ip;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "patent_portfolio", 
		joinColumns = { @JoinColumn(name = "patent_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "portfolio_id") })
	private List<Portfolio> listPortfolio;
	
	
	@Transient
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="patent_id", referencedColumnName="patent_id")
	private List<PatentStatus> patentStatusList;
	
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
		this.patent_appl_country = patent_appl_country;
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
		this.patent_appl_no = patent_appl_no;
	}

	public String getPatent_notice_no() {
		return patent_notice_no;
	}

	public void setPatent_notice_no(String patent_notice_no) {
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

	public PatentContext getPatentContext() {
		return patentContext;
	}

	public void setPatentContext(PatentContext patentContext) {
		this.patentContext = patentContext;
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

	public PatentExtension getPatent_extension() {
		return patent_extension;
	}

	public void setPatent_extension(PatentExtension patent_extension) {
		this.patent_extension = patent_extension;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}


	public List<Status> getListStatus() {
		return listStatus;
	}

	public void setListStatus(List<Status> listStatus) {
		this.listStatus = listStatus;
	}


	public Admin getAdmin() {
		return admin;
	}


	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<PatentStatus> getPatentStatusList() {
		return patentStatusList;
	}

	public void setPatentStatusList(List<PatentStatus> patentStatusList) {
		this.patentStatusList = patentStatusList;
	}

	public String getAdmin_ip() {
		return admin_ip;
	}

	public void setAdmin_ip(String admin_ip) {
		this.admin_ip = admin_ip;
	}


}
