package biz.mercue.campusipr.service;



import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.dao.PatentFamilyDao;
import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContext;
import biz.mercue.campusipr.model.PatentExtension;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;




@Service("patentService")
@Transactional
public class PatentServiceImpl implements PatentService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PatentDao patentDao;
	
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
 		

		patentDao.create(patent);
		return Constants.INT_SUCCESS;
	}
	
	
	@Override
	public int addPatentByApplNo(Patent patent) {
		int taskResult= -1;
		
		String applNo =  patent.getPatent_appl_no();
		
		if(!StringUtils.isNULL(applNo)) {
			Patent appNoPatent = patentDao.getByApplNo(applNo);
			if(appNoPatent==null) {
				if(StringUtils.isNULL(patent.getPatent_id())) {
					patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
				}
				if (patent.getPatentContext() != null) {
					patent.getPatentContext().setPatent_context_id(KeyGeneratorUtils.generateRandomString());
				}
				if (patent.getListInventor() != null) {
					for (Inventor inventor:patent.getListInventor()) {
						inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
					}
				}
				if (patent.getListAssignee() != null) {
					for (Assignee assignee:patent.getListAssignee()) {
						assignee.setAssignee_id(KeyGeneratorUtils.generateRandomString());
					}
				}
				
				
				patentDao.create(patent);
				return Constants.INT_SUCCESS;
			} else {
				
				if(StringUtils.isNULL(patent.getPatent_id())) {
					patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
				}
				if (patent.getPatentContext() != null) {
					patent.getPatentContext().setPatent_context_id(KeyGeneratorUtils.generateRandomString());
				}
				if (patent.getListInventor() != null) {
					for (Inventor inventor:patent.getListInventor()) {
						inventor.setInventor_id(KeyGeneratorUtils.generateRandomString());
					}
				}
				if (patent.getListAssignee() != null) {
					for (Assignee assignee:patent.getListAssignee()) {
						assignee.setAssignee_id(KeyGeneratorUtils.generateRandomString());
					}
				}
				
				
				patentDao.create(patent);
				patentDao.delete(appNoPatent.getPatent_id());
				return Constants.INT_SUCCESS;
			}
		} else {
		
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int  updatePatent(Patent patent){
		Patent dbBean = patentDao.getById(patent.getPatent_id());

		if(dbBean!=null){
			//TODO save edit history

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
			
			
			dbBean.setPatentContext(patent.getPatentContext());
			
			
			dbBean.setFamily(patent.getFamily());
			
			//TODO update list
			dbBean.setListApplicant(patent.getListApplicant());
			dbBean.setListContact(patent.getListContact());
			dbBean.setListInventor(patent.getListInventor());
			dbBean.setListCost(patent.getListCost());
			dbBean.setListPortfolio(patent.getListPortfolio());
			
			//update List Edit History
			//dbBean.setListHistory(patent.getListHistory());
			
			

			

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
		int count = list.size();
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
	
	


	
}
