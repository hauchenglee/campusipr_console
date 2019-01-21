package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;


public interface PatentEditHistoryDao {

	PatentEditHistory getById(String id);
	
	List<PatentEditHistory> getByPatentAndField(String patentId,String fieldId, String businessId,int page,int pageSize);
	
	int countByPatentAndField(String patentId,String fieldId, String businessId);
}
