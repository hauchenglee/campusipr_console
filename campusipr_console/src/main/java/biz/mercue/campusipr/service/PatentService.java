package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;



public interface PatentService {

	int addPatent(Patent patent);

	int updatePatent(Patent patent);

	int deletePatent(Patent patent);

	ListQueryForm getByBusinessId(String businessId,int page);
	
	
	List<Patent> getByPatentIds(List<String> idList,String businessId);
	
	ListQueryForm searchPatent(String text,String businessId,int page);
	
	ListQueryForm fieldSearchPatent(String text,String businessId,int page);
		
	Patent getById(String businessId,String id);
	
	Patent getById(String id);

}
