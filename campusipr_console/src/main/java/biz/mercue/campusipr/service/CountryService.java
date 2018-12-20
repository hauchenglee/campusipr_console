package biz.mercue.campusipr.service;

import java.util.List;

import biz.mercue.campusipr.model.Country;



public interface CountryService {



	List<Country> getListByLanguage(String lang);
	
	Country getByLanguage(String countryId,String lang);


}
