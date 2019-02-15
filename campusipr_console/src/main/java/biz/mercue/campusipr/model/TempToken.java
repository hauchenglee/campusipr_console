package biz.mercue.campusipr.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="temp_token")
public class TempToken extends BaseBean {
	
	@Id
	private String token_id;
	
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="admin_id")
	private Admin admin;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date expire_date;
	
	
	private boolean available = false;


	public String getToken_id() {
		return token_id;
	}


	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}


	public Admin getAdmin() {
		return admin;
	}


	public void setAdmin(Admin admin) {
		this.admin = admin;
		this.admin.setToken(this.getToken_id());
	}


	public Date getCreate_date() {
		return create_date;
	}


	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}


	public Date getExpire_date() {
		return expire_date;
	}


	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
	}


	public boolean isAvailable() {
		return available;
	}


	public void setAvailable(boolean available) {
		this.available = available;
	}

}
