package biz.mercue.campusipr.service;



import java.awt.print.Pageable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.Country;
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

	@Override
	public Patent getById(String businessId,String id) {
		log.info("get by id: " + id);
		Patent patent = patentDao.getById(businessId, id);
		if(patent!= null) {
			PatentFamily family = patent.getFamily();
			if(family!=null) {
				log.info("family id :"+family.getPatent_family_id());
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
		int taskResult= -1;
		if(StringUtils.isNULL(patent.getPatent_id())) {
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
		}
		
		if(patent.getBusiness() == null) {
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
		

		patentDao.create(patent);
		return Constants.INT_SUCCESS;
	}
	
	
	@Override
	public int addPatentByApplNo(Patent patent) {
		
		
		return Constants.INT_CANNOT_FIND_DATA;
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
			//dbBean.setListApplicant(patent.getListApplicant());
			//dbBean.setListContact(patent.getListContact());
//			dbBean.setListInventor(patent.getListInventor());
//			dbBean.setListCost(patent.getListCost());
//			dbBean.setListPortfolio(patent.getListPortfolio());
			
			//update List Edit History
			//dbBean.setListHistory(patent.getListHistory());
			
			

			

			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
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
		
		List list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE);
		int count = patentDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	
	@Override
	public List<Patent> getByPatentIds(List<String> idList,String businessId){
		List list = patentDao.getByPatentIds(idList,businessId);

		return list;
	}


	@Override
	public ListQueryForm searchPatent(String text, String businessId, int page) {
		
		//TODO no finish yet
		List list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE);
		int count = patentDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	@Override
	public ListQueryForm fieldSearchPatent(String text, String businessId, int page) {
		
		//TODO no finish yet
		List list = patentDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE);
		int count = patentDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		
		return form;
	}
	
	


	
}
