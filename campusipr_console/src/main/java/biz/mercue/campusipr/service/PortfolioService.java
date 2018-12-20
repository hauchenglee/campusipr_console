package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Portfolio;



public interface PortfolioService {

	void addPortfolio(Portfolio portfolio);

	int updatePortfolio(Portfolio portfolio);

	void deletePortfolio(Portfolio portfolio);

	List<Portfolio> getByBusinessId(String businessId,int page,int pageSize);
	
	
	Portfolio getById(String businessId,String id);
	
	Portfolio getById(String id);

}
