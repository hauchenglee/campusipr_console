package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;


public interface PatentStatusDao {

	PatentStatus getById(String id);
	
	PatentStatus getByStatusAndPatent(String patentId, String StatusId, Date createTime);
	
	void create(PatentStatus ps);
}
