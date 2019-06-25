package biz.mercue.campusipr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.campusipr.dao.AdminDao;
import biz.mercue.campusipr.dao.AnnuityReminderDao;
import biz.mercue.campusipr.dao.BusinessDao;
import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.dao.IPCClassDao;
import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.PatentEditHistoryDao;
import biz.mercue.campusipr.dao.PatentFamilyDao;
import biz.mercue.campusipr.dao.PatentStatusDao;
import biz.mercue.campusipr.dao.ReminderDao;
import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.model.Admin;
import biz.mercue.campusipr.model.Annuity;
import biz.mercue.campusipr.model.AnnuityReminder;
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
import biz.mercue.campusipr.model.PatentContact;
import biz.mercue.campusipr.model.PatentCost;
import biz.mercue.campusipr.model.PatentDescription;
import biz.mercue.campusipr.model.PatentEditHistory;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.model.PatentField;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.Portfolio;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
import biz.mercue.campusipr.util.JacksonJSONUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.MailSender;
import biz.mercue.campusipr.util.ServiceChinaPatent;
import biz.mercue.campusipr.util.ServiceStatusPatent;
import biz.mercue.campusipr.util.ServiceTaiwanPatent;
import biz.mercue.campusipr.util.ServiceUSPatent;
import biz.mercue.campusipr.util.StringUtils;




@Service("patentService")
@Transactional
public class PatentServiceImpl implements PatentService {
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
	private PatentEditHistoryDao pehDao;

	@Autowired
	private PatentFamilyDao familyDao;
	
	@Autowired
	private FieldDao fieldDao;
	
	@Autowired
	private IPCClassDao ipcDao;

	@Autowired
	private ReminderDao reminderDao;

	@Autowired
	private QuartzService quartzService;

