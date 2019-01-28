package biz.mercue.campusipr.dao;


import java.util.Date;
import java.util.List;

import biz.mercue.campusipr.model.Patent;

public interface PatentDao {

	Patent getById(String id);
	
	Patent getById(String businessId,String id);
	
	Patent getByApplNo(String applNo);

	void create(Patent patent);
	
	void delete(String id);
	
	
	List<Patent> getByBusinessId(String businessId,int page,int pageSize,String orderFieldId,int is_asc);
	int  getCountByBusinessId(String businessId);	
	
	List<Patent> getAllByBusinessId(String businessId);
	
	
	

	
	List<Patent> getByPatentIds(List<String> ids,String businessId);
	
	List<Patent> getByFamily(String familyId);
	
	
	List<Patent> searchAllFieldPatent(String  searchText,String businessId,int page,int pageSize,String orderFieldCode,int is_asc);
	int  countSearchAllFieldPatent(String searchText,String businessId);
	
	List<Patent> searchFieldCountryPatent(List<String> coutryIdList, String fieldCode, String businessId, int page, int pageSize,String orderFieldId,int is_asc);
	int countSearchFieldCountryPatent(List<String> coutryIdList, String fieldCode,String businessId);
	
	List<Patent> searchFieldPatent(String  searchText, String fieldCode, String businessId,int page,int pageSize,String orderFieldCode,int is_asc);
	int  countSearchFieldPatent(String searchText, String fieldCode,String businessId);
	
	List<Patent> searchFieldPatent(Date startDate, Date endDate, String fieldCode, String businessId,int page,int pageSize,String orderFieldCode,int is_asc);
	int  countSearchFieldPatent(Date startDate, Date endDate, String fieldCode,String businessId);
	
	List<Patent> searchFieldHumanListPatent(String searchText, String fieldCode,String businessId, int page, int pageSize,String orderFieldCode,int is_asc);
	int  countSearchFieldHumanListPatent(String searchText, String fieldCode, String businessId);
	
//	List<Patent> searchFieldApplicantListPatent(String searchText,String businessId, int page, int pageSize,String orderFieldId,int is_asc);
//	int  countSearchFieldApplicantPatent(String searchText, String businessId);
//	
//	List<Patent> searchFieldInventorListPatent(String searchText,String businessId, int page, int pageSize,String orderFieldId,int is_asc);
//	int  countSearchFieldInventorPatent(String searchText, String businessId);
	
	List<Patent> searchFieldStatusListPatent(String searchText,String businessId, int page, int pageSize,String orderFieldCode,int is_asc);
	int  countSearchFieldStatusPatent(String searchText, String businessId);
	
//	List<Patent> searchFieldCostListPatent(String searchText,String businessId, int page, int pageSize,String orderFieldId,int is_asc);
//	int  countSearchFieldCostPatent(String searchText, String businessId);
//	
//	List<Patent> searchFieldFamilyListPatent(String searchText,String businessId, int page, int pageSize,String orderFieldId,int is_asc);
//	int  countSearchFieldFamilyPatent(String searchText, String businessId);
	
	List<Patent> searchFieldExtensionListPatent(String searchText, String fieldCode, String businessId, int page, int pageSize,String orderFieldCode,int is_asc);
	int  countSearchFieldExtensionPatent(String searchText, String fieldCode, String businessId);
	
	Patent getByPatentNo(String patentNo);
	
	
	void deletePatentCost(String patentId);
	void deleteInventor(String patentId);
	void deleteAssignee(String patentId);
	void deleteApplicant(String patentId);

}