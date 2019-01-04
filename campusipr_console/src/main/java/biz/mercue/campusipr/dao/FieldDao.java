package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.PatentField;


public interface FieldDao {


	List<PatentField> getSearableFields();
	
	List<PatentField> getAllFields();
	
	PatentField getByFieldCode(String code);
	
	
	PatentField getBySearchCode(String code);

}
