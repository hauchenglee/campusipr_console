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
@Table(name="patent_contact")
public class PatentContact extends BaseBean{
	
	@Id
	@JsonView(View.PatentDetail.class)
	private String patent_contact_id;
	
	@ManyToOne
	@JoinColumn(name="patent_id")
	private Patent patent;
	
	
	@ManyToOne
	@JsonView(View.PatentDetail.class)
	@JoinColumn(name="admin_id")
	private Admin admin;
	
	@JsonView(View.PatentDetail.class)
	private String  contact_name;
	
	@JsonView(View.PatentDetail.class)
	private String  contact_email;
	
	@JsonView(View.PatentDetail.class)
	private String  contact_phone;
	
	@JsonView(View.PatentDetail.class)
	private int  contact_order;
	
	@JsonView(View.PatentDetail.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;

	public String getPatent_contact_id() {
		return patent_contact_id;
	}

	public void setPatent_contact_id(String patent_contact_id) {
		this.patent_contact_id = patent_contact_id;
	}

	public Patent getPatent() {
		return patent;
	}

	public void setPatent(Patent patent) {
		this.patent = patent;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public int getContact_order() {
		return contact_order;
	}

	public void setContact_order(int contact_order) {
		this.contact_order = contact_order;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	
	

}
