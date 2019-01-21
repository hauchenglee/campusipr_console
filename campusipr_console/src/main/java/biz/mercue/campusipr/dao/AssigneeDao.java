package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Assignee;


public interface AssigneeDao {

	Assignee getById(String id);
	
	List<Assignee> getByPatentId(String patentId);
	
	void create(Assignee assignee);
	
	void delete(String id);
}
