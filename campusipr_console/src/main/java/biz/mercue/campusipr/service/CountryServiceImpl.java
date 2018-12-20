package biz.mercue.campusipr.service;



import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.CountryDao;
import biz.mercue.campusipr.model.Country;




@Service("countryService")
@Transactional
public class CountryServiceImpl implements CountryService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private CountryDao countryDao;

	@Override
	public List<Country> getListByLanguage(String lang) {

		return countryDao.getListByLanguage(lang);
	}

	@Override
	public Country getByLanguage(String countryId, String lang) {

		return countryDao.getByLanguage(countryId, lang);
	}








	
}
