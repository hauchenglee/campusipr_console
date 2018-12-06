package biz.mercue.seed.model;



import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
@Table(name="message_template")
public class MessageTemplate extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String message_template_id;
	
	
	@JsonView(View.Public.class)
	private String message_social_type;
	
	
	@JsonView(View.Public.class)
	private String business_id;
	
	
	@JsonView(View.Public.class)
	private String message_type;
		
	//Line max length 2000
	//Facebook max length 640
	@JsonView(View.Public.class)
	private String message_text;
	
	//for template alt text
	@JsonView(View.Public.class)
	private String  alt_text;
	
	//for fb upload attachment,no db attachment
	@JsonView(View.Public.class)
	private String attachment_id;
	
	//image
	@JsonView(View.Public.class)
	private String attachment_url;
	
	//image
	@JsonView(View.Public.class)
	private String attachment_thumbnail_url;
	
	private boolean attachment_reusable;
	
	private int attachment_duration;
	
	@JsonView(View.Public.class)
	private String address;
	
	@JsonView(View.Public.class)
	private double latitude;
	
	@JsonView(View.Public.class)
	private double longitude;
	
		
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	
	@Transient
	private String push_title;
	

	public String getMessage_social_type() {
		return message_social_type;
	}
	
	public String getMessage_type() {
		return message_type;
	}
	
	public String getMessage_text() {
		return message_text;
	}



	
	public void setMessage_social_type(String message_social_type) {
		this.message_social_type = message_social_type;
	}
	
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	
	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}

//	public String getAdmin_id() {
//		return admin_id;
//	}
//
//	public void setAdmin_id(String admin_id) {
//		this.admin_id = admin_id;
//	}

	public String getAlt_text() {
		return alt_text;
	}

	public void setAlt_text(String alt_text) {
		this.alt_text = alt_text;
	}

	public String getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(String attachment_id) {
		this.attachment_id = attachment_id;
	}

	public String getAttachment_url() {
		return attachment_url;
	}

	public void setAttachment_url(String attachment_url) {
		this.attachment_url = attachment_url;
	}

	public boolean isAttachment_reusable() {
		return attachment_reusable;
	}

	public void setAttachment_reusable(boolean attachment_reusable) {
		this.attachment_reusable = attachment_reusable;
	}

	public String getAttachment_thumbnail_url() {
		return attachment_thumbnail_url;
	}

	public void setAttachment_thumbnail_url(String attachment_thumbnail_url) {
		this.attachment_thumbnail_url = attachment_thumbnail_url;
	}

	public int getAttachment_duration() {
		return attachment_duration;
	}

	public void setAttachment_duration(int attachment_duration) {
		this.attachment_duration = attachment_duration;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
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

	public String getMessage_template_id() {
		return message_template_id;
	}

	public void setMessage_template_id(String message_template_id) {
		this.message_template_id = message_template_id;
	}

	public String getPush_title() {
		return push_title;
	}

	public void setPush_title(String push_title) {
		this.push_title = push_title;
	}

}
