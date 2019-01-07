package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Applicant;


public interface ApplicantDao {

	Applicant getById(String id);
	
	void create(Applicant appl);
	
}
