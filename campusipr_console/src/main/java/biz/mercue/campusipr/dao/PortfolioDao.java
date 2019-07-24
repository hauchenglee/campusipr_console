package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Portfolio;

public interface PortfolioDao {

	Portfolio getById(String id);
	
	Portfolio getById(String businessId,String id);

	void create(Portfolio portfolio);
	
	void delete(String id);
	
	
	List<Portfolio> getByBusinessId(String businessId,int page,int pageSize);
	int getCountByBusinessId(String businessId);

	List<Portfolio> getPortfolioList();
}
