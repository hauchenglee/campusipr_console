package biz.mercue.campusipr.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;





@Entity
@Table(name="role")
public class Role  extends BaseBean{
	
	
	@Id
	@JsonView(View.Public.class)
	private String role_id;

	@JsonView(View.Public.class)
	private String role_name;
	

	
    @JsonView(View.Public.class)
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", 
	joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="role_id") }, 
	inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    @OrderBy("permission_group_order,permission_order ASC")
	private List<Permission> permissionList;


    
	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}


}
