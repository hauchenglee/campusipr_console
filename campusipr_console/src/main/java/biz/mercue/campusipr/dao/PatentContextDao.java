package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.PatentContext;


public interface PatentContextDao {

	PatentContext getById(String id);
	
	void create(PatentContext pc);
	
}
