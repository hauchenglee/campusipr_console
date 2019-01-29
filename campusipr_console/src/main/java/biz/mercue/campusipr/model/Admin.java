package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name="admin")
public class Admin extends BaseBean {
	
	
	@Id
	@JsonView({View.Admin.class,View.Role.class})
	private String admin_id;
	
	@JsonView({View.Admin.class,View.Role.class})
	private String admin_name;
	
	@JsonView({View.Admin.class,View.Role.class})
	private String admin_email;
	
	private String admin_password;
	
	@Transient
	private String re_admin_password;
	
	@JsonView({View.Admin.class,View.Role.class})
	private String admin_unit_name;
	
	@JsonView({View.Admin.class,View.Role.class})
	private boolean available;
	
	
	@JsonView({View.Admin.class,View.Role.class})
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name="business_id")
	private Business business;
	
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="role_id", referencedColumnName="role_id")
	@JsonView(View.Admin.class)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@NotFound(action=NotFoundAction.IGNORE)
	private Role role;
	
	@Transient
	@JsonView(View.Public.class)
	private String role_name;
	
	private Date create_date;
	
	private Date update_date;
	
	
	@Transient
	private String admin_ip;

	public String getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getAdmin_email() {
		return admin_email;
	}

	public void setAdmin_email(String admin_email) {
		this.admin_email = admin_email;
	}

	public String getAdmin_password() {
		return admin_password;
	}

	public void setAdmin_password(String admin_password) {
		this.admin_password = admin_password;
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

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getAdmin_unit_name() {
		return admin_unit_name;
	}

	public void setAdmin_unit_name(String admin_unit_name) {
		this.admin_unit_name = admin_unit_name;
	}

	public String getRe_admin_password() {
		return re_admin_password;
	}

	public void setRe_admin_password(String re_admin_password) {
		this.re_admin_password = re_admin_password;
	}

	public String getAdmin_ip() {
		return admin_ip;
	}

	public void setAdmin_ip(String admin_ip) {
		this.admin_ip = admin_ip;
	}
	

}
