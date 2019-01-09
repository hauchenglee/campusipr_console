package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;


public interface PatentStatusDao {

	PatentStatus getById(String id);
	
}