	@Autowired
	private AnnuityReminderDao annuityReminderDao;
	
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
			patent.getListPatentStatus().size();
			patent.getListIPC().size();
			patent.getListAgent().size();
			patent.getListApplicant().size();
			patent.getListAssignee().size();
			patent.getListContact().size();
			patent.getListCost().size();
			patent.getListInventor().size();
			patent.getListPortfolio().size();
			patent.getListAnnuity().size();
		}
		return patent;
	}
	
	@Override
	public ListQueryForm getHistoryBypatentId(String businessId,String patentId,String fieldId,int page) {
		List<PatentEditHistory> listpeh = pehDao.getByPatentAndField(patentId, fieldId, businessId,page,Constants.SYSTEM_PAGE_SIZE);
		getDisplayEditHistory(listpeh);
		int count = pehDao.countByPatentAndField(patentId, fieldId, businessId);
		ListQueryForm form = new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, listpeh);

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
			String context_desc_all = patent.getPatentDesc().getContext_desc();
			patent.getPatentDesc().setPatent_desc_id(KeyGeneratorUtils.generateRandomString());
			
			if (context_desc_all.length() > 5000) {
				String context_desc_5000 = context_desc_all.substring(0, 5000);
				context_desc_5000 += "...(完整內容請由官方專利局取得)";
				patent.getPatentDesc().setContext_desc(context_desc_5000);
			}

			patent.getPatentDesc().setPatent(patent);
		}

		if (patent.getListPatentStatus() != null) {
			for (PatentStatus ps:patent.getListPatentStatus()) {
				Status status = ps.getStatus();
				Status statusDb = null;
				if (patent.getEdit_source() == Patent.EDIT_SOURCE_SERVICE) {
					statusDb = statusDao.getByEditCode(status.getStatus_desc());
				}else {
					statusDb = statusDao.getByEditCode(status.getStatus_desc());
				}
         	   	if (statusDb == null) {
         	   		status.setStatus_id(KeyGeneratorUtils.generateRandomString());
         	   		statusDao.create(status);
         	   	} else {
    				ps.setStatus(statusDb);
         	   	}
				ps.setPatent(patent);
				log.info(ps.getPrimaryKey());
			}
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
				IPCClass ipcDb = ipcDao.getByIdAndVersion(ipc.getIpc_class_id());
				if (ipcDb == null) {
					ipcDao.create(ipc);
				}
			}
		}

		if (Patent.EDIT_SOURCE_SERVICE == patent.getEdit_source()) {
			patent.setIs_public(true);
			patent.setIs_sync(true);
		}

		// 同步時同時新增預設聯絡人
		if (Patent.EDIT_SOURCE_SERVICE == patent.getEdit_source()) {
			if (patent.getBusiness() != null) {
				Business business = patent.getBusiness();
				if (!StringUtils.isNULL(business.getContact_name()) ||
						!StringUtils.isNULL(business.getContact_email()) ||
								!StringUtils.isNULL(business.getContact_phone())) {
					PatentContact pContact = new PatentContact();
					pContact.setPatent_contact_id(KeyGeneratorUtils.generateRandomString());
					pContact.setPatent(patent);
					pContact.setBusiness(business);
					pContact.setCreate_date(new Date());
					pContact.setContact_name(business.getContact_name());
					pContact.setContact_email(business.getContact_email());
					pContact.setContact_phone(business.getContact_phone());
					pContact.setContact_character("聯絡人");
					pContact.setContact_order(0);
					patent.addContact(pContact);
				}
			}
		}

		// 自動新增聯絡人資料
		if (StringUtils.isNULL(applNo) || StringUtils.isNULL(patent.getPatent_name())) {
			log.info("無申請號或有申請號無資料時新增聯絡人，如果在資料庫搜尋時找不到申請號，請改用相似(like)查詢");
			contactData(patent);
		}
				
		handleReminder(patent, patent.getListBusiness());
		patentDao.create(patent);
		return Constants.INT_SUCCESS;
	}

	private void contactData(Patent patent) {
		if (patent.getBusiness() != null) {
			Business business = patent.getBusiness();
			PatentContact pContact = new PatentContact();
			if (!StringUtils.isNULL(business.getContact_name()) || !StringUtils.isNULL(business.getContact_email())
					|| !StringUtils.isNULL(business.getContact_phone())) {
				pContact.setPatent_contact_id(KeyGeneratorUtils.generateRandomString());
				pContact.setPatent(patent);
				pContact.setBusiness(business);
				pContact.setCreate_date(new Date());
				pContact.setContact_name(business.getContact_name());
				pContact.setContact_email(business.getContact_email());
				pContact.setContact_phone(business.getContact_phone());
				pContact.setContact_character("聯絡人");
				pContact.setContact_order(0);
				patent.addContact(pContact);
			}
		}
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
	          //02/23更新停止同步api狀態資料
//	  		  ServiceStatusPatent.syncListPatentStatus(list);
	          
	          log.info("start sync");
	        //check exit patent and sync
	          List<Patent> listSyncPatent = patentDao.getByNotSyncPatent(businessId);
	          log.info(listSyncPatent.get(listSyncPatent.size()-1).getPatent_appl_no());
	          for (Patent syncPatent:listSyncPatent) {
	        	  log.info("next_patent:"+syncPatent.getPatent_appl_no());
	        	  if (!dupucateStr.contains(syncPatent.getPatent_appl_no())) {
	        		  	log.info("no_duplicate_next_patent:"+syncPatent.getPatent_appl_no());
	        		  	Patent patent = new Patent();
	        		  	patent.setPatent_appl_no(syncPatent.getPatent_appl_no());
	        		  	patent.setPatent_appl_country(syncPatent.getPatent_appl_country());
	        		  	if ((patent.getPatent_appl_no().length() == 8 ||
	        		  			patent.getPatent_appl_no().length() == 9) && 
	        		  			Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country())) {
			      	         ServiceTaiwanPatent.getPatentRightByApplNo(patent);
			      	    }else if (patent.getPatent_appl_no().length() == 8 && 
			      	             Constants.APPL_COUNTRY_US.endsWith(patent.getPatent_appl_country())) {
			      	         ServiceUSPatent.getPatentRightByapplNo(patent);
			      	    }else {
			      	         ServiceChinaPatent.getPatentRightByApplicantNo(patent);
			      	    }
	        		  	log.info("add patent no:"+patent.getPatent_appl_no());
	        		  	list.add(patent);
	        		  	dupucateStr.add(patent.getPatent_appl_no());
	        	  }
	          }
	          
	          log.info("update to db");
	          
	          for (Patent patent:list) {
	        	   syncPatentStatus(patent);
	               patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
	               patent.setAdmin(adminDao.getById(adminId));
	               patent.setBusiness(ownBusiness);
	               patent.setSync_date(DateUtils.getDayStart(new Date()));
	               if(!StringUtils.isNULL(patent.getPatent_name()) || !StringUtils.isNULL(patent.getPatent_name_en())) {
	                   String applNo =  patent.getPatent_appl_no();
	                   if (!StringUtils.isNULL(applNo)) {
	                       Patent appNoPatent = patentDao.getByApplNo(applNo);
	                       if(appNoPatent==null) {
	                       	   this.addPatent(patent);
	                           taskResult = Constants.INT_SUCCESS;
	                       } else {
	                           patent.setComparePatent(appNoPatent);
	                           taskResult = updatePatent(patent, null);
	                       }
	                   }
	               } else {
	                   
	                   taskResult = Constants.INT_CANNOT_FIND_DATA;
	               }
	           }
	          
	          return taskResult;
	}

	@Override
	public int syncPatentData(Patent patent) {
		log.info("syncPatentData: ");

		if (patent == null) {
			return Constants.INT_DATA_ERROR;
		}

		int syncResult = Constants.INT_SYSTEM_PROBLEM;

		// cn patent appl no
		String originApplNo = patent.getPatent_appl_no();
		String appl_cn_onlyNo = "";
		int indexOfDot = originApplNo.indexOf(".");

		if (indexOfDot != -1) {
			appl_cn_onlyNo = originApplNo.substring(2, indexOfDot);
		} else {
			appl_cn_onlyNo = originApplNo.substring(2, originApplNo.length());
		}

		String appl_indexOf0to4 = appl_cn_onlyNo.substring(0, 5);
		String appl_indexOf5toEnd = appl_cn_onlyNo.substring(5, appl_cn_onlyNo.length());
		String changeApplNo = "";

		// us patent appl no
		String appl_us_onlyNO = originApplNo.replace("/", "").replace(",", "");
		log.info("appl_us_onlyNO: " + appl_us_onlyNO);
		
		
		if ((Constants.APPL_COUNTRY_TW.endsWith(patent.getPatent_appl_country()))) {
			if (originApplNo.length() == 10 || originApplNo.length() == 11) {
				syncResult = ServiceTaiwanPatent.getPatentRightByApplNo(patent);
			} else {
				return Constants.INT_DATA_ERROR;
			}
		}

		if (Constants.APPL_COUNTRY_US.endsWith(patent.getPatent_appl_country())) {
			if (originApplNo.length() == 10 || originApplNo.length() == 12) {
				patent.setPatent_appl_no(appl_us_onlyNO);
				log.info(patent == null);
				syncResult = ServiceUSPatent.getPatentRightByapplNo(patent);
			} else {
				return Constants.INT_DATA_ERROR;
			}
		}

		if (Constants.APPL_COUNTRY_CN.equals(patent.getPatent_appl_country())) {
			if (appl_cn_onlyNo.length() == 12) {
				changeApplNo = "CN" + appl_cn_onlyNo;
			}
			if (appl_cn_onlyNo.length() == 11) {
				changeApplNo = "CN" + appl_indexOf0to4 + "0" + appl_indexOf5toEnd;
			}
			if (appl_cn_onlyNo.length() == 10) {
				changeApplNo = "CN" + appl_indexOf0to4 + "00" + appl_indexOf5toEnd;
			}
			if (appl_cn_onlyNo.length() == 8) {
				changeApplNo = "CN" + appl_cn_onlyNo;
			}

			patent.setPatent_appl_no(changeApplNo);
			syncResult = ServiceChinaPatent.parseBilbo_byApplication(patent);

			patent.setPatent_appl_no(originApplNo);
		}

		// 02/23更新停止同步api狀態資料
		// ServiceStatusPatent.getPatentStatus(patent);
		syncPatentStatus(patent);

		if (syncResult == Constants.INT_SUCCESS) {
			if (patent.getPatentDesc() != null) {
				String context_desc_all = patent.getPatentDesc().getContext_desc();
				patent.getPatentDesc().setPatent_desc_id(KeyGeneratorUtils.generateRandomString());

				if (context_desc_all.length() > 5000) {
					String context_desc_5000 = context_desc_all.substring(0, 5000);
					context_desc_5000 += "...(完整內容請由官方專利局取得)";
					patent.getPatentDesc().setContext_desc(context_desc_5000);
				}

				patent.getPatentDesc().setPatent(patent);
			}
			
			patent.setEdit_source(Patent.EDIT_SOURCE_SERVICE);
			return Constants.INT_SUCCESS;
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}

	@Override
	public int addPatentByApplNo(Patent editPatent, Admin admin, Business business, int sourceFrom) {
		try {
			log.info("addPatentByApplNo: ");
			int taskResult = Constants.INT_SYSTEM_PROBLEM;

			if (editPatent == null) {
				return Constants.INT_DATA_ERROR;
			}

			String applNo = editPatent.getPatent_appl_no();
			if (StringUtils.isNULL(applNo)) {
				return Constants.INT_CANNOT_FIND_DATA;
			}

			Patent dbPatent = patentDao.getByApplNo(applNo);
			editPatent.setSync_date(DateUtils.getDayStart(new Date()));
			editPatent.setAdmin(admin);
			editPatent.setBusiness(business);

			if (dbPatent == null) {
				this.addPatent(editPatent);
				patentHistoryFirstAdd(editPatent, business.getBusiness_id());
				taskResult = Constants.INT_SUCCESS;
			} else {
				boolean isDuplicate = false;

				switch (sourceFrom) {
				case Constants.PATENT_APPL_SYNC:
					for (Business dbBusinessInList : dbPatent.getListBusiness()) {
						if (business.getBusiness_id().equals(dbBusinessInList.getBusiness_id())) {
							isDuplicate = true;
							editPatent.setFirstAddEditHistory(false);
							break;
						} else {
							editPatent.setFirstAddEditHistory(true);
						}
					}
					break;
				case Constants.PATENT_DETAIL_SYNC:
					isDuplicate = false;
					editPatent.setFirstAddEditHistory(false);
					break;
				default:
					break;
				}

				if (isDuplicate) {
					taskResult = Constants.INT_DATA_DUPLICATE;
				} else {
					editPatent.setComparePatent(dbPatent);
					taskResult = updatePatent(editPatent, null);
				}
			}
			return taskResult;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int addPatentByExcel(List<Patent> patentList, Admin admin, Business business, String ip) {
		try {
			log.info("addPatentByExcel: ");
			int taskResult = Constants.INT_SYSTEM_PROBLEM;
			if (patentList == null) {
				return Constants.INT_DATA_ERROR;
			}

			for (Patent editPatent : patentList) {
				int syncResult = syncPatentData(editPatent);
				if (syncResult == Constants.INT_DATA_ERROR) {
					return Constants.INT_DATA_ERROR;
				}
				if (syncResult == Constants.INT_CANNOT_FIND_DATA) {
					editPatent.setPatent_appl_no(editPatent.getPatent_appl_no() + "@" + KeyGeneratorUtils.generateRandomString());
				}

				log.info("editPatent.getPatent_appl_no(): " + editPatent.getPatent_appl_no());
				Patent dbPatent = patentDao.getByApplNo(editPatent.getPatent_appl_no());
				editPatent.setSync_date(DateUtils.getDayStart(new Date()));
				editPatent.setAdmin(admin);
				editPatent.setAdmin_ip(ip);
				editPatent.setBusiness(business);

				if (editPatent.getEdit_source() != Patent.EDIT_SOURCE_SERVICE) {
					editPatent.setEdit_source(Patent.EDIT_SOURCE_HUMAN);
				}

				handleExtensionAddAsList(editPatent, business.getBusiness_id());

				if (dbPatent == null) {
					editPatent.setSourceFrom(Constants.PATENT_EXCEL_IMPORT);
					this.addPatent(editPatent);
					patentHistoryFirstAdd(editPatent, business.getBusiness_id());

					taskResult = Constants.INT_SUCCESS;
				} else {
					for (Business dbBusinessInList : dbPatent.getListBusiness()) {
						if (business.getBusiness_id().equals(dbBusinessInList.getBusiness_id())) {
							editPatent.setFirstAddEditHistory(false);
							break;
						} else {
							editPatent.setFirstAddEditHistory(true);
						}
					}

					editPatent.setComparePatent(dbPatent);
					handleExtensionExcelCompare(dbPatent, editPatent, business.getBusiness_id());
					editPatent.setSourceFrom(Constants.PATENT_EXCEL_IMPORT);
					taskResult = updatePatent(editPatent, null);
				}
			}
			return taskResult;
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	public void handleExtensionAddAsList(Patent editPatent, String businessId) {
		if (editPatent.getExtension() != null) {
			List<PatentExtension> extensionList = new ArrayList<>();
			editPatent.getExtension().setExtension_id(KeyGeneratorUtils.generateRandomString());
			editPatent.getExtension().setBusiness_id(businessId);
			editPatent.getExtension().setPatent(editPatent);
			extensionList.add(editPatent.getExtension());
			editPatent.setListExtension(extensionList);
		}
	}

	public void handleExtensionExcelCompare(Patent dbPatent, Patent editPatent, String editBusinessId) {
		boolean isDbExtensionAdd = true;
		List<PatentExtension> editExtensionList = editPatent.getListExtension();
		List<PatentExtension> dbExtensionList = dbPatent.getListExtension();

		// excel not select (is null)
		if (editPatent.getExtension() == null) {
			return;
		}

		// excel selected (is not null)
		if (dbExtensionList != null && dbExtensionList.size() > 0) {
			// update
			for (PatentExtension dbExtension : dbExtensionList) {
				String dbBusinessId = dbExtension.getBusiness_id();
				for (PatentExtension editExtension : editExtensionList) {
					if (dbBusinessId.equals(editBusinessId)) {
						String ex_business_num = editExtension.getBusiness_num();
						String ex_file_num = editExtension.getExtension_file_num();
						String ex_appl_year = editExtension.getExtension_appl_year();
						String ex_memo = editExtension.getExtension_memo();
						String ex_subsidy_unit = editExtension.getExtension_subsidy_unit();
						String ex_subsidy_num = editExtension.getExtension_subsidy_num();
						String ex_subsidy_plan = editExtension.getExtension_subsidy_plan();
						String ex_agent = editExtension.getExtension_agent();
						String ex_agent_num = editExtension.getExtension_agent_num();
						String ex_other_information = editExtension.getExtension_other_information();
						String ex_school_department = editExtension.getExtension_school_department();

						dbExtension.setBusiness_num(ex_business_num);
						dbExtension.setExtension_file_num(ex_file_num);
						dbExtension.setExtension_appl_year(ex_appl_year);
						dbExtension.setExtension_memo(ex_memo);
						dbExtension.setExtension_subsidy_unit(ex_subsidy_unit);
						dbExtension.setExtension_subsidy_num(ex_subsidy_num);
						dbExtension.setExtension_subsidy_plan(ex_subsidy_plan);
						dbExtension.setExtension_agent(ex_agent);
						dbExtension.setExtension_agent_num(ex_agent_num);
						dbExtension.setExtension_other_information(ex_other_information);
						dbExtension.setExtension_school_department(ex_school_department);

						isDbExtensionAdd = false;
						break;
					}
				}
			}
		} else {
			// add
			for (PatentExtension editExtension : editExtensionList) {
				editExtension.setExtension_id(KeyGeneratorUtils.generateRandomString());
				editExtension.setPatent(dbPatent);
			}
			dbPatent.setListExtension(editPatent.getListExtension()); // a different object
		}

		if (isDbExtensionAdd) {
			for (PatentExtension extension : editPatent.getListExtension()) {
				extension.setExtension_id(KeyGeneratorUtils.generateRandomString());
				extension.setPatent(dbPatent);
			}
			dbPatent.setListExtension(editPatent.getListExtension());
		}
	}
	
	@Override
	public int checkNoPublicApplNo(Patent editPatent, Business business) {
		try {
			log.info("addPatentByNoPublicApplNo:");
			String editPatentApplNo = editPatent.getPatent_appl_no();

			List<Patent> dbPatentList = patentDao.getPatentListByApplNo(editPatentApplNo);
			if (dbPatentList.isEmpty()) {
				// update new patent
				return Constants.INT_SUCCESS;
			} else {
				log.info("!dbPatentList.isEmpty()");
				// 判斷是否同一間學校新增
				int sameBusinessCount = 0;
				for (Patent dbPatent : dbPatentList) {
					for (Business dbBusiness : dbPatent.getListBusiness()) {
						String editBusinessId = business.getBusiness_id();
						String dbBusinessId = dbBusiness.getBusiness_id();
						if (editBusinessId.equals(dbBusinessId)) {
							sameBusinessCount++;
						}
					}
				}
				log.info("sameBusinessCount: " + sameBusinessCount);
				if (sameBusinessCount >= 2) {
					// 同一間學校：禁止新增
					return Constants.INT_DATA_DUPLICATE;
				} else {
					// 可新增：存入or合併關聯
					return Constants.INT_SUCCESS;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int addPatentByNoPublicApplNo(Patent editPatent, Business business) {
		try {
			log.info("addPatentByNoPublicApplNo:");
			boolean isSync = false;

			String editPatentApplNo = editPatent.getPatent_appl_no();
			String editApplNoWithoutAt = StringUtils.getApplNoWithoutAt(editPatentApplNo);

			log.info(editApplNoWithoutAt);

			String dbPatentId = null;
			List<Patent> dbPatentList = patentDao.getPatentListByApplNo(editPatentApplNo);
			if (dbPatentList.isEmpty()) {
				// update new patent
				editPatent.setPatent_appl_no(editApplNoWithoutAt + "@" + KeyGeneratorUtils.generateRandomString());
				log.info(editPatent.getPatent_appl_no());
				return patentDao.updatePatentApplNo(editPatent.getPatent_id(), editPatent.getPatent_appl_no());
			} else {
				log.info("!dbPatentList.isEmpty()");
				// 判斷是否同一間學校新增
				int sameBusinessCount = 0;
				for (Patent dbPatent : dbPatentList) {
					for (Business dbBusiness : dbPatent.getListBusiness()) {
						String editBusinessId = business.getBusiness_id();
						String dbBusinessId = dbBusiness.getBusiness_id();
						if (editBusinessId.equals(dbBusinessId)) {
							sameBusinessCount++;
						}
					}
				}
				log.info("sameBusinessCount: " + sameBusinessCount);
				if (sameBusinessCount >= 2) {
					log.info("sameBusinessCount: " + sameBusinessCount);
					return Constants.INT_DATA_DUPLICATE;
				}

				// 不同學校新增情況下，db資料是否已經同步
				for (Patent dbPatent : dbPatentList) {
					if (!dbPatent.isIs_sync()) {
						// 都沒同步 -> 存入資料庫，申請號random
						isSync = false;
						log.info("確認是否同步isSync = false;");
					} else {
						// 合併關聯
						dbPatentId = dbPatent.getPatent_id();
						isSync = true;
						log.info("確認是否同步issync = true; break");
						break;
					}
				}

				if (!isSync) {
					log.info("!is sync -> 存入資料庫，申請號random");
					editPatent.setPatent_appl_no(editPatent.getPatent_appl_no() + "@" + KeyGeneratorUtils.generateRandomString());
					return patentDao.updatePatentApplNo(editPatent.getPatent_id(), editPatent.getPatent_appl_no());
				} else {
					log.info("is sync -> 合併關聯");
					return mergeDiffPatent("44100767fa71b9b896b564545ee81503", editPatent);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}
	
	@Override
	public int mergeDiffPatent(String dbPatentId, Patent editPatent) {
		try {
			Patent dbPatent = patentDao.getById(dbPatentId);

			List<String> checkBusinessIds = new ArrayList<>();
			for (Business dbBussiness : dbPatent.getListBusiness()) {
				checkBusinessIds.add(dbBussiness.getBusiness_id());
			}

			if (editPatent.getBusiness() != null) {
				if (!checkBusinessIds.contains(editPatent.getBusiness().getBusiness_id())) {
					dbPatent.addBusiness(editPatent.getBusiness());
				}
			}

			List<PatentStatus> psList = new ArrayList<>();
			if (editPatent.getListPatentStatus() != null) {
				for (PatentStatus editPs : editPatent.getListPatentStatus()) {
					Status status = editPs.getStatus();
					if (status.getStatus_from().equals("user")) {
						status.setStatus_id(KeyGeneratorUtils.generateRandomString());
						editPs.setPatent(dbPatent);
						psList.add(editPs);
						dbPatent.addPatentStatus(editPs);
					}
				}
			}

			List<PatentContact> patentConstactList = new ArrayList<>();
			if (editPatent.getListContact() != null) {
				for (PatentContact editPc : editPatent.getListContact()) {
					editPc.setPatent(dbPatent);
					patentConstactList.add(editPc);
				}
			}
			patentConstactList.addAll(dbPatent.getListContact());
			if (patentConstactList != null && !patentConstactList.isEmpty()) {
				dbPatent.setListContact(patentConstactList);
			}

			List<PatentCost> patentCostList = new ArrayList<>();
			if (editPatent.getListCost() != null) {
				for (PatentCost editPc : editPatent.getListCost()) {
					editPc.setPatent(dbPatent);
					patentCostList.add(editPc);
				}
			}
			patentCostList.addAll(dbPatent.getListCost());
			if (patentCostList != null && !patentCostList.isEmpty()) {
				dbPatent.setListCost(patentCostList);
			}

			List<PatentExtension> extensionList = new ArrayList<>();
			if (editPatent.getListExtension() != null) {
				for (PatentExtension editPe : editPatent.getListExtension()) {
					editPe.setExtension_id(KeyGeneratorUtils.generateRandomString());
					editPe.setPatent(dbPatent);
					extensionList.add(editPe);
				}
			}
			extensionList.addAll(dbPatent.getListExtension());
			if (extensionList != null && !extensionList.isEmpty()) {
				dbPatent.setListExtension(extensionList);
			}

			List<PatentEditHistory> patentHistoryList = new ArrayList<>();
			if (editPatent.getListHistory() != null) {
				for (PatentEditHistory editPh : editPatent.getListHistory()) {
					editPh.setPatent(dbPatent);
					patentHistoryList.add(editPh);
				}
			}
			patentHistoryList.addAll(dbPatent.getListHistory());
			if (patentHistoryList != null && !patentHistoryList.isEmpty()) {
				dbPatent.setListHistory(patentHistoryList);
			}

			List<Annuity> annuityList = new ArrayList<>();
			if (editPatent.getListHistory() != null) {
				for (Annuity editAnnuity : editPatent.getListAnnuity()) {
					editAnnuity.setPatent(dbPatent);
					annuityList.add(editAnnuity);
				}
			}
			annuityList.addAll(dbPatent.getListAnnuity());
			if (annuityList != null && !annuityList.isEmpty()) {
				dbPatent.setListAnnuity(annuityList);
			}

			List<Portfolio> portfolioList = new ArrayList<>();
			if (editPatent.getListHistory() != null) {
				for (Portfolio editPh : editPatent.getListPortfolio()) {
					portfolioList.add(editPh);
				}
			}
			portfolioList.addAll(dbPatent.getListPortfolio());
			if (portfolioList != null && portfolioList.isEmpty()) {
				dbPatent.setListPortfolio(portfolioList);
			}

			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}
	
	@Override
	public int authorizedUpdatePatent(String businessId, Patent patent) {
		Patent dbBean = patentDao.getById(businessId, patent.getPatent_id());
		if (dbBean != null) {
			return updatePatent(patent, businessId);
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}

	@Override
	public int updatePatent(Patent patent, String businessId) {
		log.info("updatePatent:" + patent.getPatent_id());
		List<PatentEditHistory> editList = new ArrayList<PatentEditHistory>();
		Patent dbBean = patent.getComparePatent();
		if (dbBean == null) {
			log.info("Compare Patent is null");
			dbBean = patentDao.getById(patent.getPatent_id());
		}

		if (dbBean != null) {
			log.info("db patent is not  null");
			// TODO save edit history
			if (patent.isFirstAddEditHistory()) {
				log.info("is first add");
				patentHistoryFirstAdd(patent, businessId);
			} else {
				log.info("is not first add");
				comparePatent(dbBean, patent, businessId);
			}

			if ((Constants.APPL_COUNTRY_TW.equals(dbBean.getPatent_appl_country())
					&& Patent.EDIT_SOURCE_SERVICE == patent.getEdit_source())
					|| Patent.EDIT_SOURCE_HUMAN == patent.getEdit_source()) {
				dbBean.setPatent_bdate(patent.getPatent_bdate());
				dbBean.setPatent_edate(patent.getPatent_edate());

				dbBean.setPatent_cancel_date(patent.getPatent_cancel_date());
				dbBean.setPatent_charge_expire_date(patent.getPatent_charge_expire_date());

				dbBean.setPatent_charge_duration_year(patent.getPatent_charge_duration_year());
			}

			
			if(Patent.EDIT_SOURCE_SERVICE   == patent.getEdit_source()) {
				dbBean.setIs_public(true);
				dbBean.setIs_sync(true);
				dbBean.setSync_date(patent.getSync_date());
				if (patent.getListIPC() != null) {
					dbBean.setListIPC(patent.getListIPC());
	                for (IPCClass ipc:patent.getListIPC()) {
	             	    IPCClass ipcDb = ipcDao.getByIdAndVersion(ipc.getIpc_class_id());
	             	    log.info(ipcDb);
	             	    if (ipcDb == null) {
	             		   ipcDao.create(ipc);
	             	    }
	                }
	            }
			}

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
			
			//TODO charles 
//			mappingAssignee(dbBean,patent);
//			mappingApplicant(dbBean,patent);
//			mappingInventor(dbBean,patent);
			
			//TODO Leo edit
			log.info("delete list data");
			//handleAssignee(dbBean, patent);
			//handleApplicant(dbBean, patent);
			//handleInventor(dbBean, patent);
			handleCost(dbBean, patent, businessId);
			handleContact(dbBean, patent);
			handleAnnuity(dbBean, patent);
			handlePatentStatus(dbBean, patent);

			if (patent.getSourceFrom() != Constants.PATENT_EXCEL_IMPORT) {
				// source from != excel
				handleExtension(dbBean, patent, businessId);
			}
			
			if(Patent.EDIT_SOURCE_HUMAN   == patent.getEdit_source()) {
				dbBean.setFamily(patent.getFamily());
				dbBean.setListPortfolio(patent.getListPortfolio());
			}

			List<String> checkBusinessIds = new ArrayList<>();
			for (Business bussiness : dbBean.getListBusiness()) {
				checkBusinessIds.add(bussiness.getBusiness_id());
			}
			log.info(checkBusinessIds);
			if (patent.getBusiness() != null) {
					log.info(patent.getBusiness().getBusiness_id());
					if (!checkBusinessIds.contains(patent.getBusiness().getBusiness_id())) {
						log.info("add business");
						dbBean.addBusiness(patent.getBusiness());
					}
			}
			handleReminder(patent, dbBean.getListBusiness());
			return Constants.INT_SUCCESS;
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}
	
	
	@Override
	public int combinePatentFamily(PatentFamily inputFamily, String businessId, String patentId, Admin tokenAdmin, String ip) {
		if(StringUtils.isNULL(inputFamily.getPatent_family_id())) {
			
		}
		List<String> ids = inputFamily.getListPatentIds();

		// family edit history
		List<String> inputFamilyListData = new ArrayList<>();

		if (ids.isEmpty()) {
			//delete family
			List<Patent> checkPatentList = patentDao.getByFamily(inputFamily.getPatent_family_id());
			for (Patent checkPatent:checkPatentList) {
				checkPatent.setFamily(null);
			}
			familyDao.delete(inputFamily.getPatent_family_id());
			return Constants.INT_SUCCESS;
		}
		List<Patent> list = patentDao.getByPatentIds(ids, businessId);
		log.info("list :"+list.size());
		PatentFamily family =null;
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
					// patent.setFamily(family);
				}
			}
			inputFamilyListData.add(JacksonJSONUtils.mapObjectWithView(patent, View.PatentIdApplNo.class));
		}

		// family edit history
		try {
			String editor = tokenAdmin.getAdmin_name();
			Patent currentPatent = patentDao.getById(patentId);
			currentPatent.setAdmin(tokenAdmin);
			currentPatent.setAdmin_ip(ip);
//			PatentEditHistory peh = checkFieldValue(currentPatent, sourceData, newData, Constants.PATENT_FAMILY_FIELD);
			PatentEditHistory peh = insertFieldHistory(currentPatent, inputFamilyListData, "update", Constants.PATENT_FAMILY_FIELD, editor, businessId);
			if (peh != null) {
				currentPatent.addHistory(peh);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (family == null) {
			log.info("no family");
			family = new PatentFamily();
			family.setPatent_family_id(KeyGeneratorUtils.generateRandomString());
			family.setCreate_date(new Date());
			family.setUpdate_date(new Date());
			family.setBusiness_id(inputFamily.getBusiness_id());
			familyDao.create(family);
			for (Patent patent : list) {
				family.addPatent(patent);
				// patent.setFamily(family);
			}
		} else {
			log.info("set family");
			for(Patent patent : list) {
				log.info("patent :"+patent.getPatent_id());
				if(patent.getFamily() == null) {
					family.addPatent(patent);
				}
			}
			family.setBusiness_id(inputFamily.getBusiness_id());
		}
		
		//check patent family if not in inputFamily and remove it
		List<Patent> checkPatentList = patentDao.getByFamily(inputFamily.getPatent_family_id());
		for (Patent checkPatent:checkPatentList) {
			if (!ids.contains(checkPatent.getPatent_id())) {
				checkPatent.setFamily(null);
			}
		}
		BeanUtils.copyProperties(family, inputFamily);
		log.info("id : "+inputFamily.getPatent_family_id());
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
			orderList = "listPatentStatus";
			orderFieldCode = "status_id";
			break;
		case Constants.INVENTOR_NAME_FIELD:
			orderList = "listInventor";
			break;
		default:
			break;
		}
		
		List<Patent> list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE, orderList, orderFieldCode,is_asc);
		
		for(Patent patent : list) {
			patent.getListPatentStatus().size();
			patent.getListExtension().size();
			patent.getListBusiness().size();
			patent.getListInventor().size();
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
			patent.getListPatentStatus().size();
			patent.getListIPC().size();
			patent.getListExtension().size();
			patent.getListBusiness().size();
			patent.getListAnnuity().size();

		}
		return patentList;
	}

	@Override
	public List<Patent> getByPatentIds(List<String> idList,String businessId){
		List<Patent> list = patentDao.getByPatentIds(idList,businessId);
		for(Patent patent : list) {
			patent.getListPatentStatus().size();
			patent.getListExtension().size();
			patent.getListBusiness().size();
		}
		return list;
	}

	@Override
	public Patent getByPatentNo(String patentNo) {
		return patentDao.getByPatentNo(patentNo);
	}

	
	@Override
	public List<Patent> getByFamily(String familyId){
		List<Patent> list = patentDao.getByFamily(familyId);
		for(Patent patent : list) {
			patent.getListBusiness().size();
			patent.getListPatentStatus().size();
			patent.getListExtension().size();
			patent.getListInventor().size();
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
			orderList = "listPatentStatus";
			orderFieldCode = "status_id";
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
				String statusStr = (String) searchObj;
				//parse to json
				String status_desc = new JSONObject(statusStr).optString("status_desc");
				String status_desc_en = new JSONObject(statusStr).optString("status_desc_en");
				log.info("status:"+status_desc);
				log.info("status_en:"+status_desc_en);
				list = patentDao.searchFieldStatusListPatent(status_desc,status_desc_en, businessId, page, Constants.SYSTEM_PAGE_SIZE, orderList,orderFieldCode,is_asc);
				count = patentDao.countSearchFieldStatusPatent(status_desc,status_desc_en, businessId);
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
				patent.getListPatentStatus().size();
				patent.getListExtension().size();
				patent.getListBusiness().size();
				patent.getListInventor().size();
			}
		}
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	

	@Override
	public List<Status> getEditStatus(){
		return statusDao.getEditable();
	}
	
	@Override
	public void patentHistoryFirstAdd(Patent patent, String businessId) {
		log.info("patentHistory:");
		Patent dbBean = patentDao.getByApplNo(patent.getPatent_appl_no());

		String editor = null;
		if (patent.getEdit_source() == Patent.EDIT_SOURCE_SERVICE) {
			editor = "Official";
		} else {
			editor = patent.getAdmin().getAdmin_name();
		}
		String editor_excel = patent.getAdmin().getAdmin_name();

		List<PatentField> fieldList = fieldDao.getAllFields();
		for (PatentField field : fieldList) {
			if (Constants.PATENT_NAME_FIELD.equals(field.getField_id())) {
				// for excel data
				if (patent.getEdit_source() == Patent.EDIT_SOURCE_SERVICE && patent.getSourceFrom() == Constants.PATENT_EXCEL_IMPORT) {
					String sourceField_excel = null;
					String newField_excel = patent.getPatent_excel_name();
					PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
					if (peh_excel != null) {
						dbBean.addHistory(peh_excel);
					}
				}
				// for sync data
				String sourceField_sync = null;
				String newField_sync = patent.getPatent_name();
				PatentEditHistory peh_sync = checkFieldValueFirstAdd(patent, sourceField_sync, newField_sync, field.getField_id(), editor, businessId);
				if (peh_sync != null) {
					dbBean.addHistory(peh_sync);
				}
			}
			if (Constants.PATENT_NAME_EN_FIELD.equals(field.getField_id())) {
				// for excel data
				if (patent.getEdit_source() == Patent.EDIT_SOURCE_SERVICE && patent.getSourceFrom() == Constants.PATENT_EXCEL_IMPORT) {
					String sourceField_excel = null;
					String newField_excel = patent.getPatent_excel_name_en();
					PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
					if (peh_excel != null) {
						dbBean.addHistory(peh_excel);
					}
				}
				// for sync data
				String sourceField_sync = null;
				String newField_sync = patent.getPatent_name_en();
				PatentEditHistory peh_sync = checkFieldValueFirstAdd(patent, sourceField_sync, newField_sync, field.getField_id(), editor, businessId);
				if (peh_sync != null) {
					dbBean.addHistory(peh_sync);
				}
			}
			if (Constants.ASSIGNEE_NAME_FIELD.equals(field.getField_id())) {
				// for excel
				if (patent.getPatent_excel_assignee() != null && patent.getSourceFrom() == Constants.PATENT_EXCEL_IMPORT) {
					List<String> assigneeAddData_excel = new ArrayList<>();
					for (Assignee assignee : patent.getPatent_excel_assignee()) {
						assigneeAddData_excel.add(JacksonJSONUtils.mapObjectWithView(assignee, View.PatentHistoryExcel.class));
						log.info(JacksonJSONUtils.mapObjectWithView(assignee, View.PatentHistoryExcel.class));
					}

					if (!assigneeAddData_excel.isEmpty()) {
						PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, assigneeAddData_excel, "create", field.getField_id(), editor_excel, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
				// for sync
				if (patent.getListAssignee() != null) {
					List<String> assigneeAddData = new ArrayList<>();
					for (Assignee assignee : patent.getListAssignee()) {
						assigneeAddData.add(JacksonJSONUtils.mapObjectWithView(assignee, View.PatentDetail.class));
					}
					if (!assigneeAddData.isEmpty()) {
						PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, assigneeAddData, "create", field.getField_id(), editor, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
			}
			if (Constants.APPLIANT_NAME_FIELD.equals(field.getField_id())) {
				// for excel
				if (patent.getPatent_excel_applicant() != null && patent.getSourceFrom() == Constants.PATENT_EXCEL_IMPORT) {
					List<String> applAddData_excel = new ArrayList<>();
					for (Applicant appl : patent.getPatent_excel_applicant()) {
						applAddData_excel.add(JacksonJSONUtils.mapObjectWithView(appl, View.PatentHistoryExcel.class));
						log.info(JacksonJSONUtils.mapObjectWithView(appl, View.PatentHistoryExcel.class));
					}
					if (!applAddData_excel.isEmpty()) {
						PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, applAddData_excel, "create", field.getField_id(), editor_excel, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
				// for sync
				if (patent.getListApplicant() != null) {
					List<String> applAddData = new ArrayList<>();
					for (Applicant appl : patent.getListApplicant()) {
						log.info(appl.getApplicant_name());
						applAddData.add(JacksonJSONUtils.mapObjectWithView(appl, View.PatentDetail.class));
					}
					if (!applAddData.isEmpty()) {
						PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, applAddData, "create", field.getField_id(), editor, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
			}
			if (Constants.INVENTOR_NAME_FIELD.equals(field.getField_id())) {
				// for excel
				if (patent.getPatent_excel_inventor() != null && patent.getSourceFrom() == Constants.PATENT_EXCEL_IMPORT) {
					List<String> invAddData_excel = new ArrayList<>();
					for (Inventor inv : patent.getPatent_excel_inventor()) {
						log.info(inv.getInventor_name());
						invAddData_excel.add(JacksonJSONUtils.mapObjectWithView(inv, View.PatentHistoryExcel.class));
						log.info(JacksonJSONUtils.mapObjectWithView(inv, View.PatentHistoryExcel.class));
					}
					if (!invAddData_excel.isEmpty()) {
						PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, invAddData_excel, "create", field.getField_id(), editor_excel, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
				// for sync
				if (patent.getListInventor() != null) {
					List<String> invAddData = new ArrayList<>();
					for (Inventor inv : patent.getListInventor()) {
						log.info(inv.getInventor_name());
						invAddData.add(JacksonJSONUtils.mapObjectWithView(inv, View.PatentDetail.class));
					}
					if (!invAddData.isEmpty()) {
						PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, invAddData, "create", field.getField_id(), editor, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
			}
			if (Constants.PATENT_STATUS_FIELD.equals(field.getField_id())) {
				List<String> statusAddData = new ArrayList<>();
				if (dbBean.getListPatentStatus() != null && patent.getListPatentStatus() != null) {
					for (PatentStatus patentStatus : patent.getListPatentStatus()) {
						Status status = patentStatus.getStatus();
						if (patentStatus.getCreate_date() != null && !status.getStatus_from().equals("user")) {
							status.setCreate_date(patentStatus.getCreate_date());
							statusAddData.add(JacksonJSONUtils.mapObjectWithView(status, View.Patent.class));
						}
					}
				}
				if (!statusAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistoryFirstAdd(patent, statusAddData, "create", field.getField_id(), editor, businessId);
					if (peh != null) {
						dbBean.addHistory(peh);
					}
				}
			}
			if (Constants.SCHOOL_NO_FIELD.equals(field.getField_id())) {
				String sourceField = null;
				String newField = patent.getPatent_excel_school_no();
				PatentEditHistory peh = checkFieldValueFirstAdd(patent, sourceField, newField, field.getField_id(), editor_excel, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
				}
			}
			if (Constants.SCHOOL_APPL_YEAR_FIELD.equals(field.getField_id())) {
				String sourceField = null;
				String newField = patent.getPatent_excel_school_appl_year();
				PatentEditHistory peh = checkFieldValueFirstAdd(patent, sourceField, newField, field.getField_id(), editor_excel, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
				}
			}
			if (Constants.PATENT_MEMO.equals(field.getField_id())) {
				String sourceField = null;
				String newField = patent.getPatent_excel_memo();
				PatentEditHistory peh = checkFieldValueFirstAdd(patent, sourceField, newField, field.getField_id(), editor_excel, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
				}
			}
		}
	}

	private void comparePatent(Patent dbBean, Patent patent, String businessId) {
		List<PatentField> fieldList = fieldDao.getAllFields();

		String editor = null;
		if (patent.getEdit_source() == Patent.EDIT_SOURCE_SERVICE) {
			editor = "Official";
		} else {
			editor = patent.getAdmin().getAdmin_name();
		}

		String editor_excel = patent.getAdmin().getAdmin_name();
		
		for (PatentField field:fieldList) {
			if (Constants.PATENT_NAME_FIELD.equals(field.getField_id())) {
				// excel data
				String sourceField_excel = null;
				String newField_excel = patent.getPatent_excel_name();
				PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
				if (peh_excel != null) {
					dbBean.addHistory(peh_excel);
				}

				// update data
				String sourceField = dbBean.getPatent_name();
				String newField = patent.getPatent_name();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_name(newField);
				}
			}
			if (Constants.PATENT_NAME_EN_FIELD.equals(field.getField_id())) {
				// excel data
				String sourceField_excel = null;
				String newField_excel = patent.getPatent_excel_name_en();
				PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
				if (peh_excel != null) {
					dbBean.addHistory(peh_excel);
				}

				// update data
				String sourceField = dbBean.getPatent_name_en();
				String newField = patent.getPatent_name_en();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_name_en(newField);
				}
			}
			if (Constants.PATENT_COUNTRY_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_appl_country();
				String newField = patent.getPatent_appl_country();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_appl_country(newField);
				}
			}
			if (Constants.PATENT_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_no();
				String newField = patent.getPatent_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_no(newField);
				}
			}
			if (Constants.PATENT_APPL_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_appl_no();
				String newField = patent.getPatent_appl_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_appl_no(newField);
				}
			}
			if (Constants.PATENT_NOTICE_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_notice_no();
				String newField = patent.getPatent_notice_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_notice_no(newField);
				}
			}
			if (Constants.PATENT_PUBLISH_NO_FIELD.equals(field.getField_id())) {
				String sourceField = dbBean.getPatent_publish_no();
				String newField = patent.getPatent_publish_no();
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
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
				if (patent.getPatent_appl_date() != null) {
					newField = DateUtils.getSimpleSlashFormatDate(patent.getPatent_appl_date());
				}
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
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
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
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
				if (patent.getPatent_publish_date() != null) {
					newField = DateUtils.getSimpleSlashFormatDate(patent.getPatent_publish_date());
				}
				PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
				if (peh != null) {
					dbBean.addHistory(peh);
					dbBean.setPatent_publish_date(patent.getPatent_publish_date());
				}
			}

			if (Constants.PATENT_STATUS_FIELD.equals(field.getField_id())) {
				List<String> statusAddData = new ArrayList<>();
				if (dbBean.getListPatentStatus() != null && patent.getListPatentStatus() != null) {
					for (PatentStatus patentStatus : patent.getListPatentStatus()) {
						Status status = patentStatus.getStatus();
						if (patentStatus.getCreate_date() != null) {
							if (StringUtils.isNULL(patentStatus.getBusiness_id())) { // official
								status.setCreate_date(patentStatus.getCreate_date());
								statusAddData.add(JacksonJSONUtils.mapObjectWithView(status, View.Patent.class));
							} else if (patentStatus.getBusiness_id().equals(businessId)) { // user status
								status.setCreate_date(patentStatus.getCreate_date());
								statusAddData.add(JacksonJSONUtils.mapObjectWithView(status, View.Patent.class));
							}
						}
					}
				}
				if (!statusAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, statusAddData, "create", field.getField_id(), editor, businessId);
					if (peh != null) {
						dbBean.addHistory(peh);
					}
				}
			}
			if (Constants.ASSIGNEE_NAME_FIELD.equals(field.getField_id()) && patent.getListAssignee() != null) {
				//add
				List<String> assigneeAddData = new ArrayList<>();
				HashMap<String, Assignee> mapping = new HashMap<String, Assignee>();
				for (Assignee assignee:patent.getListAssignee()) {
					if (assignee.getAssignee_id() == null) {
						assignee.setAssignee_id(KeyGeneratorUtils.generateRandomString());
						assignee.setPatent(patent);
						dbBean.addAssignee(assignee);
						assigneeAddData.add(JacksonJSONUtils.mapObjectWithView(assignee, View.PatentDetail.class));
					}
					mapping.put(assignee.getAssignee_id(), assignee);

				}
				if (!assigneeAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, assigneeAddData, "create", field.getField_id(), editor, businessId);
					if (peh != null) {
						dbBean.addHistory(peh);
					}
				}
				List<String> assigneeUpdateData = new ArrayList<>();
				List<String> assigneeRemoveData = new ArrayList<>();
				if (dbBean.getListAssignee() != null) {
					Iterator<Assignee> iterator = dbBean.getListAssignee().iterator();
					while (iterator.hasNext()) {
						Assignee assignee = iterator.next();
						if (mapping.containsKey(assignee.getAssignee_id())) {
							//update
							String assName = "";
							if (assignee.getAssignee_name() != null) {
								assName = assignee.getAssignee_name();
							}
							String assNameMap = "";
							if (mapping.get(assignee.getAssignee_id()).getAssignee_name() != null) {
								assNameMap = mapping.get(assignee.getAssignee_id()).getAssignee_name();
							}
							String assNameEn = "";
							if (assignee.getAssignee_name_en() != null) {
								assNameEn = assignee.getAssignee_name_en();
							}
							String assNameEnMap = "";
							if (mapping.get(assignee.getAssignee_id()).getAssignee_name_en() != null) {
								assNameEnMap = mapping.get(assignee.getAssignee_id()).getAssignee_name_en();
							}
							if (!assName.equals(assNameMap) || !assNameEn.equals(assNameEnMap)) {
								assignee.setAssignee_name(assNameMap);
								assignee.setAssignee_name_en(assNameEnMap);
								assigneeUpdateData.add(JacksonJSONUtils.mapObjectWithView(mapping.get(assignee.getAssignee_id()),  View.PatentDetail.class));
							}
						}else {
							iterator.remove();
							assigneeRemoveData.add(JacksonJSONUtils.mapObjectWithView(assignee,  View.PatentDetail.class));
						}
					}
				}
				if (!assigneeUpdateData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, assigneeUpdateData, "update", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
				if (!assigneeRemoveData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, assigneeRemoveData, "remove", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
			}
			if (Constants.APPLIANT_NAME_FIELD.equals(field.getField_id()) && patent.getListApplicant() != null) {
				//add
				List<String> applAddData = new ArrayList<>();
				HashMap<String, Applicant> mapping = new HashMap<String, Applicant>();
				for (Applicant appl:patent.getListApplicant()) {
					if (appl.getApplicant_id() == null) {
						appl.setApplicant_id(KeyGeneratorUtils.generateRandomString());
						appl.setPatent(patent);
						dbBean.addApplicant(appl);
						applAddData.add(JacksonJSONUtils.mapObjectWithView(appl, View.PatentDetail.class));
					}
					mapping.put(appl.getApplicant_id(), appl);

				}
				if (!applAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, applAddData, "create", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
				List<String> applUpdateData = new ArrayList<>();
				List<String> applRemoveData = new ArrayList<>();

				if (dbBean.getListApplicant() != null) {
					Iterator<Applicant> iterator = dbBean.getListApplicant().iterator();
					while (iterator.hasNext()) {
						Applicant appl = iterator.next();
						if (mapping.containsKey(appl.getApplicant_id())) {
							//update
							String appName = "";
							if (appl.getApplicant_name() != null) {
								appName = appl.getApplicant_name();
							}
							String appNameMap = "";
							if (mapping.get(appl.getApplicant_id()).getApplicant_name() != null) {
								appNameMap = mapping.get(appl.getApplicant_id()).getApplicant_name();
							}
							String appNameEn = "";
							if (appl.getApplicant_name_en() != null) {
								appNameEn = appl.getApplicant_name_en();
							}
							String appNameEnMap = "";
							if (mapping.get(appl.getApplicant_id()).getApplicant_name_en() != null) {
								appNameEnMap = mapping.get(appl.getApplicant_id()).getApplicant_name_en();
							}
							if (!appName.equals(appNameMap) || !appNameEn.equals(appNameEnMap)) {
								appl.setApplicant_name(appNameMap);
								appl.setApplicant_name_en(appNameEnMap);
								applUpdateData.add(JacksonJSONUtils.mapObjectWithView(mapping.get(appl.getApplicant_id()),  View.PatentDetail.class));
							}
						}else {
							iterator.remove();
							applRemoveData.add(JacksonJSONUtils.mapObjectWithView(appl,  View.PatentDetail.class));
						}
					}
				}
				if (!applUpdateData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, applUpdateData, "update", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
				if (!applRemoveData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, applRemoveData, "remove", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
			}
			if (Constants.INVENTOR_NAME_FIELD.equals(field.getField_id()) && patent.getListInventor() != null) {
				//add
				List<String> invAddData = new ArrayList<>();
				HashMap<String, Inventor> mapping = new HashMap<String, Inventor>();
				
				//TODO
				for (Inventor inv:patent.getListInventor()) {
					if (inv.getInventor_id() == null) {
						inv.setInventor_id(KeyGeneratorUtils.generateRandomString());
						inv.setPatent(patent);
						dbBean.addInventor(inv);
						invAddData.add(JacksonJSONUtils.mapObjectWithView(inv,  View.PatentDetail.class));
					} 
					mapping.put(inv.getInventor_id(), inv);

				}
				if (!invAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, invAddData, "create", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
				List<String> invUpdateData = new ArrayList<>();
				List<String> invRemoveData = new ArrayList<>();

				if (dbBean.getListInventor() != null) {
					Iterator<Inventor> iterator = dbBean.getListInventor().iterator();
					while (iterator.hasNext()) {
						Inventor inv = iterator.next();
						log.info("inventor id :"+inv.getInventor_id());
						if (mapping.containsKey(inv.getInventor_id())) {
							log.info("contain");
							//update
							String invName = "";
							if (inv.getInventor_name() != null) {
								invName = inv.getInventor_name();
							}
							String invNameMap = "";
							if (mapping.get(inv.getInventor_id()).getInventor_name() != null) {
								invNameMap = mapping.get(inv.getInventor_id()).getInventor_name();
							}
							String invNameEn = "";
							if (inv.getInventor_name_en() != null) {
								invNameEn = inv.getInventor_name_en();
							}
							String invNameEnMap = "";
							if (mapping.get(inv.getInventor_id()).getInventor_name_en() != null) {
								invNameEnMap = mapping.get(inv.getInventor_id()).getInventor_name_en();
							}
							if (!invName.equals(invNameMap) || !invNameEn.equals(invNameEnMap)) {
								inv.setInventor_name(invNameMap);
								inv.setInventor_name_en(invNameEnMap);
								invUpdateData.add(JacksonJSONUtils.mapObjectWithView(mapping.get(inv.getInventor_id()),  View.PatentDetail.class));
							}
						} else {
							log.info("going to remove");
							inv.setPatent(null);
							iterator.remove();
							invRemoveData.add(JacksonJSONUtils.mapObjectWithView(inv, View.PatentDetail.class));
						}
					}
				}
				log.info("dbBean inventor list size :"+dbBean.getListInventor().size());
				if (!invUpdateData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, invUpdateData, "update", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
				if (!invRemoveData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, invRemoveData, "remove", field.getField_id(), editor, businessId);
					if (peh != null) {dbBean.addHistory(peh);}
				}
			}

			// TODO
			if (Constants.PATENT_COST_FIELD.equals(field.getField_id())) {
				List<String> costAddData = new ArrayList<>();
				if (patent.getListCost() != null) {
					for (PatentCost cost : patent.getListCost()) {
						if (cost.getBusiness_id().equals(businessId)) {
							costAddData.add(JacksonJSONUtils.mapObjectWithView(cost, View.PatentDetail.class));
						}
					}
				}
				if (!costAddData.isEmpty()) {
					PatentEditHistory peh = insertFieldHistory(patent, costAddData, "create", field.getField_id(), editor, businessId);
					if (peh != null) {
						dbBean.addHistory(peh);
					}
				}
			}
			if (Constants.SCHOOL_NO_FIELD.equals(field.getField_id())) {
				log.info("school no");
				// excel data
				String sourceField_excel = null;
				String newField_excel = patent.getPatent_excel_school_no();
				PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
				if (peh_excel != null) {
					dbBean.addHistory(peh_excel);
				}

				// update data
				String sourceField = null;
				List<PatentExtension> dbExtensionList = dbBean.getListExtension();
				if (dbExtensionList != null) {
					for (PatentExtension dbExtension : dbExtensionList) {
						sourceField = dbExtension.getBusiness_num();
					}
				}

				if (patent.getListExtension() != null) {
					for (PatentExtension editExtension : patent.getListExtension()) {
						String newField = editExtension.getBusiness_num();
						PatentEditHistory peh = checkFieldValueFirstAdd(patent, sourceField, newField, field.getField_id(), editor, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
			}
			if (Constants.SCHOOL_APPL_YEAR_FIELD.equals(field.getField_id())) {
				log.info("appl year");
				// excel data
				String sourceField_excel = null;
				String newField_excel = patent.getPatent_excel_school_appl_year();
				PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
				if (peh_excel != null) {
					dbBean.addHistory(peh_excel);
				}

				// update data
				String sourceField = null;
				List<PatentExtension> dbExtensionList = dbBean.getListExtension();
				if (dbExtensionList != null) {
					for (PatentExtension dbExtension : dbExtensionList) {
						sourceField = dbExtension.getExtension_appl_year();
					}
				}

				if (patent.getListExtension() != null) {
					for (PatentExtension editExtension : patent.getListExtension()) {
						String newField = editExtension.getExtension_appl_year();
						PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
			}
			if (Constants.PATENT_MEMO.equals(field.getField_id())) {
				log.info("memo");
				// excel data
				String sourceField_excel = null;
				String newField_excel = patent.getPatent_excel_memo();
				PatentEditHistory peh_excel = checkFieldValueFirstAdd(patent, sourceField_excel, newField_excel, field.getField_id(), editor_excel, businessId);
				if (peh_excel != null) {
					dbBean.addHistory(peh_excel);
				}

				// update data
				String sourceField = null;
				List<PatentExtension> dbExtensionList = dbBean.getListExtension();
				if (dbExtensionList != null) {
					for (PatentExtension dbExtension : dbExtensionList) {
						sourceField = dbExtension.getExtension_memo();
					}
				}

				if (patent.getListExtension() != null) {
					for (PatentExtension editExtension : patent.getListExtension()) {
						String newField = editExtension.getExtension_memo();
						PatentEditHistory peh = checkFieldValue(patent, sourceField, newField, field.getField_id(), editor, businessId);
						if (peh != null) {
							dbBean.addHistory(peh);
						}
					}
				}
			}
		}
	}
	
	private PatentEditHistory insertFieldHistoryFirstAdd(Patent patent, List<String> historyDataList, String status, String fieldId, String editor, String businessId) {
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
		peh.setEditor(editor);
		peh.setBusiness_id(businessId);
		return peh;
	}
	
	private PatentEditHistory checkFieldValueFirstAdd(Patent patent, String sourceData, String newData, String fieldId, String editor, String businessId) {
		if (StringUtils.isNULL(newData)) {
			return null;
		}

		Date now = new Date();
		PatentEditHistory peh = null;
		peh = new PatentEditHistory();
		peh.setHistory_id(KeyGeneratorUtils.generateRandomString());
		peh.setPatent(patent);
		peh.setField_id(fieldId);
		peh.setHistory_data(newData);
		peh.setHistory_status("insert");
		peh.setAdmin(patent.getAdmin());
		peh.setAdmin_ip(patent.getAdmin_ip());
		peh.setCreate_date(now);
		peh.setEditor(editor);
		peh.setBusiness_id(businessId);
		return peh;
	}
	
	private PatentEditHistory insertFieldHistory(Patent patent, List<String> historyDataList, String status, String fieldId, String editor, String businessId) {	
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
		peh.setEditor(editor);
		peh.setBusiness_id(businessId);
		return peh;
	}
	
	private PatentEditHistory checkFieldValue(Patent patent, String sourceData, String newData, String fieldId, String editor, String businessId) {
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
				peh.setEditor(editor);
				peh.setBusiness_id(businessId);
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
					peh.setEditor(editor);
					peh.setBusiness_id(businessId);
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
					peh.setEditor(editor);
					peh.setBusiness_id(businessId);
				}
			} else {
				log.info("ignore update  when import or sync from service");
			}
		}
		return peh;
	}

	private void handleCost(Patent dbPatent, Patent editPatent, String business_id) {
		if (editPatent.getListCost() != null && editPatent.getListCost().size() > 0) {
			List<PatentCost> listCost = editPatent.getListCost();
			for (PatentCost cost : listCost) {
				cost.setCost_id(KeyGeneratorUtils.generateRandomString());
				cost.setPatent(dbPatent);
			}
			patentDao.deletePatentCost(dbPatent.getPatent_id());
			dbPatent.setListCost(editPatent.getListCost());
		}
	}

	private void handleContact(Patent dbPatent, Patent editPatent) {
		if (editPatent.getListContact() != null && editPatent.getListContact().size() > 0) {
			List<PatentContact> listContact = editPatent.getListContact();
			for (PatentContact contact : listContact) {
				if (StringUtils.isNULL(contact.getPatent_contact_id())) {
					contact.setPatent_contact_id(KeyGeneratorUtils.generateRandomString());
				}
				contact.setPatent(dbPatent);
				if (Patent.EDIT_SOURCE_HUMAN == editPatent.getEdit_source()) {
					contact.setBusiness(editPatent.getAdmin().getBusiness());
				}

				if (Patent.EDIT_SOURCE_SERVICE == editPatent.getEdit_source()) {
					contact.setBusiness(editPatent.getBusiness());
				}
				contact.setCreate_date(new Date());
			}
			patentDao.deletePatentContact(dbPatent.getPatent_id());
			dbPatent.setListContact(editPatent.getListContact());
		} else {
			if (dbPatent.getListContact() != null) {
				patentDao.deletePatentContact(dbPatent.getPatent_id());
			}
		}
	}

	private void handleAnnuity(Patent dbPatent, Patent editPatent) {
		if (editPatent.getListAnnuity() != null && editPatent.getListAnnuity().size() > 0) {
			List<Annuity> listAnnuity = editPatent.getListAnnuity();
			for (Annuity annuity : listAnnuity) {
				if (StringUtils.isNULL(annuity.getAnnuity_id())) {
					annuity.setAnnuity_id(KeyGeneratorUtils.generateRandomString());
				}
				if (annuity.getAnnuity_date() != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtils.getDayStart(annuity.getAnnuity_date()));
					calendar.add(Calendar.YEAR, annuity.getAnnuity_charge_year());
					calendar.add(Calendar.DATE, -1);
					annuity.setAnnuity_end_date(calendar.getTime());
				}
				annuity.setPatent(dbPatent);
			}
			patentDao.deletePatentAnnuity(dbPatent.getPatent_id());
			dbPatent.setListAnnuity(editPatent.getListAnnuity());
		} else {
			if (dbPatent.getListAnnuity() != null) {
				patentDao.deletePatentAnnuity(dbPatent.getPatent_id());
			}
		}
	}

	private void handleReminder(Patent patent, List<Business> listBusiness) {
		try {
			List<ReminderTask> reminderList = reminderDao.getAvailableReminderByPatentId(patent.getPatent_id());
			log.info("remove reminder patent:"+patent.getPatent_id());
			for (ReminderTask reminder:reminderList) {
				reminderDao.delete(reminder.getTask_id());
				quartzService.removeJob(reminder);
			}
			if (patent.getListAnnuity() != null && patent.getListAnnuity().size() > 0) {
				List<Annuity> listAnnuity = patent.getListAnnuity();
				for (Annuity annuity : listAnnuity) {
					for (Business business:listBusiness) {
						List<AnnuityReminder> annuityReminderList = annuityReminderDao.getByBusinessId(business.getBusiness_id());
						log.info(annuityReminderList.size());
						List<AnnuityReminder> listARSendRightNow = new ArrayList<>();
						Date now = DateUtils.getDayStart(new Date());
						for (AnnuityReminder annuityReminder : annuityReminderList) {
							if (annuityReminder.isAvailable()) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(annuity.getAnnuity_end_date());
								calendar.add(Calendar.DATE, -annuityReminder.getEmail_day());

								ReminderTask reminder = new ReminderTask();
								reminder.setTask_id(KeyGeneratorUtils.generateRandomString());
								reminder.setPatent_id(patent.getPatent_id());
								reminder.setBusiness_id(annuityReminder.getBusiness().getBusiness_id());
								reminder.setTask_type(ReminderTask.reminderTypeAnnuity);
								reminder.setTask_date(calendar.getTime());
								reminder.setReminder_day(annuityReminder.getEmail_day());
								reminder.setIs_send(false);
								reminder.setIs_remind(annuity.is_reminder());
								
								log.info("before:"+reminder.getTask_date());
								log.info("now:"+now);
								log.info("after:"+annuity.getAnnuity_end_date());
								if (reminder.getTask_date().after(now)) {
									if (reminder.is_remind() && !reminder.is_send()) {
										log.info("send on schulder");
										reminderDao.create(reminder);
										quartzService.createJob(reminder);
									}
								}

								if (reminder.getTask_date().equals(now) || (now.compareTo(reminder.getTask_date()) >= 0
										&& now.compareTo(annuity.getAnnuity_end_date()) <= 0)) {
									listARSendRightNow.add(annuityReminder);
									log.info("send right now List:" + listARSendRightNow.size());
								}
							}
						}
						// get last expire reminder but annuity not expire
						if (listARSendRightNow.size() > 0) {
							AnnuityReminder sendRemindInfo = listARSendRightNow.get(listARSendRightNow.size() - 1);
							Calendar calendarNowSend = Calendar.getInstance();
							calendarNowSend.setTime(annuity.getAnnuity_end_date());
							calendarNowSend.add(Calendar.DATE, -sendRemindInfo.getEmail_day());

							ReminderTask reminder = new ReminderTask();
							reminder.setTask_id(KeyGeneratorUtils.generateRandomString());
							reminder.setPatent_id(patent.getPatent_id());
							reminder.setBusiness_id(sendRemindInfo.getBusiness().getBusiness_id());
							reminder.setTask_type(ReminderTask.reminderTypeAnnuity);
							reminder.setTask_date(calendarNowSend.getTime());
							reminder.setReminder_day(sendRemindInfo.getEmail_day());
							reminder.setIs_send(false);
							reminder.setIs_remind(annuity.is_reminder());

							if (reminder.getTask_date().equals(now) || (now.compareTo(reminder.getTask_date()) >= 0
									&& now.compareTo(annuity.getAnnuity_end_date()) <= 0)) {
								if (reminder.is_remind()) {
									log.info("send right now");
									reminder.setIs_send(true);
									reminderDao.create(reminder);
									MailSender mail = new MailSender();
									Country country = countryDao.getByLanguage(patent.getPatent_appl_country(), "tw");
									patent.setCountry_name(country.getCountry_name());
									String annuity_date = DateUtils.getSimpleSlashFormatDate(annuity.getAnnuity_end_date());
									patent.setAnnuity_date(annuity_date);
									List<PatentContact> listContact = new ArrayList<>();
									for(PatentContact contact:patent.getListContact()) {
										log.info("contact:"+contact.getContact_email());
										if (contact.getBusiness() != null) {
											if (reminder.getBusiness_id().equals(contact.getBusiness().getBusiness_id())) {
												listContact.add(contact);
											}
										}
									}
									if (!listContact.isEmpty()) {
										mail.sendPatentAnnuityReminder(patent, listContact);
									} else {
										log.error("no contact for this patent");
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleExtension(Patent dbPatent, Patent editPatent, String businessId) {
		if (editPatent.getListExtension() != null && editPatent.getListExtension().size() > 0) {
			List<PatentExtension> listExtension = editPatent.getListExtension();
			List<String> duplicateBussinessId = new ArrayList<>();
			for (PatentExtension extension : listExtension) {
				extension.setExtension_id(KeyGeneratorUtils.generateRandomString());
				extension.setPatent(dbPatent);
				if (!duplicateBussinessId.contains(extension.getBusiness_id())) {
					duplicateBussinessId.add(extension.getBusiness_id());
				}
			}
			log.info(businessId);
			patentDao.deletePatentExtension(dbPatent.getPatent_id(), businessId);
			dbPatent.setListExtension(editPatent.getListExtension());
		} else {
			if (dbPatent.getListExtension() != null) {
				patentDao.deletePatentExtension(dbPatent.getPatent_id(), businessId);
			}
		}
	}

	private void handlePatentStatus(Patent dbPatent, Patent editPatent) {
		HashMap<String, PatentStatus> newMapping = new HashMap<String, PatentStatus>();
		HashMap<String, PatentStatus> dbMapping = new HashMap<String, PatentStatus>();
		if (editPatent.getListPatentStatus() != null) {
			for (PatentStatus patentStatus : editPatent.getListPatentStatus()) {
				Status status = patentStatus.getStatus();
				if (StringUtils.isNULL(status.getStatus_id())) {
					status.setStatus_id(KeyGeneratorUtils.generateRandomString());
					statusDao.create(status);
				}
				patentStatus.setStatus(status);
				patentStatus.setPatent(editPatent);
				if (patentStatus.getCreate_date() != null) {
					String dateStr = DateUtils.getDashFormatDate(patentStatus.getCreate_date());
					newMapping.put(patentStatus.getStatus().getStatus_id() + "-" + dateStr, patentStatus);
				}
			}

			for (PatentStatus patentStatus : dbPatent.getListPatentStatus()) {
				Status status = patentStatus.getStatus();
				if (patentStatus.getCreate_date() != null) {
					String dateStr = DateUtils.getDashFormatDate(patentStatus.getCreate_date());
					dbMapping.put(status.getStatus_id() + "-" + dateStr, patentStatus);
				}
			}

			for (PatentStatus patentStatus : editPatent.getListPatentStatus()) {
				Status status = patentStatus.getStatus();
				if (patentStatus.getCreate_date() != null) {
					String dateStr = DateUtils.getDashFormatDate(patentStatus.getCreate_date());
					if (!dbMapping.containsKey(status.getStatus_id() + "-" + dateStr)) {
						dbPatent.addPatentStatus(patentStatus);
					}
				}
			}
		}
		if (dbPatent.getListPatentStatus() != null) {
			Iterator<PatentStatus> iterator = dbPatent.getListPatentStatus().iterator();
			while (iterator.hasNext()) {
				PatentStatus patentStatus = iterator.next();
				Status status = patentStatus.getStatus();
				String dateStr = DateUtils.getDashFormatDate(patentStatus.getCreate_date());
				if (!newMapping.containsKey(status.getStatus_id() + "-" + dateStr)) {
					if (editPatent.getEdit_source() == Patent.EDIT_SOURCE_HUMAN) {
						if ("user".equals(status.getStatus_from())) {
							iterator.remove();
						} else {
							// human
							editPatent.addStatus(status);
						}
					} else if (editPatent.getEdit_source() == Patent.EDIT_SOURCE_SERVICE) {
						if ("uspto".equals(status.getStatus_from()) || "epo".equals(status.getStatus_from())) {
							iterator.remove();
						} else {
							editPatent.addStatus(status);
						}
					}
				}
			}
		}
	}

	private void getDisplayEditHistory(List<PatentEditHistory> list) {
		for (PatentEditHistory history : list) {
			String fieldId = history.getField_id();
			if (!StringUtils.isNULL(fieldId)) {
				switch (fieldId) {
				case Constants.PATENT_APPL_DATE_FIELD:
				case Constants.PATENT_NOTICE_DATE_FIELD:
				case Constants.PATENT_PUBLISH_DATE_FIELD:
				case Constants.PATENT_NAME_EN_FIELD:
				case Constants.PATENT_NAME_FIELD:
				case Constants.PATENT_COUNTRY_FIELD:
				case Constants.PATENT_APPL_NO_FIELD:
				case Constants.PATENT_NO_FIELD:
				case Constants.PATENT_NOTICE_NO_FIELD:
				case Constants.PATENT_PUBLISH_NO_FIELD:
				case Constants.SCHOOL_NO_FIELD:
				case Constants.SCHOOL_APPL_YEAR_FIELD:
				case Constants.PATENT_MEMO:
				case Constants.PATENT_COST_FIELD:
				case Constants.PATENT_FAMILY_FIELD:
				case Constants.PATENT_STATUS_FIELD:
					history.setDisplay_data(history.getHistory_data());
					history.setDisplay_data_en(history.getHistory_data());
					break;
				case Constants.INVENTOR_NAME_FIELD:

					List<Inventor> listInventor = (List<Inventor>) JacksonJSONUtils.readValue(history.getHistory_data(), new TypeReference<List<Inventor>>(){});
					if(listInventor!=null && listInventor.size() > 0 ) {
						List<String> name1 = new ArrayList<String>();
						List<String> name2 = new ArrayList<String>();
						for (Inventor inventor : listInventor) {
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
						for (Applicant applicant : listApplicant) {
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

	private void syncPatentStatus(Patent patent) {
		// 02/23新增根據日期同步狀態
		List<Status> ListStatus = statusDao.getEditable();
		for (Status status:ListStatus) {
			switch (status.getStatus_id()) {
			case Constants.STATUS_PUBLISH:
				if (patent.getPatent_publish_date() != null) {
					patent.addStatus(status, patent.getPatent_publish_date());
				}
				break;
			case Constants.STATUS_APPLICANTING:
				if (patent.getPatent_appl_date() != null) {
					patent.addStatus(status, patent.getPatent_appl_date());
				}
				break;
			case Constants.STATUS_NOTICE:
				if (patent.getPatent_notice_date() != null) {
					patent.addStatus(status, patent.getPatent_notice_date());
				}
				break;
			case Constants.STATUS_EXPIRED:
				if (patent.getPatent_edate() != null) {
					if (patent.getPatent_edate().before(new Date())) {
						patent.addStatus(status, patent.getPatent_charge_expire_date());
					}
				}
				break;
			}
		}
	}
	
	
	@Override
	public void deleteById(String id) {
		patentDao.deletePatentStatus(id);
		patentDao.delete(id);
	}

	
}
