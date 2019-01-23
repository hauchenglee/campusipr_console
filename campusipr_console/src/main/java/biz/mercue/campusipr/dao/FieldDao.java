package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.PatentField;


public interface FieldDao {

	PatentField getById(String id);
	
	List<PatentField> getSearableFields();
	
	List<PatentField> getAllFields();
	
	List<PatentField> getInputFields();
	
	PatentField getByFieldCode(String code);
	
	
	PatentField getBySearchCode(String code);

}
