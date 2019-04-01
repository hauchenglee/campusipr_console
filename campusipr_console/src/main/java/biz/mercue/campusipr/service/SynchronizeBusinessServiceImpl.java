package biz.mercue.campusipr.service;



import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.SynchronizeBusinessDao;
import biz.mercue.campusipr.model.MessageTemplate;
import biz.mercue.campusipr.model.SynchronizeBusiness;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;


@Service("synchronizeBusinessService")
@Transactional
public class SynchronizeBusinessServiceImpl implements SynchronizeBusinessService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private SynchronizeBusinessDao synchronizeDao;

	@Override
	public void addSyncBusiness(SynchronizeBusiness sync) {
		synchronizeDao.create(sync);
		
	}

	@Override
	public int updateSyncBusiness(SynchronizeBusiness sync) {
		SynchronizeBusiness dbBean = synchronizeDao.getByBusinessId(sync.getBusiness().getBusiness_id());
		if(dbBean!=null){
			
			dbBean.setSync_date(sync.getSync_date());
			dbBean.setSync_next_date(sync.getSync_next_date());
			dbBean.setBusiness(sync.getBusiness());
			dbBean.setRandom_time(sync.getRandom_time());
			
		}
		return 0;
	}

	@Override
	public void deleteSyncBusiness(SynchronizeBusiness sync) {
		synchronizeDao.delete(sync.getSync_id());
		
	}

	@Override
	public SynchronizeBusiness getByBusinessId(String id) {
		return synchronizeDao.getByBusinessId(id);
	}


	


}
