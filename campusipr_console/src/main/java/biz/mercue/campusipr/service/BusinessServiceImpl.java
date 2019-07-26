package biz.mercue.campusipr.service;




import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.campusipr.dao.AnnuityReminderDao;
import biz.mercue.campusipr.dao.BusinessDao;
import biz.mercue.campusipr.dao.SynchronizeBusinessDao;
import biz.mercue.campusipr.dao.SynchronizeTaskDao;
import biz.mercue.campusipr.model.AnnuityReminder;
import biz.mercue.campusipr.model.Business;
import biz.mercue.campusipr.model.ListQueryForm;
import biz.mercue.campusipr.model.SynchronizeBusiness;
import biz.mercue.campusipr.model.SynchronizeTask;
import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.DateUtils;
import biz.mercue.campusipr.util.KeyGeneratorUtils;
import biz.mercue.campusipr.util.StringUtils;




@Service("businessService")
@Transactional
public class BusinessServiceImpl implements BusinessService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private BusinessDao businessDao;
	
	@Autowired
	private SynchronizeBusinessDao syncDao;
	
	@Autowired
	private AnnuityReminderDao annuityReminderDao;
	
	@Autowired
	private SynchronizeTaskDao syncTaskDao;

	@Autowired
	private QuartzService quartzService;
	
	@Override
	public void addBusiness(Business business) {
		if(StringUtils.isNULL(business.getBusiness_id())) {
			business.setBusiness_id(KeyGeneratorUtils.generateRandomString());
		}
		businessDao.create(business);
	}
	
	@Override
	public int safeAddBusiness(Business business) {
		int result = -1;
		try {
			if(StringUtils.isNULL(business.getBusiness_id())) {
			    business.setBusiness_id(KeyGeneratorUtils.generateRandomString());    
				//add anuityReminder
			    for (Integer defaultReminderDay:Constants.defaultReminderDays) {
				    AnnuityReminder anuityReminder = new AnnuityReminder();
				    anuityReminder.setReminder_id(KeyGeneratorUtils.generateRandomString());
				    anuityReminder.setReminder_text("專利{@patent_number}將於{@charges_edate}到期，請記得聯絡事務所進行繳費");
				    anuityReminder.setBusiness(business);
				    anuityReminder.setEmail_day(defaultReminderDay);
				    anuityReminder.setCreate_date(new Date());
				    anuityReminder.setAvailable(true);
				    annuityReminderDao.create(anuityReminder);
			    }
				business.setCreate_date(new Date());
				addBusiness(business);
				//add sync work quartz
				List<SynchronizeBusiness> syncList = syncDao.getAllSyncTask();
				
				//this sunday
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				c.add(Calendar.DATE, 7);
				
				SynchronizeBusiness sync = new SynchronizeBusiness();
				sync.setSync_id(KeyGeneratorUtils.generateRandomString());
				sync.setSync_date(DateUtils.getDayStart(c.getTime()));
				c.add(Calendar.DATE, 7);
				sync.setSync_next_date(DateUtils.getDayStart(c.getTime()));
				sync.setBusiness(business);
				Random rand = new Random();
				int n = rand.nextInt(60);
				//get max random number
				int random_time = n;
				if (syncList.size() > 0) {
					random_time = syncList.get(syncList.size()-1).getRandom_time() + n;
				}
				sync.setRandom_time(random_time);
				
				syncDao.create(sync);
				
				SynchronizeTask syncTask = new SynchronizeTask();
				syncTask.setTask_id(KeyGeneratorUtils.generateRandomString());
				syncTask.setSync(sync);
				Calendar cTask = Calendar.getInstance();
				cTask.setTime(DateUtils.getDayStart(sync.getSync_date()));
				cTask.add(Calendar.MINUTE, sync.getRandom_time());
				syncTask.setTask_date(cTask.getTime());
				syncTask.setBusiness_id(business.getBusiness_id());
				syncTask.setIs_sync(false);
				syncTaskDao.create(syncTask);
				
				quartzService.createJob(syncTask);
				
				result = Constants.INT_SUCCESS;
			}else {
				result = Constants.INT_DATA_ERROR;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	
	@Override
	public int updateBusiness(Business business) {
		Business dbBean =businessDao.getById(business.getBusiness_id());
		int result = -1;
		try {
			if(dbBean!=null){
				dbBean.setBusiness_alias(business.getBusiness_alias());
				dbBean.setBusiness_alias_en(business.getBusiness_alias_en());
				dbBean.setBusiness_name(business.getBusiness_name());
				dbBean.setBusiness_name_en(business.getBusiness_name_en());
				dbBean.setAvailable(business.isAvailable());
				
				dbBean.setContact_name(business.getContact_name());
				dbBean.setContact_email(business.getContact_email());
				dbBean.setContact_tel(business.getContact_tel());
				dbBean.setContact_tel_extension(business.getContact_tel_extension());
				dbBean.setContact_phone(business.getContact_phone());
				dbBean.setUpdate_date(new Date());
				
				//add annuity reminder
				if (business.isAvailable()) {
					List<AnnuityReminder> listReminder = annuityReminderDao.getByBusinessId(business.getBusiness_id());
					if (listReminder.isEmpty()) {
					    for (Integer defaultReminderDay:Constants.defaultReminderDays) {
						    AnnuityReminder anuityReminder = new AnnuityReminder();
						    anuityReminder.setReminder_id(KeyGeneratorUtils.generateRandomString());
						    anuityReminder.setReminder_text("專利{@patent_number}將於{@charges_edate}到期，請記得聯絡事務所進行繳費");
						    anuityReminder.setBusiness(business);
						    anuityReminder.setEmail_day(defaultReminderDay);
						    anuityReminder.setCreate_date(new Date());
						    anuityReminder.setAvailable(true);
						    anuityReminder.setIs_user_define(false);
						    annuityReminderDao.create(anuityReminder);
					    }
					} else {
						for (AnnuityReminder reminder:listReminder) {
							reminder.setAvailable(true);
						}
					}
				} else {
					List<AnnuityReminder> listReminder = annuityReminderDao.getByBusinessId(business.getBusiness_id());
					if (!listReminder.isEmpty()) {
						for (AnnuityReminder reminder:listReminder) {
							reminder.setAvailable(false);
						}
					}
				}
				
				if (business.isAvailable()) {
					List<SynchronizeBusiness> syncList = syncDao.getAllSyncTask();
					SynchronizeBusiness sync = syncDao.getByBusinessId(business.getBusiness_id());
					if (sync == null) {
						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
						c.add(Calendar.DATE, 7);
						
						sync = new SynchronizeBusiness();
						sync.setSync_id(KeyGeneratorUtils.generateRandomString());
						sync.setSync_date(DateUtils.getDayStart(c.getTime()));
						c.add(Calendar.DATE, 7);
						sync.setSync_next_date(DateUtils.getDayStart(c.getTime()));
						sync.setBusiness(business);
						Random rand = new Random();
						int n = rand.nextInt(60);
						//get max random number
						int random_time = n;
						if (syncList.size() > 0) {
							random_time = syncList.get(syncList.size()-1).getRandom_time() + n;
						}
						sync.setRandom_time(random_time);
						
						syncDao.create(sync);
					}
					
					//remove job
					SynchronizeTask syncTaskDB = syncTaskDao.getAvailableBusinessId(business.getBusiness_id());
					if (syncTaskDB != null) {
						syncTaskDao.delete(syncTaskDB.getTask_id());
						quartzService.removeJob(syncTaskDB);
					}
					
					SynchronizeTask syncTask = new SynchronizeTask();
					syncTask.setTask_id(KeyGeneratorUtils.generateRandomString());
					syncTask.setSync(sync);
					Calendar cTask = Calendar.getInstance();
					cTask.setTime(DateUtils.getDayStart(sync.getSync_date()));
					cTask.add(Calendar.MINUTE, sync.getRandom_time());
					syncTask.setTask_date(cTask.getTime());
					syncTask.setBusiness_id(business.getBusiness_id());
					syncTask.setIs_sync(false);
					syncTaskDao.create(syncTask);
					
					quartzService.createJob(syncTask);
				} else {
					//remove sync business
					SynchronizeBusiness sync = syncDao.getByBusinessId(business.getBusiness_id());
					if  (sync != null) {
						syncDao.delete(sync.getSync_id());
					}
					//remove job
					SynchronizeTask syncTaskDB = syncTaskDao.getAvailableBusinessId(business.getBusiness_id());
					if (syncTaskDB != null) {
						syncTaskDao.delete(syncTaskDB.getTask_id());
						quartzService.removeJob(syncTaskDB);
					}
				}
				result = Constants.INT_SUCCESS;
			}else {
				result = Constants.INT_CANNOT_FIND_DATA;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	@Override
	public void deleteBusiness(Business business) {
		try {
			Business dbBean =businessDao.getById(business.getBusiness_id());
	
			if(dbBean!=null){
				businessDao.delete(dbBean.getBusiness_id());
				
				//remove job
				SynchronizeTask syncTaskDB = syncTaskDao.getAvailableBusinessId(business.getBusiness_id());
				syncTaskDao.delete(syncTaskDB.getTask_id());
				quartzService.removeJob(syncTaskDB);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public ListQueryForm getAllByPage(int page){
		List<Business> businessesList = businessDao.getAllByPage(page,Constants.SYSTEM_PAGE_SIZE);
		int count = businessDao.getAllCount();
		log.info("count:"+count);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,businessesList);
		return form;
	}

	@Override
	public ListQueryForm getAll(){
		List<Business> businessesList = businessDao.getAll();
		int count = businessDao.getAllCount();
		log.info("count:"+count);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,businessesList);
		return form;
	}
	
	
	@Override
	public ListQueryForm search(String text,int page) {
		int count = businessDao.searchCount(text);
		List<Business> businessesList = businessDao.search(text, page, Constants.SYSTEM_PAGE_SIZE);
		log.info("count:"+count);
		ListQueryForm form = new ListQueryForm(count,Constants.SYSTEM_PAGE_SIZE,businessesList);
		return form;
	}
	
	
	

	
	@Override
	public List<Business> getAvailable(int page,int pageSize){
		return businessDao.getAvailable(page, pageSize);
	}
	
	@Override
	public Business getById(String id) {
		log.info("get by id: " + id);
		Business bean = businessDao.getById(id);

		return bean;
	}
	
	
	@Override
	public List<Business> getByName(String name){
		return businessDao.getByName(name);
	}
	
	@Override
	public List<Business> getByFuzzyName(String name){
		return businessDao.getByName(name);
	}
	




	
}
