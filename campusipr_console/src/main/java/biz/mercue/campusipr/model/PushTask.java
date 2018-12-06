package biz.mercue.campusipr.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@SuppressWarnings("serial")
@Entity
@Table(name="push_task")
public class PushTask extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String push_id;
	
	@JsonView(View.Public.class)
	private String push_type;
	
	@JsonView(View.Public.class)
	private boolean is_send;
	
	
	@JsonView(View.Public.class)
	private boolean is_instant;
	
	@JsonView(View.Public.class)
	private boolean available;
	
	@JsonView(View.Public.class)
	private String content_ids;
	
	private String business_id;
	
	@JsonView(View.Public.class)
	private String push_extension_name;
	

	@JsonView(View.Public.class)
	private String account_id;
	
	@JsonView(View.Public.class)
	private String target_gender;
	
	@JsonView(View.Public.class)
	private Integer highest_age;
	
	@JsonView(View.Public.class)
	private Integer lowest_age;
	
	@JsonView(View.Public.class)
	private String push_location;
	
	@Transient
	@JsonView(View.Public.class)
	private String push_title;
	
	@Transient
	@JsonView(View.Public.class)
	private String push_media_url;
	
	@JsonView(View.Public.class)
	@Transient
	private List<String> contentList;
	
	@JsonView(View.Public.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date push_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	
	

	
	@JsonView(View.Public.class)
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="message_template_id", referencedColumnName="message_template_id")
	private MessageTemplate messageTemplate;
	

	
	
	@Transient
	private String fcm_token;
	
	public String getPush_id() {
		return push_id;
	}
	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}
	

	
	public String getPush_type() {
		return push_type;
	}
	public void setPush_type(String push_type) {
		this.push_type = push_type;
	}




	
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	
	public boolean getIs_send() {
		return is_send;
	}

	public void setIs_send(boolean is_send) {
		this.is_send = is_send;
	}
	
	public boolean getAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public Date getPush_date() {
		return push_date;
	}
	public void setPush_date(Date push_date) {
		this.push_date = push_date;
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

	public String getFcm_token() {
		return fcm_token;
	}
	public void setFcm_token(String fcm_token) {
		this.fcm_token = fcm_token;
	}

	public MessageTemplate getMessageTemplate() {
		return messageTemplate;
	}
	public void setMessageTemplate(MessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
	public boolean is_instant() {
		return is_instant;
	}
	public void setIs_instant(boolean is_instant) {
		this.is_instant = is_instant;
	}
	public String getContent_ids() {
		return content_ids;
	}
	public void setContent_ids(String content_ids) {
		this.content_ids = content_ids;
	}

	public List<String> getContentList() {
		return contentList;
	}
	
	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getPush_extension_name() {
		return push_extension_name;
	}
	public void setPush_extension_name(String push_extension_name) {
		this.push_extension_name = push_extension_name;
	}

	public String getTarget_gender() {
		return target_gender;
	}
	public void setTarget_gender(String target_gender) {
		this.target_gender = target_gender;
	}
	
	public Integer getHighest_age() {
		return highest_age;
	}
	public void setHighest_age(Integer highest_age) {
		this.highest_age = highest_age;
	}
	
	public Integer getLowest_age() {
		return lowest_age;
	}
	public void setLowest_age(Integer lowest_age) {
		this.lowest_age = lowest_age;
	}
	
	public String getPush_location() {
		return push_location;
	}
	public void setPush_location(String push_location) {
		this.push_location = push_location;
	}
	
	public String getPush_title() {
		return push_title;
	}
	public void setPush_title(String push_title) {
		this.push_title = push_title;
	}
	
	public String getPush_media_url() {
		return push_media_url;
	}
	public void setPush_media_url(String push_media_url) {
		this.push_media_url = push_media_url;
	}
}
