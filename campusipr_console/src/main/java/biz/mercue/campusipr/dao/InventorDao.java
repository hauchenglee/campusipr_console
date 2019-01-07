package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;


public interface InventorDao {

	Inventor getById(String id);
	
	void create(Inventor inventor);
	
}
