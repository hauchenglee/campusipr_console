package biz.mercue.campusipr.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.StatusDao;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;


@Service("statusService")
@Transactional
public class StatusServiceImpl implements StatusService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private StatusDao statusDao;

	@Override
	public Status getById(String id) {
		log.info("get push by id: " + id);
		Status bean = statusDao.getById(id);
		return bean;
	}
	
	@Override
	public Status getByEventClass(String countryId,String eventClass) {
		return statusDao.getByEventClass(countryId, eventClass);
	}

	@Override
	public int addStatus(Status bean) {
		if(StringUtils.isNULL(bean.getStatus_id())) {
			bean.setStatus_id(KeyGeneratorUtils.generateRandomString());
		}
		
		statusDao.create(bean);
		return Constants.INT_SUCCESS;
	}
	
	
	@Override
	public int patchAddStatus(List<Status> list) {

		for(Status status : list) {
			statusDao.create(status);
		}

		return Constants.INT_SUCCESS;
	}


	@Override
	public int  update(Status bean){
		Status dbBean = statusDao.getById(bean.getStatus_id());

		if(dbBean!=null){



			dbBean.setEvent_code_desc(bean.getEvent_code_desc());
			dbBean.setEvent_code(bean.getEvent_code());
			dbBean.setEvent_class(bean.getEvent_class());
			dbBean.setStatus_desc(bean.getStatus_desc());
			dbBean.setStatus_desc_en(bean.getStatus_desc_en());
			dbBean.setStatus_color(bean.getStatus_color());
			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	
	}
	@Override
	public int delete(Status bean) {
		Status dbBean = statusDao.getById(bean.getStatus_id());

		if(dbBean!=null){
			statusDao.delete(bean.getStatus_id());
			return Constants.INT_SUCCESS;
		}else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}

	@Override
	public List<Status> getByCountry(String countryId) {
		
		return statusDao.getByCountry(countryId);
	}

}
