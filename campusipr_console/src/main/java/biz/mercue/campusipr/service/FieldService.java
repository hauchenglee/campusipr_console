package biz.mercue.campusipr.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import biz.mercue.campusipr.model.Country;
import biz.mercue.campusipr.model.PatentField;



public interface FieldService {

	

	List<PatentField> getSearableFields();
	
	List<PatentField> getAllFields();
	
	PatentField getByFieldCode(String code);
	
	PatentField getBySearchCode(String code);


}
