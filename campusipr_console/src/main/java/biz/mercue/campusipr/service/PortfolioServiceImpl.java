package biz.mercue.campusipr.service;



import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.PortfolioDao;
import biz.mercue.campusipr.model.Portfolio;
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
		return portfolio;
	}
	
	
	@Override
	public Portfolio getById(String id) {
		log.info("get by id: " + id);
		Portfolio patent = portfolioDao.getById(id);

		return patent;
	}

	@Override
	public void addPortfolio(Portfolio portfolio) {
		if(StringUtils.isNULL(portfolio.getPortfolio_id())) {
			portfolio.setPortfolio_id(KeyGeneratorUtils.generateRandomString());
		}

		portfolioDao.create(portfolio);
	}


	@Override
	public int  updatePortfolio(Portfolio portfolio){
		Portfolio dbBean = portfolioDao.getById(portfolio.getPortfolio_id());

		if(dbBean!=null){
	
			dbBean.setPortfolio_name(portfolio.getPortfolio_name());
			dbBean.setPortfolio_memo(portfolio.getPortfolio_memo());
			dbBean.setUpdate_date(new Date());
			dbBean.setListPatent(portfolio.getListPatent());
			dbBean.setPortfolio_family_num(portfolio.getPortfolio_family_num());
			dbBean.setPortfolio_patent_num(portfolio.getPortfolio_patent_num());

		}
		return 0;
	}
	

	@Override
	public void deletePortfolio(Portfolio portfolio) {
		portfolioDao.delete(portfolio.getPortfolio_id());
	}

	
	@Override
	public 	List<Portfolio> getByBusinessId(String businessId,int page,int pageSize){
		return portfolioDao.getByBusinessId(businessId,page,pageSize);
	}
	

	
}
