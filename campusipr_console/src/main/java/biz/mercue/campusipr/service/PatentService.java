package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentEditHistory;



public interface PatentService {

	int addPatent(Patent patent);
	
	int syncPatentStatus(Patent patent);
	
	int addPatentByApplNo(Patent patent);


	int updatePatent(Patent patent);
	
	int authorizedUpdatePatent(String businessId,Patent patent);

	int deletePatent(Patent patent);
	
	List<Patent> getAllByBussinessId(String businessId);

	ListQueryForm getByBusinessId(String businessId,int page);
	
	
	List<Patent> getByPatentIds(List<String> idList,String businessId);
	
	ListQueryForm searchPatent(String text,String businessId,int page);
	
	ListQueryForm fieldSearchPatent(String text,String businessId,int page);
	
	
	List<Patent> getByFamily(String family);
	
	
	//Patent getByApplNo(String applNo,String businessId);
		
	Patent getById(String businessId,String id);
	
	Patent getById(String id);
	

	int combinePatentFamily(List<String> ids,String businessId); 

	Patent getByPatentNo(String patentNo);
	
	List<PatentEditHistory> getHistoryBypatentId(String businessId,String patentId);


}
