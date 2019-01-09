package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Applicant;


public interface ApplicantDao {

	Applicant getById(String id);
	
	List<Applicant> getByPatentId(String patentId);
	
	void create(Applicant appl);
	
}
