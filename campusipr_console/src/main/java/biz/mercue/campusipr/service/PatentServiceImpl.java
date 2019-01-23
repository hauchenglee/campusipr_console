package biz.mercue.campusipr.service;



import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.ApplicantDao;
import biz.mercue.campusipr.dao.AssigneeDao;
import biz.mercue.campusipr.dao.BusinessDao;
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
	private AdminDao adminDao;
	
	@Autowired
	private BusinessDao businessDao;
	
	@Autowired
	private PatentDao patentDao;
	
	@Autowired
	private StatusDao statusDao;
	
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
	public int addPatentByApplicant(List<Patent> list, String businessName, String adminId, String ip) {
		int taskResult= -1;

		List<Business> businesses = businessDao.search(businessName);
		if (businesses.isEmpty()) {
			List<String> dupucateStr = new ArrayList<>();
			List<String> businessNames = new ArrayList<>();
			businessNames.add(businessName);
			if(StringUtils.hasChinese(businessName)) {
				List<Patent> addlist = ServiceTaiwanPatent.getPatentRightByAssignee(businessNames, dupucateStr);
				list.addAll(addlist);
				addlist = ServiceChinaPatent.getPatentRightByAssignee(businessNames, dupucateStr);
				list.addAll(addlist);
			}else {
				List<Patent> addlist = ServiceUSPatent.getPatentRightByAssignee(businessNames, dupucateStr);
				list.addAll(addlist);
				addlist = ServiceTaiwanPatent.getPatentRightByAssignee(businessNames, dupucateStr);
				list.addAll(addlist);
			}
			
			for (Patent patent:list) {
				patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
				patent.setAdmin(adminDao.getById(adminId));
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
							
							patentDao.create(patent);
							taskResult = Constants.INT_SUCCESS;
						} else {
							if(StringUtils.isNULL(patent.getPatent_id())) {
								patent.setPatent_id(appNoPatent.getPatent_id());
							}
							
							taskResult = updatePatent(patent);
						}
					}
				} else {
					
					taskResult = Constants.INT_CANNOT_FIND_DATA;
				}
			}
		} else {
			for (Business business:businesses) {
				List<String> englishNames = new ArrayList<>();
				englishNames.add(business.getBusiness_name_en());
				String[] itemsEn = business.getBusiness_alias_en().replaceAll("\\[", "")
								.replaceAll("\\]", "").split(",");
				for (String item:itemsEn) {
					englishNames.add(item);
				}
				
				List<String> chineseNames = new ArrayList<>();
				chineseNames.add(business.getBusiness_name());
				String[] itemsCh = business.getBusiness_alias().replaceAll("\\[", "")
								.replaceAll("\\]", "").split(",");
				for (String item:itemsCh) {
					chineseNames.add(item);
				}
				
				List<String> dupucateStr = new ArrayList<>();
				if (!englishNames.isEmpty()) {
					List<Patent> addlist = ServiceUSPatent.getPatentRightByAssignee(englishNames, dupucateStr);
					list.addAll(addlist);
					addlist = ServiceTaiwanPatent.getPatentRightByAssignee(englishNames, dupucateStr);
					list.addAll(addlist);
				} 
				if (!chineseNames.isEmpty()) {
					List<Patent> addlist = ServiceTaiwanPatent.getPatentRightByAssignee(chineseNames, dupucateStr);
					list.addAll(addlist);
					addlist = ServiceChinaPatent.getPatentRightByAssignee(chineseNames, dupucateStr);
					list.addAll(addlist);
				}
				
				for (Patent patent:list) {
					patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
					patent.setAdmin(adminDao.getById(adminId));
					patent.addBusiness(business);
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
								
								patentDao.create(patent);
								taskResult = Constants.INT_SUCCESS;
							} else {
								if(StringUtils.isNULL(patent.getPatent_id())) {
									patent.setPatent_id(appNoPatent.getPatent_id());
								}
								
								taskResult = updatePatent(patent);
							}
						}
					} else {
						
						taskResult = Constants.INT_CANNOT_FIND_DATA;
					}
				}
			}
		}
		return taskResult;
	}
	
	
	@Override
	public int addPatentByApplNo(Patent patent) {
		int taskResult= -1;
		
		if (patent.getPatent_appl_no().length() == 10 && 
				Constants.APPL_COUNTRY_TW.equals(patent.getPatent_appl_country())) {
			patent.setPatent_appl_no(patent.getPatent_appl_no().substring(2));
		}
		
		if (patent.getPatent_appl_no().length() == 8 && 
				Constants.APPL_COUNTRY_TW.equals(patent.getPatent_appl_country())) {
			patent.setPatent_appl_no("0"+patent.getPatent_appl_no());
		}
		
		if (patent.getPatent_appl_no().length() == 11 && 
				Constants.APPL_COUNTRY_TW.equals(patent.getPatent_appl_country())) {
			patent.setPatent_appl_no(patent.getPatent_appl_no().substring(2));
		}
		
		//查詢台灣專利
		if (patent.getPatent_appl_no().length() == 9 && 
				Constants.APPL_COUNTRY_TW.equals(patent.getPatent_appl_country())) {
			ServiceTaiwanPatent.getPatentRightByApplNo(patent);
		}else if (patent.getPatent_appl_no().length() == 10 && 
				Constants.APPL_COUNTRY_US.equals(patent.getPatent_appl_country())) {
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
			insertEditHistory(dbBean,patent);

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
//			mappingAssignee(dbBean,patent);
//			mappingApplicant(dbBean,patent);
//			mappingInventor(dbBean,patent);
			
			//TODO Leo edit
			
//			log.info("contact :"+patent.getListContact().size());
//			dbBean.setListContact(patent.getListContact());
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
	public List<Patent> getExcelByPatentIds(List<String> idList,String businessId){
		List<Patent> patentList = patentDao.getByPatentIds(idList,businessId);
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
				String[] searchDateObj = ((String) searchObj).split("-");
				Date sd = new Date(Long.valueOf(searchDateObj[0]));
				Date ed = new Date(Long.valueOf(searchDateObj[1]));
				list = patentDao.searchFieldPatent(sd, ed, "patent_appl_date", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent(sd, ed, "patent_appl_date", businessId);
			} else if (Constants.PATENT_NOTICE_NO_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_notice_no", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_notice_no", businessId);
			} else if (Constants.PATENT_NOTICE_DATE_FIELD.equals(field.getField_id())) {
				String[] searchDateObj = ((String) searchObj).split("-");
				Date sd = new Date(Long.valueOf(searchDateObj[0]));
				Date ed = new Date(Long.valueOf(searchDateObj[1]));
				list = patentDao.searchFieldPatent(sd, ed, "patent_notice_date", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent(sd, ed, "patent_notice_date", businessId);
			} else if (Constants.PATENT_PUBLISH_NO_FIELD.equals(field.getField_id())) {
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', "patent_publish_no", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent('%'+text+'%', "patent_publish_no", businessId);
			} else if (Constants.PATENT_PUBLISH_DATE_FIELD.equals(field.getField_id())) {
				String[] searchDateObj = ((String) searchObj).split("-");
				Date sd = new Date(Long.valueOf(searchDateObj[0]));
				Date ed = new Date(Long.valueOf(searchDateObj[1]));
				list = patentDao.searchFieldPatent(sd, ed, "patent_publish_date", businessId, page, Constants.SYSTEM_PAGE_SIZE);
				count = patentDao.countSearchFieldPatent(sd, ed, "patent_publish_date", businessId);
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
			}
		}
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
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
				String sourceField = null;
				String newField = null;
				if (dbBean.getPatent_appl_date() != null) {
					sourceField = DateUtils.getSimpleSlashFormatDate(dbBean.getPatent_appl_date());
				}
				if (dbBean.getPatent_appl_date() != null) {
					newField = DateUtils.getSimpleSlashFormatDate(patent.getPatent_appl_date());
				}
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_NOTICE_DATE_FIELD.equals(field.getField_id())) {
				String sourceField = null;
				String newField = null;
				if (dbBean.getPatent_notice_date() != null) {
					sourceField = DateUtils.getSimpleSlashFormatDate(dbBean.getPatent_notice_date());
				}
				if (patent.getPatent_notice_date() != null) {
					newField = DateUtils.getSimpleSlashFormatDate(patent.getPatent_notice_date());
				}
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
			}
			if (Constants.PATENT_PUBLISH_DATE_FIELD.equals(field.getField_id())) {
				String sourceField = null;
				String newField = null;
				if (dbBean.getPatent_publish_date() != null) {
					sourceField = DateUtils.getSimpleSlashFormatDate(dbBean.getPatent_publish_date());
				}
				if (dbBean.getPatent_publish_date() != null) {
					newField = DateUtils.getSimpleSlashFormatDate(patent.getPatent_publish_date());
				}
				PatentEditHistory peh = checkFieldName(patent, sourceField, newField, field.getField_id());
				if (peh != null) {dbBean.addHistory(peh);}
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
			
			if (Constants.ASSIGNEE_NAME_FIELD.equals(field.getField_id())) {
				//add
				List<String> assigneeAddData = new ArrayList<>();
				HashMap<String, Assignee> mapping = new HashMap<String, Assignee>();
				for (Assignee assignee:patent.getListAssignee()) {
					if (assignee.getAssignee_id() == null) {
						assigneeAddData.add(JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class));
					} else {
						mapping.put(assignee.getAssignee_id(), assignee);
					}
				}
				if (!assigneeAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, assigneeAddData, "create", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
				List<String> assigneeUpdateData = new ArrayList<>();
				List<String> assigneeRemoveData = new ArrayList<>();
				for (Assignee assignee:dbBean.getListAssignee()) {
					if (mapping.containsKey(assignee.getAssignee_id())) {
						//update
						if (!assignee.getAssignee_name().equals(
								mapping.get(assignee.getAssignee_id()).getAssignee_name())
								|| !assignee.getAssignee_name_en().equals(
										mapping.get(assignee.getAssignee_id()).getAssignee_name_en())) {
							assigneeUpdateData.add(JacksonJSONUtils.mapObjectWithView(mapping.get(assignee.getAssignee_id()),  View.PatentDetail.class));
						}
					}else {
						assigneeRemoveData.add(JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class));
					}
				}
				if (!assigneeUpdateData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, assigneeUpdateData, "update", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
				if (!assigneeRemoveData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, assigneeRemoveData, "remove", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
			}
			if (Constants.APPLIANT_NAME_FIELD.equals(field.getField_id())) {
				//add
				List<String> applAddData = new ArrayList<>();
				HashMap<String, Applicant> mapping = new HashMap<String, Applicant>();
				for (Applicant appl:patent.getListApplicant()) {
					if (appl.getApplicant_id() == null) {
						applAddData.add(JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class));
					} else {
						mapping.put(appl.getApplicant_id(), appl);
					}
				}
				if (!applAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, applAddData, "create", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
				List<String> applUpdateData = new ArrayList<>();
				List<String> applRemoveData = new ArrayList<>();
				for (Applicant appl:dbBean.getListApplicant()) {
					if (mapping.containsKey(appl.getApplicant_id())) {
						//update
						if (!appl.getApplicant_name().equals(
								mapping.get(appl.getApplicant_id()).getApplicant_name())
								|| !appl.getApplicant_name_en().equals(
										mapping.get(appl.getApplicant_id()).getApplicant_name_en())) {
							applUpdateData.add(JacksonJSONUtils.mapObjectWithView(mapping.get(appl.getApplicant_id()),  View.PatentDetail.class));
						}
					}else {
						applRemoveData.add(JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class));
					}
				}
				if (!applUpdateData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, applUpdateData, "update", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
				if (!applRemoveData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, applRemoveData, "remove", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
			}
			if (Constants.IVENTOR_NAME_FIELD.equals(field.getField_id())) {
				//add
				List<String> invAddData = new ArrayList<>();
				HashMap<String, Inventor> mapping = new HashMap<String, Inventor>();
				for (Inventor inv:patent.getListInventor()) {
					if (inv.getInventor_id() == null) {
						invAddData.add(JacksonJSONUtils.mapObjectWithView(inv,  View.PatentDetail.class));
					} else {
						mapping.put(inv.getInventor_id(), inv);
					}
				}
				if (!invAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, invAddData, "create", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
				List<String> invUpdateData = new ArrayList<>();
				List<String> invRemoveData = new ArrayList<>();
				for (Inventor inv:dbBean.getListInventor()) {
					if (mapping.containsKey(inv.getInventor_id())) {
						//update
						if (!inv.getInventor_name().equals(
								mapping.get(inv.getInventor_id()).getInventor_name())
								|| !inv.getInventor_name_en().equals(
										mapping.get(inv.getInventor_id()).getInventor_name_en())) {
							invUpdateData.add(JacksonJSONUtils.mapObjectWithView(mapping.get(inv.getInventor_id()),  View.PatentDetail.class));
						}
					}else {
						invRemoveData.add(JacksonJSONUtils.mapObjectWithView(inv,  View.PatentDetail.class));
					}
				}
				if (!invUpdateData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, invUpdateData, "update", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
				if (!invRemoveData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, invRemoveData, "remove", field.getField_id());
					if (peh != null) {dbBean.addHistory(peh);}
				}
			}
		
		}
	}
	
	private PatentEditHistory insertFieldHistory(Patent patent, List<String> historyDataList, String status, String fieldId) {
		Date now = new Date();
		PatentEditHistory peh = new PatentEditHistory();
		peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
		peh.setPatent(patent);
		peh.setField_id(fieldId);
		peh.setHistory_data(Arrays.toString(historyDataList.toArray()));
		peh.setHistory_status(status);
		peh.setAdmin(patent.getAdmin());
		peh.setAdmin_ip(patent.getAdmin_ip());
		peh.setCreate_date(now);
		return peh;
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
	
	private  void handleCost(Patent dbPatent,Patent editPatent,List<PatentEditHistory> listHistory) {
		
		
	}
	
	


	
}
