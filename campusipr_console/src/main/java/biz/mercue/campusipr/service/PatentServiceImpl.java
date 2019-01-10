package biz.mercue.campusipr.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.ApplicantDao;
import biz.mercue.campusipr.dao.AssigneeDao;
import biz.mercue.campusipr.dao.InventorDao;
import biz.mercue.campusipr.dao.PatentContextDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.PatentFamilyDao;
import biz.mercue.campusipr.dao.PatentStatusDao;
import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContext;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.util.Constants;
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
	private AssigneeDao assigneeDao;
	
	@Autowired
	private ApplicantDao applicantDao;
	
	@Autowired
	private InventorDao inventorDao;
	
	@Autowired
	private StatusDao statusDao;
	
	@Autowired
	private PatentStatusDao patentStatusDao;
	
	@Autowired
	private PatentContextDao patentContextDao;
	
	@Autowired
	private PatentFamilyDao familyDao;

	@Override
	public Patent getById(String businessId,String id) {
		log.info("get by id: " + id);
		Patent patent = patentDao.getById(businessId, id);
		if(patent!= null) {
			PatentFamily family = patent.getFamily();
			if(family!=null) {
				log.info("family id :"+family.getPatent_family_id());
				log.info(patent.getFamily().getListPatent().size());
			}else {
				log.info("family is null");
			}
			PatentContext context = patent.getPatentContext();
			if(context!=null) {
				log.info("context id :"+context.getPatent_context_id());
			}else {
				log.info("context is null");
			}
			
			PatentExtension extension = patent.getPatent_extension();
			if(extension!=null) {
				log.info("extension id :"+extension.getExtension_id());
			}else {
				log.info("extension is null");
			}
			
			

			patent.getListBusiness().size();
			//TODO change eger
			patent.getListStatus().size();
			patent.getListIPC().size();
			patent.getListAgent().size();
			patent.getListApplicant().size();
			patent.getListAssignee().size();
			patent.getListContact().size();
			patent.getListCost().size();
			patent.getListInventor().size();
			patent.getListPortfolio().size();
			

		}
		return patent;
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
		peh.setField_id(Constants.PATENT_NAME_FIELD);
		peh.setPatent(patent);
		peh.setAdmin(patent.getAdmin());
		peh.setHistory_data("create");
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
		
		if (!patent.getPatentStatusList().isEmpty()) {
			for (PatentStatus patentStatus:patent.getPatentStatusList()) {
				Status status = statusDao.getByEventCode(patentStatus.getStatus().getEvent_code(), patentStatus.getStatus().getCountry_id());
				log.info("0");
				if (status != null) {
					patentStatus.setStatus_id(status.getStatus_id());
					PatentStatus dBean = patentStatusDao.getByStatusAndPatent(patentStatus.getPatent_id(), patentStatus.getStatus_id(), patentStatus.getCreate_date());
					log.info("1");
					if (dBean == null) {
						log.info("2");
						patentStatusDao.create(patentStatus);
					}
				} else {
					Status createStatus = patentStatus.getStatus();
					if(StringUtils.isNULL(createStatus.getStatus_id())) {
						createStatus.setStatus_id(KeyGeneratorUtils.generateRandomString());
					}
					statusDao.create(createStatus);
					patentStatus.setStatus_id(createStatus.getStatus_id());
					PatentStatus dBean = patentStatusDao.getByStatusAndPatent(patentStatus.getPatent_id(), patentStatus.getStatus_id(), patentStatus.getCreate_date());
					if (dBean == null) {
						patentStatusDao.create(patentStatus);
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
		
		Patent syncPatent = new Patent();
		//查詢台灣專利
		if (patent.getPatent_appl_no().length() == 9 && 
				Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
			syncPatent = ServiceTaiwanPatent.getPatentRightByApplNo(patent.getPatent_appl_no());
		}else if (patent.getPatent_appl_no().length() == 10 && 
				Constants.APPL_COUNTRY_US.endsWith(patent.getPatent_appl_country())) {
			syncPatent = ServiceUSPatent.getPatentRightByapplNo(patent.getPatent_appl_no());
		}else {
			syncPatent = ServiceChinaPatent.getPatentRightByApplicantNo(patent.getPatent_appl_no());
		}
		
		if(syncPatent != null) {
			String applNo =  syncPatent.getPatent_appl_no();
			if (StringUtils.isNULL(applNo) == false) {
				log.info("1");
				patent.setPatent_name(syncPatent.getPatent_name());
				patent.setPatent_name_en(syncPatent.getPatent_name_en());
				patent.setPatent_appl_country(syncPatent.getPatent_appl_country());
				
				patent.setPatent_appl_no(syncPatent.getPatent_appl_no());
				patent.setPatent_appl_date(syncPatent.getPatent_appl_date());

				patent.setPatent_notice_no(syncPatent.getPatent_notice_no());
				patent.setPatent_notice_date(syncPatent.getPatent_notice_date());
				
				patent.setPatent_publish_no(syncPatent.getPatent_publish_no());
				patent.setPatent_publish_date(syncPatent.getPatent_publish_date());
				
				patent.setPatent_no(syncPatent.getPatent_no());
				patent.setPatent_bdate(syncPatent.getPatent_bdate());
				patent.setPatent_edate(syncPatent.getPatent_edate());
				
				patent.setPatent_cancel_date(syncPatent.getPatent_cancel_date());
				patent.setPatent_charge_expire_date(syncPatent.getPatent_charge_expire_date());
				
				patent.setPatent_charge_duration_year(syncPatent.getPatent_charge_duration_year());
				
				patent.setListAssignee(syncPatent.getListAssignee());
				patent.setListInventor(syncPatent.getListInventor());
				patent.setPatentContext(syncPatent.getPatentContext());
				
				Patent appNoPatent = patentDao.getByApplNo(applNo);
				if(appNoPatent==null) {
					log.info("2");
					if(StringUtils.isNULL(patent.getPatent_id())) {
						patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
					}
					if (patent.getPatentContext() != null) {
						patent.getPatentContext().setPatent_context_id(KeyGeneratorUtils.generateRandomString());
						patent.getPatentContext().setPatent(patent);
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
					
					patent.addBusiness(patent.getBusiness());
					
					Date now = new Date();
					PatentEditHistory peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setField_id(Constants.PATENT_NAME_FIELD);
					peh.setPatent(patent);
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setHistory_data("create");
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
					taskResult = Constants.INT_SUCCESS;
				} else {
					if(StringUtils.isNULL(patent.getPatent_id())) {
						patent.setPatent_id(appNoPatent.getPatent_id());
					}
					if (patent.getPatentContext() != null) {
						if (appNoPatent.getPatentContext() != null) {
							patent.getPatentContext().setPatent_context_id(appNoPatent.getPatentContext().getPatent_context_id());
						} else {
							patent.getPatentContext().setPatent_context_id(KeyGeneratorUtils.generateRandomString());
							patent.getPatentContext().setPatent(patent);
						}
					} 
					if (patent.getListInventor() != null) {
						for (Inventor inventor:patent.getListInventor()) {
							for (Inventor inventoDb:appNoPatent.getListInventor()) {
								if (inventoDb.getInventor_order() == inventor.getInventor_order()) {
									inventor.setInventor_id(inventoDb.getInventor_id());
								}
							}
						}
					}
					if (patent.getListAssignee() != null) {
						for (Assignee assignee:patent.getListAssignee()) {
							for (Assignee assigneeDb:appNoPatent.getListAssignee()) {
								if (assigneeDb.getAssignee_order() == assignee.getAssignee_order()) {
									assignee.setAssignee_id(assigneeDb.getAssignee_id());
								}
							}
						}
					}
					
					
					taskResult = updatePatent(patent);
					
					syncPatentStatus(patent);
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
	public int  updatePatent(Patent patent){
		Patent dbBean = patentDao.getById(patent.getPatent_id());

		if(dbBean!=null){
			//TODO save edit history
			String patentNameDb = "";
			if (dbBean.getPatent_name() != null) {
				patentNameDb = dbBean.getPatent_name();
			}
			String patentName = "";
			if (patent.getPatent_name() != null) {
				patentName = patent.getPatent_name();
			}
			if (!patentNameDb.equals(patentName)) {
					addEditHistory(patent, patent.getAdmin(), Constants.PATENT_NAME_FIELD);
			}
			
			String patentNameEnDb = "";
			if (dbBean.getPatent_name_en() != null) {
				patentNameEnDb = dbBean.getPatent_name_en();
			}
			String patentNameEn = "";
			if (patent.getPatent_name_en() != null) {
				patentNameEn = patent.getPatent_name_en();
			}
			if (!patentNameEnDb.equals(patentNameEn)) {
					addEditHistory(patent, patent.getAdmin(), Constants.PATENT_NAME_EN_FIELD);
			}
			addEditHistory(patent, patent.getAdmin(), Constants.ASSIGNEE_FIELD);
			addEditHistory(patent, patent.getAdmin(), Constants.APPLIANT_FIELD);
			addEditHistory(patent, patent.getAdmin(), Constants.IVENTOR_FIELD);

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
			
			
			if (patent.getPatentContext()!= null) {
				PatentContext pcDb = patentContextDao.getById(patent.getPatentContext().getPatent_context_id());
				if (pcDb != null) {
					pcDb.setContext_abstract(patent.getPatentContext().getContext_abstract());
					pcDb.setContext_claim(patent.getPatentContext().getContext_claim());
					pcDb.setContext_desc(patent.getPatentContext().getContext_desc());
				} else {
					dbBean.setPatentContext(patent.getPatentContext());
				}
			}
			
			//TODO update list
			if (patent.getListApplicant() != null) {
				for (Applicant appl:patent.getListApplicant()) {
					Applicant applDb = applicantDao.getById(appl.getApplicant_id());
					if (applDb != null) {
						applDb.setApplicant_name(appl.getApplicant_name());
						applDb.setApplicant_name_en(appl.getApplicant_name_en());
						applDb.setApplicant_address(appl.getApplicant_address());
						applDb.setApplicant_address_en(appl.getApplicant_address_en());
						applDb.setCountry_id(appl.getCountry_id());
						applDb.setCountry_name(appl.getCountry_name());
					} else {
						if (StringUtils.isNULL(appl.getApplicant_id())) {
							appl.setApplicant_id(KeyGeneratorUtils.generateRandomString());
						}
						appl.setPatent(patent);
						applicantDao.create(appl);
					}
				}
			}
			
			if (patent.getListAssignee() != null) {
				for (Assignee assignee:patent.getListAssignee()) {
					Assignee assigneeDb = assigneeDao.getById(assignee.getAssignee_id());
					if (assigneeDb != null) {
						assigneeDb.setAssignee_name(assignee.getAssignee_name());
						assigneeDb.setAssignee_name_en(assignee.getAssignee_name_en());
						assigneeDb.setCountry_id(assignee.getCountry_id());
						assigneeDb.setCountry_name(assignee.getCountry_name());
					} else {
						if (StringUtils.isNULL(assignee.getAssignee_id())) {
							assignee.setAssignee_id(KeyGeneratorUtils.generateRandomString());
						}
						assignee.setPatent(patent);
						assigneeDao.create(assignee);
					}
				}
			}
			
			if (patent.getListInventor() != null) {
				for (Inventor inventor:patent.getListInventor()) {
					Inventor inventorDb = inventorDao.getById(inventor.getInventor_id());
					if (inventorDb != null) {
						inventorDb.setInventor_name(inventor.getInventor_name());
						inventorDb.setInventor_name_en(inventor.getInventor_name_en());
						inventorDb.setCountry_id(inventor.getCountry_id());
						inventorDb.setCountry_name(inventor.getCountry_name());
					} else {
						if (StringUtils.isNULL(inventor.getInventor_id())) {
							inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
						}
						inventor.setPatent(patent);
						inventorDao.create(inventor);
					}
				}
			}
			dbBean.setListContact(patent.getListContact());
			dbBean.setListCost(patent.getListCost());
			dbBean.setListPortfolio(patent.getListPortfolio());
			dbBean.setListHistory(patent.getListHistory());
			
			boolean isDuplicate = false;
			List<Business> listBusiness = dbBean.getListBusiness();
			for(Business business : listBusiness) {
				if(patent.getBusiness().getBusiness_id().equals(business.getBusiness_id())) {
					isDuplicate = true;
				}
			}
			if (isDuplicate == false) {
				dbBean.addBusiness(patent.getBusiness());
			}
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
		}
		int count = patentDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	@Override
	public List<Patent> getAllByBussinessId(String businessId){
		List<Patent> patentList = patentDao.getAllByBusinessId(businessId);
		for (Patent patent:patentList) {
			List<Applicant> listAppl = applicantDao.getByPatentId(patent.getPatent_id());
			patent.setListApplicant(listAppl);
			List<Assignee> listAssign = assigneeDao.getByPatentId(patent.getPatent_id());
			patent.setListAssignee(listAssign);
			List<Inventor> listInventor = inventorDao.getByPatentId(patent.getPatent_id());
			patent.setListInventor(listInventor);
			List<PatentStatus> list = patentStatusDao.getByPatent(patent.getPatent_id());
			for (PatentStatus ps:list) {
				ps.setPatent_id(ps.getPatent_id());
				Status status = statusDao.getById(ps.getStatus_id());
				ps.setStatus(status);
			}
			if (list.size() > 0) {
				patent.setPatentStatusList(list);
			}
		}
		return patentList;
	}
	
	@Override
	public List<Patent> getByPatentIds(List<String> idList,String businessId){
		List<Patent> list = patentDao.getByPatentIds(idList,businessId);
		for(Patent patent : list) {
			patent.getListStatus().size();
		}
		return list;
	}
	
	@Override
	public Patent getByPatentNo(String patentNo){
		return patentDao.getByPatentNo(patentNo);
	}


	@Override
	public ListQueryForm searchPatent(String text, String businessId, int page) {
		
		//TODO no finish yet
		List<Patent> list = patentDao.searchPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE);
		
		int count = patentDao.searchCountPatent('%'+text+'%', businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	@Override
	public ListQueryForm fieldSearchPatent(String text, String businessId, int page) {
		
		//TODO no finish yet
		List<Patent> list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE);
		for(Patent patent : list) {
			patent.getListStatus().size();
		}
		int count = patentDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	private void addEditHistory(Patent patent, Admin admin, String addField) {
		Date now = new Date();
		PatentEditHistory peh = new PatentEditHistory();
		if (Constants.PATENT_NAME_FIELD.equals(addField) && patent.getPatent_name() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id(Constants.PATENT_NAME_FIELD);
			peh.setPatent(patent);
			peh.setAdmin(admin);
			peh.setHistory_data(patent.getPatent_name());
			peh.setAdmin_ip(patent.getAdmin_ip());
			peh.setCreate_date(now);
		}
		if (Constants.PATENT_NAME_EN_FIELD.equals(addField) && patent.getPatent_name_en() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id(Constants.PATENT_NAME_EN_FIELD);
			peh.setPatent(patent);
			peh.setAdmin(admin);
			peh.setHistory_data(patent.getPatent_name_en());
			peh.setAdmin_ip(patent.getAdmin_ip());
			peh.setCreate_date(now);
		}
		if (Constants.ASSIGNEE_FIELD.equals(addField) && patent.getListAssignee() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id(Constants.ASSIGNEE_FIELD);
			peh.setPatent(patent);
			peh.setAdmin(admin);
			String assigneeStr = "";
			int lastIndex = patent.getListAssignee().size() - 1;
			for (Assignee assignee:patent.getListAssignee()) {
				Assignee assigneeDb = assigneeDao.getById(assignee.getAssignee_id());
				if (assigneeDb == null) {
					if (assignee.getAssignee_id().equals(patent.getListAssignee().get(lastIndex).getAssignee_id())) {
						assigneeStr += JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class);
					} else {
						assigneeStr += JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class) + ",";
					}
				} else {
					String assigneeNameDb = "";
					if (assigneeDb.getAssignee_name() != null) {
						assigneeNameDb = assigneeDb.getAssignee_name();
					}
					String assigneeName = "";
					if (assignee.getAssignee_name() != null) {
						assigneeName = assignee.getAssignee_name();
					}
					String assigneeNameEnDb = "";
					if (assigneeDb.getAssignee_name_en() != null) {
						assigneeNameEnDb = assigneeDb.getAssignee_name_en();
					}
					String assigneeNameEn = "";
					if (assignee.getAssignee_name_en() != null) {
						assigneeNameEn = assignee.getAssignee_name_en();
					}
					if (!assigneeNameDb.equals(assigneeName) ||
							!assigneeNameEnDb.equals(assigneeNameEn)) {
						if (assignee.getAssignee_id().equals(patent.getListAssignee().get(lastIndex).getAssignee_id())) {
							assigneeStr += JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class);
						} else {
							assigneeStr += JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class) + ",";
						}
					}
				}
			}
			peh.setHistory_data(assigneeStr);
			peh.setAdmin_ip(patent.getAdmin_ip());
			peh.setCreate_date(now);
		}
		if (Constants.APPLIANT_FIELD.equals(addField) && patent.getListApplicant() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id(Constants.APPLIANT_FIELD);
			peh.setPatent(patent);
			peh.setAdmin(admin);
			String applicantStr = "";
			int lastIndex = patent.getListApplicant().size() - 1;
			for (Applicant appl:patent.getListApplicant()) {
				Applicant applDb = applicantDao.getById(appl.getApplicant_id());
				if (applDb == null) {
					if (appl.getApplicant_id().equals(patent.getListApplicant().get(lastIndex).getApplicant_id())) {
						applicantStr += JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class);
					} else {
						applicantStr += JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class)+ ",";
					}
				} else {
					String applNameDb = "";
					if (applDb.getApplicant_name() != null) {
						applNameDb = applDb.getApplicant_name();
					}
					String applName = "";
					if (appl.getApplicant_name() != null) {
						applName = appl.getApplicant_name();
					}
					String applNameEnDb = "";
					if (applDb.getApplicant_name_en() != null) {
						applNameEnDb = applDb.getApplicant_name_en();
					}
					String applNameEn = "";
					if (appl.getApplicant_name_en() != null) {
						applNameEn = appl.getApplicant_name_en();
					}
					String applAddressDb = "";
					if (applDb.getApplicant_address() != null) {
						applAddressDb = applDb.getApplicant_address();
					}
					String applAddress = "";
					if (appl.getApplicant_address() != null) {
						applAddress = appl.getApplicant_address();
					}
					String applAddressEnDb = "";
					if (applDb.getApplicant_address_en() != null) {
						applAddressEnDb = applDb.getApplicant_address_en();
					}
					String applAddressEn = "";
					if (appl.getApplicant_address_en() != null) {
						applAddressEn = appl.getApplicant_address_en();
					}
					if (!applNameDb.equals(applName) ||
							!applNameEnDb.equals(applNameEn) ||
								!applAddressDb.equals(applAddress) ||
									!applAddressEnDb.equals(applAddressEn)) {
						if (appl.getApplicant_id().equals(patent.getListApplicant().get(lastIndex).getApplicant_id())) {
							applicantStr += JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class);
						} else {
							applicantStr += JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class)+ ",";
						}
					}
				}
			}
			peh.setHistory_data(applicantStr);
			peh.setAdmin_ip(patent.getAdmin_ip());
			peh.setCreate_date(now);
		}
		if (Constants.IVENTOR_FIELD.equals(addField) && patent.getListInventor() != null) {
			peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
			peh.setField_id(Constants.IVENTOR_FIELD);
			peh.setPatent(patent);
			peh.setAdmin(admin);
			String inventorStr = "";
			int lastIndex = patent.getListInventor().size() - 1;
			for (Inventor inventor:patent.getListInventor()) {
				Inventor inventorDb = inventorDao.getById(inventor.getInventor_id());
				if (inventorDb == null) {
					if (inventor.getInventor_id().equals(patent.getListInventor().get(lastIndex).getInventor_id())) {
						inventorStr += JacksonJSONUtils.mapObjectWithView(inventor,  View.PatentDetail.class);
					} else {
						inventorStr += JacksonJSONUtils.mapObjectWithView(inventor,  View.PatentDetail.class)+ ",";
					}
				} else {
					String inventorNameDb = "";
					if (inventorDb.getInventor_name() != null) {
						inventorNameDb = inventorDb.getInventor_name();
					}
					String inventorName = "";
					if (inventor.getInventor_name() != null) {
						inventorName = inventor.getInventor_name();
					}
					String inventorNameEnDb = "";
					if (inventorDb.getInventor_name_en() != null) {
						inventorNameEnDb = inventorDb.getInventor_name_en();
					}
					String inventorNameEn = "";
					if (inventor.getInventor_name_en() != null) {
						inventorNameEn = inventor.getInventor_name_en();
					}
					if (!inventorNameDb.equals(inventorName) ||
							!inventorNameEnDb.equals(inventorNameEn)) {
						if (inventor.getInventor_id().equals(patent.getListInventor().get(lastIndex).getInventor_id())) {
							inventorStr += JacksonJSONUtils.mapObjectWithView(inventor,  View.PatentDetail.class);
						} else {
							inventorStr += JacksonJSONUtils.mapObjectWithView(inventor,  View.PatentDetail.class)+ ",";
						}
					}
				}
			}
			peh.setHistory_data(inventorStr);
			peh.setAdmin_ip(patent.getAdmin_ip());
			peh.setCreate_date(now);
		}
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
	}
	
	


	
}
