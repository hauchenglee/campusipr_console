package biz.mercue.campusipr.dao;

import java.util.List;

import biz.mercue.campusipr.model.Department;

public interface DepartmentDao {
	Department getById(String id);

	void delete(String patentId, String businessId);

	List<Department> getByPatentId(String patentId);
	
	void create(Department department);
	
	void delete(String id);
}
