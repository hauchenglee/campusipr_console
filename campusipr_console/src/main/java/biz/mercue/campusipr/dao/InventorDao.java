package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;


public interface InventorDao {

	Inventor getById(String id);
	
	List<Inventor> getByPatentId(String patentId);
	
	void create(Inventor inventor);
	
	void delete(String id);
	
}
