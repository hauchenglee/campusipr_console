package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Patent;

public interface PatentDao {

	Patent getById(String id);
	
	Patent getById(String businessId,String id);

	void create(Patent patent);
	
	void delete(String id);
	
	
	List<Patent> getByBusinessId(String businessId,int page,int pageSize);
	
//	List<Patent> getByAdminId(String adminId,int page,int pageSize);
	
	
	List<Patent> getByPatentIds(List<String> ids);
	
	
	List<Patent> fieldSearchPatent(Patent patent);
	
	Patent getByPatentNo(String patentNo);
}
