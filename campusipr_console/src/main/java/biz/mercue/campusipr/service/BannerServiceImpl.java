package biz.mercue.campusipr.service;



import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.BannerDao;

import biz.mercue.campusipr.model.Banner;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;




@Service("bannerService")
@Transactional
public class BannerServiceImpl implements BannerService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private BannerDao bannerDao;
	
	
	@Override
	public Banner getById(String id) {
		return bannerDao.getById(id);
	}
	
	@Override
	public void addBanner(Banner banner) {
		if(StringUtils.isNULL(banner.getBanner_id())) {
			 banner.setBanner_id(KeyGeneratorUtils.generateRandomString());
		}
		
		bannerDao.create(banner);
	}

	@Override
	public int updateBanner(Banner banner) {
		Banner dbBean = bannerDao.getById(banner.getBanner_id());

		if(dbBean!=null){
			dbBean.setBanner_title(banner.getBanner_title());
			dbBean.setBanner_content(banner.getBanner_content());
			dbBean.setBanner_order(banner.getBanner_order());
			dbBean.setBanner_image_file(banner.getBanner_image_file());
			dbBean.setUpdate_date(new Date());
		}
		return 0;
	}
	
	@Override
	public int updateBannerList(List<Banner> list) {
		for(Banner banner : list) {
			updateBanner(banner);
		}
		return 0;
	}

	@Override
	public void deleteBanner(Banner banner) {
		log.info("banner id "+banner.getBanner_id());
		Banner dbBean = bannerDao.getById(banner.getBanner_id());

		if(dbBean!=null){
			bannerDao.delete(dbBean.getBanner_id());
		}
	}

	@Override
	public List<Banner> getAll(){
		return bannerDao.getAll();
	}
	
	
	@Override
	public List<Banner> getAvailable(){
		return bannerDao.getAvailable();
	}

	
}
