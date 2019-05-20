package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.model.Status;



public interface PatentService {

	int addPatent(Patent patent);
	
//	int syncPatentStatus(Patent patent);
	
	int syncPatentData(Patent patent, List<Patent> patentList); 
	
	int addPatentByApplNo(Patent patent, List<Patent> patentList, Admin admin, Business business);

	int syncPatentsByApplicant(List<Patent> list, String adminId, String businessId, String ip);

	int updatePatent(Patent patent, String businessId);
	
	int authorizedUpdatePatent(String businessId,Patent patent);

	int deletePatent(Patent patent);
	
	List<Patent> getExcelByPatentIds(List<String> idList,String businessId);

	ListQueryForm getByBusinessId(String businessId,int page,String orderFieldId,int is_asc);
	
	
	List<Patent> getByPatentIds(List<String> idList,String businessId);
	
	ListQueryForm fieldSearchPatent(Object searchObj, String fieldId, String businessId,int page,String orderFieldId,int is_asc);
	
	
	List<Patent> getByFamily(String family);
	
	
	//Patent getByApplNo(String applNo,String businessId);
		
	Patent getById(String businessId,String id);
	
	Patent getById(String id);
	

	int combinePatentFamily(PatentFamily family,String businessId); 
	
	int importPatent(List<Patent> list, Admin admin,Business business);

	Patent getByPatentNo(String patentNo);
	
	ListQueryForm getHistoryBypatentId(String businessId,String patentId,String fieldId,int page);
	
	
	List<Status> getEditStatus();

	void deleteById(String id);
}
