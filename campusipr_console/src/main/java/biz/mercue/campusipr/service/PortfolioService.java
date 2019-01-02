package biz.mercue.campusipr.service;


import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.Portfolio;



public interface PortfolioService {

	int addPortfolio(Portfolio portfolio);

	int updatePortfolio(Portfolio portfolio);

	int deletePortfolio(Portfolio portfolio);

	ListQueryForm getByBusinessId(String businessId,int page);
	
	
	Portfolio getById(String businessId,String id);
	
	Portfolio getById(String id);

}
