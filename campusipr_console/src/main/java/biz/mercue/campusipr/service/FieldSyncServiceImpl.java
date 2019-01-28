package biz.mercue.campusipr.service;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.FieldSyncDao;
import biz.mercue.campusipr.model.FieldSync;




@Service("fieldSyncService")
@Transactional
public class FieldSyncServiceImpl implements FieldSyncService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private FieldSyncDao fieldSyncDao;

	@Override
	public List<FieldSync> getAll(){

		return fieldSyncDao.getAll();
	}







	
}
