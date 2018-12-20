package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="ipc_class")
public class IPCClass {
	
	@Id
	@JsonView(View.Public.class)
	private String ipc_class_id;
	
	
	@JsonView(View.Public.class)
	private String ipc_version;
	
	@JsonView(View.Public.class)
	private String ipc_desc_tw;
	
	@JsonView(View.Public.class)
	private String ipc_desc_cn;
	
	
	@JsonView(View.Public.class)
	private String ipc_desc_wipo;
	
	@JsonView(View.Public.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	
	@JsonView(View.Public.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;


	public String getIpc_class_id() {
		return ipc_class_id;
	}


	public void setIpc_class_id(String ipc_class_id) {
		this.ipc_class_id = ipc_class_id;
	}





	public String getIpc_version() {
		return ipc_version;
	}


	public void setIpc_version(String ipc_version) {
		this.ipc_version = ipc_version;
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


	public String getIpc_desc_tw() {
		return ipc_desc_tw;
	}


	public void setIpc_desc_tw(String ipc_desc_tw) {
		this.ipc_desc_tw = ipc_desc_tw;
	}


	public String getIpc_desc_cn() {
		return ipc_desc_cn;
	}


	public void setIpc_desc_cn(String ipc_desc_cn) {
		this.ipc_desc_cn = ipc_desc_cn;
	}


	public String getIpc_desc_wipo() {
		return ipc_desc_wipo;
	}


	public void setIpc_desc_wipo(String ipc_desc_wipo) {
		this.ipc_desc_wipo = ipc_desc_wipo;
	}
	
	
	
	

}
