package biz.mercue.campusipr.service;



import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.FieldDao;
import biz.mercue.campusipr.model.PatentField;




@Service("fieldService")
@Transactional
public class FieldServiceImpl implements FieldService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private FieldDao fieldDao;

	@Override
	public List<PatentField> getSearableFields(){
		
		return fieldDao.getSearableFields();
	}
	
	@Override
	public List<PatentField> getAllFields(){

		return fieldDao.getAllFields();
	}
	
	@Override
	public PatentField getByFieldCode(String code) {

		return fieldDao.getByFieldCode(code);
	}
	
	
	@Override
	public PatentField getBySearchCode(String code) {
		
		return fieldDao.getBySearchCode(code);
	}







	
}
