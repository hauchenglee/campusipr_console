package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Status;


public interface StatusDao {

	Status getById(String id);
	
	Status getByEventCode(String eventCode);
	
	void create(Status status);
	
}
