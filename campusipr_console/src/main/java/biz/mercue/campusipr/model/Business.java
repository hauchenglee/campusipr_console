package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="business")
public class Business extends BaseBean{
	
	@Id
	@JsonView(View.Business.class)
	private String business_id;
	
	@JsonView(View.Business.class)
	private String business_name;
	
	@JsonView(View.Business.class)
	private String business_name_en;
	
	@JsonView(View.BusinessDetail.class)
	private String business_alias;
	
	@JsonView(View.BusinessDetail.class)
	private String business_alias_en;
	
	@JsonView(View.BusinessDetail.class)
	private String contact_name;
	
	@JsonView(View.BusinessDetail.class)
	private String contact_email;
	
	@JsonView(View.BusinessDetail.class)
	private String contact_tel;
	
	
	@JsonView(View.BusinessDetail.class)
	private String contact_tel_extension;
	
	@JsonView(View.BusinessDetail.class)
	private String contact_phone;
	
	@JsonView(View.Business.class)
	private boolean available;
		
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getBusiness_name_en() {
		return business_name_en;
	}

	public void setBusiness_name_en(String business_name_en) {
		this.business_name_en = business_name_en;
	}

	public String getBusiness_alias() {
		return business_alias;
	}

	public void setBusiness_alias(String business_alias) {
		this.business_alias = business_alias;
	}

	public String getBusiness_alias_en() {
		return business_alias_en;
	}

	public void setBusiness_alias_en(String business_alias_en) {
		this.business_alias_en = business_alias_en;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
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

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_email() {
		return contact_email;
	}

	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public String getContact_tel_extension() {
		return contact_tel_extension;
	}

	public void setContact_tel_extension(String contact_tel_extension) {
		this.contact_tel_extension = contact_tel_extension;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

}
