package biz.mercue.campusipr.service;



import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PatentDao;
import biz.mercue.campusipr.model.Patent;
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

		
		return patent;
	}
	
	
	@Override
	public Patent getById(String id) {
		log.info("get by id: " + id);
		Patent patent = patentDao.getById(id);

		return patent;
	}

	@Override
	public void addPatent(Patent patent) {
		if(StringUtils.isNULL(patent.getPatent_id())) {
			patent.setPatent_id(KeyGeneratorUtils.generateRandomString());
		}

		patentDao.create(patent);
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
			
			

			


		}
		return 0;
	}
	

	@Override
	public void deletePatent(Patent patent) {
		patentDao.delete(patent.getPatent_id());
	}

//	@Override
//	public 	List<Patent> getByAdminId(String adminId,int page,int pageSize){
//		return patentDao.getByAdminId(adminId,page,pageSize);
//	}
	
	@Override
	public 	List<Patent> getByBusinessId(String businessId,int page,int pageSize){
		return patentDao.getByBusinessId(businessId,page,pageSize);
	}
	
	
	@Override
	public List<Patent> getByPatentIds(List<String> list){
		return patentDao.getByPatentIds(list);
	}

	
}
