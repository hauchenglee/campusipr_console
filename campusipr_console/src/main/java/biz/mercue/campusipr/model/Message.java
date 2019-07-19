package biz.mercue.campusipr.model;

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
@Table(name="message")
public class Message extends BaseBean{

	@Id
	@JsonView(View.Message.class)
	private String message_id;

	@JsonView(View.Message.class)
	private String message_type;


	@JsonView(View.Message.class)
	private String business_id;


	@JsonView(View.Message.class)
	private String channel_id;

	@JsonView(View.Message.class)
	private String sender_id;


	@Transient
	@JsonView(View.Message.class)
	private String sender_name;


	@JsonView(View.Message.class)
	private String receiver_id;

	@Transient
	@JsonView(View.Message.class)
	private String receiver_name;



	//Line max length 2000
	//Facebook max length 640
	@JsonView(View.Message.class)
	private String message_text;


	@JsonView(View.Message.class)
	private String attachment_url;

	@JsonView(View.Message.class)
	private String attachment_thumbnail_url;

	@JsonView(View.Message.class)
	private Long message_date;

	@JsonView(View.Message.class)
	private boolean is_read;

	public String getMessage_id() {
		return message_id;
	}

	public String getMessage_type() {
		return message_type;
	}

	public String getMessage_text() {
		return message_text;
	}

	public Long getMessage_date() {
		return message_date;
	}

	public void setMessage_date(Long message_date) {
		this.message_date = message_date;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}

	public String getAttachment_url() {
		return attachment_url;
	}

	public void setAttachment_url(String attachment_url) {
		this.attachment_url = attachment_url;
	}

	public String getAttachment_thumbnail_url() {
		return attachment_thumbnail_url;
	}

	public void setAttachment_thumbnail_url(String attachment_thumbnail_url) {
		this.attachment_thumbnail_url = attachment_thumbnail_url;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

	public String getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public boolean isIs_read() {
		return is_read;
	}

	public void setIs_read(boolean is_read) {
		this.is_read = is_read;
	}
}
