package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Assignee;


public interface AssigneeDao {

	Assignee getById(String id);
	
	void create(Assignee assignee);
}
