package biz.mercue.campusipr.service;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PortfolioDao;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentFamily;
import biz.mercue.campusipr.model.Portfolio;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;




@Service("portfolioService")
@Transactional
public class PortfolioServiceImpl implements PortfolioService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PortfolioDao portfolioDao;
	

	@Override
	public Portfolio getById(String businessId,String id) {
		log.info("get by id: " + id);
		Portfolio portfolio = portfolioDao.getById(businessId, id);
		if(portfolio.getListPatent()!=null) {
			log.info(portfolio.getListPatent().size());
		}
		return portfolio;
	}
	
	
	@Override
	public Portfolio getById(String id) {
		log.info("get by id: " + id);
		Portfolio portfolio = portfolioDao.getById(id);
		
		if(portfolio.getListPatent()!=null) {
			log.info(portfolio.getListPatent().size());
		}

		return portfolio;
	}

	@Override
	public int addPortfolio(Portfolio portfolio) {
		
		if(StringUtils.isNULL(portfolio.getPortfolio_id())) {
			portfolio.setPortfolio_id(KeyGeneratorUtils.generateRandomString());
		}
		
		if(portfolio.getListPatent() !=null && portfolio.getListPatent().size() > 0) {
			//TODO count patent family
			portfolio.setPortfolio_patent_num(portfolio.getListPatent().size());
			portfolio.setPortfolio_family_num(portfolio.getListPatent().size());
		}else {
			portfolio.setPortfolio_patent_num(0);
			portfolio.setPortfolio_family_num(0);
		}

		portfolioDao.create(portfolio);
		return Constants.INT_SUCCESS;
	}


	@Override
	public int  updatePortfolio(Portfolio portfolio){
		Portfolio dbBean = portfolioDao.getById(portfolio.getPortfolio_id());
		int taskResult = -1;
		if(dbBean!=null){
	
			dbBean.setPortfolio_name(portfolio.getPortfolio_name());
			dbBean.setPortfolio_memo(portfolio.getPortfolio_memo());
			dbBean.setUpdate_date(new Date());
			dbBean.setListPatent(portfolio.getListPatent());

			//re-count patent num and family num

			
			if(portfolio.getListPatent() !=null && portfolio.getListPatent().size() > 0) {
				dbBean.setPortfolio_patent_num(portfolio.getListPatent().size());
				Map<String, PatentFamily> familyMap = new HashMap<String, PatentFamily>();
				for (Patent patent : portfolio.getListPatent()) {
					PatentFamily family = patent.getFamily();
					if(family != null) {
						if(familyMap.get(family.getPatent_family_id()) == null) {
							familyMap.put(family.getPatent_family_id(), family);
						}
					}
				}
				dbBean.setPortfolio_family_num(portfolio.getListPatent().size());
			}else {
				dbBean.setPortfolio_patent_num(0);
				dbBean.setPortfolio_family_num(0);
			}
			
			//TODO check patent 
			dbBean.setListPatent(portfolio.getListPatent());
			taskResult = Constants.INT_SUCCESS;
		}else {
			taskResult = Constants.INT_CANNOT_FIND_DATA;
		}
		return taskResult;
	}
	

	@Override
	public int deletePortfolio(Portfolio portfolio) {
		Portfolio dbBean = portfolioDao.getById(portfolio.getPortfolio_id());
		if(dbBean!=null) {
			portfolioDao.delete(portfolio.getPortfolio_id());
			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}

	
	@Override
	public 	ListQueryForm getByBusinessId(String businessId,int page){
		
		List list = portfolioDao.getByBusinessId(businessId,page,Constants.SYSTEM_PAGE_SIZE);
		int count = portfolioDao.getCountByBusinessId(businessId);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,list);
		return form;
	}
	

	
}
