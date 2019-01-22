package biz.mercue.campusipr.service;



import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.ApplicantDao;
import biz.mercue.campusipr.dao.AssigneeDao;
import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.dao.IPCClassDao;
import biz.mercue.campusipr.dao.InventorDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.PatentEditHistoryDao;
import biz.mercue.campusipr.dao.PatentFamilyDao;
import biz.mercue.campusipr.dao.PatentStatusDao;
import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentAbstract;
import biz.mercue.campusipr.model.PatentClaim;
import biz.mercue.campusipr.model.PatentCost;
import biz.mercue.campusipr.model.PatentDescription;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.ServiceChinaPatent;
import biz.mercue.campusipr.util.ServiceStatusPatent;
import biz.mercue.campusipr.util.ServiceTaiwanPatent;
import biz.mercue.campusipr.util.ServiceUSPatent;
import biz.mercue.campusipr.util.StringUtils;




@Service("patentService")
@Transactional
public class PatentServiceImpl implements PatentService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PatentDao patentDao;
	
	@Autowired
	private StatusDao statusDao;
	
//	@Autowired
//	private InventorDao inventorDao;
//	
//	@Autowired
//	private ApplicantDao applicantDao;
//	
//	@Autowired
//	private AssigneeDao assigneeDao;
	
	@Autowired
	private PatentStatusDao patentStatusDao;
	
	@Autowired
	private PatentEditHistoryDao pehDao;
	
	@Autowired
	private PatentFamilyDao familyDao;
	
	@Autowired
	private FieldDao fieldDao;

	@Override
	public Patent getById(String businessId,String id) {
		log.info("get by id: " + id);
		log.info("businessId: " + businessId);
		Patent patent = patentDao.getById(businessId, id);
		if(patent!= null) {
			PatentFamily family = patent.getFamily();
			if(family!=null) {
				log.info("family id :"+family.getPatent_family_id());
				log.info(patent.getFamily().getListPatent().size());
			}else {
				log.info("family is null");
			}
			PatentAbstract patentAbstrat = patent.getPatentAbstract();
			if(patentAbstrat!=null) {
				log.info("abstract id :"+patentAbstrat.getPatent_abstract_id());
			}else {
				log.info("abstract is null");
			}
			
			PatentClaim patentClaim = patent.getPatentClaim();
			if(patentClaim!=null) {
				log.info("claim id :"+patentClaim.getPatent_claim_id());
			}else {
				log.info("claim is null");
			}
			
			PatentDescription patentDesc = patent.getPatentDesc();
			if(patentDesc!=null) {
				log.info("desc id :"+patentDesc.getPatent_desc_id());
			}else {
				log.info("desc is null");
			}
			
			patent.getListExtension().size();
			
			

			patent.getListBusiness().size();
			patent.getListStatus().size();
			patent.getListIPC().size();
			patent.getListAgent().size();
			patent.getListApplicant().size();
			patent.getListAssignee().size();
			patent.getListContact().size();
			patent.getListCost().size();
			patent.getListInventor().size();
			patent.getListPortfolio().size();
			patent.getListAnnuity().size();
			
			List<PatentStatus> listPatentStatus = patentStatusDao.getByPatent(patent.getPatent_id());
			
			for (PatentStatus patentStatus:listPatentStatus) {
				for (Status status:patent.getListStatus()) {
					if (status.getStatus_id().equals(patentStatus.getStatus_id())) {
						status.setPatentStatus(patentStatus);
					}
				}
			}
			
		}
		return patent;
	}
	
	@Override
	public ListQueryForm getHistoryBypatentId(String businessId,String patentId,String fieldId,int page) {
		List<PatentEditHistory> listpeh = pehDao.getByPatentAndField(patentId, fieldId, businessId,page,Constants.SYSTEM_PAGE_SIZE);
		
		int count = pehDao.countByPatentAndField(patentId, fieldId, businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,listpeh);
		
		return form;
	}
	
	
	@Override
	public Patent getById(String id) {
		log.info("get by id: " + id);
		Patent patent = patentDao.getById(id);

		return patent;
	}

	@Override
	public int addPatent(Patent patent) {
		
		if(StringUtils.isNULL(patent.getPatent_id())) {
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
		}
		
		if(patent.getBusiness() == null) {
			log.error("no business data");
			return Constants.INT_DATA_ERROR;
		}
		
		if(StringUtils.isNULL(patent.getPatent_appl_country())) {
			log.error("no applicant country");
			return Constants.INT_DATA_ERROR;
		}
		
		String applNo =  patent.getPatent_appl_no();
		
		if(!StringUtils.isNULL(applNo)) {
			Patent appNoPatent = patentDao.getByApplNo(applNo);
			if(appNoPatent!=null) {
				List<Business> listBusiness = appNoPatent.getListBusiness();
				for(Business business : listBusiness) {
					if(patent.getBusiness().getBusiness_id().equals(business.getBusiness_id())) {
						return Constants.INT_DATA_DUPLICATE;
					}
				}
			}
		}
		patent.addBusiness(patent.getBusiness());
		
		List<Applicant> listApplicant =patent.getListApplicant();
		if(listApplicant  !=null  && listApplicant.size() >0) {
			for(Applicant mApplicant : listApplicant) {
				mApplicant.setApplicant_id(KeyGeneratorUtils.generateRandomString());
			}
		}
		
		List<Inventor> listInventor =patent.getListInventor();
		if(listInventor  !=null  && listInventor.size() >0) {
			for(Inventor mInventor : listInventor) {
				mInventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
			}
		}
		
		//create history
		Date now = new Date();
		PatentEditHistory peh = new PatentEditHistory();
		peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
		peh.setField_id(Constants.PATENT_ALL_FIELD);
		peh.setPatent(patent);
		peh.setAdmin(patent.getAdmin());
		peh.setHistory_data("create");
		peh.setHistory_status("create");
		peh.setAdmin_ip(patent.getAdmin_ip());
		peh.setCreate_date(now);
		
		if (peh != null) {
			
			if (patent.getListHistory() != null) {
				if (StringUtils.isNULL(peh.getHistory_data()) == false) {
					patent.getListHistory().add(peh);
				}
			} else {
				if (StringUtils.isNULL(peh.getHistory_data()) == false) {
					
					List<PatentEditHistory> pehList = new ArrayList<PatentEditHistory>();
					pehList.add(peh);
					patent.setListHistory(pehList);
				}
			}
		}
 		

		patentDao.create(patent);
		return Constants.INT_SUCCESS;
	}
	
	@Override
	public int syncPatentStatus(Patent patent) {
		int taskResult= -1;
		ServiceStatusPatent.getPatentStatus(patent);
		
		if (patent.getListStatus() != null) {
			for (Status status:patent.getListStatus()) {
				Status statusDb = statusDao.getByEventCode(status.getEvent_code(), status.getCountry_id());
				if (statusDb != null) {
					status.setStatus_id(statusDb.getStatus_id());
					status.setStatus_desc(statusDb.getStatus_desc());
					status.setStatus_desc_en(statusDb.getStatus_desc_en());
					status.setStatus_color(statusDb.getStatus_color());
					status.setEvent_class(statusDb.getEvent_class());
					status.getPatentStatus().setStatus_id(status.getStatus_id());
					PatentStatus dBean = patentStatusDao.getByStatusAndPatent(status.getPatentStatus().getPatent_id(), status.getPatentStatus().getStatus_id(), status.getPatentStatus().getCreate_date());
					
					if (dBean == null) {
						patentStatusDao.create(status.getPatentStatus());
					}
				} else {
					
					if(StringUtils.isNULL(status.getStatus_id())) {
						status.setStatus_id(KeyGeneratorUtils.generateRandomString());
					}
					statusDao.create(status);
					status.getPatentStatus().setStatus_id(status.getStatus_id());
					PatentStatus dBean = patentStatusDao.getByStatusAndPatent(status.getPatentStatus().getPatent_id(), status.getPatentStatus().getStatus_id(), status.getPatentStatus().getCreate_date());
					if (dBean == null) {
						patentStatusDao.create(status.getPatentStatus());
					}
				}
			}
		}else {
			taskResult = Constants.INT_CANNOT_FIND_DATA;
		}
		
		return taskResult;
	}
	
	
	@Override
	public int addPatentByApplNo(Patent patent) {
		int taskResult= -1;
		
		if (patent.getPatent_appl_no().length() == 10 && 
				Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
			patent.setPatent_appl_no(patent.getPatent_appl_no().substring(2));
		}
		
		if (patent.getPatent_appl_no().length() == 8 && 
				Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
			patent.setPatent_appl_no("0"+patent.getPatent_appl_no());
		}
		
		if (patent.getPatent_appl_no().length() == 11 && 
				Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
			patent.setPatent_appl_no(patent.getPatent_appl_no().substring(2));
		}
		
		//查詢台灣專利
		if (patent.getPatent_appl_no().length() == 9 && 
				Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
			ServiceTaiwanPatent.getPatentRightByApplNo(patent);
		}else if (patent.getPatent_appl_no().length() == 10 && 
				Constants.APPL_COUNTRY_US.endsWith(patent.getPatent_appl_country())) {
			ServiceUSPatent.getPatentRightByapplNo(patent);
		}else {
			ServiceChinaPatent.getPatentRightByApplicantNo(patent);
		}
		
		if(!StringUtils.isNULL(patent.getPatent_name())
				|| !StringUtils.isNULL(patent.getPatent_name_en())) {
			String applNo =  patent.getPatent_appl_no();
			if (StringUtils.isNULL(applNo) == false) {
				Patent appNoPatent = patentDao.getByApplNo(applNo);
				if(appNoPatent==null) {
					
					if(StringUtils.isNULL(patent.getPatent_id())) {
						patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
					}
					if (patent.getPatentAbstract() != null) {
						patent.getPatentAbstract().setPatent_abstract_id(KeyGeneratorUtils.generateRandomString());
						patent.getPatentAbstract().setPatent(patent);
					}
					if (patent.getPatentClaim() != null) {
						patent.getPatentClaim().setPatent_claim_id(KeyGeneratorUtils.generateRandomString());
						patent.getPatentClaim().setPatent(patent);
					}
					if (patent.getPatentDesc() != null) {
						patent.getPatentDesc().setPatent_desc_id(KeyGeneratorUtils.generateRandomString());
						patent.getPatentDesc().setPatent(patent);
					}
					if (patent.getListApplicant() != null) {
						for (Applicant appl:patent.getListApplicant()) {
							appl.setApplicant_id(KeyGeneratorUtils.generateRandomString());
							appl.setPatent(patent);
						}
					}
					if (patent.getListInventor() != null) {
						for (Inventor inventor:patent.getListInventor()) {
							inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
							inventor.setPatent(patent);
						}
					}
					if (patent.getListAssignee() != null) {
						for (Assignee assignee:patent.getListAssignee()) {
							assignee.setAssignee_id(KeyGeneratorUtils.generateRandomString());
							assignee.setPatent(patent);
						}
					}
					
					Date now = new Date();
					PatentEditHistory peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setField_id(Constants.PATENT_ALL_FIELD);
					peh.setPatent(patent);
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setHistory_data("create");
					peh.setHistory_status("create");
					peh.setCreate_date(now);
					
					patent.addHistory(peh);
					
					patentDao.create(patent);
					taskResult = Constants.INT_SUCCESS;
				} else {
					if(StringUtils.isNULL(patent.getPatent_id())) {
						patent.setPatent_id(appNoPatent.getPatent_id());
					}
					
					taskResult = updatePatent(patent);
				}
			} else {
				taskResult = Constants.INT_CANNOT_FIND_DATA;
			}
		} else {
		
			taskResult = Constants.INT_CANNOT_FIND_DATA;
		}
		return taskResult;
	}
	
	
	
	@Override
	public int authorizedUpdatePatent(String businessId,Patent patent) {
		
		Patent dbBean = patentDao.getById(businessId,patent.getPatent_id());
		if(dbBean != null) {
			return updatePatent(patent);
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	
	}

	@Override
	public int  updatePatent(Patent patent){
		List<PatentEditHistory> editList = new ArrayList<PatentEditHistory>(); 
		Patent dbBean = patentDao.getById(patent.getPatent_id());

		if(dbBean!=null){
			//TODO save edit history
			//insertEditHistory(dbBean,patent);

			dbBean.setPatent_name(patent.getPatent_name());
			dbBean.setPatent_name_en(patent.getPatent_name_en());
			dbBean.setPatent_appl_country(patent.getPatent_appl_country());
			
			dbBean.setPatent_appl_no(patent.getPatent_appl_no());
			dbBean.setPatent_appl_date(patent.getPatent_appl_date());

			dbBean.setPatent_notice_no(patent.getPatent_notice_no());
			dbBean.setPatent_notice_date(patent.getPatent_notice_date());
			
			dbBean.setPatent_publish_no(patent.getPatent_publish_no());
			dbBean.setPatent_publish_date(patent.getPatent_publish_date());
			
			dbBean.setPatent_no(patent.getPatent_no());
			dbBean.setPatent_bdate(patent.getPatent_bdate());
			dbBean.setPatent_edate(patent.getPatent_edate());
			
			dbBean.setPatent_cancel_date(patent.getPatent_cancel_date());
			dbBean.setPatent_charge_expire_date(patent.getPatent_charge_expire_date());
			
			dbBean.setPatent_charge_duration_year(patent.getPatent_charge_duration_year());
			
			dbBean.setFamily(patent.getFamily());
			
			dbBean.setListIPC(patent.getListIPC());
			
			if (patent.getPatentAbstract() != null) {
				if (dbBean.getPatentAbstract() != null) {
					PatentAbstract paDb = dbBean.getPatentAbstract();
					paDb.setContext_abstract(patent.getPatentAbstract().getContext_abstract());
				} else {
					if (StringUtils.isNULL(
							patent.getPatentAbstract().getPatent_abstract_id())) {
						patent.getPatentAbstract().setPatent_abstract_id(KeyGeneratorUtils.generateRandomString());
					}
					patent.getPatentAbstract().setPatent(patent);
					dbBean.setPatentAbstract(patent.getPatentAbstract());
				}
			}
			
			if (patent.getPatentClaim() != null) {
				if (dbBean.getPatentClaim() != null) {
					PatentClaim pcDb = dbBean.getPatentClaim();
					pcDb.setContext_claim(patent.getPatentClaim().getContext_claim());
				} else {
					if (StringUtils.isNULL(
							patent.getPatentClaim().getPatent_claim_id())) {
						patent.getPatentClaim().setPatent_claim_id(KeyGeneratorUtils.generateRandomString());
					}
					patent.getPatentClaim().setPatent(patent);
					dbBean.setPatentClaim(patent.getPatentClaim());
				}
			}
			
			if (patent.getPatentDesc() != null) {
				String descStr = patent.getPatentDesc().getContext_desc();
				if (patent.getPatentDesc().getContext_desc().length() > 65000) {
					descStr = descStr.substring(0, 65000)+"....";
				}
				if (dbBean.getPatentDesc() != null) {
					PatentDescription pdDb = dbBean.getPatentDesc();
					pdDb.setContext_desc(descStr);
				} else {
					if (StringUtils.isNULL(
							patent.getPatentDesc().getPatent_desc_id())) {
						patent.getPatentDesc().setPatent_desc_id(KeyGeneratorUtils.generateRandomString());
					}
					patent.getPatentDesc().setPatent(patent);
					patent.getPatentDesc().setContext_desc(descStr);
					dbBean.setPatentDesc(patent.getPatentDesc());
				}
			}
			
			//TODO charles 

			//mappingAssignee(dbBean,patent);
			//mappingApplicant(dbBean,patent);
			//mappingInventor(dbBean,patent);

//			mappingAssignee(dbBean,patent);
//			mappingApplicant(dbBean,patent);
//			mappingInventor(dbBean,patent);

			
			//TODO Leo edit
			
//			log.info("contact :"+patent.getListContact().size());
			
			log.info("1");
			handleCost(dbBean, patent);
			//dbBean.getListCost().clear();
			//log.info("db cost size :"+dbBean.getListCost().size());
			//dbBean.getListCost().addAll(patent.getListCost());
			//log.info("db cost size :"+dbBean.getListCost().size());
			log.info("2");
//			log.info("cost :"+patent.getListCost().size());
//			dbBean.setListCost(patent.getListCost());
//			dbBean.setListPortfolio(patent.getListPortfolio());
			///dbBean.setListHistory(patent.getListHistory());
			
			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}
	
	
	@Override
	public int combinePatentFamily(List<String> ids,String businessId) {
		log.info("businessId :"+businessId);
		List<Patent> list = patentDao.getByPatentIds(ids, businessId);
		log.info("list :"+list.size());
		PatentFamily family = null;
		for(Patent patent : list) {
			log.info("patent :"+patent.getPatent_id());
			if(patent.getFamily()!=null) {
				if(family == null) {
					family = patent.getFamily();
				}else {
					if(!family.getPatent_family_id().equals(patent.getFamily().getPatent_family_id())) {
						return Constants.INT_DATA_DUPLICATE;
					}
				}
			}else {
				if(family!=null) {
					family.addPatent(patent);
					//patent.setFamily(family);
				}
			}	
		}
		
		if(family == null) {
			log.info("no family");
			family = new PatentFamily();
			family.setPatent_family_id(KeyGeneratorUtils.generateRandomString());
			family.setCreate_date(new Date ());
			family.setUpdate_date(new Date());
			familyDao.create(family);
			for(Patent patent : list) {
				family.addPatent(patent);
				//patent.setFamily(family);
			}
			return Constants.INT_SUCCESS;
		}else {
			log.info("set family");
			for(Patent patent : list) {
				log.info("patent :"+patent.getPatent_id());
				if(patent.getFamily() == null) {
					family.addPatent(patent);
					
				}
			}
		}
		
		return Constants.INT_SUCCESS;
	}
	

	@Override
	public int deletePatent(Patent patent) {
		Patent dbBean = patentDao.getById(patent.getPatent_id());
		if(dbBean!=null) {
			patentDao.delete(patent.getPatent_id());
			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
		
	}

	
	@Override
	public 	ListQueryForm getByBusinessId(String businessId,int page){
		log.info("businessId:"+businessId);
		List<Patent> list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE);
		for(Patent patent : list) {
			patent.getListStatus().size();
			patent.getListExtension().size();
			patent.getListBusiness().size();
		}
		int count = patentDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	@Override
	public List<Patent> getAllByBussinessId(String businessId){
		List<Patent> patentList = patentDao.getAllByBusinessId(businessId);
		for (Patent patent:patentList) {
			patent.getListApplicant().size();
			patent.getListAssignee().size();
			patent.getListInventor().size();
			patent.getListHistory().size();
			patent.getListStatus().size();
			patent.getListIPC().size();
			patent.getListExtension().size();
			patent.getListBusiness().size();
			List<PatentStatus> listPatentStatus = patentStatusDao.getByPatent(patent.getPatent_id());
			
			for (PatentStatus patentStatus:listPatentStatus) {
				for (Status status:patent.getListStatus()) {
					if (status.getStatus_id().equals(patentStatus.getStatus_id())) {
						status.setPatentStatus(patentStatus);
					}
				}
			}
			
		}
		return patentList;
	}
	
	@Override
	public List<Patent> getByPatentIds(List<String> idList,String businessId){
		List<Patent> list = patentDao.getByPatentIds(idList,businessId);
		for(Patent patent : list) {
			patent.getListStatus().size();
			patent.getListExtension().size();
			patent.getListBusiness().size();
		}
		return list;
	}
	
	@Override
	public Patent getByPatentNo(String patentNo){
		return patentDao.getByPatentNo(patentNo);
	}

	
	@Override
	public List<Patent> getByFamily(String familyId){
		List<Patent> list = patentDao.getByFamily(familyId);
		for(Patent patent : list) {
			patent.getListStatus().size();
		}
		return list;
	}

	@Override
	public ListQueryForm fieldSearchPatent(Object searchObj, String fieldId, String businessId, int page) {
		//check text is date or not
		PatentField field = fieldDao.getById(fieldId);
		int count = 0;
		List<Patent> list = new ArrayList<>();
		if (Constants.PATENT_ALL_FIELD.equals(fieldId) || field == null) {
			String text = (String) searchObj;
			list = patentDao.searchAllFieldPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
			count = patentDao.countSearchAllFieldPatent('%'+text+'%', businessId);
			if (list.isEmpty()) {
				list = patentDao.searchFieldInventorListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldInventorPatent('%'+text+'%', businessId);
			}
			if (list.isEmpty()) {
				list = patentDao.searchFieldApplicantListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldApplicantPatent('%'+text+'%', businessId);
			}
			if (list.isEmpty()) {
				list = patentDao.searchFieldAssigneeListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldAssigneePatent('%'+text+'%', businessId);
			}
		}else {
			if (Constants.PATENT_NAME_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_name", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_name", businessId);
			} else if (Constants.PATENT_NAME_EN_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_name_en", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_name_en", businessId);
			} else if (Constants.PATENT_COUNTRY_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_appl_country", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_appl_country", businessId);
			} else if (Constants.PATENT_NO_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_no", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_no", businessId);
			} else if (Constants.PATENT_APPL_NO_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_appl_no", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_appl_no", businessId);
			} else if (Constants.PATENT_APPL_DATE_FIELD.equals(field.getField_id())) {
				Long longTimeStamp = (Long) searchObj;
				Date d = new Date(longTimeStamp);
				list = patentDao.searchFieldPatent(d, "patent_appl_date", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent(d, "patent_appl_date", businessId);
			} else if (Constants.PATENT_NOTICE_NO_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_notice_no", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_notice_no", businessId);
			} else if (Constants.PATENT_NOTICE_DATE_FIELD.equals(field.getField_id())) {
				Long longTimeStamp = (Long) searchObj;
				Date d = new Date(longTimeStamp);
				list = patentDao.searchFieldPatent(d, "patent_notice_date", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent(d, "patent_notice_date", businessId);
			} else if (Constants.PATENT_PUBLISH_NO_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_publish_no", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_publish_no", businessId);
			} else if (Constants.PATENT_PUBLISH_DATE_FIELD.equals(field.getField_id())) {
				Long longTimeStamp = (Long) searchObj;
				Date d = new Date(longTimeStamp);
				list = patentDao.searchFieldPatent(d, "patent_publish_date", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent(d, "patent_publish_date", businessId);
			} else if (Constants.ASSIGNEE_NAME_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldAssigneeListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldAssigneePatent('%'+text+'%', businessId);
			} else if (Constants.APPLIANT_NAME_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldApplicantListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldApplicantPatent('%'+text+'%', businessId);
			} else if (Constants.IVENTOR_NAME_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldInventorListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldInventorPatent('%'+text+'%', businessId);
			} else if (Constants.PATENT_STATUS_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldStatusListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldStatusPatent('%'+text+'%', businessId);
			} else if (Constants.PATENT_COST_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldCostListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldCostPatent('%'+text+'%', businessId);
			} else if (Constants.PATENT_FAMILY_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldFamilyListPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldFamilyPatent('%'+text+'%', businessId);
			} else {
				String text = (String) searchObj;
				list = patentDao.searchFieldExtensionListPatent('%'+text+'%', field.getField_code(), businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldExtensionPatent('%'+text+'%', field.getField_code(), businessId);
			}
		}
		if (!list.isEmpty()) {
			for(Patent patent : list) {

				patent.getListStatus().size();
				patent.getListExtension().size();
				patent.getListBusiness().size();
				
//				List<PatentStatus> listPatentStatus = patentStatusDao.getByPatent(patent.getPatent_id());
//				
//				for (PatentStatus patentStatus:listPatentStatus) {
//					for (Status status:patent.getListStatus()) {
//						if (status.getStatus_id().equals(patentStatus.getStatus_id())) {
//							status.setPatentStatus(patentStatus);
//						}
//					}
//				}
			}
		}
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	private void mappingInventor(Patent dbBean,Patent patent) {
		List<Inventor> mapInventor = dbBean.getListInventor();
		dbBean.setListInventor(null);
		patentDao.deleteInventor(dbBean.getPatent_id());	
		if (patent.getListInventor() != null) {
			for (Inventor inventor:patent.getListInventor()) {
				if (StringUtils.isNULL(inventor.getInventor_id())) {
					inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
				}
				inventor.setPatent(patent);
				dbBean.addInventor(inventor);
			}
		}
	}
	
	private void mappingApplicant(Patent dbBean,Patent patent) {
		List<Applicant> mapApplicant = dbBean.getListApplicant();
		dbBean.setListApplicant(null);
		patentDao.deleteApplicant(dbBean.getPatent_id());
		if (patent.getListApplicant() != null) {
			for (Applicant appl:patent.getListApplicant()) {
				if (StringUtils.isNULL(appl.getApplicant_id())) {
					appl.setApplicant_id(KeyGeneratorUtils.generateRandomString());
				}
				appl.setPatent(patent);
				dbBean.addApplicant(appl);
			}
		}
	}
	
	private void mappingAssignee(Patent dbBean,Patent patent) {
		List<Assignee> mapAssignee = dbBean.getListAssignee();
		dbBean.setListAssignee(null);
		patentDao.deleteAssignee(dbBean.getPatent_id());
		if (patent.getListAssignee() != null) {
			for (Assignee assign:patent.getListAssignee()) {
				if (StringUtils.isNULL(assign.getAssignee_id())) {
					assign.setAssignee_id(KeyGeneratorUtils.generateRandomString());
				}
				assign.setPatent(patent);
				dbBean.addAssignee(assign);
			}
		}
	}
	
	private void insertEditHistory(Patent dbBean,Patent patent) {
		List<PatentField> fieldList = fieldDao.getAllFields();
		for (PatentField field:fieldList) {
			if (Constants.PATENT_NAME_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_name();
				String newField = patent.getPatent_name();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_NAME_EN_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_name_en();
				String newField = patent.getPatent_name_en();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_COUNTRY_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_appl_country();
				String newField = patent.getPatent_appl_country();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_no();
				String newField = patent.getPatent_no();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_APPL_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_appl_no();
				String newField = patent.getPatent_appl_no();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_NOTICE_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_notice_no();
				String newField = patent.getPatent_notice_no();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_PUBLISH_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_publish_no();
				String newField = patent.getPatent_publish_no();
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_APPL_DATE_FIELD.equals(field.getField_id())) {
				String sourceField = DateUtils.getDashFormatDate(dbBean.getPatent_appl_date());
				String newField = DateUtils.getDashFormatDate(patent.getPatent_appl_date());
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_NOTICE_DATE_FIELD.equals(field.getField_id())) {
				String sourceField = null;
				String newField = null;
				if (dbBean.getPatent_notice_date() != null) {
					sourceField = DateUtils.getDashFormatDate(dbBean.getPatent_notice_date());
				}
				if (patent.getPatent_notice_date() != null) {
					newField = DateUtils.getDashFormatDate(patent.getPatent_notice_date());
				}
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_PUBLISH_DATE_FIELD.equals(field.getField_id())) {
				String sourceField = DateUtils.getDashFormatDate(dbBean.getPatent_publish_date());
				String newField = DateUtils.getDashFormatDate(patent.getPatent_publish_date());
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.ASSIGNEE_NAME_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				for (Assignee assignee:dbBean.getListAssignee()) {
					JSONObject sourceObj = new JSONObject();
					sourceObj.put("assignee_name", assignee.getAssignee_name());
					sourceObj.put("assignee_name_en", assignee.getAssignee_name_en());
					sourceObj.put("country_id", assignee.getCountry_id());
					sourceObj.put("country_name", assignee.getCountry_name());
					sourceObj.put("assignee_order", assignee.getAssignee_order());
					String source = sourceObj.toString();
					souceList.put(assignee.getAssignee_order(), source);
				}
				Map<Integer, String> newList = new HashMap<>();
				for (Assignee assignee:patent.getListAssignee()) {
					JSONObject newObj = new JSONObject();
					newObj.put("assignee_name", assignee.getAssignee_name());
					newObj.put("assignee_name_en", assignee.getAssignee_name_en());
					newObj.put("country_id", assignee.getCountry_id());
					newObj.put("country_name", assignee.getCountry_name());
					newObj.put("assignee_order", assignee.getAssignee_order());
					String newData = newObj.toString();
					newList.put(assignee.getAssignee_order(), newData);
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
			if (Constants.APPLIANT_NAME_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				for (Applicant appl:dbBean.getListApplicant()) {
					JSONObject sourceObj = new JSONObject();
					sourceObj.put("applicant_name", appl.getApplicant_name());
					sourceObj.put("applicant_name_en", appl.getApplicant_name_en());
					sourceObj.put("applicant_address", appl.getApplicant_address());
					sourceObj.put("applicant_address_en", appl.getApplicant_address_en());
					sourceObj.put("country_id", appl.getCountry_id());
					sourceObj.put("country_name", appl.getCountry_name());
					sourceObj.put("applicant_order", appl.getApplicant_order());
					String source = sourceObj.toString();
					souceList.put(appl.getApplicant_order(), source);
				}
				Map<Integer, String> newList = new HashMap<>();
				for (Applicant appl:patent.getListApplicant()) {
					JSONObject newObj = new JSONObject();
					newObj.put("applicant_name", appl.getApplicant_name());
					newObj.put("applicant_name_en", appl.getApplicant_name_en());
					newObj.put("applicant_address", appl.getApplicant_address());
					newObj.put("applicant_address_en", appl.getApplicant_address_en());
					newObj.put("country_id", appl.getCountry_id());
					newObj.put("country_name", appl.getCountry_name());
					newObj.put("applicant_order", appl.getApplicant_order());
					String newData = newObj.toString();
					newList.put(appl.getApplicant_order(), newData);
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
			if (Constants.IVENTOR_NAME_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				for (Inventor inv:dbBean.getListInventor()) {
					JSONObject sourceObj = new JSONObject();
					sourceObj.put("inventor_name", inv.getInventor_name());
					sourceObj.put("inventor_name_en", inv.getInventor_name_en());
					sourceObj.put("country_id", inv.getCountry_id());
					sourceObj.put("country_name", inv.getCountry_name());
					sourceObj.put("inventor_order", inv.getInventor_order());
					String source = sourceObj.toString();
					souceList.put(inv.getInventor_order(), source);
				}
				Map<Integer, String> newList = new HashMap<>();
				for (Inventor inv:patent.getListInventor()) {
					JSONObject newObj = new JSONObject();
					newObj.put("inventor_name", inv.getInventor_name());
					newObj.put("inventor_name_en", inv.getInventor_name_en());
					newObj.put("country_id", inv.getCountry_id());
					newObj.put("country_name", inv.getCountry_name());
					newObj.put("inventor_order", inv.getInventor_order());
					String newData = newObj.toString();
					newList.put(inv.getInventor_order(), newData);
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
			if (Constants.PATENT_COST_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				int index = 0;
				if (!dbBean.getListCost().isEmpty()) {
					for (PatentCost cost:dbBean.getListCost()) {
						JSONObject sourceObj = new JSONObject();
						sourceObj.put("cost_name", cost.getCost_name());
						sourceObj.put("cost_price", cost.getCost_price());
						sourceObj.put("cost_unit", cost.getCost_unit());
						sourceObj.put("cost_currency", cost.getCost_currency());
						sourceObj.put("cost_date", cost.getCost_date());
						String source = sourceObj.toString();
						souceList.put(index, source);
						index++;
					}
				}
				Map<Integer, String> newList = new HashMap<>();
				int indexN = 0;
				for (PatentCost cost:patent.getListCost()) {
						JSONObject newObj = new JSONObject();
						newObj.put("cost_name", cost.getCost_name());
						newObj.put("cost_price", cost.getCost_price());
						newObj.put("cost_unit", cost.getCost_unit());
						newObj.put("cost_currency", cost.getCost_currency());
						newObj.put("cost_date", cost.getCost_date());
						String newData = newObj.toString();
						newList.put(indexN, newData);
						indexN++;
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
			if (Constants.PATENT_FAMILY_FIELD.equals(field.getField_id())) {
				String sourceField = null;
				String newField = null;
				if (dbBean.getFamily() != null) {
					sourceField = dbBean.getFamily().getPatent_family_id();
				}
				if (patent.getFamily() != null) {
					newField = patent.getFamily().getPatent_family_id();
				}
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.SCHOOL_NO_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				int index = 0;
				for (PatentExtension ext:dbBean.getListExtension()) {
					JSONObject sourceObj = new JSONObject();
					sourceObj.put("extension_file_num", ext.getExtension_file_num());
					String source = sourceObj.toString();
					souceList.put(index, source);
					index++;
				}
				Map<Integer, String> newList = new HashMap<>();
				int indexN = 0;
				for (PatentExtension ext:patent.getListExtension()) {
					JSONObject newObj = new JSONObject();
					newObj.put("extension_file_num", ext.getExtension_file_num());
					String newData = newObj.toString();
					newList.put(indexN, newData);
					indexN++;
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
			if (Constants.SCHOOL_APPL_YEAR_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				int index = 0;
				for (PatentExtension ext:dbBean.getListExtension()) {
					JSONObject sourceObj = new JSONObject();
					sourceObj.put("extension_appl_year", ext.getExtension_appl_year());
					String source = sourceObj.toString();
					souceList.put(index, source);
					index++;
				}
				Map<Integer, String> newList = new HashMap<>();
				int indexN = 0;
				for (PatentExtension ext:patent.getListExtension()) {
					JSONObject newObj = new JSONObject();
					newObj.put("extension_appl_year", ext.getExtension_appl_year());
					String newData = newObj.toString();
					newList.put(indexN, newData);
					indexN++;
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
			if (Constants.SCHOOL_MEMO_FIELD.equals(field.getField_id())) {
				Map<Integer, String> souceList = new HashMap<>();
				int index = 0;
				for (PatentExtension ext:dbBean.getListExtension()) {
					JSONObject sourceObj = new JSONObject();
					sourceObj.put("extension_memo", ext.getExtension_memo());
					String source = sourceObj.toString();
					souceList.put(index, source);
					index++;
				}
				Map<Integer, String> newList = new HashMap<>();
				int indexN = 0;
				for (PatentExtension ext:patent.getListExtension()) {
					JSONObject newObj = new JSONObject();
					newObj.put("extension_memo", ext.getExtension_memo());
					String newData = newObj.toString();
					newList.put(indexN, newData);
					indexN++;
				}
				List<PatentEditHistory> list = checkFieldList(patent, souceList, newList, field.getField_id());
				dbBean.addHistory(list);
			}
		}
	}
	
	private List<PatentEditHistory> checkFieldList(Patent patent, 
			Map<Integer, String> souceList, Map<Integer, String> newList, String fieldId) {
		List<PatentEditHistory> listPeh = new ArrayList<>();
		Date now = new Date();
		if (souceList.isEmpty()) {
			if (!newList.isEmpty()) {
				Set<Entry<Integer, String>> newSet = newList.entrySet();
				for (Entry<Integer, String> entry:newSet) {
					PatentEditHistory peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setPatent(patent);
					peh.setField_id(fieldId);
					peh.setHistory_data(entry.getValue());
					peh.setHistory_status("insert");
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setCreate_date(now);
					listPeh.add(peh);
				}
			}
		} else {
			if (newList.isEmpty()) {
				Set<Entry<Integer, String>> sourceSet = souceList.entrySet();
				for (Entry<Integer, String> entry:sourceSet) {
					PatentEditHistory peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setPatent(patent);
					peh.setField_id(fieldId);
					peh.setHistory_data(entry.getValue());
					peh.setHistory_status("remove");
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setCreate_date(now);
					listPeh.add(peh);
				}
			} else {
				if (souceList.size() <= newList.size()) {
					Set<Entry<Integer, String>> newSet = newList.entrySet();
					for (Entry<Integer, String> entry:newSet) {
						if (souceList.containsKey(entry.getKey())) {
							if (!entry.getValue().equals(souceList.get(entry.getKey()))) {
								PatentEditHistory peh = new PatentEditHistory();
								peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
								peh.setPatent(patent);
								peh.setField_id(fieldId);
								peh.setHistory_data(entry.getValue());
								peh.setHistory_status("update");
								peh.setAdmin(patent.getAdmin());
								peh.setAdmin_ip(patent.getAdmin_ip());
								peh.setCreate_date(now);
								listPeh.add(peh);
							}
						} else {
							PatentEditHistory peh = new PatentEditHistory();
							peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
							peh.setPatent(patent);
							peh.setField_id(fieldId);
							peh.setHistory_data(entry.getValue());
							peh.setHistory_status("insert");
							peh.setAdmin(patent.getAdmin());
							peh.setAdmin_ip(patent.getAdmin_ip());
							peh.setCreate_date(now);
							listPeh.add(peh);
						}
					}
				} else {
					Set<Entry<Integer, String>> sourceSet = souceList.entrySet();
					for (Entry<Integer, String> entry:sourceSet) {
						if (newList.containsKey(entry.getKey())) {
							if (!entry.getValue().equals(newList.get(entry.getKey()))) {
								PatentEditHistory peh = new PatentEditHistory();
								peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
								peh.setPatent(patent);
								peh.setField_id(fieldId);
								peh.setHistory_data(newList.get(entry.getKey()));
								peh.setHistory_status("update");
								peh.setAdmin(patent.getAdmin());
								peh.setAdmin_ip(patent.getAdmin_ip());
								peh.setCreate_date(now);
								listPeh.add(peh);
							}
						} else {
							PatentEditHistory peh = new PatentEditHistory();
							peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
							peh.setPatent(patent);
							peh.setField_id(fieldId);
							peh.setHistory_data(entry.getValue());
							peh.setHistory_status("remove");
							peh.setAdmin(patent.getAdmin());
							peh.setAdmin_ip(patent.getAdmin_ip());
							peh.setCreate_date(now);
							listPeh.add(peh);
						}
					}
				}
				
			}
		}
		return listPeh;
	}
	
	private PatentEditHistory checkFieldName(Patent patent, String sourceField, String newField, String fieldId) {
		Date now = new Date();
		PatentEditHistory peh = null;
		if (newField != null) {
			if (sourceField == null) {
				peh = new PatentEditHistory();
				peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
				peh.setPatent(patent);
				peh.setField_id(fieldId);
				peh.setHistory_data(newField);
				peh.setHistory_status("insert");
				peh.setAdmin(patent.getAdmin());
				peh.setAdmin_ip(patent.getAdmin_ip());
				peh.setCreate_date(now);
			} else {
				if (!sourceField.equals(newField)) {
					peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setPatent(patent);
					peh.setField_id(fieldId);
					peh.setHistory_data(newField);
					peh.setHistory_status("update");
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setCreate_date(now);
				}
			}
		} else {
			if (sourceField != null) {
				peh = new PatentEditHistory();
				peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
				peh.setPatent(patent);
				peh.setField_id(fieldId);
				peh.setHistory_data(sourceField);
				peh.setHistory_status("remove");
				peh.setAdmin(patent.getAdmin());
				peh.setAdmin_ip(patent.getAdmin_ip());
				peh.setCreate_date(now);
			}
		}
		return peh;
	}
	
	private  void handleCost(Patent dbPatent,Patent editPatent) {
		List<PatentCost> listCost = editPatent.getListCost();
		for(PatentCost cost :listCost) {
			if(StringUtils.isNULL(cost.getCost_id())) {
				cost.setCost_id(KeyGeneratorUtils.generateRandomString());
			}
			cost.setPatent(dbPatent);
		}
		patentDao.deletePatentCost(dbPatent.getPatent_id());
		dbPatent.setListCost(editPatent.getListCost());
	}
	
}
