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
import javax.persistence.Transient;

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
	private String role_name_en;
	

	
    @JsonView(View.Permission.class)
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", 
	joinColumns = { @JoinColumn(name = "role_id", referencedColumnName="role_id") }, 
	inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    @OrderBy("permission_group_order,permission_order ASC")
	private List<Permission> permissionList;
    
    
    @Transient
    @JsonView(View.Role.class)
    private boolean canAdd = false;
    
    @Transient
    @JsonView(View.Role.class)
    private boolean canUpdate =false; 
    
    @Transient
    @JsonView(View.Role.class)
    private List<Admin> listAdmin;
    
    @Transient
    @JsonView(View.Role.class)
    private int total_count;
    
    @Transient
    @JsonView(View.Role.class)
    private int page_size;


    
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

	public boolean isCanAdd() {
		return canAdd;
	}

	public void setCanAdd(boolean canAdd) {
		this.canAdd = canAdd;
	}

	public boolean isCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	public List<Admin> getListAdmin() {
		return listAdmin;
	}

	public void setListAdmin(List<Admin> listAdmin) {
		this.listAdmin = listAdmin;
	}

	public String getRole_name_en() {
		return role_name_en;
	}

	public void setRole_name_en(String role_name_en) {
		this.role_name_en = role_name_en;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}


}
