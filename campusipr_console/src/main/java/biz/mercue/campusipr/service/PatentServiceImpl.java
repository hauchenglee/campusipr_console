package biz.mercue.campusipr.service;



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


import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.ApplicantDao;
import biz.mercue.campusipr.dao.AssigneeDao;
import biz.mercue.campusipr.dao.BusinessDao;
import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.dao.IPCClassDao;
import biz.mercue.campusipr.dao.InventorDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.PatentEditHistoryDao;
import biz.mercue.campusipr.dao.PatentFamilyDao;
import biz.mercue.campusipr.dao.PatentStatusDao;
import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Annuity;
import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Country;
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
	private CountryDao countryDao;
	
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
	
	@Autowired
	private IPCClassDao ipcDao;

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
		getDisplayEditHistory(listpeh);
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

		if (StringUtils.isNULL(patent.getPatent_id())) {
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
		}

		if (patent.getBusiness() == null) {
			log.error("no business data");
			return Constants.INT_DATA_ERROR;
		}

		if (StringUtils.isNULL(patent.getPatent_appl_country())) {
			log.error("no applicant country");
			return Constants.INT_DATA_ERROR;
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

		String applNo = patent.getPatent_appl_no();

		if (!StringUtils.isNULL(applNo)) {
			Patent appNoPatent = patentDao.getByApplNo(applNo);
			if (appNoPatent != null) {
				List<Business> listBusiness = appNoPatent.getListBusiness();
				for (Business business : listBusiness) {
					if (patent.getBusiness().getBusiness_id().equals(business.getBusiness_id())) {
						return Constants.INT_DATA_DUPLICATE;
					}
				}
			}
		}
		patent.addBusiness(patent.getBusiness());

		List<Assignee> listAssignee = patent.getListAssignee();
		if (listAssignee != null && listAssignee.size() > 0) {
			for (Assignee assignee : listAssignee) {
				assignee.setAssignee_id(KeyGeneratorUtils.generateRandomString());
				assignee.setPatent(patent);
			}
		}

		List<Applicant> listApplicant = patent.getListApplicant();
		if (listApplicant != null && listApplicant.size() > 0) {
			for (Applicant mApplicant : listApplicant) {
				mApplicant.setApplicant_id(KeyGeneratorUtils.generateRandomString());
				mApplicant.setPatent(patent);
			}
		}

		List<Inventor> listInventor = patent.getListInventor();
		if (listInventor != null && listInventor.size() > 0) {
			for (Inventor mInventor : listInventor) {
				mInventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
				mInventor.setPatent(patent);
			}
		}
		
		List<Annuity> listAnnuity = patent.getListAnnuity();
		if (listAnnuity != null && listAnnuity.size() > 0) {
			for (Annuity mAnnuity : listAnnuity) {
				mAnnuity.setAnnuity_id(KeyGeneratorUtils.generateRandomString());
				mAnnuity.setPatent(patent);
			}
		}

		if (patent.getListIPC() != null) {
			for (IPCClass ipc : patent.getListIPC()) {
				IPCClass ipcDb = ipcDao.getByIdAndVersion(ipc.getIpc_class_id(), ipc.getIpc_version());
				if (ipcDb == null) {
					ipcDao.create(ipc);
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
	public int syncPatentsByApplicant(List<Patent> list, String adminId, String businessId, String ip) {
	          int taskResult= -1;
	          Business ownBusiness = businessDao.getById(businessId);
	          List<String> englishNames = new ArrayList<>();
	          if (!StringUtils.isNULL(ownBusiness.getBusiness_name_en())) {
	        	  englishNames.add(ownBusiness.getBusiness_name_en());
	          }
	          if (!StringUtils.isNULL(ownBusiness.getBusiness_alias_en())) {
	        	  String[] itemsEn = ownBusiness.getBusiness_alias_en().split(",");
	        	  for (String item:itemsEn) {
		              englishNames.add(item);
		          }
	          }
	                  
	          List<String> chineseNames = new ArrayList<>();
	          if (!StringUtils.isNULL(ownBusiness.getBusiness_name())) {
	        	  chineseNames.add(ownBusiness.getBusiness_name());
	          }
	          if (!StringUtils.isNULL(ownBusiness.getBusiness_alias())) {
		          String[] itemsCh = ownBusiness.getBusiness_alias().split(",");
		          for (String item:itemsCh) {
		               chineseNames.add(item);
		          }
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
	               patent.addBusiness(ownBusiness);
	               if(!StringUtils.isNULL(patent.getPatent_name()) || !StringUtils.isNULL(patent.getPatent_name_en())) {
	                   String applNo =  patent.getPatent_appl_no();
	                   if (!StringUtils.isNULL(applNo)) {
	                       Patent appNoPatent = patentDao.getByApplNo(applNo);
	                       if(appNoPatent==null) {
	                       	   this.addPatent(patent);
	                           taskResult = Constants.INT_SUCCESS;
	                       } else {
	                           patent.setComparePatent(appNoPatent);
	                           taskResult = updatePatent(patent);
	                       }
	                   }
	               } else {
	                   
	                   taskResult = Constants.INT_CANNOT_FIND_DATA;
	               }
	           }
	          return taskResult;
	}
	
	@Override
	public int addPatentByApplNo(Patent patent) {
		int taskResult= -1;
		
		if (patent.getPatent_appl_no().length() == 10 && 
                Constants.APPL_COUNTRY_TW.equals(patent.getPatent_appl_country())) {
            patent.setPatent_appl_no(patent.getPatent_appl_no().replace("TW", "").replace("tw", ""));
        }
        
        if (patent.getPatent_appl_no().length() == 11 && 
                Constants.APPL_COUNTRY_TW.equals(patent.getPatent_appl_country())) {
            patent.setPatent_appl_no(patent.getPatent_appl_no().replace("TW", "").replace("tw", ""));
        }
     
	     //查詢台灣專利
	    if ((patent.getPatent_appl_no().length() == 8 ||
	                patent.getPatent_appl_no().length() == 9) && 
	             Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
	         ServiceTaiwanPatent.getPatentRightByApplNo(patent);
	    }else if (patent.getPatent_appl_no().length() == 10 && 
	             Constants.APPL_COUNTRY_US.endsWith(patent.getPatent_appl_country())) {
	         ServiceUSPatent.getPatentRightByapplNo(patent);
	    }else {
	         ServiceChinaPatent.getPatentRightByApplicantNo(patent);
	    }
		
		if(!StringUtils.isNULL(patent.getPatent_name())|| !StringUtils.isNULL(patent.getPatent_name_en())) {
			String applNo =  patent.getPatent_appl_no();
			if (!StringUtils.isNULL(applNo)) {
				Patent appNoPatent = patentDao.getByApplNo(applNo);
                if(appNoPatent==null) {
                	this.addPatent(patent);
                    taskResult = Constants.INT_SUCCESS;
                } else {
                	patent.setComparePatent(appNoPatent);
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
	public int importPatent(List<Patent> list, Admin admin,Business business) {
		int taskResult= -1;
        for (Patent patent:list) {
            patent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
            patent.setAdmin(admin);
            patent.addBusiness(business);
            if(!StringUtils.isNULL(patent.getPatent_name()) || !StringUtils.isNULL(patent.getPatent_name_en())) {
                String applNo =  patent.getPatent_appl_no();
                if (!StringUtils.isNULL(applNo)) {
                    Patent appNoPatent = patentDao.getByApplNo(applNo);
                    if(appNoPatent==null) {
                    	this.addPatent(patent);
                        taskResult = Constants.INT_SUCCESS;
                    } else {
                    	patent.setComparePatent(appNoPatent);
                        taskResult = updatePatent(patent);
                    }
                }else {
                   this.addPatent(patent);
                }
            } else {
                
                taskResult = Constants.INT_CANNOT_FIND_DATA;
            }
        }
        
        //TODO how many success imported patent
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
		Patent dbBean = patent.getComparePatent();
		if(dbBean == null) {
			dbBean = patentDao.getById(patent.getPatent_id());
		}

		if(dbBean!=null){
			
			//TODO save edit history
			comparePatent(dbBean,patent);

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
					if (StringUtils.isNULL(patent.getPatentAbstract().getPatent_abstract_id())) {
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
					if (StringUtils.isNULL(patent.getPatentClaim().getPatent_claim_id())) {
						patent.getPatentClaim().setPatent_claim_id(KeyGeneratorUtils.generateRandomString());
					}
					patent.getPatentClaim().setPatent(patent);
					dbBean.setPatentClaim(patent.getPatentClaim());
				}
			}
			
			if (patent.getPatentDesc() != null) {
				String descStr = patent.getPatentDesc().getContext_desc();
				if (dbBean.getPatentDesc() != null) {
					PatentDescription pdDb = dbBean.getPatentDesc();
					pdDb.setContext_desc(descStr);
				} else {
					if (StringUtils.isNULL(patent.getPatentDesc().getPatent_desc_id())) {
						patent.getPatentDesc().setPatent_desc_id(KeyGeneratorUtils.generateRandomString());
					}
					patent.getPatentDesc().setPatent(patent);
					patent.getPatentDesc().setContext_desc(descStr);
					dbBean.setPatentDesc(patent.getPatentDesc());
				}
			}
			
			if (patent.getListIPC() != null) {
                for (IPCClass ipc:patent.getListIPC()) {
             	   IPCClass ipcDb = ipcDao.getByIdAndVersion(ipc.getIpc_class_id(), ipc.getIpc_version());
             	   if (ipcDb == null) {
             		   ipcDao.create(ipc);
             	   }
                }
                
                handleIPC(dbBean, patent);
            }
			
			//TODO charles 
//			mappingAssignee(dbBean,patent);
//			mappingApplicant(dbBean,patent);
//			mappingInventor(dbBean,patent);
			
			//TODO Leo edit
			handleCost(dbBean, patent);
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
	public 	ListQueryForm getByBusinessId(String businessId,int page,String orderFieldId,int is_asc) {
		log.info("businessId:"+businessId);
		
		PatentField orderField = null;
		String orderList = null;
		String orderFieldCode = null;
		if(!StringUtils.isNULL(orderFieldId)) {
			orderField = fieldDao.getById(orderFieldId);
			if(orderField!=null) {
				orderFieldCode = orderField.getField_code();
			}
		}
		switch(orderFieldId) {
		case Constants.PATENT_COUNTRY_FIELD:
			orderFieldCode = "patent_appl_country";
			break;
		case Constants.SCHOOL_NO_FIELD:
			orderList = "listExtension";
			break;
		case Constants.PATENT_FAMILY_FIELD:
			orderFieldCode = "family";
			break;
		case Constants.PATENT_STATUS_FIELD:
			orderList = "listStatus";
			orderFieldCode = "status_desc";
			break;
		default:
			break;
		}
		
		List<Patent> list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE, orderList, orderFieldCode,is_asc);
		
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
			patent.getListBusiness().size();
			patent.getListStatus().size();
			patent.getListExtension().size();
		}
		return list;
	}

	@Override
	public ListQueryForm fieldSearchPatent(Object searchObj, String fieldId, String businessId, int page,String orderFieldId,int is_asc) {
		//check text is date or not
		PatentField field = fieldDao.getById(fieldId);
		PatentField orderField = null;
		String orderList = null;
		String orderFieldCode = null;
		if(!StringUtils.isNULL(orderFieldId)) {
			orderField = fieldDao.getById(orderFieldId);
			if(orderField!=null) {
				orderFieldCode = orderField.getField_code();
			}
		}
		switch(orderFieldId) {
		case Constants.PATENT_COUNTRY_FIELD:
			orderFieldCode = "patent_appl_country";
			break;
		case Constants.SCHOOL_NO_FIELD:
			orderList = "listExtension";
			break;
		case Constants.PATENT_FAMILY_FIELD:
			orderFieldCode = "family";
			break;
		case Constants.PATENT_STATUS_FIELD:
			orderList = "listStatus";
			orderFieldCode = "status_desc";
			break;
		default:
			break;
		}
		log.info("fieldId:"+fieldId);
		log.info("orderFieldCode:"+orderFieldCode);
		int count = 0;
		List<Patent> list = new ArrayList<>();
		if (Constants.PATENT_ALL_FIELD.equals(fieldId) || field == null) {
			String text = (String) searchObj;
			list = patentDao.searchAllFieldPatent('%'+text+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList, orderFieldCode, is_asc);
			count = patentDao.countSearchAllFieldPatent('%'+text+'%', businessId);
		}else {
			
			switch (fieldId) {
			case Constants.PATENT_NAME_FIELD:
			case Constants.PATENT_NAME_EN_FIELD:
			case Constants.PATENT_NO_FIELD:	
			case Constants.PATENT_APPL_NO_FIELD:
			case Constants.PATENT_NOTICE_NO_FIELD:
			case Constants.PATENT_PUBLISH_NO_FIELD:
				String text = (String) searchObj;
				list = patentDao.searchFieldPatent('%'+text+'%', field.getField_code(), businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList,orderFieldCode,is_asc);
				count = patentDao.countSearchFieldPatent('%'+text+'%', field.getField_code(), businessId);
				break;
			case Constants.PATENT_COUNTRY_FIELD:
				String countryName = (String) searchObj;
				List<Country> countryList = countryDao.getListByFuzzy(countryName);
				List<String> coutryIdList = new ArrayList<>();
				for (Country country:countryList) {
					if (!coutryIdList.contains(country.getCountry_id())) {
						coutryIdList.add(country.getCountry_id());
					}
				}
				log.info(coutryIdList);
				if (!coutryIdList.isEmpty()) {
					list = patentDao.searchFieldCountryPatent(coutryIdList, field.getField_code(), businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList, orderFieldCode, is_asc);
					count = patentDao.countSearchFieldCountryPatent(coutryIdList, field.getField_code(), businessId);
				}
				break;
				
				
			case Constants.PATENT_APPL_DATE_FIELD:
			case Constants.PATENT_PUBLISH_DATE_FIELD:
			case Constants.PATENT_NOTICE_DATE_FIELD:
				String[] searchDateObj = ((String) searchObj).split("-");
				Date sd = new Date(Long.valueOf(searchDateObj[0]));
				Date ed = new Date(Long.valueOf(searchDateObj[1]));
				list = patentDao.searchFieldPatent(sd, ed, field.getField_code(), businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList,orderFieldCode,is_asc);
				count = patentDao.countSearchFieldPatent(sd, ed, field.getField_code(), businessId);
				break;
				
			case Constants.ASSIGNEE_NAME_FIELD:
			case Constants.APPLIANT_NAME_FIELD:
			case Constants.INVENTOR_NAME_FIELD:
				String name = (String) searchObj;
				list = patentDao.searchFieldHumanListPatent('%'+name+'%',field.getField_code(), businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList,orderFieldCode,is_asc);
				count = patentDao.countSearchFieldHumanListPatent('%'+name+'%',field.getField_code(), businessId);
				break;
				
			case Constants.PATENT_STATUS_FIELD:
				String status = (String) searchObj;
				log.info("status:"+status);
				list = patentDao.searchFieldStatusListPatent('%'+status+'%', businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList,orderFieldCode,is_asc);
				count = patentDao.countSearchFieldStatusPatent('%'+status+'%', businessId);
				break;
				
			case Constants.SCHOOL_NO_FIELD:
				String num = (String) searchObj;
				list = patentDao.searchFieldExtensionListPatent('%'+num+'%',field.getField_code(), businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList,orderFieldCode,is_asc);
				count = patentDao.countSearchFieldExtensionPatent('%'+num+'%', field.getField_code(), businessId);
				break;

				
				

			default:
				list = new ArrayList<Patent>();
				count = 0;
				break;
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
	

	@Override
	public List<Status> getEditStatus(){
		return statusDao.getEditable();
	}
	
//	private void mappingInventor(Patent dbBean,Patent patent) {
//		List<Inventor> mapInventor = dbBean.getListInventor();
//		dbBean.setListInventor(null);
//		for (Inventor inventor:mapInventor) {
//			inventorDao.delete(inventor.getInventor_id());
//		}
//		
//		if (patent.getListInventor() != null) {
//			for (Inventor inventor:patent.getListInventor()) {
//				if (StringUtils.isNULL(inventor.getInventor_id())) {
//					inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
//				}
//				inventor.setPatent(patent);
//				dbBean.addInventor(inventor);
//			}
//		}
//	}
//	
//	private void mappingApplicant(Patent dbBean,Patent patent) {
//		List<Applicant> mapApplicant = dbBean.getListApplicant();
//		dbBean.setListApplicant(null);
//		for (Applicant appl:mapApplicant) {
//			applicantDao.delete(appl.getApplicant_id());
//		}
//		if (patent.getListApplicant() != null) {
//			for (Applicant appl:patent.getListApplicant()) {
//				if (StringUtils.isNULL(appl.getApplicant_id())) {
//					appl.setApplicant_id(KeyGeneratorUtils.generateRandomString());
//				}
//				appl.setPatent(patent);
//				dbBean.addApplicant(appl);
//			}
//		}
//	}
	
//	private void mappingAssignee(Patent dbBean,Patent patent) {
//		List<Assignee> mapAssignee = dbBean.getListAssignee();
//		dbBean.setListAssignee(null);
//		for (Assignee assign:mapAssignee) {
//			assigneeDao.delete(assign.getAssignee_id());
//		}
//		if (patent.getListAssignee() != null) {
//			for (Assignee assign:patent.getListAssignee()) {
//				if (StringUtils.isNULL(assign.getAssignee_id())) {
//					assign.setAssignee_id(KeyGeneratorUtils.generateRandomString());
//				}
//				assign.setPatent(patent);
//				dbBean.addAssignee(assign);
//			}
//		}
//	}
	

	private void comparePatent(Patent dbBean,Patent patent) {
		List<PatentField> fieldList = fieldDao.getAllFields();
		for (PatentField field:fieldList) {
			if (Constants.PATENT_NAME_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_name();
				String newField = patent.getPatent_name();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_name(newField);
				}
			}
			if (Constants.PATENT_NAME_EN_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_name_en();
				String newField = patent.getPatent_name_en();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_name_en(newField);
				}
			}
			if (Constants.PATENT_COUNTRY_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_appl_country();
				String newField = patent.getPatent_appl_country();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_appl_country(newField);
				}
			}
			if (Constants.PATENT_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_no();
				String newField = patent.getPatent_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_no(newField);
				}
			}
			if (Constants.PATENT_APPL_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_appl_no();
				String newField = patent.getPatent_appl_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_appl_no(newField);
				}
			}
			if (Constants.PATENT_NOTICE_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_notice_no();
				String newField = patent.getPatent_notice_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_notice_no(newField);
				}
			}
			if (Constants.PATENT_PUBLISH_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_publish_no();
				String newField = patent.getPatent_publish_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_publish_no(newField);
				}
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
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_appl_date(patent.getPatent_appl_date());
				}
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
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_notice_date(patent.getPatent_notice_date());
				}
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
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id());
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_publish_date(patent.getPatent_publish_date());
				}
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
			if (Constants.INVENTOR_NAME_FIELD.equals(field.getField_id())) {
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
	
	private PatentEditHistory checkFieldValue(Patent patent, String sourceData, String newData, String fieldId) {
		Date now = new Date();
		PatentEditHistory peh = null;
		if (newData != null) {
			if (sourceData == null) {
				peh = new PatentEditHistory();
				peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
				peh.setPatent(patent);
				peh.setField_id(fieldId);
				peh.setHistory_data(newData);
				peh.setHistory_status("insert");
				peh.setAdmin(patent.getAdmin());
				peh.setAdmin_ip(patent.getAdmin_ip());
				peh.setCreate_date(now);
			} else {
				if (!sourceData.equals(newData)) {
					peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setPatent(patent);
					peh.setField_id(fieldId);
					peh.setHistory_data(newData);
					peh.setHistory_status("update");
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setCreate_date(now);
				}
			}
		} else {
			if (Patent.EDIT_SOURCE_HUMAN == patent.getEdit_source()) {
				if (sourceData != null) {
					peh = new PatentEditHistory();
					peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
					peh.setPatent(patent);
					peh.setField_id(fieldId);
					peh.setHistory_data(sourceData);
					peh.setHistory_status("remove");
					peh.setAdmin(patent.getAdmin());
					peh.setAdmin_ip(patent.getAdmin_ip());
					peh.setCreate_date(now);
				}
			}else {
				log.info("ignore update  when import or sync from service");
			}
		}
		return peh;
	}
	
	private void handleCost(Patent dbPatent, Patent editPatent) {
		if (editPatent.getListCost() != null && editPatent.getListCost().size() > 0) {
			List<PatentCost> listCost = editPatent.getListCost();
			for (PatentCost cost : listCost) {
				if (StringUtils.isNULL(cost.getCost_id())) {
					cost.setCost_id(KeyGeneratorUtils.generateRandomString());
				}
				cost.setPatent(dbPatent);
			}
			patentDao.deletePatentCost(dbPatent.getPatent_id());
			dbPatent.setListCost(editPatent.getListCost());
		}
	}
	 
	 
	 private  void handleIPC(Patent dbBean, Patent editPatent) {
		 if ( editPatent.getListIPC()!= null && editPatent.getListIPC().size() > 0) {
//				List<PatentCost> listCost = editPatent.getListCost();
//				for (PatentCost cost : listCost) {
//					if (StringUtils.isNULL(cost.getCost_id())) {
//						cost.setCost_id(KeyGeneratorUtils.generateRandomString());
//					}
//					cost.setPatent(dbPatent);
//				}
//				patentDao.deletePatentCost(dbPatent.getPatent_id());
//				dbPatent.setListCost(editPatent.getListCost());
		 }
	 }
	   
	
	
	
	private void getDisplayEditHistory(List<PatentEditHistory> list) {
		for(PatentEditHistory history : list) {
			String fieldId = history.getField_id();
			if(!StringUtils.isNULL(fieldId)) {
				switch (fieldId) {
				case Constants.PATENT_APPL_DATE_FIELD:
				case Constants.PATENT_NOTICE_DATE_FIELD:
				case Constants.PATENT_PUBLISH_DATE_FIELD:
				case Constants.PATENT_NAME_EN_FIELD:
				case Constants.PATENT_NAME_FIELD:
				case Constants.PATENT_NO_FIELD:
				case Constants.PATENT_NOTICE_NO_FIELD:
				case Constants.PATENT_PUBLISH_NO_FIELD:
				case Constants.SCHOOL_NO_FIELD:
				case Constants.SCHOOL_APPL_YEAR_FIELD:
				case Constants.PATENT_MEMO:
					history.setDisplay_data(history.getHistory_data());
					history.setDisplay_data_en(history.getHistory_data());
					break;
				case Constants.INVENTOR_NAME_FIELD:

					List<Inventor> listInventor = (List<Inventor>) JacksonJSONUtils.readValue(history.getHistory_data(), new TypeReference<List<Inventor>>(){});
					if(listInventor!=null && listInventor.size() > 0 ) {
						List<String> name1 = new ArrayList<String>();
						List<String> name2 = new ArrayList<String>();
						for(Inventor inventor : listInventor) {
							name1.add(inventor.getInventor_name());
							name2.add(inventor.getInventor_name_en());
						}
						String result1 = String.join("\n", name1);
						String result2 = String.join("\n", name2);
						history.setDisplay_data(result1);
						history.setDisplay_data_en(result2);
					}
					break;
				case Constants.ASSIGNEE_NAME_FIELD:

					List<Assignee> listAssignee = (List<Assignee>) JacksonJSONUtils.readValue(history.getHistory_data(), new TypeReference<List<Assignee>>(){});
					if(listAssignee!=null && listAssignee.size() > 0 ) {
						List<String> name1 = new ArrayList<String>();
						List<String> name2 = new ArrayList<String>();
						for(Assignee assignee : listAssignee) {
							name1.add(assignee.getAssignee_name());
							name2.add(assignee.getAssignee_name_en());
						}
						String result1 = String.join("\n", name1);
						String result2 = String.join("\n", name2);
						history.setDisplay_data(result1);
						history.setDisplay_data_en(result2);
					}
					break;
				case Constants.APPLIANT_NAME_FIELD:
				
					List<Applicant> listApplicant = (List<Applicant>) JacksonJSONUtils.readValue(history.getHistory_data(), new TypeReference<List<Applicant>>(){});
					if(listApplicant!=null && listApplicant.size() > 0 ) {
						List<String> name1 = new ArrayList<String>();
						List<String> name2 = new ArrayList<String>();
						for(Applicant applicant : listApplicant) {
							name1.add(applicant.getApplicant_name());
							name2.add(applicant.getApplicant_name_en());
						}
						String result1 = String.join("\n", name1);
						String result2 = String.join("\n", name2);
						history.setDisplay_data(result1);
						history.setDisplay_data_en(result2);
					}
					break;

				default:
					break;
				}
			}
			
		}
	}
	
	


	
}
