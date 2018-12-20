package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Banner;



public interface BannerService {


	Banner getById(String id);
	
	void addBanner(Banner banner);

	int updateBanner(Banner banner);
	
	int updateBannerList(List<Banner> list);

	void deleteBanner(Banner banner);

	List<Banner> getAll();
	
	
	List<Banner> getAvailable();


}
