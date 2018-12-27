package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Patent;



public interface PatentService {

	void addPatent(Patent patent);

	int updatePatent(Patent patent);

	void deletePatent(Patent patent);

	List<Patent> getByBusinessId(String businessId,int page,int pageSize);
	
	
	List<Patent> getByPatentIds(List<String> list);
	
//	List<Patent> getByAdminId(String adminId,int page,int pageSize);
	
	
	Patent getById(String businessId,String id);
	
	Patent getById(String id);
	
	Patent getByPatentNo(String patentNo);

}
