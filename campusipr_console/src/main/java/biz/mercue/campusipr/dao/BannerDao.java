package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Banner;


public interface BannerDao {

	Banner getById(String id);

	void create(Banner banner);
	
	void delete(String id);
	
	
	List<Banner> getAll();
	
	List<Banner> getAvailable();


}
