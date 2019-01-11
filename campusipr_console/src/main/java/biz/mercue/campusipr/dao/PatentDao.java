package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Patent;

public interface PatentDao {

	Patent getById(String id);
	
	Patent getById(String businessId,String id);
	
	Patent getByApplNo(String applNo);

	void create(Patent patent);
	
	void delete(String id);
	
	
	List<Patent> getByBusinessId(String businessId,int page,int pageSize);
	
	List<Patent> getAllByBusinessId(String businessId);
	
	
	
	int  getCountByBusinessId(String businessId);	
	
	List<Patent> getByPatentIds(List<String> ids,String businessId);
	
	List<Patent> getByFamily(String familyId);
	
	
	List<Patent> searchPatent(String  searchText,String businessId,int page,int pageSize);
	
	int  searchCountPatent(String searchText,String businessId);
	
	List<Patent> fieldSearchPatent(Patent patent,String businessId,int page,int pageSize);
	
	
	
	Patent getByPatentNo(String patentNo);
}
