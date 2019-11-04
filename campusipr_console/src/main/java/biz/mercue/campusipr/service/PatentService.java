package biz.mercue.campusipr.service;

import biz.mercue.campusipr.model.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface PatentService {

    int demo(String applNo, String businessId, String patentId, String str);

    int addPatent(Patent patent) throws Exception;

//	int syncPatentStatus(Patent patent);

    int syncPatentsByApplicant(List<Patent> list, String adminId, String businessId, String ip) throws Exception;

    int syncPatentData(Patent patent);

    int addPatentByApplNo(Patent patent, Admin admin, Business business, int sourceFrom) throws Exception;

    Map<String, Patent> addPatentByExcel(List<Patent> patentList, Admin admin, Business business, String ip) throws Exception;

    Map<String, Patent> syncPatentDataBySchedule(Patent patent);

    JSONObject checkNoPublicApplNo(Patent editPatent, Business business);

    int addPatentByNoPublicApplNo(Patent editPatent, Business business, Admin admin);

    int mergeDiffPatent(String dbPatentId, Patent editPatent, Admin admin, Business business);

    int updatePatent(Patent patent, String businessId) throws Exception;

    ListQueryForm advancedSearch(String searchStr, String business, int page, int pageSize);

    void patentHistoryFirstAdd(Patent patent, String patentId, String businessId);

    void mergeDiffPatentByExcel(Map<String, Patent> mergeMap, Admin admin, Business business);

    int authorizedUpdatePatent(String businessId, Patent patent) throws Exception;

    List<Patent> getExcelByPatentIds(List<String> idList, String businessId);

    List<Patent> getPatentList();

    ListQueryForm getByBusinessId(String businessId, int page, String orderFieldId, int is_asc);

    List<Patent> getByPatentIds(List<String> idList, String businessId);

    ListQueryForm fieldSearchPatent(Object searchObj, String fieldId, String businessId, int page, String orderFieldId,
                                    int is_asc);

    List<Patent> getByFamily(String family);

    // Patent getByApplNo(String applNo,String businessId);

    Patent getById(String businessId, String id);

    Patent getById(String id);

    int combinePatentFamily(PatentFamily family, String businessId, String patentId, Admin tokenAdmin, String ip);

    Patent getByPatentNo(String patentNo);

    ListQueryForm getHistoryBypatentId(String businessId, String patentId, String fieldId, int page);

    List<Status> getEditStatus();

    void deleteByIds(List<String> patentIds, String business) throws Exception;

    void deleteById(String id, String businessId) throws Exception;

    void deletePatentByScheduled(String deletePatentId, String businessId);

    void syncPatentStatus(Patent patent);

}
