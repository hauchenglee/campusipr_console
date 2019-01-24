package biz.mercue.campusipr.service;



import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import biz.mercue.campusipr.dao.CurrencyDao;
import biz.mercue.campusipr.model.Currency;




@Service("currencyService")
@Transactional
public class CurrencyServiceImpl implements CurrencyService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private CurrencyDao currencyDao;

	@Override
	public List<Currency> getAll() {

		return currencyDao.getAll();
	}










	
}
