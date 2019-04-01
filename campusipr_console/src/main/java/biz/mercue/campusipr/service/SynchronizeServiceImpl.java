package biz.mercue.campusipr.service;



import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.SynchronizeTaskDao;
import biz.mercue.campusipr.model.MessageTemplate;
import biz.mercue.campusipr.model.ReminderTask;
import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;


@Service("synchronizeService")
@Transactional
public class SynchronizeServiceImpl implements SynchronizeService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private SynchronizeTaskDao synchronizeDao;

	@Override
	public void addSync(SynchronizeTask sync) {
		synchronizeDao.create(sync);
		
	}

	@Override
	public int updateSync(SynchronizeTask sync) {
		SynchronizeTask dbBean = synchronizeDao.getById(sync.getTask_id());
		if(dbBean!=null){
			
			dbBean.setSync(sync.getSync());
			dbBean.setTask_date(sync.getTask_date());
			dbBean.setBusiness_id(sync.getBusiness_id());
			dbBean.setIs_sync(sync.is_sync());
			
		}
		return 0;
	}

	@Override
	public void deleteSync(SynchronizeTask sync) {
		synchronizeDao.delete(sync.getTask_id());
		
	}

	@Override
	public SynchronizeTask getById(String id) {
		return synchronizeDao.getById(id);
	}
	
	@Override
	public List<SynchronizeTask> getAvailableSynchronize() {
		return synchronizeDao.getAvailableSynchronize();
	}

	@Override
	public void changeSyncStatus(String id, boolean status) {
		SynchronizeTask dbBean = synchronizeDao.getById(id);
		if(dbBean!=null){
			dbBean.setIs_sync(status);
		}
	}


	


}
