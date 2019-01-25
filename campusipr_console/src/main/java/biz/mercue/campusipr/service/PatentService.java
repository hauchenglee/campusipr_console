package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.Status;



public interface PatentService {

	int addPatent(Patent patent);
	
	int syncPatentStatus(Patent patent);
	
	int addPatentByApplNo(Patent patent);

	int addPatentByApplicant(List<Patent> list, String businessName, String adminId, String businessId, String ip);

	int updatePatent(Patent patent);
	
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
	

	int combinePatentFamily(List<String> ids,String businessId); 

	Patent getByPatentNo(String patentNo);
	
	ListQueryForm getHistoryBypatentId(String businessId,String patentId,String fieldId,int page);
	
	
	List<Status> getEditStatus();


}
