package biz.mercue.campusipr.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;



@Entity
@Table(name="permission")
public class Permission extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String permission_id;
	
	@JsonView(View.Public.class)
	private String permission_model_name;
	
	@JsonView(View.Public.class)
	private String permission_name;
	
	@JsonView(View.Public.class)
	private int permission_group_order;

	@JsonView(View.Public.class)
	private int permission_order;
	
	@JsonView(View.Public.class)
	private String function_url;
	
	@JsonView(View.Public.class)
	private String function_icon;

	public int getPermission_group_order() {
		return permission_group_order;
	}

	public String getFunction_url() {
		return function_url;
	}

	public String getFunction_icon() {
		return function_icon;
	}
	
	public void setPermission_group_order(int permission_group_order) {
		this.permission_group_order = permission_group_order;
	}

	public void setFunction_url(String function_url) {
		this.function_url = function_url;
	}

	public void setFunction_icon(String function_icon) {
		this.function_icon = function_icon;
	}

	public String getPermission_id() {
		return permission_id;
	}

	public void setPermission_id(String permission_id) {
		this.permission_id = permission_id;
	}

	public String getPermission_name() {
		return permission_name;
	}

	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}



	public int getPermission_order() {
		return permission_order;
	}

	public void setPermission_order(int permission_order) {
		this.permission_order = permission_order;
	}

	public String getPermission_model_name() {
		return permission_model_name;
	}

	public void setPermission_model_name(String permission_model_name) {
		this.permission_model_name = permission_model_name;
	}

}
