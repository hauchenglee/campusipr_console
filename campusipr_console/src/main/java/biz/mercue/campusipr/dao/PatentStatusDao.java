package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;


public interface PatentStatusDao {

	PatentStatus getById(String id);
	
	PatentStatus getByStatusAndPatent(String patentId, String StatusId, Date createTime);
	
	List<PatentStatus> getByPatent(String patentId);

	List<String> getStatusIds(String patentId);

    String checkStatusIdExist(String patentId, String statusId);

    void create(PatentStatus ps);

    void updateStatusPatent(List<String> targetId, String updateId);


    //void  deletePatentStatus(String patentId);
}
