package biz.mercue.campusipr.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.campusipr.util.Constants;

@Entity
@Table(name="banner")
public class Banner extends BaseBean{
	
	@Id
	@JsonView(View.Banner.class)
	private String banner_id;
	
	@JsonView(View.Banner.class)
	private String banner_title;
	
	@JsonView(View.Banner.class)
	private String banner_content;
	
	@JsonView(View.Banner.class)
	private String banner_image_file;
	
	@JsonView(View.Banner.class)
	@Transient
	private String f_banner_image_url;
	
	
	@JsonView(View.Banner.class)
	private int banner_order;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date update_date;
	
	@JsonView(View.Banner.class)
	private boolean available;

	public String getBanner_id() {
		return banner_id;
	}

	public void setBanner_id(String banner_id) {
		this.banner_id = banner_id;
	}

	public String getBanner_title() {
		return banner_title;
	}

	public void setBanner_title(String banner_title) {
		this.banner_title = banner_title;
	}

	public String getBanner_content() {
		return banner_content;
	}

	public void setBanner_content(String banner_content) {
		this.banner_content = banner_content;
	}

	public String getBanner_image_file() {
		return this.banner_image_file;
	}

	public void setBanner_image_file(String banner_image_file) {
		this.banner_image_file = banner_image_file;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getBanner_order() {
		return banner_order;
	}

	public void setBanner_order(int banner_order) {
		this.banner_order = banner_order;
	}

	public String getF_banner_image_url() {
		return Constants.IMAGE_LOAD_URL + this.banner_image_file;
	}


}
