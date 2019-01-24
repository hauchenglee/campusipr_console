package biz.mercue.campusipr.dao;


import java.util.List;

import biz.mercue.campusipr.model.Country;


public interface CountryDao {


	List<Country> getAll();
	
	List<Country> getListByLanguage(String lang);
	
	Country getByLanguage(String countryId,String lang);
	
	
	List<Country> getListByFuzzy(String name);
	

}
